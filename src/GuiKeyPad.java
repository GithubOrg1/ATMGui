import java.awt.*;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.*;

import javax.swing.JButton;


public class GuiKeyPad extends Panel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String[] names = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "CLEAR", "ENTER"
    };

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


    /** Read input in ACCOUNT mode - allow user to enter several characters,
     *  and to clear the line if the user wishes; echo as asterisks
     */
    private static final int ACCOUNT_MODE = 4;

    private String str = "";
    private JButton[] buttons;
    private GridLayout gridLayout;
    private GuiScreen display;
    String keystroke;

    /** Current input mode - one of the values defined below
     */
    private int mode;

    /** Current partial line of input
     */
    private StringBuffer currentInput;

    /** Cancellation flag - set to true if user cancels
     */

    private boolean cancelled;


    /** Maximum valid value - used in MENU_MODE only
     */
    private int maxValue;

    //private static int i;
    public GuiKeyPad(GuiScreen disp) {
        display = disp;

        gridLayout = new GridLayout(4, 3, 5, 5);
        buttons = new JButton[12];

        for (int count = 0; count < 12; count++) {
            buttons[count] = new JButton(names[count]);
            add(buttons[count]);
        }

        setLayout(gridLayout);

        //--------------------------------------------------------------
        for (int i = 0; i < 10; i++)
            buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        digitKeyPressed(Integer.parseInt(e.getActionCommand()));
                    }
                });


        //--------------------------------------------------------------
        buttons[10].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearKeyPressed();
                    System.out.println(str);
                }
            });

        buttons[10].setForeground(Color.black);
        buttons[10].setBackground(new Color(255, 128, 128)); // Light red

        buttons[11].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enterKeyPressed();
                    System.out.println(str);
                    str = "";
                }
            });

        buttons[11].setForeground(Color.black);
        buttons[11].setBackground(new Color(128, 128, 255)); // Light blue

        currentInput = new StringBuffer();
        mode = IDLE_MODE;
    }

    private String getKeyPadInput(String digit) {
        keystroke = digit;
        str += digit;
        System.out.println(str);

        return str;
    }

    public String getString() {
        return str;
    }

    // return an integer value entered by user
    public int intoInt(String str) {
        if (str != "") {
            return Integer.parseInt(str); // we assume that user enters an integer
        } else {
            return 0;
        }
    }
    // end method getInput


    /** Read input from the keyboard
     *
     *  @param mode the input mode to use - one of the constants defined below.
     *  @param maxValue the maximum acceptable value (used in MENU_MODE only)
     *  @return the line that was entered - null if user pressed CANCEL.
     */
    synchronized String readInput(int mode, int maxValue) {
        this.mode = mode;
        this.maxValue = maxValue;
        currentInput.setLength(0);

        setEcho("");

        requestFocus();

        try {
            wait();
        } catch (InterruptedException e) {
        }

        this.mode = IDLE_MODE;


        // if (cancelled)
        //return null;
        //else
        return currentInput.toString();
    }

    /** Handle a digit key
     *
     *  @param digit the value on the key
     */
    private synchronized void digitKeyPressed(int digit) {
        switch (mode) {
        case IDLE_MODE:
            break;

        case PIN_MODE: {
            currentInput.append(digit);

            StringBuffer echoString = new StringBuffer();

            //for (int i = 0; i < currentInput.length(); i ++)
            echoString.append('*');
            setEcho(echoString.toString());

            break;
        }

        case ACCOUNT_MODE: {
            currentInput.append(digit);

            //String input = currentInput.toString ();
            //StringBuffer echoString = new StringBuffer ();
            //for (int i = 0; i < currentInput.length(); i ++)
            //echoString.append (Integer.toString(digit));
            setEcho(Integer.toString(digit));

            break;
        }

        case AMOUNT_MODE: {
            currentInput.append(digit);

            //String input = currentInput.toString ();
            setEcho(Integer.toString(digit));

            /*
            if (input.length () == 1)
              setEcho ("0.0" + input);
            else if (input.length () == 2)
              setEcho ("0." + input);
            else
              setEcho (input.substring (0, input.length () - 2) + "." +
                       input.substring (input.length () - 2));*/
            break;
        }

        case MENU_MODE: {
            if ((digit > 0) && (digit <= maxValue)) {
                currentInput.append(digit);
                notify();
            } else {
                getToolkit().beep();
            }

            break;
        }
        }
    }

    /** Handle the ENTER key
     */
    private synchronized void enterKeyPressed() {
        switch (mode) {
        case IDLE_MODE:
            break;

        case PIN_MODE:
        case AMOUNT_MODE:
        case ACCOUNT_MODE:

            if (currentInput.length() > 0) {
                notify();
            } else {
                getToolkit().beep();
            }

            break;

        case MENU_MODE:
            getToolkit().beep();

            break;
        }
    }

    /** Handle the CLEAR key
     */
    private synchronized void clearKeyPressed() {
        switch (mode) {
        case IDLE_MODE:
            break;

        case PIN_MODE:
            display.displayMessageLine("PIN: ");
            currentInput.setLength(0);
            setEcho("");

            break;

        case ACCOUNT_MODE:
            display.displayMessageLine("Again account: ");
            currentInput.setLength(0);
            setEcho("");

            break;

        case AMOUNT_MODE:
            display.displayMessageLine("Again amount: ");
            currentInput.setLength(0);
            setEcho("");

            break;


        case MENU_MODE:
            getToolkit().beep();

            break;
        }
    }


    /** Set the echo string displayed on the display
     *
     *  @param echo the text to set the echo to (the whole line)
     */
    private void setEcho(String echo) {
        display.setEcho(echo);
    }
}
