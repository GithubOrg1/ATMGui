import javax.swing.JFrame;


public class ATMCaseStudy {
    // main method creates and runs the ATM
    public static void main(String[] args) {
        GuiATM ATM = new GuiATM();

        ATM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ATM.setBounds(100, 100, 400, 500);
        ATM.setVisible(true);
    }
     // end main
}
 // end class ATMCaseStudy
