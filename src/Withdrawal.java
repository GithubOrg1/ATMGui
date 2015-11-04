
public class Withdrawal extends Transaction
{
     /** Not currently reading input - ignore keys (except CANCEL)
      */
 // private static final int IDLE_MODE = 0;

     /** Read input in PIN mode - allow user to enter several characters,
      *  and to clear the line if the user wishes; echo as asterisks
      */
  //private static final int PIN_MODE = 1;

     /** Read input in amount mode - allow user to enter several characters,
      *  and to clear the line if the user wishes; echo what use types
      */
  //private static final int AMOUNT_MODE = 2;

     /** Read input in menu choice mode - wait for one digit key to be pressed,
      *  and return value immediately.
      */
  private static final int MENU_MODE = 3;
  
  private static final int OVER_BUDGET = 0;
  
  private static final int WITHIN_BUDGET = 1;
 

  private int amount;		// amount to withdraw
  private GuiKeyPad keypad;	// reference to keypad
  private CashDispenser cashDispenser;	// reference to cash dispenser

  // constant corresponding to menu option to cancel
  private final static int CANCELED = 6;
  private int withdraw;

  // Withdrawal constructor
  public Withdrawal (int userAccountNumber, GuiScreen atmScreen,
		     BankDatabase atmBankDatabase, GuiKeyPad atmKeypad,
		     CashDispenser atmCashDispenser)
  {
    // initialize superclass variables
    super (userAccountNumber, atmScreen, atmBankDatabase);

    // initialize references to keypad and cash dispenser
    keypad = atmKeypad;
    cashDispenser = atmCashDispenser;
  }				// end Withdrawal constructor

  // perform transaction
   @Override public void execute ()
  {
    boolean cashDispensed = false;	// cash was not dispensed yet
    double availableBalance;	// amount available for withdrawal

    // get references to bank database and screen
    BankDatabase bankDatabase = getBankDatabase ();
    GuiScreen screen = getScreen ();

    // loop until cash is dispensed or the user cancels
    do
      {
	// obtain a chosen withdrawal amount from the user
	amount = displayMenuOfAmounts ();
	
	withdraw = ifAvailable(amount);
	
	switch(withdraw)
	{
		case WITHIN_BUDGET:
			bankDatabase.debit (getAccountNumber (), amount);
	
		    cashDispenser.dispenseCash (amount);	// dispense cash
		    cashDispensed = true;	// cash was dispensed
		    // instruct user to take cash
		    screen.displayMessageLine ("Your cash has been dispensed.");
		    screen.displayMessageLine ("Please take your cash now.");
		    break;
		   
		case OVER_BUDGET:
			screen.displayMessageLine
		    ("Insufficient cash available in the ATM." +
		     "\nPlease choose a smaller amount.");
			break;
			
	}

	// check whether user chose a withdrawal amount or canceled
	/*
	if (amount != CANCELED)
	  {
	    // get available balance of account involved
	    availableBalance =
	      bankDatabase.getAvailableBalance (getAccountNumber ());

	    // check whether the user has enough money in the account
	    if (amount <= availableBalance)
	      {
		// check whether the cash dispenser has enough money
		if (cashDispenser.isSufficientCashAvailable (amount))
		  {
		    // update the account involved to reflect the withdrawal
		    bankDatabase.debit (getAccountNumber (), amount);

		    cashDispenser.dispenseCash (amount);	// dispense cash
		    cashDispensed = true;	// cash was dispensed
		    // instruct user to take cash
		    screen.displayMessageLine ("Your cash has been dispensed.");
		    screen.displayMessageLine ("Please take your cash now.");
		  }		// end if
		else		// cash dispenser does not have enough cash
		  screen.displayMessageLine
		    ("Insufficient cash available in the ATM." +
		     "Please choose a smaller amount.");
	      }			// end if
	    else		// not enough money available in user's account
	      {
		screen.displayMessageLine
		  ("Insufficient funds in your account." +
		   "Please choose a smaller amount.");
	      }			// end else
	  }			// end if
	else			// user chose cancel menu option
	  {
	    screen.displayMessageLine("\nCanceling transaction...");
	    return;		// return to main menu because user canceled
	  }			// end else
      
	  */
	  // delay here
      // delay 2s and back to main menu 
      try
      {
        // to sleep 10 seconds
        Thread.sleep (4000);
      } catch (InterruptedException e)
      {
        // recommended because catching
        // InterruptedException clears interrupt
        // flag
        Thread.currentThread ().interrupt ();
        // you probably want to quit if the
        // thread is interrupted
      }
	  
	  } while (!cashDispensed);

  }				// end method execute

  // display a menu of withdrawal amounts and the option to cancel;
  // return the chosen amount or 0 if the user chooses to cancel
  private int displayMenuOfAmounts ()
  {
    int userChoice = 0;		// local variable to store return value

    GuiScreen screen = getScreen ();	// get screen reference

    // array of amounts to correspond to menu numbers
    int[] amounts = { 0, 20, 40, 60, 100, 200 };

    // loop while no valid choice has been made
    while (userChoice == 0)
      {
	// display the withdrawal menu
	screen.clearScreen ();
	screen.displayMessageLine ("Withdrawal Menu:");
	screen.displayMessageLine ("1 - $20");
	screen.displayMessageLine ("2 - $40");
	screen.displayMessageLine ("3 - $60");
	screen.displayMessageLine ("4 - $100");
	screen.displayMessageLine ("5 - $200");
	screen.displayMessageLine ("6 - Cancel transaction");
	screen.displayMessage ("\nChoose [1-6]: ");

	int input = keypad.intoInt (keypad.readInput (MENU_MODE, 6));	// get user input through keypad

	// determine how to proceed based on the input value
	switch (input)
	  {
	  case 1:		// if the user chose a withdrawal amount
	  case 2:		// (i.e., chose option 1, 2, 3, 4 or 5), return the
	  case 3:		// corresponding amount from amounts array
	  case 4:
	  case 5:
	    userChoice = amounts[input];	// save user's choice
	    break;
	  case CANCELED:	// the user chose to cancel
	    userChoice = CANCELED;	// save user's choice
	    screen.displayMessageLine("\nCanceling transaction...");
	    break;
	  default:		// the user did not enter a value from 1-6
	    screen.displayMessageLine ("\nInvalid selection. \nTry again.");
	  }			// end switch
      }				// end while

    return userChoice;		// return withdrawal amount or CANCELED
  }				// end method displayMenuOfAmounts
  
  private int ifAvailable(int amount)
  {
	  int num = ifEnough(amount);
	  
	  switch(num)
	  {
	  case 1:withdraw =  WITHIN_BUDGET;break;
	  case 0:withdraw =  OVER_BUDGET;break;
	  }
			
	  return withdraw;
  }
  
  private int ifEnough(int amount){
	  double availableBalance;
	  BankDatabase bankDatabase = getBankDatabase ();
	  availableBalance =
		      bankDatabase.getAvailableBalance (getAccountNumber ());
	  if(amount > availableBalance)
		  return 0;
	  else
		  return 1;
  }
}
