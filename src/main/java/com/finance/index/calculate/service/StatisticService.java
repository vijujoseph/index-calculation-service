package com.finance.index.calculate.service;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.exception.IndexCalculationException;

import java.util.Date;

public interface StatisticService {

    Statistic getAllTickStatsInLastOneMin(Date currentDate) throws IndexCalculationException;

    Statistic getTickStatsByInstrumentInLastOneMin(String instrument, Date currentDate) throws IndexCalculationException;

}
