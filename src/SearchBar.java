import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The search bar. Modified textfield, allows users to search up queries and
 * passes them on respectively to retrieve data from the database, returning the
 * retrieved information for display.
 */
public class SearchBar extends JTextField implements DocumentListener {

	protected long updated;
	private PanelSetter control;
	private ResultsPanel resultsPanel;
	private boolean isChanging;

	public SearchBar(PanelSetter control) {
		this.control = control;
		this.setBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		// Sets the preferred size and font of the search bar
		this.setPreferredSize(new Dimension(150, 35));
		this.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.setText("Search");
		this.setForeground(Color.LIGHT_GRAY);
		this.getDocument().addDocumentListener(this);

		// When the search bar gains focus it will delete the search hint, set
		// the text to black, and highlight the border of the search bar.
		// When the search bar loses focus it will return to the default unless
		// text has been entered.
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent event) {
				if (getText().equals("Search")) {
					SearchBar.this.resetText();
					setForeground(Color.BLACK);
				}
				setBorder(javax.swing.BorderFactory.createLineBorder(new Color(123, 227, 226)));
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				setBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY));

			}

		});
		this.updated = 0;
	}

	public SearchBar(PanelSetter control, ResultsPanel resultsPanel) {
		this.control = control;

		// Sets the preferred size and font of the search bar
		this.setPreferredSize(new Dimension(150, 35));
		this.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.getDocument().addDocumentListener(this);

		// When the search bar gains focus it will delete the search hint, set
		// the text to black, and highlight the border of the search bar.
		// When the search bar loses focus it will return to the default unless
		// text has been entered.
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent event) {
				SearchBar.this.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(123, 227, 226)));

			}

			@Override
			public void focusLost(FocusEvent arg0) {

				SearchBar.this.setBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY));

			}

		});
		this.updated = 0;
		this.resultsPanel = resultsPanel;
	}

	/**
	 * Retrieves another round of data based on changes in user query
	 */
	private void update() {
		if (!this.isChanging()) {
			this.updated += 1;
			if (Integer.MAX_VALUE - this.updated < 2) {
				this.updated = 0;
			}
			new Updatable(this.updated) {
				public void run() {
					try {
						boolean reloadResults = false;
						if (SearchBar.this.resultsPanel == null) {
							reloadResults = true;
						}
						Thread.sleep(PanelSetter.SEARCH_DELAY);
						if (this.getUpdateTime() == SearchBar.this.getLastUpdated()) {
							SearchBar.this.control.showResults(SearchBar.this.getText(), reloadResults);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	/**
	 * Sets text without updating
	 */
	public void resetText() {
		setChanging(true);
		this.setText("");
		setChanging(false);
	}

	/**
	 * Changing state of search bar (to prevent 'update()' method from
	 * activating while changing text)
	 * 
	 * @param change
	 *            State
	 */
	public synchronized void setChanging(boolean change) {
		this.isChanging = change;
	}

	/**
	 * Getter for current state of search bar
	 * 
	 * @return Current state of search bar
	 */
	public synchronized boolean isChanging() {
		return isChanging;
	}

	/**
	 * Getter for the latest value of 'updated', which represents the number of
	 * times the user has searched something
	 * 
	 * @return Number of times user searched something
	 */
	public synchronized long getLastUpdated() {
		return this.updated;
	}

	/**
	 * When the user query is changed, update the results again and reshow the
	 * results
	 */
	@Override
	public void changedUpdate(DocumentEvent event) {
		update();
	}

	/**
	 * When the user query is changed, update the results again and reshow the
	 * results
	 */
	@Override
	public void insertUpdate(DocumentEvent event) {
		update();
	}

	/**
	 * When the user query is changed, update the results again and reshow the
	 * results
	 */
	@Override
	public void removeUpdate(DocumentEvent event) {
		update();
	}
}
