import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ScrollPane;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import database.Database;

/**
 * Creates and displays the GUI
 *
 */
public class NutritionMain extends JFrame
{
	private BorderLayout borderlayout = new BorderLayout();
	
	public NutritionMain()
	{
		//Sets the title and icon
		super("Nutrition Database");
		setIconImage(new ImageIcon("HEAD_ICON.png").getImage());
		
		this.setLayout(borderlayout);
		
		//Initiates the panel setter (card layout) 
		//Default displays the home panel
		PanelSetter panelSetter = new PanelSetter(borderlayout);
		this.getContentPane().add(panelSetter);
		panelSetter.showHome();
		
		//Creates and adds the header
		Header header = new Header(panelSetter);
		this.add(header, BorderLayout.PAGE_START);	
		
		this.pack();
		
	}

	public static void main(String[] args)
	{
		// Set up frame/grid
		NutritionMain frame = new NutritionMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 700);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocusInWindow();
		frame.setFocusable(true);
		frame.setVisible(true);

	}
}
