import database.Database;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Loading screen that's displayed while the database loads
 *
 */
public class Loading extends JPanel {
	private PanelSetter mainPanel;
	private Database database;
	private JLabel label;

	public Loading(PanelSetter mainPanel) {
		this.mainPanel = mainPanel;
		this.database = mainPanel.getDatabase();
		setPreferredSize(new Dimension(900, 600));
		setBackground(Color.LIGHT_GRAY);
		this.label = new JLabel();
		this.label.setFont(new Font("Serif", 0, 24));
		add(this.label);
		displayLoadMessages();
	}

	private void displayLoadMessages() {
		new Thread() {
			public void run() {
				while (!Loading.this.database.isLoaded()) {
					Loading.this.label.setText(Loading.this.database
							.getLoadingMessage());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Loading.this.mainPanel.doneLoading();
			}
		}.start();
	}

	/**
	 * Sets the background of the loading screen
	 */
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.drawImage(new ImageIcon("LOADING.png").getImage().getScaledInstance(900, 600, Image.SCALE_DEFAULT), 0, 0, null);
		
	}
}
