package com.finance.index.calculate.service.impl;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.service.TickService;
import com.finance.index.calculate.util.IndexCalcConstants;
import com.finance.index.calculate.util.IndexCalcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TickServiceImpl implements TickService {

    private static final Logger log = LoggerFactory.getLogger(TickServiceImpl.class);

    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    TickRepository tickRepository;

    @Override
    public String saveTick(Tick tick) throws IndexCalculationException {
        tick.setCreatedDate(new Date());
        tick.setCreatedUser(IndexCalcConstants.SYSTEM_ADMIN);

        long currentTimeStamp = Instant.now(Clock.systemUTC()).getEpochSecond();
        long tickTimestamp = Instant.ofEpochMilli(tick.getTimestamp()).atOffset(ZoneOffset.UTC).toEpochSecond();
        long diff = currentTimeStamp - tickTimestamp;

        log.info("currentTimeStamp: {}, tickTimestamp: {}, diff: {}, amount: {}", currentTimeStamp, tickTimestamp, diff,
                tick.getPrice());

        // tickTimestamp is older than 1 minute
        if (diff > IndexCalcConstants.EXPIRATION_DURATION) {
            log.info("Invalid tickTimestamp - currentTimeStamp: {}, tickTimestamp: {}, diff: {}", currentTimeStamp, tickTimestamp, diff);
            return IndexCalcConstants.NO_CONTENT;
        }

        //tickRepository.save(tick);
        return IndexCalcUtil.isTickPastDate(tick) ? IndexCalcConstants.NO_CONTENT : addStatistics(BigDecimal.valueOf(tick.getPrice()), tickTimestamp);
    }


    public String addStatistics(BigDecimal amount, long timestamp) {
        lock.lock();
        try {
            IndexCalcUtil.addStatistics(IndexCalcUtil.latest, amount);
        } finally {
            lock.unlock();
        }

        Statistic statistics = IndexCalcUtil.statisticsMap.computeIfAbsent(timestamp, key -> new Statistic());
        IndexCalcUtil.addStatistics(statistics, amount);
        return IndexCalcConstants.CREATED;
    }

}