import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Header class, can be accessed from any panel and used to navigate basically
 * between the home panel, results panel, and about panel.
 */
public class Header extends JPanel {

	private PanelSetter control;
	private Image home, homeLT, search, searchLT, about, aboutLT;

	Header(PanelSetter control) {

		// load images for header
		home = new ImageIcon("home.png").getImage().getScaledInstance(40, 35,
				Image.SCALE_SMOOTH);
		homeLT = new ImageIcon("homeLT.png").getImage().getScaledInstance(40,
				35, Image.SCALE_SMOOTH);
		search = new ImageIcon("search.png").getImage().getScaledInstance(30,
				30, Image.SCALE_SMOOTH);
		searchLT = new ImageIcon("searchLT.png").getImage().getScaledInstance(
				30, 30, Image.SCALE_SMOOTH);

		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.control = control;

		// Adds the search button first (farthest from the right)
		// Sets the border and background to invisible for design
		JButton searchBtn = new JButton(new ImageIcon(search));
		searchBtn.setBorderPainted(false);
		searchBtn.setContentAreaFilled(false);
		searchBtn.setOpaque(false);
		searchBtn.setFocusPainted(false);
		searchBtn.setRolloverEnabled(true);
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				control.showResults("");
			}

		});
		searchBtn.setRolloverIcon(new ImageIcon(searchLT));
		this.add(searchBtn);

		// Applies the same to the home button
		JButton homeBtn = new JButton(new ImageIcon(home));
		homeBtn.setBorderPainted(false);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setOpaque(false);
		homeBtn.setFocusPainted(false);
		homeBtn.setRolloverEnabled(true);
		homeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				control.showHome();
			}

		});
		homeBtn.setRolloverIcon(new ImageIcon(homeLT));
		this.add(homeBtn);

	}

}
