package com.finance.index.calculate.util;

import com.finance.index.calculate.domain.Tick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public final class IndexCalcUtil {

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
