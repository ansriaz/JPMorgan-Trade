package test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.Test;

import assignment.solution.BadInputFormatException;
import assignment.solution.BusinessLogic;

public class JPMorganTradeTest {

	@Test
	public void testParser() {
		Set<LocalDate> result = null;
		BusinessLogic businessLogic = new BusinessLogic();

		try {
			businessLogic.parseMessage("foo B 0.50 SGP 01-Jan-2016 02-Jan-2016 200 100.25");
			businessLogic.parseMessage("bar S 0.22 AED 05-Jan-2016 07-Jan-2016 450 150.5");

			// in case of weekend, settlement day is the next working day
			businessLogic.adjustSettlementDatesAndPopulateMap();

			businessLogic.doCalculation();
			result = businessLogic.printDailySettlements();
			businessLogic.printEntityRanking();

		} catch (BadInputFormatException b) {
			b.printStackTrace();
		}

		assertEquals(0.0, 0.0, businessLogic.dateIncomingUsdMap.get(result.iterator().next()));
		assertEquals(10025.0, 10025.0, businessLogic.dateIncomingUsdMap.get(result.iterator().next()));
	}

}
