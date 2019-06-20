package app;

import java.util.Scanner;
import utilities.DateTime;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Author:		Rodney Cocker
 * Edited by: 	Rodrigo Miguel Rojas
 */
public class Menu
{
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run()
	{
		
		//loads data from save file
		application.loadData();
		
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		
		do
		{
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH)
			{
				System.out.println("Error - selection must be two characters!");
			} else
			{
				System.out.println();

				switch (input)
				{
				case "CC":
					createCar();
					break;
				case "BC":
					book();
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					System.out.println(application.displayAll());
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					searchAvailable();
				case "SD":
					application.seedData();
					break;
				case "EX":
					application.saveData();
					application.saveDataBackup();
					choice = "EX";
					System.out.println("Exiting Program ... Goodbye!");
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
				}
			}

		} while (choice != "EX");
	}
	
	/*
	 * ALGORITHM - Search Available Method
	 * 	BEGIN
	 * 		GET desired type of car
	 * 		GET date available
	 * 		PRINT cars available on date
	 * 	END
	 */
	
	/*
	 * Searches available cars for a given date
	 */
	private void searchAvailable() {
		System.out.println("Enter type (SD/SS): ");
		String type = console.nextLine().toUpperCase();
		
		switch(type) {
		case "SD":
			try {
			System.out.println("Date (DD/MM/YYYY): ");
			String dateEntered = console.nextLine();
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
			DateTime dateGiven = new DateTime(day, month, year);
			
			System.out.println(application.carsAvailableSD(dateGiven));
			}catch(StringIndexOutOfBoundsException e) {
				System.out.println("Exception - No date was entered...");
			}catch(NumberFormatException e) {
				System.out.println("Exception - Single digit number entered without starting 0...");
			}catch(NullPointerException e) {
				System.out.println("Exception - There was an error with the date inputted...");
			}
			break;
		case "SS":
			try {
			System.out.println("Date (DD/MM/YYYY): ");
			String dateEnteredSS = console.nextLine();
			int daySS = Integer.parseInt(dateEnteredSS.substring(0, 2));
			int monthSS = Integer.parseInt(dateEnteredSS.substring(3, 5));
			int yearSS = Integer.parseInt(dateEnteredSS.substring(6));
			DateTime dateGivenSS = new DateTime(daySS, monthSS, yearSS);
			
			System.out.println(application.carsAvailableSS(dateGivenSS));
			}catch(StringIndexOutOfBoundsException e) {
				System.out.println("Exception - No date was entered...");
			}catch(NumberFormatException e) {
				System.out.println("Exception - Single digit number entered without starting 0...");
			}catch(NullPointerException e) {
				System.out.println("Exception - There was an error with the date inputted...");
			}
		}
		
		
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar()
	{
		String id = "", make, model, driverName;
		int numPassengers = 0;
		String[] refreshments = new String[3];

		System.out.print("Enter registration number: ");
		id = promptUserForRegNo();
		if (id.length() != 0)
		{
			// Get details required for creating a car.
			System.out.print("Enter Make: ");
			make = console.nextLine();

			System.out.print("Enter Model: ");
			model = console.nextLine();

			System.out.print("Enter Driver Name: ");
			driverName = console.nextLine();

			System.out.print("Enter number of passengers: ");
			numPassengers = promptForPassengerNumbers();
			
			System.out.println("Enter service type (SD/SS): ");
			String serviceType = console.nextLine().toUpperCase();
			
			switch(serviceType) {
			case "SD":
				boolean result = application.checkIfCarExists(id);

				if (!result)
				{
					String carRegistrationNumber = application.createCar(id, make, model, driverName, numPassengers);
					System.out.println(carRegistrationNumber);
				} else
				{
					System.out.println("Error - Already exists in the system");
				}
				break;
			case "SS":
				System.out.println("Enter standard fee: ");
				double standardFee = console.nextDouble();
				if(isValidFee(standardFee) == true) {
					System.out.println("Enter refreshments: ");
					String ref = console.nextLine();
					ref = console.nextLine();
					refreshments = ref.split(",");
					
					boolean ssResult = application.checkIfCarExists(id);

					if (!ssResult)
					{
						String carRegistrationNumber = application.createSSCar(id, make, model, driverName, numPassengers, standardFee, refreshments);
						System.out.println(carRegistrationNumber);
					} else
					{
						System.out.println("Error - Already exists in the system");
					}

				}else {
					System.out.println("Error - Standard Fee must be at least $3.00");
				}
				break;
			default:
				System.out.println("Error - Invalid input...");	
			}
		}
	}
	
	/*
	 * Checks if standardFee given is greater than or equal to $3.00
	 */
	private boolean isValidFee(double standardFee) {
		if(standardFee < 3) {
			return false;
		}else {
			return true;
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book()
	{
		DateTime dateRequired = null;
		
		System.out.println("Enter date car required: ");
		System.out.println("format DD/MM/YYYY)");
		
		//exceptions due to errors with date input
		try {
			String dateEntered = console.nextLine();
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
			dateRequired = new DateTime(day, month, year);
		}catch(StringIndexOutOfBoundsException e) {
			System.out.println("Exception - No date was entered...");
			return false;
		}catch(NumberFormatException e) {
			System.out.println("Exception - Single digit number entered without starting 0...");
		}catch(NullPointerException e) {
			System.out.println("Exception - There was an error with the date inputted...");
		}
		
		String[] availableCars = application.book(dateRequired);
		for (int i = 0; i < availableCars.length; i++)
		{
			System.out.println(availableCars[i]);
		}
		if (availableCars.length != 0)
		{
			System.out.println("Please enter a number from the list:");
			int itemSelected = Integer.parseInt(console.nextLine());
			
			String regNo = availableCars[itemSelected - 1];
			regNo = regNo.substring(regNo.length() - 6);
			System.out.println("Please enter your first name:");
			String firstName = console.nextLine();
			System.out.println("Please enter your last name:");
			String lastName = console.nextLine();
			System.out.println("Please enter the number of passengers:");
			int numPassengers = Integer.parseInt(console.nextLine());
			String result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);

			System.out.println(result);
		} else
		{
			System.out.println("There are no available cars on this date.");
		}
		return true;
	}
	
	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking()
	{
		System.out.print("Enter Registration or Booking Date:");
		String response = console.nextLine();
		
		String result;
		// User entered a booking date
		if (response.contains("/"))
		{
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			System.out.print("Enter kilometers:");
			double kilometers = Double.parseDouble(console.nextLine());
			int day = Integer.parseInt(response.substring(0, 2));
			int month = Integer.parseInt(response.substring(3, 5));
			int year = Integer.parseInt(response.substring(6));
			DateTime dateOfBooking = new DateTime(day, month, year);
			result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
			System.out.println(result);
		} else
		{
			
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			if(application.getBookingByName(firstName, lastName, response))
			{
				System.out.print("Enter kilometers:");
				double kilometers = Double.parseDouble(console.nextLine());
				result = application.completeBooking(firstName, lastName, response, kilometers);
				System.out.println(result);
			}
			else
			{
				System.out.println("Error: Booking not found.");
			}
		}
		
	}
	
	/*
	 * Method for getting passenger numbers from user input
	 */
	private int promptForPassengerNumbers()
	{
		int numPassengers = 0;
		boolean validPassengerNumbers = false;
		// By pass user input validation.
		if (!testingWithValidation)
		{
			return Integer.parseInt(console.nextLine());
		} 
		else
		{
			while (!validPassengerNumbers)
			{
				
				//exception case to catch if user entered a string instead of an integer for numPassengers
				try {
					numPassengers = Integer.parseInt(console.nextLine());
				}catch(NumberFormatException e) {
					System.out.println("Exception - A number was not inputted...");
				}
				

				String validId = application.isValidPassengerCapacity(numPassengers);
				if (validId.contains("Error:"))
				{
					
					System.out.println(validId);
					System.out.println("Enter passenger capacity: ");
			
				} else
				{
					validPassengerNumbers = true;
				}
			}
			return numPassengers;
		}
	}

	/*
	 * Prompt user for registration number and validate it is in the correct form.
	 * Boolean value for indicating test mode allows by passing validation to test
	 * program without user input validation.
	 */
	private String promptUserForRegNo()
	{
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation)
		{
			return console.nextLine();
		} 
		else
		{
			while (!validRegistrationNumber)
			{
				regNo = console.nextLine();
				boolean exists = application.checkIfCarExists(regNo);
				if(exists)
				{
					// Empty string means the menu will not try to process
					// the registration number
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0)
				{
					break;
				}

				String validId = application.isValidId(regNo);
				if (validId.contains("Error:"))
				{
					System.out.println(validId);
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				} else
				{
					validRegistrationNumber = true;
				}
			}
			return regNo;
		}
	}

	/*
	 * Prints the menu.
	 */
	private void printMenu()
	{
		System.out.printf("\n********** MiRide System Menu **********\n\n");

		System.out.printf("%-30s %s\n", "Create Car", "CC");
		System.out.printf("%-30s %s\n", "Book Car", "BC");
		System.out.printf("%-30s %s\n", "Complete Booking", "CB");
		System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
		System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
		System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
		System.out.printf("%-30s %s\n", "Seed Data", "SD");
		System.out.printf("%-30s %s\n", "Exit Program", "EX");
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
}
