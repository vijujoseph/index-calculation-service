package com.finance.index.calculate.service.impl;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.service.StatisticService;
import com.finance.index.calculate.util.IndexCalcConstants;
import com.finance.index.calculate.util.IndexCalcUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class StatisticServiceImpl implements StatisticService {

    private static final Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);
    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    TickRepository tickRepository;

    @Override
    public Statistic getAllTickStatsInLastOneMin(Date currentDate) throws IndexCalculationException {
        //===========================Implementation for DB===============================
        /*        List<Tick> tickList = tickRepository.getAllInstrumentsByLastMin
                (IndexCalcUtil.getOneMinPastTimestampOfGivenDate(currentDate), currentDate);
        return findTickStatisticsSummary(tickList);*/
        //===========================Implementation for DB===============================

        lock.lock();
        try {
            return getLatestStatistics(IndexCalcUtil.latest);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Statistic getTickStatsByInstrumentInLastOneMin(String instrument, Date currentDate) throws IndexCalculationException {

        //===========================Implementation for DB===============================
        /*List<Tick> tickList = tickRepository.getInstrumentByLastMin
                (instrument, IndexCalcUtil.getOneMinPastTimestampOfGivenDate(currentDate), currentDate);
        return findTickStatisticsSummary(tickList);*/
        //===========================Implementation for DB===============================

        lock.lock();
        try {
            return getStatisticsByInstrument(StringUtils.lowerCase(instrument));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Statistics by Instrument identifier
     *
     * @param instrument identifier
     * @return a new Statistics object
     */
    private Statistic getStatisticsByInstrument(String instrument) {
        //to fetch all the entries for the requested instrument in Map
        String[] result = IndexCalcUtil.instrumentMap.entrySet().stream()
                .map(t -> t.getKey())
                .filter(t -> StringUtils.endsWith(t, String.valueOf(instrument)))
                .toArray(String[]::new);

        List<Statistic> stats = new ArrayList<>();
        //to fetch all the stats from the Map which are related to the requested instrument
        for (String key : result) {
            if (IndexCalcUtil.statisticsMap.containsKey(key))
                stats.add(IndexCalcUtil.statisticsMap.get(key));
        }
        Statistic from = new Statistic();
        if (stats.size() > 0) {
            //To find the max and minimum numbers of Map
            List<Double> maxMinAmountList = stats.parallelStream()
                    .flatMapToDouble(s -> Arrays.asList(s.getMin(), s.getMax()).parallelStream().mapToDouble(t -> t))
                    .boxed().collect(Collectors.toList());

            //To find the sum of Map
            BigDecimal sum = stats.parallelStream()
                    .map(s -> BigDecimal.valueOf(s.getSum()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(IndexCalcConstants.SCALE, RoundingMode.HALF_UP);

            long count = stats.stream().mapToLong(s -> s.getCount()).sum();

            from.setCount(count);
            from.setSum(sum.doubleValue());
            from.setAvg(
                    sum.divide(BigDecimal.valueOf(from.getCount()), IndexCalcConstants.SCALE, RoundingMode.HALF_UP)
                            .doubleValue());
            Supplier<DoubleStream> doubleStreamSupplier = () -> maxMinAmountList.parallelStream().mapToDouble(d -> d);
            from.setMin(doubleStreamSupplier.get().min().orElse(0.0));
            from.setMax(doubleStreamSupplier.get().max().orElse(0.0));

        }
        return from;
    }


    /**
     * clones a Statistics object
     *
     * @param statistics object to clone
     * @return a new Statistics object
     */
    public static Statistic getLatestStatistics(Statistic statistics) {
        return statistics;
    }

    private Statistic findTickStatisticsSummary(List<Tick> tickList) {
        Statistic response = new Statistic();
        if (tickList.size() == 0) {
            return response;
        }
        DoubleSummaryStatistics tickStats = tickList
                .stream()
                .map(Tick::getPrice)
                .collect(DoubleSummaryStatistics::new,
                        DoubleSummaryStatistics::accept,
                        DoubleSummaryStatistics::combine);
        System.out.println(tickStats);
        return tickStatsMapper(tickStats, response);
    }

    private Statistic tickStatsMapper(DoubleSummaryStatistics tickStats, Statistic response) {
        response.setAvg(IndexCalcUtil.twoDigitPrecisionConversion(tickStats.getAverage()));
        response.setMax(IndexCalcUtil.twoDigitPrecisionConversion(tickStats.getMax()));
        response.setMin(IndexCalcUtil.twoDigitPrecisionConversion(tickStats.getMin()));
        response.setCount(tickStats.getCount());
        return response;
    }


}