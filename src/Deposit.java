public class Deposit extends Transaction {
    private final static int CANCELED = 0; // constant for cancel option
    private final static int UNCANCELED = 1;

    /** Not currently reading input - ignore keys (except CANCEL)
     */
    private static final int IDLE_MODE = 0;

    /** Read input in PIN mode - allow user to enter several characters,
     *  and to clear the line if the user wishes; echo as asterisks
     */
    private static final int PIN_MODE = 1;

    /** Read input in amount mode - allow user to enter several characters,
     *  and to clear the line if the user wishes; echo what use types
     */
    private static final int AMOUNT_MODE = 2;

    /** Read input in menu choice mode - wait for one digit key to be pressed,
     *  and return value immediately.
     */
    private static final int MENU_MODE = 3;
    private double amount; // amount to deposit
    private GuiKeyPad keypad; // reference to keypad
    private DepositSlot depositSlot; // reference to deposit slot
    private int ifCanceled;

    // Deposit constructor
    public Deposit(int userAccountNumber, GuiScreen atmScreen,
        BankDatabase atmBankDatabase, GuiKeyPad atmKeypad,
        DepositSlot atmDepositSlot) {
        // initialize superclass variables
        super(userAccountNumber, atmScreen, atmBankDatabase);

        // initialize references to keypad and deposit slot
        keypad = atmKeypad;
        depositSlot = atmDepositSlot;
    }
     // end Deposit constructor

    // perform transaction
    @Override 
    public void execute() {
        //BankDatabase bankDatabase = getBankDatabase ();	// get reference
        GuiScreen screen = getScreen(); // get reference

        screen.clearScreen();
        screen.displayMessage(
            "\nPlease enter a deposit amount in \n CENTS (or 0 to cancel): ");
        amount = promptForDepositAmount(); // get deposit amount from user

        ifCanceled = state();

        // check whether user entered a deposit amount or canceled
        switch (ifCanceled) {
        // request deposit envelope containing specified amount
        case UNCANCELED:
            screen.displayMessage(
                "\nPlease insert a deposit envelope \ncontaining ");
            screen.displayDollarAmount(amount);
            screen.displayMessageLine(".");
            screen.displayMessageLine("Your envelope has been received." +
                "\nNOTE: The money just deposited " +
                "\nwill not be available until we verify " +
                "\nthe amount of any enclosed cash" +
                "\n and your checks clear.");

            break;

        case CANCELED:
            screen.displayMessageLine("\nCanceling transaction...");
        }

        try {
            // to sleep 10 seconds
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // recommended because catching
            // InterruptedException clears interrupt
            // flag
            Thread.currentThread().interrupt();

            // you probably want to quit if the
            // thread is interrupted
        }
    }  // end method execute
    
	// prompt user to enter a deposit amount in cents
    private double promptForDepositAmount() {
        //GuiScreen screen = getScreen ();	// get reference to screen
        // display the prompt
        //screen.displayMessage ("\nPlease enter a deposit amount in \n CENTS (or 0 to cancel): ");
        int input = keypad.intoInt(keypad.readInput(AMOUNT_MODE, 0)); // receive input of deposit amount

        // check whether the user canceled or entered a valid amount
        return (double) input / 100; // return dollar amount

        // end else
    }
     // end method promptForDepositAmount

    private int state() {
        int num = ifDeposit(amount);

        switch (num) {
        case 0:
            ifCanceled = CANCELED;
            break;

        case 1:
            ifCanceled = UNCANCELED;
            break;
        }

        return ifCanceled;
    }

    private int ifDeposit(double amount) {
        double deposit = amount;

        if (deposit > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
 // end class Deposit
