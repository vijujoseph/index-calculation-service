package com.finance.index.calculate.controller;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    /*  method returns aggregated statistics for all ticks across all instruments in the last 60 seconds.
         Response sample:
             {
                 "avg": 100,
                 "max": 200,
                 "min": 50,
                 "count": 10
             }
             where:
             • avg is a double specifying the average amount of all tick prices in the last 60 seconds
             • max is a double specifying single highest tick price in the last 60 seconds
             • min is a double specifying single lowest tick price in the last 60 seconds
             • count is a long specifying the total number of ticks happened in the last 60 seconds
     */
    @RequestMapping(method = RequestMethod.GET)
    public Statistic getAllStats() throws IndexCalculationException {
        return statisticService.getAllTickStatsInLastOneMin(new Date());
    }

    /*  This is the endpoint with statistics for a given instrument in the last 60 seconds.
        Response sample:
            {
                "avg": 100,
                "max": 200,
                "min": 50,
                "count": 10
            }
            where:
            • avg is a double specifying the average amount of all tick prices in the last 60 seconds
            • max is a double specifying single highest tick price in the last 60 seconds
            • min is a double specifying single lowest tick price in the last 60 seconds
            • count is a long specifying the total number of ticks happened in the last 60 seconds
    */
    @RequestMapping(value = "/{instrument_identifier}", method = RequestMethod.GET)
    public Statistic getInstrumentStats(@PathVariable("instrument_identifier") String instrument) throws IndexCalculationException {
        return statisticService.getTickStatsByInstrumentInLastOneMin(instrument, new Date());
    }
}
