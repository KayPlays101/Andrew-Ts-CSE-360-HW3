package application;


public class PasswordEvaluator {

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */

	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean upperCase = false;
	public static boolean lowerCase = false;
	public static boolean numericChar = false;
	public static boolean specialChar = false;
	public static boolean longEnough = false;
	public static boolean otherChar = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int charCounter;						// The character counter used for keeping track of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running

	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static void displayInputState() {
		// Display the entire input line
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,charCounter) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				charCounter + "  |  The currentChar: \"" + currentChar + "\"");
	}

	/**********
	 * This method is a mechanical transformation of a Directed Graph diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		charCounter = 0;					// The index of the current character
		
		if(input.length() <= 0) return "*** Error *** The password is empty!";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		passwordInput = input;				// Save a copy of the input
		upperCase = false;					// Reset the Boolean flag
		lowerCase = false;					// Reset the Boolean flag
		numericChar = false;				// Reset the Boolean flag
		specialChar = false;				// Reset the Boolean flag
		longEnough = false;					// Reset the Boolean flag
		otherChar = false;					// Reset the Boolean flag
		running = true;						// Start the loop

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition
		while (running) {
			displayInputState();
			// The cascading if statement sequentially tries the current character against all of the
			// valid transitions
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				upperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				lowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				numericChar = true;
			} else if ("~`!@#$%^&*()_-+{}[]|:,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				specialChar = true;
			} else {
				System.out.println("Other character found");
				otherChar = true;
			}
			if (charCounter >= 8) {
				System.out.println("At least 9 characters found");
				longEnough = true;
			}
			
			// Increment charCounter
			charCounter++;
			
			// Checked if charCounter is smaller than the length of the inputLine, if satisfied, set currentChar
			// the next input char
			if (charCounter >= inputLine.length())
				running = false;
			else
				currentChar = input.charAt(charCounter);
			
			System.out.println();
		}
		
		String errMessage = "";
		if (!upperCase)
			errMessage += "Upper case; ";
		
		if (!lowerCase)
			errMessage += "Lower case; ";
		
		if (!numericChar)
			errMessage += "Numeric digits; ";
			
		if (!specialChar)
			errMessage += "Special character; ";
			
		if (!longEnough)
			errMessage += "Long Enough; ";
		
		if (otherChar)
			errMessage += "Invalid character; ";
		
		if (errMessage == "")
			return "";
		
		passwordIndexofError = charCounter;
		return errMessage + "conditions were not satisfied";

	}
}
