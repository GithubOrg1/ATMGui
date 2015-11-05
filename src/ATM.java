public class ATM implements Runnable {
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

    private static final int ACCOUNT_MODE = 4;


    // Possible values for state

    /** The ATM is off.  The switch must be turned on before it can operate
     */
    private static final int OFF_STATE = 0;

    /** The ATM is on, but idle.  It can service a customer, or it can be shut down
     */
    private static final int IDLE_STATE = 1;

    /** The ATM is servicing a customer.
     */
    private static final int SERVING_CUSTOMER_STATE = 2;

    // constants corresponding to main menu options
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int DEPOSIT = 3;
    private static final int EXIT = 4;


    // State information

    /** The current state of the ATM - one of the possible values listed below
     */
    private int state;

    /** Becomes true when the operator panel informs the ATM that the switch has
     *  been turned on - becomes false when the operator panel informs the ATM
     *  that the switch has been turned off.
     */
    private boolean switchOn;

    /** Becomes true when the card reader informs the ATM that a card has been
     *  inserted - the ATM will make this false when it has tried to read the
     *  card
     */
    private boolean cardInserted;
    public boolean userAuthenticated; // whether user is authenticated
    private int currentAccountNumber; // current user's account number
    private GuiScreen screen; // ATM's screen
    private GuiKeyPad keypad; // ATM's keypad
    private CashDispenser cashDispenser; // ATM's cash dispenser
    private DepositSlot depositSlot; // ATM's deposit slot
    private BankDatabase bankDatabase; // account information database
    private GuiATM gatm;

    // ATM constructor initializes instance variables
    public ATM(GuiATM gatm) {
        this.gatm = gatm;
        userAuthenticated = false; // user is not authenticated to start
        currentAccountNumber = 0; // no current account number to start

        screen = gatm.screen;
        keypad = gatm.keypad;

        cashDispenser = new CashDispenser(); // create cash dispenser
        depositSlot = new DepositSlot(); // create deposit slot
        bankDatabase = new BankDatabase(); // create acct info database

        //start up ...
        screen.displayMessageLine("#GITHUBORG1");

        state = IDLE_STATE;
        switchOn = true;
        cardInserted = false;
    }
     // end no-argument ATM constructor

    public void run() {
        while (true) {
            System.out.println(".");

            switch (state) {
            case OFF_STATE:
                screen.displayMessageLine("Not currently available");

                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }

                if (switchOn) {
                    state = IDLE_STATE;
                }

                break;

            case IDLE_STATE:
                screen.clearScreen();
                screen.displayMessageLine("Please insert card.\n <----------\n");
                cardInserted = false;

                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }

                if (cardInserted) {
                    state = SERVING_CUSTOMER_STATE;
                } else if (!switchOn) {
                    state = OFF_STATE;
                }

                break;

            case SERVING_CUSTOMER_STATE:

                // The following will not return until the session has
                // completed
                authenticateUser(); // authenticate user

                if (!userAuthenticated) {
                    break;
                }

                performTransactions(); // user is now authenticated

                state = IDLE_STATE;

                break;
            }
        }
    }

    /** Inform the ATM that the switch on the operator console has been moved
     *  to the "on" position.
     */
    public synchronized void switchOn() {
        switchOn = true;
        notify();
    }

    /** Inform the ATM that the switch on the operator console has been moved
     *  to the "off" position.
     */
    public synchronized void switchOff() {
        switchOn = false;
        notify();
    }

    /** Inform the ATM that a card has been inserted into the card reader.
     */
    public synchronized void cardInserted() {
        cardInserted = true;
        notify();
    }

    public synchronized void cardEjected() {
        cardInserted = false;
        notify();
    }

    /*
       public void run ()
       {
       while(true){
       System.out.println("11");
       // welcome and authenticate user; perform transactions
       // loop while user is not yet authenticated
       while (!userAuthenticated)
       {
       authenticateUser ();       // authenticate user
       }                  // end while

       performTransactions ();    // user is now authenticated
       userAuthenticated = false; // reset before next ATM session
       currentAccountNumber = 0;  // reset before next ATM session
       screen.displayMessageLine ("\nThank you! Goodbye!");
       }
       }                          // end method run


     */

    // attempts to authenticate user against database
    public void authenticateUser() {
        screen.clearScreen();

        screen.displayMessage("Welcome\nPlease enter your \n account number: ");

        String str1 = keypad.readInput(ACCOUNT_MODE, 4);

        //  screen.displayMessage (str1);
        //    int     accountNumber = keypad.intoInt(str1);// input account number
        int accountNumber = keypad.intoInt(str1);


        screen.displayMessage("\nPIN: "); // prompt for PIN

        String str2 = keypad.readInput(PIN_MODE, 4);

        //screen.displayMessage (str2);
        int pin = keypad.intoInt(str2); // input PIN

        // set userAuthenticated to boolean value returned by database
        userAuthenticated = bankDatabase.authenticateUser(accountNumber, pin);

        // check whether authentication succeeded
        if (userAuthenticated) {
            currentAccountNumber = accountNumber; // save user's account #
        } // end if
        else {
            screen.displayMessageLine("Invalid PIN.\n Card ejected!");

            try {
                // to sleep 10 seconds
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // recommended because catching
                // InterruptedException clears interrupt
                // flag
                Thread.currentThread().interrupt();

                // you probably want to quit if the
                // thread is interrupted
            }

            cardEjected();
            userAuthenticated = false;
        }
    }
     // end method authenticateUser

    // display the main menu and perform transactions
    private void performTransactions() {
        // local variable to store transaction currently being processed
        Transaction currentTransaction = null;
        boolean userExited = false; // user has not chosen to exit

        // loop while user has not chosen option to exit system
        while (!userExited) {
            // show main menu and get user selection
            int mainMenuSelection = displayMainMenu();
            System.out.println(String.format("m:%d", mainMenuSelection));

            // decide how to proceed based on user's menu selection
            switch (mainMenuSelection) {
            // user chose to perform one of three transaction types
            case BALANCE_INQUIRY:
            case WITHDRAWAL:
            case DEPOSIT:

                // initialize as new object of chosen type
                currentTransaction = createTransaction(mainMenuSelection);
                currentTransaction.execute(); // execute transaction

                break;

            case EXIT: // user chose to terminate session
                screen.clearScreen();
                screen.displayMessageLine("\nExiting the system...");
                userExited = true; // this ATM session should end

                break;

            default: // user did not enter an integer from 1-4
                screen.displayMessageLine(
                    "\nNot a valid selection. \nTry again.");

                break;
            } // end switch
        }
         // end while
    }
     // end method performTransactions

    // display the main menu and return an input selection
    private int displayMainMenu() {
        screen.clearScreen();
        screen.displayMessageLine("Main menu:\n");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Deposit funds");
        screen.displayMessageLine("4 - Exit");

        screen.displayMessage("\nEnter a choice ");


        int number = Integer.parseInt(keypad.readInput(MENU_MODE, 4));

        return number; // return user's selection
    }
     // end method displayMainMenu

    // return object of specified Transaction subclass
    private Transaction createTransaction(int type) {
        Transaction temp = null; // temporary Transaction variable

        switch (type) {
        case BALANCE_INQUIRY: // create new BalanceInquiry transaction
            temp = new BalanceInquiry(currentAccountNumber, screen, bankDatabase);

            break;

        case WITHDRAWAL: // create new Withdrawal transaction
            temp = new Withdrawal(currentAccountNumber, screen, bankDatabase,
                    keypad, cashDispenser);

            break;

        case DEPOSIT: // create new Deposit transaction
            temp = new Deposit(currentAccountNumber, screen, bankDatabase,
                    keypad, depositSlot);

            break;
        } // end switch

        return temp; // return the newly created object
    }
     // end method createTransaction
}
