package cars;

import exceptions.InvalidDate;
import exceptions.InvalidID;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		SilverServiceCar
 * Description:	The class represents a SilverServiceCar in a ride sharing system. 
 * Author:		Rodrigo Miguel Rojas
 */
public class SilverServiceCar extends Car {
	
	//variables 
	private double bookingFee;
	private String[] refreshments = new String[3];

	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity,
			double bookingFee, String[] refreshments) throws InvalidID {
		super(regNo, make, model, driverName, passengerCapacity);
		this.bookingFee = bookingFee; //user inputs bookingFee >= $3.00
		
		//Exceptions for refreshments
		boolean isFull = false;
		boolean noDuplicates = true;
		
		for(int i = 0; i < refreshments.length; i++) {
			for(int j = i+1; j < refreshments.length; j++) {
				if(refreshments[i].equals(refreshments[j])) {
					noDuplicates = false;
				}
			}
		}
		
		for(int i = 0; i < refreshments.length; i++) {
			if(refreshments[i] != null) {
				if(i == 2) {
					isFull = true;
				}
			}
		}
		
		if(isFull == true && noDuplicates == true) {
			this.refreshments = refreshments;
		}else {
			throw new InvalidID("Exception - There was a problem with the refreshments...");
		}
	}
	
	/*
	 * Override of book method from car class
	 */
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidDate {
		//checks if date is valid (not more than 3 days or in the past)
		if(DateUtilities.dateIsNotInPast(required) == true && DateUtilities.dateIsNotMoreThan3Days(required) == true) {
			return super.book(firstName, lastName, required, numPassengers);
		}else {
			return false;
		}
	}
	
	/*
	 * Create readable representation of Silver Service Car details
	 */
	@Override
	public String getDetails() {
		String details = "";
		details += super.getDetailsForCar();
		details += "Standard Fee: \t" + bookingFee + "\n";
		details += "\n Refreshments Available: \n";
		details += refreshmentDetails() + "\n";
		details += super.getCurrentBookings() + "\n";
		details += super.getPastBookings() + "\n";
		
		return details;
	}
	
	/*
	 * ALGORITHM - Refreshment Details Method
	 * 	BEGIN
	 * 		GO TO first object of refreshments array
	 * 		CHECK if the space is null
	 * 		IF the space is not null
	 * 			ADD the refreshment to a string
	 * 		GO TO next object of refreshments array
	 * 		REPEAT until end of array
	 * 		RETURN the string 
	 * 	END
	 */
	
	/*
	 * Gets refreshments and creates a readable list
	 */
	public String refreshmentDetails() {
		String list = "";
		for(int i = 0; i < refreshments.length; i++) {
			if(refreshments[i] != null) {
				int refreshmentNum = i+1;
				list += "Item " + refreshmentNum + ": \t" + refreshments[i] + "\n";
			}
		}
		
		return list;
	}
	
	/*
	 * Computer readable state of the Silver Service Car
	 */
	@Override
	public String toString() {
		String string = "";
		string += super.toStringInitial();
		string += ":" + bookingFee;
		string += refreshmentsString();
		string += ":" + super.currentBookingsToString();
		string += super.pastBookingsToString();
		
		return string;
	}
	
	/*
	 * Provides start of toString
	 */
	@Override
	public String toStringInitial() {
		String string = "";
		string += super.toStringInitial();
		string += ":" + bookingFee;
		string += refreshmentsString();
		
		return string;
	}
	
	/*
	 * Gets refreshments and writes it out into the toString format
	 */
	public String refreshmentsString() {
		String refToString = "";
		for(int i = 0; i < refreshments.length; i++) {
			if(refreshments[i] != null) {
				int refreshmentNum = i+1;
				refToString += ":Item " + refreshmentNum + " " + refreshments[i];
			}
		}
		return refToString;
	}
	
	/*
	 * Required getters
	 */
	public double getBookingFee() {
		return bookingFee;
	}
	public double getPercentOfTripFee() {
		return 0.4;
	}
}


