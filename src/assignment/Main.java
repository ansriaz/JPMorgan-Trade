package assignment;

import assignment.solution.BadInputFormatException;
import assignment.solution.BusinessLogic;

public class Main {
	public static void main(String[] args) {
		BusinessLogic businessLogic = new BusinessLogic();

		try {
			businessLogic.parseInputFile("inputFile.txt");

			// in case of weekend, settlement day is the next working day
			businessLogic.adjustSettlementDatesAndPopulateMap();

			businessLogic.doCalculation();
			businessLogic.printDailySettlements();
			businessLogic.printEntityRanking();
		} catch (BadInputFormatException b) {
			b.printStackTrace();
		}
	}
}