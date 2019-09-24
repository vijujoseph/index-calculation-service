package com.finance.index.calculate;

import com.finance.index.calculate.domain.Statistic;
import com.finance.index.calculate.domain.Tick;
import com.finance.index.calculate.exception.IndexCalculationException;
import com.finance.index.calculate.repository.TickRepository;
import com.finance.index.calculate.service.StatisticService;
import com.finance.index.calculate.service.TickService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexCalculationServiceTests {

	@Autowired
	TickService tickService;

	@Autowired
	StatisticService statisticService;

	@Autowired
	private TickRepository tickRepository;

	@Test
	public void getAllTickStatisticsInLastOneMinValidTest() throws IndexCalculationException {
		Statistic response = statisticService.getAllTickStatsInLastOneMin(new Date());
		assertEquals(response.getCount(), 0);
	}

	//Below test case is to validate the service with DB, currently executed using in memory
    //solution and ignored
	@Test
    @Ignore
	public void getAllDBTickStatisticsInLastOneMinValid1Test() throws IndexCalculationException {
		Date currentDate = new Date(1569022135477L);
		Date pastDate = new Date();
		pastDate.setTime(currentDate.getTime() - 600000);
		Tick tick = new Tick("IBM.N",143.82,1478192204000L, currentDate,"ADMIN");
		Tick tick1 = new Tick("IBM.N",16.78,1478192304000L, currentDate,"ADMIN");
		//10 mins past date
		Tick tick2 = new Tick("IBM.N",2220.0,1478191304000L, pastDate,"ADMIN");
		tickRepository.save(tick);
		tickRepository.save(tick1);
		tickRepository.save(tick2);

		Statistic response = statisticService.getAllTickStatsInLastOneMin(currentDate);
		assertEquals(response.getCount(), 2);
		assertEquals(response.getMax(), 143.82,.5);
	}

    //Below test case is to validate the service with DB, currently executed using in memory
    //solution and ignored
	@Test
    @Ignore
	public void getAllDBTickStatisticsInLastOneMinValid2Test() throws IndexCalculationException {
		Date currentDate = new Date(1569022735477L);

		Tick tick = new Tick("IBM.N",143.82,1478192204000L, currentDate,"ADMIN");
		Tick tick1 = new Tick("IBM.M",16.78,1478192304000L, currentDate,"ADMIN");
		Tick tick2 = new Tick("IBM.N",222.0,1478191304000L, currentDate,"ADMIN");
		tickRepository.save(tick);
		tickRepository.save(tick1);
		tickRepository.save(tick2);

		Statistic response = statisticService.getAllTickStatsInLastOneMin(currentDate);
		assertEquals(response.getCount(), 3);
		assertEquals(response.getMax(), 222,.5);
		assertEquals(response.getAvg(), 127.53,.5);
		assertEquals(response.getMin(), 16.78,.5);

		Statistic instrumentResponse = statisticService.getTickStatsByInstrumentInLastOneMin("IBM.N",currentDate);
		assertEquals(instrumentResponse.getCount(), 2);
		assertEquals(instrumentResponse.getMax(), 222,.5);
		assertEquals(instrumentResponse.getAvg(), 182.9,.5);
		assertEquals(instrumentResponse.getMin(), 143.82,.5);
	}

    @Test
    public void getAllInMemoryTickStatisticsInLastOneMin1Test() throws IndexCalculationException {
        Date currentDate = new Date();
        //current date minus 50 secs
        long millis = currentDate.getTime() - 50000;
        Tick tick = new Tick("IBM.N",143.82,millis, currentDate,"ADMIN");
        Tick tick1 = new Tick("IBM.M",16.78,millis, currentDate,"ADMIN");
        Tick tick2 = new Tick("IBM.N",222.0,millis, currentDate,"ADMIN");
        tickService.saveTick(tick);
        tickService.saveTick(tick1);
        tickService.saveTick(tick2);

        Statistic response = statisticService.getAllTickStatsInLastOneMin(currentDate);
        assertEquals(response.getCount(), 3);
        assertEquals(response.getMax(), 222,.5);
        assertEquals(response.getAvg(), 127.53,.5);
        assertEquals(response.getMin(), 16.78,.5);

        Statistic instrumentResponse = statisticService.getTickStatsByInstrumentInLastOneMin("IBM.N",currentDate);
        assertEquals(instrumentResponse.getCount(), 2);
        assertEquals(instrumentResponse.getMax(), 222,.5);
        assertEquals(instrumentResponse.getAvg(), 182.9,.5);
        assertEquals(instrumentResponse.getMin(), 143.82,.5);
    }

    @Test
    public void getAllInMemoryTickStatisticsInLastOneMinInvalidDataTest() throws IndexCalculationException {
        Date currentDate = new Date();
        //current date minus 60 secs - shouldn't save
        long millis = (currentDate.getTime() - 60000);
        Tick tick = new Tick("IBM.N",143.82,millis, currentDate,"ADMIN");
        Tick tick1 = new Tick("IBM.M",16.78,millis, currentDate,"ADMIN");
        Tick tick2 = new Tick("IBM.N",222.0,millis, currentDate,"ADMIN");
        tickService.saveTick(tick);
        tickService.saveTick(tick1);
        tickService.saveTick(tick2);

        Statistic response = statisticService.getAllTickStatsInLastOneMin(currentDate);
        assertEquals(response.getCount(), 0);
     }

}
