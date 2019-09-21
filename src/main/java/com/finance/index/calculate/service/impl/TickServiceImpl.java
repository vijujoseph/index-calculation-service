package com.finance.index.calculate.service.impl;

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

import java.util.Date;

@Service
public class TickServiceImpl implements TickService {

    private static final Logger log = LoggerFactory.getLogger(TickServiceImpl.class);

    @Autowired
    TickRepository tickRepository;

    @Override
    public String saveTick(Tick tick) throws IndexCalculationException {
        tick.setCreatedDate(new Date());
        tick.setCreatedUser(IndexCalcConstants.SYSTEM_ADMIN);
        tickRepository.save(tick);
        return IndexCalcUtil.isTickPastDate(tick) ? IndexCalcConstants.NO_CONTENT : IndexCalcConstants.CREATED;
    }

}