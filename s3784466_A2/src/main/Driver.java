package main;

import app.Menu;

/*
 * Class:		Driver
 * Description:	The class runs the program.
 * Author:		Rodney Cocker
 * Edited by:	Rodrigo Miguel Rojas
 */

public class Driver {

	public static void main(String[] args) 
	{
		/*
		 * ALGORITHM - Run menu
		 * 	BEGIN
		 * 		INITIALIZE Menu as menu
		 * 		RUN the run method in menu
		 * 	END
		 */
		Menu menu = new Menu();
		menu.run();	
	}

}
