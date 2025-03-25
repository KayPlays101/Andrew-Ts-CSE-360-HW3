package application;


public class TextInputEvaluator {

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */

	public static String textInputErrorMessage = "";		// The error message text
	public static String textInput = "";			// The input being processed
	public static int textInputIndexofError = -1;		// The index where the error was located
	public static boolean tooLong = false;
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
		System.out.println("The input size: " + inputLine.length() + "  |  The currentCharNdx: " + 
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
	public static String evaluateTextInput(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		textInputErrorMessage = "";
		textInputIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		charCounter = 0;					// The index of the current character
		
		if(input.length() <= 0) return "*** Error *** The input is empty!";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		textInput = input;				// Save a copy of the input
		tooLong = false;					// Reset the Boolean flag
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
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
			} else if ("!%^&()_-+{}[]:,.? ".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
			} else {
				System.out.println("Other character found");
				otherChar = true;
			}
		
			if (charCounter >= 255) {
				System.out.println("Text too long");
				tooLong = true;
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
		if (tooLong)
			errMessage += "Long Enough; ";
		
		if (otherChar)
			errMessage += "Invalid character; ";
		
		if (errMessage == "")
			return "";
		
		textInputIndexofError = charCounter;
		return errMessage + "conditions were not satisfied";

	}
}
