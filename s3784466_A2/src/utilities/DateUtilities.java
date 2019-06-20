package utilities;

/*
 * Class:		DateUtilities
 * Description:	The class provides date validations for use in other classes.
 * Author:		Rodney Cocker
 * Edited by:	Rodrigo Miguel Rojas
 */

public class DateUtilities {

	public static boolean dateIsNotInPast(DateTime date)
	{
		final int OFFSET_FOR_DAYS_IN_MILLISECONDS = 1;
		boolean notInPast = false;
		
		DateTime today = new DateTime();
		
		int daysInPast = DateTime.diffDays(date, today) + OFFSET_FOR_DAYS_IN_MILLISECONDS;
		
		if(daysInPast >= 0)
		{
			notInPast = true;
		}
		
		return notInPast;
	}
	
	
	public static boolean datesAreTheSame(DateTime date1, DateTime date2)
	{
		if(date1.getEightDigitDate().equals(date2.getEightDigitDate()))
		{
			return true;
		}
		return false;
	}
	
	public static boolean dateIsNotMoreThan7Days(DateTime date)
	{
		
		boolean within7Days = false;
		DateTime today = new DateTime();
		DateTime nextWeek = new DateTime(7);
		
		int daysInFuture = DateTime.diffDays(nextWeek, date);
		if(daysInFuture >0 && daysInFuture <8)
		{
			within7Days = true;
		}
		return within7Days;
	}
	
	/*
	 * ALGORITHM - Date is not more than 3 days method
	 * 	BEGIN
	 * 		SET boolean value to false
	 * 		CREATE new date time for today
	 * 		CREATE new date time for 3 days from now
	 * 		CHECK difference between date
	 * 		IF difference is between 1-3 days
	 * 			RETURN true
	 * 		ELSE
	 * 			RETURN false
	 * 	END
	 */
	public static boolean dateIsNotMoreThan3Days(DateTime date) {
		
		boolean within3Days = false;
		DateTime today = new DateTime();
		DateTime threeDays = new DateTime(3);
		
		int daysInFuture = DateTime.diffDays(threeDays, date);
		if(daysInFuture > 0 && daysInFuture < 4)
		{
			within3Days = true;
		}
		return within3Days;
		
	}
}
