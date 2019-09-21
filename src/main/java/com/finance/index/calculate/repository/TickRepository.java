package com.finance.index.calculate.repository;

import com.finance.index.calculate.domain.Tick;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TickRepository extends CrudRepository<Tick, Long> {

    List<Tick> findByInstrument(String instrument);

    List<Tick> findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Date startDate, Date endDate);

    default List<Tick> getAllInstrumentsByLastMin(final Date startDate,final Date endDate) {
        return findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(startDate, endDate);
    }

    List<Tick> findAllByInstrumentAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(String instrument, Date startDate, Date endDate);

    default List<Tick> getInstrumentByLastMin(final String instrument,final Date startDate,final Date endDate) {
        return findAllByInstrumentAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(instrument, startDate, endDate);
    }
}