
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

// hell 
public class GuiATM extends JFrame
{

  private static final long serialVersionUID = 1L;
  public GuiScreen screen;
  public GuiKeyPad keypad;

  public GuiATM ()
  {

    screen = new GuiScreen ();
    keypad = new GuiKeyPad ();

    ATM theATM = new ATM (this);


      setLayout (new GridLayout (2, 2));
      add (new ImagePanel (
			      new ImageIcon("cardslot.jpeg").getImage())); //Card slot
      add (screen);
      add (new Panel ());
      add (keypad, BorderLayout.WEST);

  }
}
