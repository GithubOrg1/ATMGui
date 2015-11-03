public class BalanceInquiry extends Transaction
{
  // BalanceInquiry constructor
  public BalanceInquiry (int userAccountNumber, GuiScreen atmScreen,
			 BankDatabase atmBankDatabase)
  {
    super (userAccountNumber, atmScreen, atmBankDatabase);
  }				// end BalanceInquiry constructor

  // performs the transaction
   @Override public void execute ()
  {
    // get references to bank database and screen
    BankDatabase bankDatabase = getBankDatabase ();
    GuiScreen screen = getScreen ();

    // get the available balance for the account involved
    double availableBalance =
      bankDatabase.getAvailableBalance (getAccountNumber ());

    // get the total balance for the account involved
    double totalBalance = bankDatabase.getTotalBalance (getAccountNumber ());

    screen.clearScreen ();
    // display the balance information on the screen
    screen.displayMessageLine ("\nBalance Information:");
    screen.displayMessage ("\n - Available balance: \n");
    screen.displayDollarAmount (availableBalance);
    screen.displayMessage ("\n - Total balance: \n");
    screen.displayDollarAmount (totalBalance);
    //screen.displayMessageLine( "\n\nPress 1:  Exit" );

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




  }				// end method execute
}
