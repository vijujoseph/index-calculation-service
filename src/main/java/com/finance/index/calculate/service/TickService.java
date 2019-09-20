package com.finance.index.calculate.service;

import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;

import java.util.List;

public interface TickService {

    String saveTick(Tick tick) throws IndexCalculationException;

}
