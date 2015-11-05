import java.awt.Color;
import java.awt.Panel;

import javax.swing.JTextArea;


public class GuiScreen extends Panel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextArea txt;

    public GuiScreen() {
        txt = new JTextArea(10, 24);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(false);
        add(txt);
        setBackground(new Color(240, 255, 255));

        //txt.setOpaque(false);
    }

    public void show(String str) {
        //txt.setText(txt.getText()+str);
        System.out.println(str);
        txt.append(str + "\n");
    }

    public void setEcho(String msg) {
        txt.append(msg);
        System.out.println(msg);
    }

    // display a message without a carriage return
    public void displayMessage(String message) {
        txt.append(message);
    }

    // end method displayMessage
    // display a message with a carriage return
    public void displayMessageLine(String message) {
        txt.append("\n" + message);
    }

    // end method displayMessageLine
    // displays a dollar amount
    public void displayDollarAmount(double amount) {
        txt.append(String.format("$%,.2f", amount));
    }

    // end method displayDollarAmount
    public void clearScreen() {
        txt.setText("");
    }
}
