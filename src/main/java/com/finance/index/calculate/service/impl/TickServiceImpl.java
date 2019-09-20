package com.finance.index.calculate.service.impl;

import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.service.TickService;
import com.finance.index.calculate.util.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TickServiceImpl implements TickService {

    private static final Logger log = LoggerFactory.getLogger(TickServiceImpl.class);

    @Autowired
    TickRepository tickRepository;

    @Override
    public String saveTick(Tick tick) throws IndexCalculationException {
        tickRepository.save(tick);
        return isTickPastDate(tick) ? ApplicationConstants.NO_CONTENT: ApplicationConstants.CREATED;
    }

    /*
    function to check if the tick date is older than 60 seconds
    201 - in case of success
    204 - if tick is older than 60 seconds
    */
    private boolean isTickPastDate(Tick tick) {
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
}