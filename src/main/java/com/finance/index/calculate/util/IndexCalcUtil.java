package com.finance.index.calculate.util;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.domain.Tick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

public final class IndexCalcUtil {

    public static final Map<String, String> instrumentMap = new ConcurrentHashMap<>();
    public static final Map<String, Statistic> statisticsMap = new ConcurrentHashMap<>();
    public static Statistic latest = new Statistic();

    /*
        function to check if the tick date is older than 60 seconds
        201 - in case of success
        204 - if tick is older than 60 seconds
    */
    public static boolean isTickPastDate(Tick tick) {
        Date pastTimestamp = new Date();
        //past date -> 60 seconds older than current timestamp
        pastTimestamp.setTime(new Date().getTime() - 60000);

        //tick timestamp
        Date tickTimeStamp = new Date(tick.getTimestamp());

        //tick timestamp < 60 seconds?
        if(tickTimeStamp.compareTo(pastTimestamp) < 0) {
            return true;
        }
        return false;
    }

    /**
     * adds the amount to a Statistic object
     *
     * @param to     object to add
     * @param amount the new amount
     */
    public static void addStatistics(Statistic to, BigDecimal amount) {
        to.setCount(to.getCount() + 1);
        double dAmount = amount.doubleValue();
        if (to.getCount() == 1) {
            to.setSum(dAmount);
            to.setAvg(dAmount);
            to.setMin(dAmount);
            to.setMax(dAmount);
        } else {
            BigDecimal sum = BigDecimal.valueOf(to.getSum()).add(amount).setScale(IndexCalcConstants.SCALE,
                    RoundingMode.HALF_UP);
            to.setSum(sum.doubleValue());
            to.setAvg(sum.divide(BigDecimal.valueOf(to.getCount()), RoundingMode.HALF_UP).doubleValue());
            to.setMin(Math.min(to.getMin(), dAmount));
            to.setMax(Math.max(to.getMax(), dAmount));
        }
    }

     /**
     * removes expired statistics from total statistics
     *
     * @param from             object to remove
     * @param to               removing object
     * @param maxMinAmountList min and max amount values of all seconds
     */
    public static void remove(Statistic from, Statistic to, List<Double> maxMinAmountList) {
        BigDecimal sum = BigDecimal.valueOf(from.getSum()).subtract(BigDecimal.valueOf(to.getSum()))
                .setScale(IndexCalcConstants.SCALE, RoundingMode.HALF_UP);
        from.setCount(from.getCount() - to.getCount());
        from.setSum(sum.doubleValue());

        if (from.getCount() == 1) {
            from.setAvg(from.getSum());
            from.setMin(from.getSum());
            from.setMax(from.getSum());
        } else if (from.getCount() > 0) {
            from.setAvg(
                    sum.divide(BigDecimal.valueOf(from.getCount()), IndexCalcConstants.SCALE, RoundingMode.HALF_UP)
                            .doubleValue());
            Supplier<DoubleStream> doubleStreamSupplier = () -> maxMinAmountList.parallelStream().mapToDouble(d -> d);
            from.setMin(doubleStreamSupplier.get().min().orElse(0.0));
            from.setMax(doubleStreamSupplier.get().max().orElse(0.0));
        } else {
            from.setAvg(0.0);
            from.setMin(0.0);
            from.setMax(0.0);
        }
    }

    /*
    function to return timestamp which is 60 seconds older than given date
    201 - in case of success
    204 - if tick is older than 60 seconds
*/
    public static Date getOneMinPastTimestampOfGivenDate(Date givenDate) {
        Date pastTimestamp = new Date();
        //past date -> 60 seconds older than current timestamp
        pastTimestamp.setTime(givenDate.getTime() - 60000);
        return pastTimestamp;
    }

    /*
       Also used for 2-digit precision for all double values.
     */
    public static double twoDigitPrecisionConversion(double input) {
        BigDecimal bd = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
