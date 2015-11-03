
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;

import java.awt.event.*;


public class GuiKeyPad extends Panel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String str="";
	private JButton[] buttons;
	public static final String[] names=
		{"1","2","3","4","5","6","7","8","9","0","Delete","Enter"};
	private GridLayout gridLayout;
	//private static int i;
	
	public GuiKeyPad()
	{
		gridLayout=new GridLayout(4,3,5,5);
		buttons=new JButton[12];
		for(int count=0;count<12;count++)
		{
			buttons[count]=new JButton(names[count]);
			add(buttons[count]);
		}
		setLayout(gridLayout);
	
		//--------------------------------------------------------------
	        buttons[0].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[0]);
	                System.out.println(str);
	            }
	        });
	        buttons[1].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[1]);
	                System.out.println(str);
	            }
	        });
	        buttons[2].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[2]);
	                System.out.println(str);
	            }
	        });
	        buttons[3].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[3]);
	                System.out.println(str);
	            }
	        });
	        buttons[4].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[4]);
	                System.out.println(str);
	            }
	        });
	        buttons[5].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[5]);
	                System.out.println(str);
	            }
	        });
	        buttons[6].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[6]);
	                System.out.println(str);
	            }
	        });
	        buttons[7].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[7]);
	                System.out.println(str);
	            }
	        });
	        buttons[8].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[8]);
	                System.out.println(str);
	            }
	        });
	        buttons[9].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	                getKeyPadInput(names[9]);
	                System.out.println(str);
	            }
	        });
		
		//--------------------------------------------------------------
		buttons[10].addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e)
	        {
	        	deleteKeyPressed();
	        	System.out.println(str);
	        }
	    });
		
		buttons[11].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	enterKeyPressed();
            	System.out.println(str);
            	str="";
            }
        });
	}
	

	private String getKeyPadInput(String digit){
		str+=digit;
		System.out.println(str);
		return str;
	}
	/*private String getKeyPadInput(int digit){
		str+=names[digit];
		System.out.println(str);
		return str;
	}
	*/
	private String deleteKeyPressed(){
		if (!str.isEmpty())
		  str=str.substring(0,str.length()-1);
		System.out.println(str);
		return str;
	}
	
	private String enterKeyPressed(){
		System.out.println(str);
		return str;
	}
	
	public String getString(){
		return str;
	}
	
	
	// return an integer value entered by user
	public int intoInt(String str)
	{
		if(str!="")
			return Integer.parseInt(str) ;// we assume that user enters an integer
		else
			return 0;
	} // end method getInput
	
	
}
