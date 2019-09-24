package com.finance.index.calculate.scheduler;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.util.IndexCalcConstants;
import com.finance.index.calculate.util.IndexCalcUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class StatisticScheduler {

    private static final Logger log = LoggerFactory.getLogger(StatisticScheduler.class);

    private ReentrantLock lock = new ReentrantLock();

    /**
     * Scheduler to clean up ticks older than 60 seconds
     *
     */
    @Scheduled(cron = "${stats.remove.cron}")
    private void removeOldRecords() {
        long currentTimestamp = Instant.now(Clock.systemUTC()).getEpochSecond();

        long before60seconds = currentTimestamp - IndexCalcConstants.EXPIRATION_DURATION;
        //String instruments[] = IndexCalcUtil.instrumentMap.get(before60seconds);

        String[] result = IndexCalcUtil.instrumentMap.entrySet().stream()
                .map(t -> t.getKey())
                .filter(t -> StringUtils.startsWith(t ,String.valueOf(before60seconds)))
                .toArray(String[]::new);

        for(String instrument: result) {

            Statistic statistics = IndexCalcUtil.statisticsMap.remove(instrument);
            if (statistics == null) {
                return;
            }

            log.info("Triggerred at {} for {}", currentTimestamp, before60seconds);

            List<Double> maxMinAmountList = IndexCalcUtil.statisticsMap.entrySet().parallelStream().map(e -> e.getValue())
                    .flatMapToDouble(s -> Arrays.asList(s.getMin(), s.getMax()).parallelStream().mapToDouble(t -> t))
                    .boxed().collect(Collectors.toList());

            lock.lock();
            try {
                IndexCalcUtil.remove(IndexCalcUtil.latest, statistics, maxMinAmountList);
            } finally {
                lock.unlock();
            }

            log.info(IndexCalcUtil.latest.toString());
        }
    }    
    
}
