package assignment.solution;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BusinessLogic {

	public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-uuuu")
			.withLocale(Locale.ENGLISH);
	public Map<LocalDate, List<TradeEntity>> dateEntityMap;
	public Map<LocalDate, Double> dateOutgoingUsdMap;
	public Map<LocalDate, Double> dateIncomingUsdMap;
	public Map<String, Double> entityTotalOutgoingMap;
	public Map<String, Double> entityTotalIncomingMap;
	public List<TradeEntity> entityArrayList = new ArrayList<>();

	public BusinessLogic() {
		dateEntityMap = new TreeMap<>();
		dateIncomingUsdMap = new HashMap<>();
		dateOutgoingUsdMap = new HashMap<>();
		entityTotalOutgoingMap = new HashMap<>();
		entityTotalIncomingMap = new HashMap<>();
	}

	// takes an input text file and a list of entities; fill the list with values
	// read from the file
	public void parseInputFile(String filename) throws BadInputFormatException {
		String line;

		try {
			InputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				parseMessage(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseMessage(String msg) {
		String[] parts;
		int partsLength = 8;
		parts = msg.split(" ");

		TradeEntity entity = new TradeEntity();

		// taking for granted the input line will always have length 8, not doing
		// the check for now
		for (int i = 0; i < partsLength; i++) {
			switch (i) {
			case 0:
				entity.setEntityName(parts[i]);
				break;
			case 1:
				entity.setBuySell(parts[i]);
				break;
			case 2:
				entity.setAgreedFx(Double.parseDouble(parts[i]));
				break;
			case 3:
				entity.setCurrency(parts[i]);
				break;
			case 4:
				entity.setInstructionDate(LocalDate.parse(parts[i], dateTimeFormatter));
				break;
			case 5:
				entity.setSettlementDate(LocalDate.parse(parts[i], dateTimeFormatter));
				break;
			case 6:
				entity.setUnits(Integer.parseInt(parts[i]));
				break;
			case 7:
				entity.setPricePerUnit(Double.parseDouble(parts[i]));
				break;

			default:
				try {
					throw new BadInputFormatException("Each input line should contain exactly 8 entries");
				} catch (BadInputFormatException e) {
					e.printStackTrace();
				}
			}
		}
		entityArrayList.add(entity);
	}

	// determines if the day is a working day for the currency
	public boolean isWorkingDay(String currency, DayOfWeek dayOfWeek) {

		if (currency.equalsIgnoreCase("AED") || currency.equalsIgnoreCase("SAR")) {

			switch (dayOfWeek) {
			case SUNDAY:
			case MONDAY:
			case TUESDAY:
			case WEDNESDAY:
			case THURSDAY:
				return true;
			case FRIDAY:
			case SATURDAY:
				return false;
			}

		} else {
			switch (dayOfWeek) {
			case MONDAY:
			case TUESDAY:
			case WEDNESDAY:
			case THURSDAY:
			case FRIDAY:
				return true;
			case SATURDAY:
			case SUNDAY:
				return false;

			}
		}

		return false; // the flow will never reach this point; putting to keep IDE happy
	}

	// returns the next working day for the entity's currency
	public LocalDate getNextWorkingDay(TradeEntity e) {
		LocalDate date = e.getSettlementDate();
		while (!isWorkingDay(e.getCurrency(), date.getDayOfWeek())) {
			date = date.plusDays(1);
		}
		return date;
	}

	// ensures that the "effective settlement days" are always working days
	public void adjustSettlementDatesAndPopulateMap() {
		for (TradeEntity ee : entityArrayList) {
			if (!isWorkingDay(ee.getCurrency(), ee.getSettlementDate().getDayOfWeek())) {
				ee.setEffectiveSettlementDate(getNextWorkingDay(ee));
			} else {
				ee.setEffectiveSettlementDate(ee.getSettlementDate());
			}
			populateDateEntityMap(ee);
		}

	}

	// populates the dateEnityMap
	// where keys are the effective settlement dates and values are the list of
	// entities that are settled on that day
	private void populateDateEntityMap(TradeEntity entity) {
		List<TradeEntity> entities = dateEntityMap.get(entity.getEffectiveSettlementDate());

		if (entities == null) {
			entities = new ArrayList<>();
			entities.add(entity);
		} else {
			entities.add(entity);
		}
		dateEntityMap.put(entity.getEffectiveSettlementDate(), entities);
	}

	// calculates incoming and outgoing amounts settled
	public void doCalculation() throws BadInputFormatException {
		Set<LocalDate> allTradingDays = dateEntityMap.keySet();

		for (LocalDate ld : allTradingDays) {
			Double outgoingUsd = 0d;
			Double incomingUsd = 0d;
			List<TradeEntity> entitiesOnThisDay = dateEntityMap.get(ld);

			for (TradeEntity e : entitiesOnThisDay) {
				if (e.getBuySell().equalsIgnoreCase("B")) {
					outgoingUsd += e.getPricePerUnit() * e.getUnits() * e.getAgreedFx();

				} else if (e.getBuySell().equalsIgnoreCase("S")) {
					incomingUsd += e.getPricePerUnit() * e.getUnits() * e.getAgreedFx();

				} else {
					throw new BadInputFormatException("The value of BuyOrSell must be either S or B");
				}

				updateTotalIncomingForEntity(e, incomingUsd);
				updateTotalOutgoingForEntity(e, outgoingUsd);
			}

			dateOutgoingUsdMap.put(ld, outgoingUsd);
			dateIncomingUsdMap.put(ld, incomingUsd);
		}
	}

	private void updateTotalIncomingForEntity(TradeEntity entity, Double incomingUsd) {
		Double tempIncomingUsdForEntity = entityTotalIncomingMap.get(entity.getEntityName());
		if (tempIncomingUsdForEntity == null) {
			entityTotalIncomingMap.put(entity.getEntityName(), incomingUsd);
		} else {
			tempIncomingUsdForEntity += incomingUsd;
			entityTotalIncomingMap.put(entity.getEntityName(), tempIncomingUsdForEntity);
		}
	}

	private void updateTotalOutgoingForEntity(TradeEntity entity, Double outgoingUsd) {
		Double tempOutgoingUsdForEntity = entityTotalOutgoingMap.get(entity.getEntityName());
		if (tempOutgoingUsdForEntity == null) {
			entityTotalOutgoingMap.put(entity.getEntityName(), outgoingUsd);
		} else {
			tempOutgoingUsdForEntity += outgoingUsd;
			entityTotalOutgoingMap.put(entity.getEntityName(), tempOutgoingUsdForEntity);
		}

	}

	public void printEntityRanking() {
		MyMapUtils<String, Double> mmu = new MyMapUtils<String, Double>();

		System.out.println("\n****************************************************");
		System.out.println("***** Entity Ranking Based on Incoming Amounts *****");
		System.out.println("****************************************************");
		mmu.sortByValueAndPrettyPrint(entityTotalIncomingMap);
		System.out.println("\n****************************************************");
		System.out.println("***** Entity Ranking Based on Outgoing Amounts *****");
		System.out.println("****************************************************");
		mmu.sortByValueAndPrettyPrint(entityTotalOutgoingMap);

	}

	public Set<LocalDate> printDailySettlements() {
		Set<LocalDate> allTradingDays = dateEntityMap.keySet();
		System.out.println("******************* Report *************************");

		for (LocalDate ld : allTradingDays) {
			System.out.print("Date : " + ld);
			System.out.print("\tTotal Settled Incoming in USD : " + dateIncomingUsdMap.get(ld));
			System.out.println("\t\tTotal Settled Outgoing in USD : " + dateOutgoingUsdMap.get(ld));
		}
		return allTradingDays;
	}
}
