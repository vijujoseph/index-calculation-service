package com.finance.index.calculate.controller;

import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.service.TickService;
import com.finance.index.calculate.util.ApplicationConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TickController {

    @Autowired
    TickService tickService;

    /*
        Status returns below:
        NO_CONTENT 204 - if tick is older than 60 seconds
        CREATED 201 - in case of success
     */
    @RequestMapping(value = "/ticks", method = RequestMethod.POST)
    public ResponseEntity saveTicks(@RequestBody Tick tick) throws IndexCalculationException {
        String status = tickService.saveTick(tick);
        return new ResponseEntity(StringUtils.equals(status, ApplicationConstants.CREATED)
                ? HttpStatus.CREATED : HttpStatus.NO_CONTENT);
    }

}
