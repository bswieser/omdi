/* 
	Frame1.java

	Author:			Bernhard Wieser
	Description:	
*/

package anneal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Frame1 extends JFrame 
{

// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
// every time the form is saved. All manual changes will be overwritten.
// BEGIN GENERATED CODE
	// member declarations
// END GENERATED CODE

	public Frame1()
	{
	}

	public void initComponents() throws Exception
	{
	JPanel contentPane = (JPanel)getContentPane();	
// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
// every time the form is saved. All manual changes will be overwritten.
// BEGIN GENERATED CODE
		// the following code sets the frame's initial state
		setSize(new java.awt.Dimension(350, 650));
		contentPane.setLayout( new FlowLayout() );
		setTitle("anneal.Frame1");
		setLocation(new java.awt.Point(0, 0));


		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				thisWindowClosing(e);
			}
		});
// END GENERATED CODE
	final AnnealModel am = new AnnealModel( 100, 0, 0, 300, 300 );
	final AnnealView av = new AnnealView( am );
	JButton button;
	JPanel p;
	JCheckBox checkBox;
	final JTextField numPoints, T, kT, maxSucc, maxIter;
	
	contentPane.add( av );
	
	p = new JPanel();
	p.add( button = new JButton( "New" ) );
	p.add( new JLabel( "#pts" ) );
	p.add( numPoints = new JTextField( "50", 6 ) );
	
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setSize( Integer.parseInt( numPoints.getText() ) );
			am.doRandom();
			}
		} );
	contentPane.add(p);
	
	p = new JPanel();
	p.add( new JLabel( "T" ) );
	p.add( T = new JTextField( ".25", 6 ) );
	p.add( new JLabel( "dT" ) );
	p.add( kT = new JTextField( ".9", 6 ) );
	contentPane.add(p);
	
	p = new JPanel();
	p.add( new JLabel( "Max Succ. *" ) );
	p.add( maxSucc = new JTextField( "10", 6 ) );
	p.add( new JLabel( "Max Iter. *" ) );
	p.add( maxIter = new JTextField( "100", 6 ) );
	contentPane.add(p);
	
	p = new JPanel();
	p.add( checkBox = new JCheckBox( "", am.isSwap() ) );
	p.add( button = new JButton( "SWAP" ) );
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.doSwap();
			}
		} );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setSwap(!am.isSwap());
			}
		} );
	
	p.add( checkBox = new JCheckBox( "", am.isSplice() ) );	
	p.add( button = new JButton( "SPLICE" ) );
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.doSplice();
			}
		} );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setSplice(!am.isSplice());
			}
		} );
		
	p.add( checkBox = new JCheckBox( "", am.isReverse() ) );
	p.add( button = new JButton( "REVERSE" ) );
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.doReverse();
			}
		} );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setReverse(!am.isReverse());
			}
		} );
	
	p.add( checkBox = new JCheckBox( "", am.isShuffle() ) );	
	p.add( button = new JButton( "SHUFFLE" ) );
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.doShuffle();
			}
		} );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setShuffle(!am.isShuffle());
			}
		} );
	contentPane.add(p);
		
	p = new JPanel();
	p.add( button = new JButton( "ANNEAL" ) );
	button.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			try {
				am.setMaxIterations( Integer.parseInt( maxIter.getText() ) );
				am.setMaxSuccesses( Integer.parseInt( maxSucc.getText() ) );
				am.setT( Double.parseDouble( T.getText() ) );
				am.setTFactor( Double.parseDouble( kT.getText() ) );
				//am.doAnneal();
				Thread t = new Thread( am );
				t.start();
				}
			catch( Throwable t )
				{
				System.err.println( t.toString() );
				}
			}
		} );
	contentPane.add(p);
	
	p = new JPanel();
	p.add( checkBox = new JCheckBox( "use metropolis", am.isUseMetropolis() ) );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			am.setUseMetropolis(!am.isUseMetropolis());
			}
		} );
		
	p.add( checkBox = new JCheckBox( "paint immediate", av.isPaintImmediate() ) );
	checkBox.addActionListener( new ActionListener() 
		{
		public void actionPerformed( ActionEvent evt )
			{
			av.setPaintImmediate(!av.isPaintImmediate());
			}
		} );
	contentPane.add(p);
	}
  
  	private boolean mShown = false;
  	
	public void addNotify() 
	{
		super.addNotify();
		
		if (mShown)
			return;
			
		// move components to account for insets
		Insets insets = getInsets();
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			Point location = components[i].getLocation();
			location.move(location.x, location.y + insets.top);
			components[i].setLocation(location);
		}

		mShown = true;
	}

	// Close the window when the close box is clicked
	void thisWindowClosing(java.awt.event.WindowEvent e)
	{
		setVisible(false);
		dispose();
		System.exit(0);
	}
	
}
