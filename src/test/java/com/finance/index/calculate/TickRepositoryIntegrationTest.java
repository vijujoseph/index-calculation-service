package com.finance.index.calculate;

import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.util.IndexCalcUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TickRepositoryIntegrationTest {

    @Autowired
    private TickRepository tickRepository;

    @Test
    public void whenFindByInstrumentValidDataTest() {

        Tick tick = new Tick("IBM.N",143.82,1478192204000L, new Date(),"ADMIN");
        tickRepository.save(tick);

        // when
        List<Tick> found = tickRepository.findByInstrument("IBM.N");

        // then
        Assert.assertEquals(found.size(), 1);
    }

    @Test
    public void whenFindByLastSixtySecondTicksValidDataTest() {
        Date pastDate = new Date();
        pastDate.setTime(new Date().getTime() - 600000);
        Tick tick = new Tick("IBM.N",143.82,1478192204000L, new Date(),"ADMIN");
        Tick tick1 = new Tick("IBM.N",16.78,1478192304000L, new Date(),"ADMIN");
        Tick tick2 = new Tick("IBM.N",222,1478191304000L, pastDate,"ADMIN");

        tickRepository.save(tick);
        tickRepository.save(tick1);
        tickRepository.save(tick2);

        Date endDate = new Date();
        Date startDate = new Date();
        startDate.setTime(endDate.getTime() - 60000);

        // when
        List<Tick> found = tickRepository.findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(startDate,endDate);
        // then
        Assert.assertEquals(found.size(), 2);
    }

    @Test
    public void whenFindByLastSixtySecondTicksValidData1Test() {
        Date currentDate = new Date(1569022735477L);

        Tick tick = new Tick("IBM.N",143.82,1478192204000L, currentDate,"ADMIN");
        Tick tick1 = new Tick("IBM.N",16.78,1478192304000L, new Date(),"ADMIN");
        Tick tick2 = new Tick("IBM.M",222,1478191304000L, new Date(),"ADMIN");
        tickRepository.save(tick);
        tickRepository.save(tick1);
        tickRepository.save(tick2);
        // when
        List<Tick> found = tickRepository.findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual
                (IndexCalcUtil.getOneMinPastTimestampOfGivenDate(currentDate),currentDate);
        // then
        Assert.assertEquals(found.size(), 1);
    }

}