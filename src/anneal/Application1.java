/* 
	Application1.java

	Author:			Bernhard Wieser
	Description:	
*/

package anneal;

public class Application1 
{
	public Application1() 
	{
		try {
			Frame1 frame = new Frame1();
			frame.initComponents();
			frame.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Main entry point
	static public void main(String[] args) 
	{
		new Application1();
	}
	
}
