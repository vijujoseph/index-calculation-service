package com.finance.index.calculate.repository;

import com.finance.index.calculate.domain.Tick;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TickRepository extends CrudRepository<Tick, Long> {

    List<Tick> findByInstrument(String instrument);

    List<Tick> findAllByCreatedDateBetween(Date startDate, Date endDate);

}