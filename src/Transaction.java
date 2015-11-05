public abstract class Transaction {
    private int accountNumber; // indicates account involved
    private GuiScreen screen; // ATM's screen
    private BankDatabase bankDatabase; // account info database

    // Transaction constructor invoked by subclasses using super()
    public Transaction(int userAccountNumber, GuiScreen atmScreen,
        BankDatabase atmBankDatabase) {
        accountNumber = userAccountNumber;
        screen = atmScreen;
        bankDatabase = atmBankDatabase;
    }
     // end Transaction constructor

    // return account number
    public int getAccountNumber() {
        return accountNumber;
    }

    // end method getAccountNumber
    // return reference to screen
    public GuiScreen getScreen() {
        return screen;
    }

    //return reference to bank database
    public BankDatabase getBankDatabase() {
        return bankDatabase;
    }
     // end method getBankDatabase

    // perform the transaction (overridden by each subclass)
    abstract public void execute();
}
 // end class Transaction
