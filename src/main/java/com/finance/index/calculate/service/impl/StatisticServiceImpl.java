package com.finance.index.calculate.service.impl;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.service.StatisticService;
import com.finance.index.calculate.util.IndexCalcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    private static final Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    TickRepository tickRepository;

    @Override
    public Statistic getAllTickStatsInLastOneMin(Date currentDate) throws IndexCalculationException {
        List<Tick> tickList = tickRepository.getAllInstrumentsByLastMin
                (IndexCalcUtil.getOneMinPastTimestampOfGivenDate(currentDate), currentDate);
        return findTickStatisticsSummary(tickList);
    }

    @Override
    public Statistic getTickStatsByInstrumentInLastOneMin(String instrument, Date currentDate) throws IndexCalculationException {
        List<Tick> tickList = tickRepository.getInstrumentByLastMin
                (instrument, IndexCalcUtil.getOneMinPastTimestampOfGivenDate(currentDate), currentDate);
        return findTickStatisticsSummary(tickList);
    }

    private Statistic findTickStatisticsSummary(List<Tick> tickList) {
        Statistic response = new Statistic();
        if(tickList.size() == 0){
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