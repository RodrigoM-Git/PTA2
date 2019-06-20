package app;

import java.util.Scanner;

import cars.Car;
import cars.SilverServiceCar;
import exceptions.InvalidDate;
import exceptions.InvalidID;
import utilities.DateTime;
import utilities.MiRidesUtilities;
import java.io.*;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker
 * Edited by:		Rodrigo Miguel Rojas
 */
public class MiRideApplication
{
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;
	Scanner console = new Scanner(System.in);

	public MiRideApplication()
	{
		
	}
	
	/*
	 * Method to create standard Car
	 */
	public String createCar(String id, String make, String model, String driverName, int numPassengers) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}
	
	/*
	 * Method to create SilverService Car
	 */
	public String createSSCar(String id, String make, String model, String driverName, int numPassengers, double bookingFee, String[] refreshments) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			try
			{
				cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee, refreshments);
				itemCount++;
				return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
			} catch (InvalidID e)
			{
				e.printStackTrace();
			}
			
		}
		return "Error: Already exists in the system.";
	}

	public String[] book(DateTime dateRequired)
	{
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for(int i=0; i<cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					numberOfAvailableCars++;
				}
			}
		}
		if(numberOfAvailableCars == 0)
		{
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for(int i=0; i<cars.length;i++)
		{
			
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	/*
	 * Method for booking car
	 */
	public String book(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber)
	{
		Car car = getCarById(registrationNumber);
		if(car != null)
        {
			try
			{
				car.book(firstName, lastName, required, numPassengers);
				

					String message = "Thank you for your booking. \n" + car.getDriverName() 
				    + " will pick you up on " + required.getFormattedDate() + ". \n"
					+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
					return message;
			
			} catch (InvalidDate e)
			{
				e.printStackTrace();
			}
        }
        else{
            return "There was an error with the booking...";
        }
		return "There was an error with the booking...";
	}
	
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
	{
		String result = "";
		
		// Search all cars for bookings on a particular date.
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if(cars[i].isCarBookedOnDate(dateOfBooking))
				{
					return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				}
			}
		}
		return "Booking not found.";
	}
	
	/*
	 * Method for complete booking
	 */
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers)
	{
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}

		if (car == null)
		{
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1)
		{
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}
	
	/*
	 * Method to get booking with name
	 */
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber)
	{
		String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}
		
		if(car == null)
		{
			return false;
		}
		if(car.getBookingByName(firstName, lastName) == -1)
		{
			return false;
		}
		return true;
	}
	public String displaySpecificCar(String regNo)
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(cars[i].getRegistrationNumber().equals(regNo))
				{
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}
	
	
	/*
	 * Creates car data and bookings
	 */
	public boolean seedData()
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				return false;
			}
		}
		// 2 cars not booked
		Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
		cars[itemCount] = honda;
		itemCount++;
		
		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
		itemCount++;
		
		// 2 cars booked
		Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
		cars[itemCount] = bmw;
		itemCount++;
		try
		{
			bmw.book("Craig", "Cocker", new DateTime(1), 3);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		
		Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = audi;
		itemCount++;
		try
		{
			audi.book("Rodney", "Cocker", new DateTime(1), 4);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		
		// 1 car booked five times (not available)
		Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
		cars[itemCount] = toyota;
		itemCount++;
		try {
			toyota.book("Rodney", "Cocker", new DateTime(1), 3);
			toyota.book("Craig", "Cocker", new DateTime(2), 7);
			toyota.book("Alan", "Smith", new DateTime(3), 3);
			toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
			toyota.book("Paul", "Scarlett", new DateTime(5), 7);
			toyota.book("Paul", "Scarlett", new DateTime(6), 7);
		}catch (InvalidDate e) {
			e.printStackTrace();
		}
	
		
		// 1 car booked five times (not available)
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		try
		{
			rover.book("Rodney", "Cocker", new DateTime(1), 3);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		
		//rover.completeBooking("Rodney", "Cocker", 75);
		DateTime inTwoDays = new DateTime(2);
		try
		{
			rover.book("Rodney", "Cocker", inTwoDays, 3);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		rover.completeBooking("Rodney", "Cocker", inTwoDays,75);
		
		//2 SSCars not booked
		String refreshments[] = new String[3];
		String mints = "Mints";
		String choc = "Chocolate Bar";
		String oJ = "Orange Juice";
		
		refreshments[0] = mints;
		refreshments[1] = oJ;
		refreshments[2] = choc;
		
		SilverServiceCar civic = null;
		try
		{
			civic = new SilverServiceCar("ASD123", "Honda", "Civic", "Henry Cavill", 5, 4.50, refreshments);
		} catch (InvalidID e1)
		{
			e1.printStackTrace();
		}
		cars[itemCount] = civic;
		itemCount++;
		
		SilverServiceCar corolla = null;
		try
		{
			corolla = new SilverServiceCar("UGH567", "Toyota", "Corolla", "Ben Affleck", 5, 5, refreshments);
		} catch (InvalidID e1)
		{
			e1.printStackTrace();
		}
		cars[itemCount] = corolla;
		itemCount++;
		
		//2 SSCars booked but not completed
		SilverServiceCar swift = null;
		try
		{
			swift = new SilverServiceCar("RTY888", "Suzuki", "Swift", "Chris Evans", 5, 3.50, refreshments);
		} catch (InvalidID e1)
		{
			e1.printStackTrace();
		}
		try
		{
			swift.book("Peter", "Howard", new DateTime(2), 3);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		cars[itemCount] = swift;
		itemCount++;
		
		SilverServiceCar golf = null;
		try
		{
			golf = new SilverServiceCar("BNM876", "Volkswagen", "Golf R", "Daigo Umehara", 4, 7.50, refreshments);
		} catch (InvalidID e)
		{
			e.printStackTrace();
		}
		try
		{
			golf.book("Howard", "Potts", new DateTime(1), 4);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		cars[itemCount] = golf;
		itemCount++;
		
		//2 SSCars booked and completed
		SilverServiceCar aventador = null;
		try
		{
			aventador = new SilverServiceCar("YUI000", "Lamborghini", "Aventador", "Bruce Wayne", 2, 10, refreshments);
		} catch (InvalidID e)
		{
			e.printStackTrace();
		}
		try
		{
			aventador.book("Kirigaya", "Kazuto", new DateTime(1), 2);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		aventador.completeBooking("Kirigaya", "Kazuto", 34);
		cars[itemCount] = aventador;
		itemCount++;
		
		SilverServiceCar model3 = null;
		try
		{
			model3 = new SilverServiceCar("UGO777", "Tesla", "Model 3", "Elon Musk", 4, 6.50, refreshments);
		} catch (InvalidID e)
		{
			e.printStackTrace();
		}
		try
		{
			model3.book("Aladdin", "Abraham", new DateTime(2), 3);
		} catch (InvalidDate e)
		{
			e.printStackTrace();
		}
		model3.completeBooking("Aladdin", "Abraham", 45);
		cars[itemCount] = model3;
		itemCount++;
		
		return true;
	}
	
	/*
	 * ALGORITHM - Display All Cars Method
	 * 	BEGIN
	 * 		IF there are no cars
	 * 			RETURN "No cars"
	 * 		IF there are cars
	 * 			CHECK input to see if SD or SS cars are wanted
	 * 			CHECK input for ascending or descending sort order
	 * 			FOR every car in array
	 * 				GO TO first element
	 * 				CHECK if it is smaller or larger than the next element
	 * 				IF the element is smaller or larger (depending on sort order)
	 * 					SWITCH places
	 * 				GO TO next element
	 * 				REPEAT until no elements left
	 * 		RETURN sorted array as car.getDetails();
	 * 	END	
	 */
	
	
	/*
	 * Displays all cars in the system
	 */
	public String displayAll() {
		
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		String sortedDetails = "";
		System.out.println("Enter Type (SD/SS): ");
		String type = console.nextLine().toUpperCase();
		
		switch(type) {
		case "SD":
			System.out.println("Enter Sort Order (A/D): ");
			String sortOrder = console.nextLine().toUpperCase();
			switch(sortOrder) {
			case "A":
				for(int i = 0; i < cars.length; i++) {
					int smallest = i;
					for(int j = i + 1; j < cars.length; j++) {
						if(cars[j] != null) {
							if(cars[j].getRegistrationNumber().compareTo(cars[i].getRegistrationNumber()) < 0) {
								smallest = j;
								Car temp = cars[i];
								cars[i] = cars[smallest];
								cars[smallest] = temp;
							}
						}
					}	
				}
				for(int i = 0; i < cars.length; i++) {
					if(cars[i] != null && !(cars[i] instanceof SilverServiceCar)) {
						sortedDetails += cars[i].getDetails();
					}
				}
				break;
			case "D":
				for(int i = 0; i < cars.length; i++) {
					int largest = i;
					for(int j = i + 1; j < cars.length; j++) {
						if(cars[j] != null) {
							if(cars[j].getRegistrationNumber().compareTo(cars[i].getRegistrationNumber()) > 0) {
								largest = j;
								Car temp = cars[i];
								cars[i] = cars[largest];
								cars[largest] = temp;
							}
						}
					}	
				}
				for(int i = 0; i < cars.length; i++) {
					if(cars[i] != null && !(cars[i] instanceof SilverServiceCar)) {
						sortedDetails += cars[i].getDetails();
					}
				}
				break;
			default:
				return "Error - Invalid Input...";		
			}
			break;
		case "SS":
			System.out.println("Enter Sort Order (A/D): ");
			String sortOrderSS = console.nextLine().toUpperCase();
			switch(sortOrderSS) {
			case "A":
				for(int i = 0; i < cars.length; i++) {
					int smallest = i;
					for(int j = i + 1; j < cars.length; j++) {
						if(cars[j] != null) {
							if(cars[j].getRegistrationNumber().compareTo(cars[i].getRegistrationNumber()) < 0) {
								smallest = j;
								Car temp = cars[i];
								cars[i] = cars[smallest];
								cars[smallest] = temp;
							}
						}
					}	
				}
				for(int i = 0; i < cars.length; i++) {
					if(cars[i] != null && cars[i] instanceof SilverServiceCar) {
						sortedDetails += cars[i].getDetails();
					}
				}
				break;
			case "D":
				for(int i = 0; i < cars.length; i++) {
					int largest = i;
					for(int j = i + 1; j < cars.length; j++) {
						if(cars[j] != null) {
							if(cars[j].getRegistrationNumber().compareTo(cars[i].getRegistrationNumber()) > 0) {
								largest = j;
								Car temp = cars[i];
								cars[i] = cars[largest];
								cars[largest] = temp;
							}
						}
					}	
				}
				for(int i = 0; i < cars.length; i++) {
					if(cars[i] != null && cars[i] instanceof SilverServiceCar) {
						sortedDetails += cars[i].getDetails();
					}
				}
				break;
			default:
				return "Error - Invalid Input...";		
			}
			break;
		default:
			return "Error - Invalid Input...";
			
		}
		return sortedDetails;
	}

	/*
	 * Displays car booking by using ID
	 */
	public String displayBooking(String id, String seatId)
	{
		Car booking = getCarById(id);
		if(booking == null)
		{
			return "Booking not found";
		}
		return booking.getDetails();
	}
	
	/*
	 * Checks if ID is valid
	 */
	public String isValidId(String id)
	{
		return MiRidesUtilities.isRegNoValid(id);
	}
	
	/*
	 * Checks if capacity is valid
	 */
	public String isValidPassengerCapacity(int passengerNumber)
	{
		return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
	}

	/*
	 * Method to check if car is already in array
	 */
	public boolean checkIfCarExists(String regNo)
	{
		Car car = null;
		if (regNo.length() != 6)
		{
			return false;
		}
		car = getCarById(regNo);
		if (car == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/*
	 * Gets car information with Registration Number
	 */
	private Car getCarById(String regNo)
	{
		Car car = null;

		for (int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(regNo))
				{
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	/*
	 * Gets list of available Standard Cars
	 */
	public String carsAvailableSD(DateTime date) {
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		
		String listOfCars = "";
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null ) {
				if(cars[i].isCarBookedOnDate(date) == false && !(cars[i] instanceof SilverServiceCar)) {
					listOfCars += cars[i].getDetails();
				}
			}
		}
		return listOfCars;
		
	}
	
	/*
	 * Gets list of available SilverService Cars
	 */
	public String carsAvailableSS(DateTime date) {
		String listOfCars = "";
		
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null ) {
				if(cars[i].isCarBookedOnDate(date) == false && cars[i] instanceof SilverServiceCar) {
					listOfCars += cars[i].getDetails();
				}
			}
		}
		return listOfCars;
	}
	
	/*
	 * Saves current data in system to a text file
	 */
	public void saveData() {
		String fileName = "CarData.txt";
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileOutputStream(fileName, false));
			outputStream.write(toStringSave());
			System.out.println("System - Successfuly saved existing data to file...");
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.out.println(e);
		}
		
		outputStream.close();
	}
	
	/*
	 * Creates backup save file.
	 */
	public void saveDataBackup() {
		String backupName = "CarDataBackup.txt";
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(backupName, false));
			outputStream.write(toStringSave());
			System.out.println("System - Successfuly saved existing data to backup...");
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.out.println(e);
		}
		
		outputStream.close();
	}
	
	/*
	 * Creates the string that the PrintWriter writes to a file.
	 */
	public String toStringSave() {
		String data = "";
		
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null) {
				if(cars[i] instanceof SilverServiceCar){
					data += "SS:" + cars[i].toStringInitial() + "\n";
				}else if(!(cars[i] instanceof SilverServiceCar)) {
					data += "SD:" + cars[i].toStringInitial() + "\n";
				}
				
			}
		}
		
		return data;
	}
	
	/*
	 * Loads data from a file onto the system.
	 */
	public void loadData() {
		try {
			File file = new File("CarData.txt");
			Scanner console = new Scanner(file);
			String type, id, make, model, driverName, skip, item1, item2, item3 = null;
			int numPassengers = 0;
			double bookingFee = 0;
			console.useDelimiter(":|\n");
			
			//get shared values for the car
			while(console.hasNext()) {
				type = console.next();
				id = console.next();
				make = console.next();
				model = console.next();
				driverName = console.next();
				numPassengers = console.nextInt();
				skip = console.next();
				
				//checks if car is Standard 
				if(type.contentEquals("SD")) {
					cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
					itemCount++;
					
					//checks if car is SilverService
				}else if(type.contentEquals("SS")) {
					bookingFee = console.nextDouble();
					item1 = console.next();
					item2 = console.next();
					item3 = console.next();
					
					String[] refreshments = new String[3];
					refreshments[0] = item1.substring(7);
					refreshments[1] = item2.substring(7);
					refreshments[2] = item3.substring(7);
					
					try	{
						cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee, refreshments);
						itemCount++;
					} catch (InvalidID e){
						e.printStackTrace();
					}
				}
			}
			System.out.println("File successfully loaded from saved file!");
			
		}catch(FileNotFoundException e) {
			try {
				
				//Retry whole process with backup file if the first file is missing.
				File file = new File("CarDataBackup.txt");
				Scanner console = new Scanner(file);
				String type, id, make, model, driverName, skip, item1, item2, item3 = null;
				int numPassengers = 0;
				double bookingFee = 0;
				console.useDelimiter(":|\n");
				
				while(console.hasNext()) {
					type = console.next();
					id = console.next();
					make = console.next();
					model = console.next();
					driverName = console.next();
					numPassengers = console.nextInt();
					skip = console.next();
					
					if(type.contentEquals("SD")) {
						cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
						itemCount++;
					}else if(type.contentEquals("SS")) {
						bookingFee = console.nextDouble();
						
						item1 = console.next();
						item2 = console.next();
						item3 = console.next();
						
						String[] refreshments = new String[3];
						refreshments[0] = item1.substring(7);
						refreshments[1] = item2.substring(7);
						refreshments[2] = item3.substring(7);
						
						try	{
							cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee, refreshments);
							itemCount++;
						} catch (InvalidID e1){
							e1.printStackTrace();
						}
					}
				}
				System.out.println("Error - File could not be found...");
				System.out.println("File successfully loaded from backup!");
			}catch(FileNotFoundException e2){
				System.out.println("Error - No file was found...");
				System.out.println("Error - No backup was found...");
				System.out.println("System has launched with no data...");
			}catch(IOException e3) {
				System.out.println(e3);
			}
		}catch(IOException e) {
			System.out.println(e);
		}
	}
	
	
	
}
