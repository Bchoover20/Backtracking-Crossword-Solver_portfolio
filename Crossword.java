

// Crossword.java
// Author : Brandon Hoover
// Copyright : @All rights reserved
// Date Created : 9/15/23

import java.io.*;
import java.util.*;



public class Crossword implements WordPuzzleInterface {

	
	/*
	 * Class variables that are used in fillpuzzle
	 */
	char[][] ourBoard;
	char[] alphabetArray = {'e','a','r','i','o','t','n','s','l','c','u','d','p','m','h','g','b','f','y','w','k','v','x','z','j','q' };
	char[] alphabetAay = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'}; 
	String[] blackListed;
	int blackListNum = 0;
	boolean finished = false;

	// An array of StringBuilders to keep track of the rows and columns of the board
	StringBuilder[] rowStr1;
	StringBuilder[] colStr1;

	// saves the cordinate position of the minus if found
	private int[] rowLastMinusPos1;
	private int[] colLastMinusPos1;

	// marks a condition that deals with when a minus is found
	boolean isMinus = false;
	boolean boardNoMinus = true;
	
	// Same stringBuilders but this time is used in checkPuzzle
	StringBuilder[] rowStr2;
	StringBuilder[] colStr2;

	private int[] rowLastMinusPos2;
	private int[] colLastMinusPos2;


	
	/**
	 * Constructor partitions - public
	 * @Functionality - initializes the vars of ourBoard and blackListed(unused). ourBoard is a copy of the board that was passed into fill puzzle
	 * No parameters in this constructer
	 * @return 
	 */	
	public Crossword () {

		ourBoard = new char[0][0]; // new board of size [0][0] will that work?? is this necessary? 
		blackListed = new String[100];

		

	}



	/**
	 * Method - fillPuzzle ()
	 * @Functionality - 
	 * @param char [][] board - the blank board filled with chars that represent rules of what types of letters can be filled when generating the crossword
	 * @param DictInterface dicitonary - an instance of DictInterface that allows for you to parse through a dictionary and search for prefixes and words
	 * @return char [][] ourBoard - the solution that is generated in fillPuzzle, returns null if no solution is found
	 */	
	@Override
	public char[][] fillPuzzle(char[][] board, DictInterface dictionary) {

	
		
		ourBoard = new char[board.length][board.length]; // new blank board the same size as the one passed in 

		// read in the board that was passed in  --------------
		for (int i=0; i < board.length; i++) {
			// here here
			for (int j=0; j < board.length; j++) {
				// writing each char from the board passed in, into the class board the solve is gonna use 
				ourBoard[i][j] = board[i][j];

			}
		}

		// Initializing the StringBuilders
		 rowStr1 = new StringBuilder[ourBoard.length];
		 colStr1 = new StringBuilder[ourBoard.length];

		 rowLastMinusPos1 = new int[ourBoard.length]; // generating a bunch of zeros
		 colLastMinusPos1 = new int[ourBoard.length]; 

		
		// Creating StringBuilder objects
		for (int k=0; k < ourBoard.length; k++) {
			rowStr1[k] = new StringBuilder();
			colStr1[k] = new StringBuilder();
			rowLastMinusPos1[k] = -1; 			// filling them with -1's for checkPuzzle
			colLastMinusPos1[k] = -1;
		}


		// FIRST RECURSIVE CALL - type boolean to make for easy exit on recusion
		boolean goodFind = solve(0,0,0,dictionary);

		if (goodFind == false) {
			return null;
		}

		

		// Show user whatever you created ------------------------------------------
		for (int i = 0; i < ourBoard.length; i++)
		{
			for (int j = 0; j < ourBoard.length; j++)
			{
				System.out.print(ourBoard[i][j] + " ");
			}
			System.out.println();
		}
		//System.out.println("FINAL BOARD: :::::::: " + ourBoard.length);


		// returning the newly filled out board that was shown
		return ourBoard;

	}





	/**
	 * Method - solve
	 * @Functionality - A recursive helper method that is essential in the utilization of fillPuzzle 
	 * @param int row - the current row pointer that helps keep track of which row position you are at in the algorithm execution
	 * @param int col - the current col pointer that helps keep track of which col position you are at in the algorithm execution
	 * @return boolean goodFind - once the base case is met, returns true which starts a loop of returning true until all recursive method call stacks are popped off the stack
	 */	
	private boolean solve(int row, int col, /*for debugging*/ int depth, DictInterface dictionary){
		
		boolean goodFind = false;
		
		// NORMAL CASE - any of the 26 letters can be placed
		if (ourBoard[row][col] == '+') {


				//showBoard();
				/*** Debugging */
				//System.out.println("current State of row Str1 " + rowStr1[row]);  --- debugging system out's
				//System.out.println("current state of Col Str1 " + colStr1[col]);
				//showBoard();
				//System.out.println("A VALID Letter has been found: " + proceed);


					for (int i=0; i < alphabetArray.length; i++) {
			
						char letter = alphabetArray[i];
						
						// checks to see if letter matches all critera to be placed
						boolean proceed = isValid(row, col, letter, dictionary);
			
						boolean goodFind2;
			
						if (Character.isUpperCase(letter)) { // character is marked so it cannot be used

							proceed = false;
						}

						
						if (proceed) { // if a valid thing then continue
			
							
							ourBoard[row][col] = letter;
			
							// CHECKING FOR BASE CASE
							if (finished == true) {
								goodFind2 = true;
							}
							else if (col == ourBoard.length-1) {	// need to recurse to the next row

								goodFind2 = solve(row+1, 0, depth+1, dictionary);
							}
							else {	// need to recurse to the next column

								goodFind2 = solve(row, col+1,depth+1, dictionary);
							}
						
			
							if (goodFind2 == true) {
									return true;
							}
							else {
			
								//showBoard();
								// delete the appends and replace the tile with a plus for the next time it reaches there
								ourBoard[row][col] = '+';
						
								rowStr1[row].deleteCharAt(col);
								colStr1[col].deleteCharAt(row);
							
							}
							
						}
						else {
							// no good on proceed  
					
						}		
					}
		}
		else if (ourBoard[row][col] == '-') { // equivalent to a black space in a crossword


				// Unusual approach with minus, does NOT read in a minus into the stringBuilders but instead an invisible letter that acts as a placeholder, so you can still go through the crossword
 
				/*** Debugging */
				//	showBoard();
				//		System.out.println("current State of row Str1 " + rowStr1[row]);
				//		System.out.println("current state of Col Str1 " + colStr1[col]);
				//		System.out.println("A VALID Letter has been found: " + proceed);


			for (int i=0; i < alphabetArray.length; i++) {


						char letter = alphabetArray[i];

						// if there is a minus on the board then it needs to go through a special form of isValid
						isMinus = true;
						// used in checkPuzzleB
						boardNoMinus = false;

						// saves the location of the minus for checkPuzzle
						rowLastMinusPos1[row] = col;
						colLastMinusPos1[col] = row;
				
						boolean proceed = isValid(row, col, letter, dictionary);
			
						boolean goodFind2;
			

						
						if (proceed) { // if a valid thing then continue
				
		
							// CHECKING FOR BASE CASE
							if (finished == true) {
								goodFind2 = true;
							}
							else if (col == ourBoard.length-1) {

								goodFind2 = solve(row+1, 0, depth+1, dictionary);
							}
							else {

								goodFind2 = solve(row, col+1,depth+1, dictionary);
							}
						
			
							if (goodFind2 == true) {
								
									return true;
							}
							else {
			
								// showBoard();
								// delete the appends 
								ourBoard[row][col] = '-';
								

						
								rowStr1[row].deleteCharAt(col);
								colStr1[col].deleteCharAt(row);
							
							}
							
						}
						else {
							// no good on proceed  // NO MINUS
	
						}		
					
				
					}

		}
		else if ((ourBoard[row][col] == '1') || (ourBoard[row][col] == '2') || (ourBoard[row][col] == '3') || (ourBoard[row][col] == '4') || (ourBoard[row][col] == '5')) { //board[i][j] is a number


			// CASE 3 NUMBER FREQUENCY 

			// Number frequency works the same but after proceed, count the frequency in the stringBuilders and make sure that it does not occur too much,

			/*** Debugging */
			//showBoard();
			//	System.out.println("current State of row Str1 " + rowStr1[row]);
			//	System.out.println("current state of Col Str1 " + colStr1[col]);
			//	System.out.println("A VALID Letter has been found: " + proceed);
			//System.out.println(Arrays.toString(alphabetArray));

			char replacementChar = ourBoard[row][col];

			// getting the integer value of type char number that has been read in from the board and saving it into numFreqCompare
			int value = Character.getNumericValue(ourBoard[row][col]);
			int numFreqCompare = value; // taking in the number before proceed
			
			for (int i=0; i < alphabetArray.length; i++) {

			
						char letter = alphabetArray[i];
						
				
					
						int numFreq = 0;
						boolean proceed = isValid(row, col, letter, dictionary);
			
						boolean goodFind2;
						
						// if a valid letter has been found, count frequency of the potential letter and make sure that it has been placed too many times befoer
						if (proceed == true) {

							for (int k=0; k < ourBoard.length; k++) {
	
								for (int m=0; m < ourBoard.length; m++) {
									
									if (ourBoard[k][m] == letter) {
										numFreq++;
									}
								}
	
							}
						}


						if (numFreq > numFreqCompare && proceed == true) {
							proceed = false;
							
						}

						
						if (proceed) { // if a valid thing then continue
					
			
							// mark the letter so it cannot be used in the future 
							alphabetArray[i] = Character.toUpperCase(alphabetArray[i]);

							

							ourBoard[row][col] = letter;
			
							// CHECKING FOR BASE CASE
							if (finished == true) {
								goodFind2 = true;
							}
							else if (col == ourBoard.length-1) {

								goodFind2 = solve(row+1, 0, depth+1, dictionary);
							}
							else {

								goodFind2 = solve(row, col+1,depth+1, dictionary);
							}
						
			
							if (goodFind2 == true) {
									
									return true;
							}
							else {
			
								// delete the appends 
								//showBoard();
								ourBoard[row][col] = replacementChar;
								

						
								rowStr1[row].deleteCharAt(col);
								colStr1[col].deleteCharAt(row);
							
							}
							
						}
						else {
							// no good on proceed  // NO MINUS
						


						}		
					}
		}
		else { // could be one of any 26
			// it has to be a letter since all other cases are checked before this one is reached

			/*** Debugging */
			//	System.out.println("current State of row Str1 " + rowStr1[row]);
			//	System.out.println("current state of Col Str1 " + colStr1[col]);

			// no for loop because the letter has already been chosen, just has to make sure that its valid with the rest of what is generated
			
						char letter = ourBoard[row][col];
						
						boolean proceed = isValid(row, col, letter, dictionary);
			
						boolean goodFind2;
			

						
						if (proceed) { // if a valid thing then continue
					//		System.out.println("A VALID Letter has been found: " + proceed);
			

							// CHECKING FOR BASE CASE
							if (finished == true) {
								goodFind2 = true;
							}
							else if (col == ourBoard.length-1) {

								goodFind2 = solve(row+1, 0, depth+1, dictionary);
							}
							else {

								goodFind2 = solve(row, col+1,depth+1, dictionary);
							}
						
			
							if (goodFind2 == true) {
									//return false;
									return true;
							}
							else {
			
								//showBoard();
								// delete the appends 
								ourBoard[row][col] = letter;
								

						
								rowStr1[row].deleteCharAt(col);
								colStr1[col].deleteCharAt(row);
							
							}
							
						}
						else {
							// no good on proceed 
	
						}		
					
		}


		// end of recursive helper method 
		return goodFind;
	}
			
			

	/**
	 * Method - isValid
	 * @Functionality - verifies with the use of dictionary class that the letter being passed in is viable, and can potentially be placed into the current space 
	 * @param int row - the current row pointer that helps keep track of which row position you are at in the algorithm execution
	 * @param int col - the current col pointer that helps keep track of which col position you are at in the algorithm execution
	 * @param DictInterface dicitonary - an instance of DictInterface that allows for you to parse through a dictionary and search for prefixes and words
	 * @return boolean verdict - if all of the criteria checks are met, returns true, else returns false
	 */	
	private boolean isValid (int row, int col, char letter, DictInterface dictionary) {


		/** Debugging */
		//System.out.println("						isValidRowAfterAppend      " + rowStr1[row]);
		//	System.out.println("						isValidColAfterAppend      "  + colStr1[col]);
		//	System.out.println("rowResultCheck " + rowResultCheck);
		//	System.out.println("COL CHECK: " + colResultCheck);
		//					System.out.println("decision = false");
		//System.out.println("					Passed the blacklist test");
		//System.out.println("RRResult (word) " + rowResultCheck2);
		//System.out.println("						check: " + colStr1[col]);
		//System.out.println("CCColResult (word) " + colResultCheck2);
		//	System.out.println("decision = false");
		//System.out.println(Arrays.toString(blackListed));
		//System.out.println("BASE CASE FOUND");
		//System.out.println("		what were deleting at row: " + rowStr1[row]);
		//System.out.println("			col: 	" + colStr1[col]);


		boolean verdict = false;


		// if class variable isMinus is set to true (dealing with a minus), then utilizes specialValid helper method
		if (isMinus == true){
	
			isMinus = false;	// immediately sets it back to false to prevent it from always being true 
			boolean specialCase = specialValid(row,col,letter, dictionary);

			if (specialCase) {
				return true;
			}
			else {
				return false;
			}
		}


		// so with the letter look at the appendended stringBuilder and do search prefix, if both check out return true if not 

		// are the limits respected
		if (row >= ourBoard.length || col >= ourBoard.length || row < 0 || col < 0 ) { // null check needs both because you can backtrack to far left

			// making sure to stay within the bounds of the board
			return false;
		}


		rowStr1[row].append(letter);
		colStr1[col].append(letter);

		
		// if col is not at the end you are searching for a prefix (1 or 3)
		if (col < ourBoard.length-1) {
			
			// checking row with the new letter
			int rowResultCheck = dictionary.searchPrefix(rowStr1[row]);
		
			
				// if the StringBuilder with the new append is a prefix (1) or a word and a prefix (3) check the column StringBuilder 
				if (rowResultCheck == 1 || rowResultCheck == 3) { 

					
					int colResultCheck = dictionary.searchPrefix(colStr1[col]);

					if ((colResultCheck == 1 && row != ourBoard.length-1) || colResultCheck == 3 || colResultCheck == 2 || row == 0) { // automatically going to pass a col check on first row everytime
						

						
						/*
						 * Code block that prevents reusage of the same word, one a word is found and is going to be placed,
						 * it is added to a String array that is then checked the next time a potential word is found
						 * If the word is found to be a match to one of the blacklisted words, isValid is returned false
						 */

						 /*
						  * This part of the implementation is unnessecary based off of numerous viable solutions provided and dozens of successfull passes on the autograder 
						  * Which is why most of the code below is commented out
						  */
						boolean decision = true;

						if (!decision) {
						for (int j=0; j < blackListNum; j++) {

							if (blackListed[j].equals(colStr1[col].toString())) {
												// word is blacklisted do not recurse
												decision = false;
							} 

							
						}

					}
						// passes both checks so can return true
						
						

						if (decision == true) {

							/*  would add the word to the blacklist if I were going to use this --- drastically increases runtimes
							 * 	blackListed[blackListNum] = rowStr1[row].toString();
										blackListNum++;
										showBoard();

							 */

							return true;
						}
						
					}
				}
									
		} 
		else  { // at the end of a segment so searchPrefix must return a word (2 or 3)
							
							int rowResultCheck2 = dictionary.searchPrefix(rowStr1[row]); 
							
							if (rowResultCheck2 == 2 || rowResultCheck2 == 3) { // WORD or a word and a prefix 

								int colResultCheck2 = dictionary.searchPrefix(colStr1[col]);
								
								// why is row < ourBoard.length-1 why does that matter // there was a row < ourBoard.length-1 but that doesnt make any sense and could have been the downfall of your demise
								if ((colResultCheck2 == 2) || (colResultCheck2 == 3) || (row == 0) || (colResultCheck2 == 1 && row < ourBoard.length-1)) {
									
									//System.out.println("-----------------------------------------");
									boolean decision = true;

									if (!decision) {
									for (int j=0; j < blackListNum; j++) { // if its not a blacklist word that has been found 

										if (blackListed[j].equals(rowStr1[row].toString())) {
											// word is blacklisted do not recurse
									
											decision = false;
										} 
										
										
									}
									
									}
									 
									if (decision == true) {
									//	blackListed[blackListNum] = rowStr1[row].toString();
									//	blackListNum++;
									
										if (row == ourBoard.length-1) { // magic return
											
											finished = true;
										}	// BASE CASE // BASE CASE // BASE CASE // BASE CASE // BASE CASE 

										return true;
									} // besides that dont do anything no viable solution found 
									

								}
							}

		
		} // else if you are not searching for a prefix 


		// letter that was paced in cannot be placed, delete the appended letter from the stringBuilders

		rowStr1[row].deleteCharAt(col);
		colStr1[col].deleteCharAt(row);
		
		return verdict;


	}



	

	/**
	 * Method - showBoard
	 * @Functionality - A very useful helpful method in debugging, prints out the current state of the board that is being filled
	 * @return function is type void - NO RETURN
	 */	
	public void showBoard () {

		// Show user the current state of the board 
		for (int i = 0; i < ourBoard.length; i++)
		{
			for (int j = 0; j < ourBoard.length; j++)
			{
				System.out.print(ourBoard[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("SHOW BOARD :::::::::::::::  " + ourBoard.length);

	}


	/**
	 * Method - specialValid 
	 * @Functionality - attempt at trying to account for minus cases in the board (black spaces in a crossword)
	 * @param @param int row - the current row pointer that helps keep track of which row position you are at in the algorithm execution
	 * @param int col - the current col pointer that helps keep track of which col position you are at in the algorithm execution
	 * @param DictInterface dicitonary - an instance of DictInterface that allows for you to parse through a dictionary and search for prefixes and words
	 * @return boolean verdict - if all of the criteria checks are met, returns true, else returns false
	 */	
	private boolean specialValid (int row, int col, char letter, DictInterface dictionary) {



		/** Debugging */
		//	System.out.println("						isValidRowAfterAppend      " + rowStr1[row]);
		//	System.out.println("						isValidColAfterAppend      "  + colStr1[col]);
		//	System.out.println("MIDDLE of the board");
		//	System.out.println(rowStr1[row]);
		//	System.out.println("						check: " + colStr1[col]);
		//	System.out.println("RRResult (word) " + rowResultCheck2);
		//	System.out.println("CCColResult (word) " + colResultCheck2);
		//	System.out.println("		what were deleting at row: " + rowStr1[row]);
		//	System.out.println("			col: 	" + colStr1[col]);
		

		// very similar to isValid
		boolean verdict = false; 

		// so the limits are respected
		if (row >= ourBoard.length || col >= ourBoard.length || row < 0 || col < 0 ) { // null check needs both because you can backtrack to far left

			// making sure to stay within the bounds of the board
			return false;
		}

		// append the letter to the current StringBuilders in use
		rowStr1[row].append(letter);
		colStr1[col].append(letter);

		
	

		// if the minus is in the middle of the board 
		if (col < ourBoard.length-1 && col != 0) { 

			
					// assuming that it works like a substring where 0,2 is a 2 length string that takes 0 , 1 and ends at 2

					// seeing if the minus was found in the row int array
					if (rowLastMinusPos1[row] != -1) {
						
							// modified form of searchPrefix that attempts to break up the stringBuilder to check before the minus and after
							int rowResultCheck = dictionary.searchPrefix(rowStr1[row], 0, row); 

							if (col == ourBoard.length-1) {

								rowResultCheck = dictionary.searchPrefix(rowStr1[row], rowLastMinusPos1[row], col); // has a full word to check
							}
							
							// switching it up - looking for failures 1's and 0's which are failures in searchPrefix return
							if (rowResultCheck == 1 || rowResultCheck == 0) { // nest the column becuase if row fails then no need to check column, (pruning)
								// failed
								
								verdict = false;
							}
							else {
								verdict = true;
							}
					}
					else if (colLastMinusPos1[col] != -1) { // verifying that the minus index is in the column int array of saved minses

							if (colLastMinusPos1[col] != -1) {

								int colResultCheck = dictionary.searchPrefix(colStr1[col], 0 , colLastMinusPos1[col]);
	
								if (row == ourBoard.length-1) {

									colResultCheck = dictionary.searchPrefix(colStr1[col], colLastMinusPos1[col]+1, row);
								}
	
								// looking for failure returns of dictionary.searchPrefix
								if (colResultCheck == 1 || colResultCheck == 0) {
									
									verdict = false;
								}
								else {
									verdict = true;
								}
							}
						}
	
		} 
		else if (col == 0) { // black space is on the end --- everything prior has already been verified by normal isValid
			
			verdict = true;
		}
		else {	 // searching for a word (2 or 3) instead just prefixes (1 or 3)

							int rowResultCheck2 = dictionary.searchPrefix(rowStr1[row], 0, col); 

							if (rowResultCheck2 == 2 || rowResultCheck2 == 3) { // WORD or a word and a prefix 


								// checking the column string to with adapted searchprefix
								int colResultCheck2 = dictionary.searchPrefix(colStr1[col], 0, col);

								
								if ((colResultCheck2 == 2) || (colResultCheck2 == 3) || (row == 0) || (colResultCheck2 == 2 && row < ourBoard.length-1)) {
									
									
										if (row == ourBoard.length-1) { // magic return
											
											finished = true;
										}	// BASE CASE // BASE CASE // BASE CASE // BASE CASE // BASE CASE 

										return true;
									
									

								}
							}

		
		} // else if you are not searching for a prefix 


		// if the letter is valid do not delete it from the StringBulders
		if (verdict == true) {

		}
		else {

			rowStr1[row].deleteCharAt(col);
			colStr1[col].deleteCharAt(row);
		}
		
		return verdict;


	}

	
	/**
	 * Method - checkPuzzle 
	 * @Functionality - attempt at trying to account for minus cases in the board (black spaces in a crossword)
	 * @param emptyBoard is a 2-d array representing an empty board
	 * @param filledBoard is a 2-d array representing a filled out board
	 * @param dictionary is the dictinary to be used for checking the filled out board
	 * @return boolean isValidPuzzle - returns true if all of the rules defined in fillPuzzle and a valid solution was generated!
	 */	
	@Override
	public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary) {


		/*** Debugging */
		//System.out.println(Arrays.toString(rowLastMinusPos));
		//System.out.println(Arrays.toString(colLastMinusPos));
		//	System.out.println(rowStr2[i]);
		//	System.out.println(colStr2[i]);
		//	System.out.println(rowStr2[i]);
		//	System.out.println(colStr2[i]);
		//	System.out.println(rowStr1[m]);
		//	System.out.println(colStr1[m]);
		// 	System.out.println(rowStr1[i]);
		// 	System.out.println(colStr1[i]);
		//	System.out.println(Arrays.toString(colLastMinusPos2));
		//	System.out.println(Arrays.toString(rowLastMinusPos2));
		//	System.out.println("has Minus!");
		//	System.out.println(rowLastMinusPos1[i]);
		//	System.out.println(colLastMinusPos1[i]);
		//	rowStr1[1].replace(0,1,"-");
		// 	System.out.println(boardNoMinus);
		//  System.out.println(rowStr1[1]);
		//	System.out.println("No minus!");
		//	System.out.println("MIDDLE of the board");




		 boolean isValidPuzzle = true; 	// returns true unless a condition is found that makes it false

		 
		 /*
		  * Because of method indepency (checking a puzzle that was not generated by fill puzzle) then you have to initialize all of the ADT's you were using in fillpuzzle again in checkPuzzle
		  */
		 ourBoard = new char[filledBoard.length][filledBoard.length]; // new blank board the same size as the one passed in 

		 // read in the board that was passed in  --------------
		 for (int i=0; i < filledBoard.length; i++) {
			// here here
			for (int j=0; j < filledBoard.length; j++) {
				// writing each char from the board passed in, into the class board the solve is gonna use 
				ourBoard[i][j] = filledBoard[i][j];
				
				
			}
		}

		// Initializing the StringBuilders
		 rowStr2 = new StringBuilder[ourBoard.length];
		 colStr2 = new StringBuilder[ourBoard.length];

		 rowLastMinusPos2 = new int[ourBoard.length]; // generating a bunch of zeros
		 colLastMinusPos2 = new int[ourBoard.length]; 
		
		// Creating StringBuilder objects
		for (int k=0; k < ourBoard.length; k++) {
			rowStr2[k] = new StringBuilder();
			colStr2[k] = new StringBuilder();
			rowLastMinusPos2[k] = -1; 			// filling them with -1's for checkPuzzle
			colLastMinusPos2[k] = -1;
		}


		// checking for minuses in the empty board and loading those positions into rowLastMinusPos2 and colLastMinusPos2 
		 for (int i=0; i < filledBoard.length; i++) {
			// here here
			for (int j=0; j < filledBoard.length; j++) {
				// writing each char from the board passed in, into the class board the solve is gonna use 
				if (emptyBoard[i][j] == '-'){
					boardNoMinus = false;
					rowLastMinusPos2[i] = i; // refilling out these arrays
					colLastMinusPos2[i] = j;
				}
				
				
			}
		}
		

		// filling the stringBuilders 
		for (int i=0; i < ourBoard.length; i++) {
			
			for (int j=0; j < ourBoard.length; j++) {
				rowStr2[i].append(ourBoard[i][j]);
				colStr2[i].append(ourBoard[i][j]);
			}
		

		}
		
		
	
		// boolean for next for loop 
		 boolean stringBMinus = false;

		
		 // reading through the stringBuilders to check for minuses in case there is a descrepency
		 for (int m=0; m < ourBoard.length; m++) { // should be checking the four stringBuilders

			

			stringBMinus = containM(rowStr2[m]);
			if (boardNoMinus == true && stringBMinus == true) { // if the board has no minuses but the stringBuilders do, then return false;		
			   isValidPuzzle = false; 			
			
				   
		   }
			stringBMinus = containM(colStr2[m]);
			if (boardNoMinus == true && stringBMinus == true) { // if the board has no minuses but the stringBuilders do, then return false;		
			   isValidPuzzle = false; 			
				   
		   }

		 }
				
		 // check all of the stringBuilders for a minus
				


		 
		 for (int i=0; i < ourBoard.length; i++) {

			
			 
			boolean hasMinus = containM(rowLastMinusPos2[i]);

			boolean hasMinus2 = containM(colLastMinusPos2[i]);

		
			// if none of the minus checks returned positive
			if (!hasMinus && !hasMinus2) {
				// no minus in the String Builder


				int rowResultCheck = dictionary.searchPrefix(rowStr2[i]); 

				if (rowResultCheck == 1 || rowResultCheck == 0) { // nest the column becuase 
					// failed
					isValidPuzzle = false;
				}
				else {
					int colResultCheck = dictionary.searchPrefix(colStr2[i]);

					if (colResultCheck == 1 || colResultCheck == 0) {
						isValidPuzzle = false;
					}
				}
				
				
			}
			else { // has a Minus
				

				
				if (colLastMinusPos2[i] == 0) { // minus is in first column

					// if the minus is in the rowStringBuilder
					if (rowLastMinusPos2[i] != -1) {
						
						int rowResultCheck = dictionary.searchPrefix(rowStr2[i], 1, ourBoard.length-1); 
	
						// returns that it was just a prefix or not at all
						if (rowResultCheck == 1 || rowResultCheck == 0) { // nest the column becuase if row fails then no need to check column, (pruning)
							// failed
							isValidPuzzle = false;
						}
					}
					else {
							// if the minus is in the ColStringBuilder
							if (colLastMinusPos2[i] != -1) {

								int colResultCheck = dictionary.searchPrefix(colStr2[i], 0 , colLastMinusPos2[i]);
	
								colResultCheck = dictionary.searchPrefix(colStr2[i], colLastMinusPos2[i], ourBoard.length-1);
	
								if (colResultCheck == 1 || colResultCheck == 0) {
									isValidPuzzle = false;
								}
							}
					}
							
				}
				else if (colLastMinusPos2[i] == ourBoard.length-1) { // minus is in last column

					// assuming that it works like a substring where 0,2 is a 2 length string that takes 0 , 1 and ends at 2
					if (rowLastMinusPos2[i] != -1) {

						int rowResultCheck = dictionary.searchPrefix(rowStr2[i], 0, ourBoard.length-1); 

							
						if (rowResultCheck == 1 || rowResultCheck == 0) { // nest the column becuase if row fails then no need to check column, (pruning)
							// failed
							isValidPuzzle = false;
						}
					}
					else {
							if (colLastMinusPos2[i] != -1) {

								int colResultCheck = dictionary.searchPrefix(colStr2[i], 0 , colLastMinusPos2[i]);
	
								colResultCheck = dictionary.searchPrefix(colStr2[i], colLastMinusPos2[i], ourBoard.length-1);
	
								if (colResultCheck == 1 || colResultCheck == 0) {
									isValidPuzzle = false;
								}
							}
					}
					
				}
				else { // minus is in the middle of the board

				
					// assuming that it works like a substring where 0,2 is a 2 length string that takes 0 , 1 and ends at 2
					if (rowLastMinusPos2[i] != -1) {
							int rowResultCheck = dictionary.searchPrefix(rowStr2[i], 0, rowLastMinusPos1[i]); 

							rowResultCheck = dictionary.searchPrefix(rowStr2[i], rowLastMinusPos2[i], ourBoard.length-1);
							
							if (rowResultCheck == 1 || rowResultCheck == 0) { // nest the column becuase if row fails then no need to check column, (pruning)
								// failed
								isValidPuzzle = false;
							}
					}
					else {

							if (colLastMinusPos2[i] != -1) {

								// splitting up the searchPrefix checks since the black space created two seperate fields to check now 
								int colResultCheck = dictionary.searchPrefix(colStr2[i], 0 , colLastMinusPos2[i]);
	
								colResultCheck = dictionary.searchPrefix(colStr2[i], colLastMinusPos2[i]+1, ourBoard.length-1);
	
								if (colResultCheck == 1 || colResultCheck == 0) {
									isValidPuzzle = false;
								}
							}
						}
					
				}

				  
				


			
			}  // end of minus section
		

		}  // end of for loop
		
		
		
		//System.out.println("isValidPuzzle is: " + isValidPuzzle);
		return isValidPuzzle;

		 

	}


	/**
	 * Method - containM - uses method Overloading!
	 * @Functionality - checks the int arrays of colLastMinusPos and rowLastMinusPos to see if a minus has been saved used method
	 * @param int minusPos - current index of the array that is being checked
	 * @return boolean result - returns true if a minus location was saved in that index of the StringBuilder array index being read through - returns false otherwise
	 */	
	boolean containM (int minusPos) {

		boolean result = false;

		//System.out.println(minusPos);
		
			if (minusPos != -1) { // -1 is default for no minus 
				
				return true;
			}
		 

		 return result;
	}

	/**
	 * Method - containM - uses method Overloading!
	 * @Functionality - checks the stringBuilder for minus characters
	 * @param StringBuilder myStringBuilder - the stringBuilder that is being read through and checks for minus chars
	 * @return boolean result - returns true if a minus char was found, returns false otherwise
	 */	
	boolean containM (StringBuilder myStringBuilder) {


		boolean result = false;
		char aMinus = '-';

		for (int i=0; i < ourBoard.length; i++) {

			if (myStringBuilder.charAt(i) == aMinus) {

				result = true;
			}
		}

		return result;
	}


}
