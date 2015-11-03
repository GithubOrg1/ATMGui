
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


public class GuiATM extends JFrame
{

  private static final long serialVersionUID = 1L;
  public GuiScreen screen;
  public GuiKeyPad keypad;


  public GuiATM ()
  {

    screen = new GuiScreen ();
    keypad = new GuiKeyPad (screen);

    final ATM theATM = new ATM (this);

    // Start the Thread that runs the ATM
    new Thread (theATM).start ();

    Panel p = new Panel ();
    JButton btn = new JButton (">>CARD HERE<<");
      btn.setSize (30, 30);
      p.add (btn, BorderLayout.CENTER);
      btn.addActionListener (new ActionListener ()
			     {
			     public void actionPerformed (ActionEvent e)
			     {
			     theATM.cardInserted ();
			     }
			     });


    setLayout (new GridLayout (2, 2));
    add (new ImagePanel (new ImageIcon ("cardslot.jpeg").getImage ()));	//Card slot
    add (new JScrollPane (screen));
    add (p);
    add (keypad, BorderLayout.WEST);


  }

  public boolean isCardReceived ()
  {
    return true;
  }

}
