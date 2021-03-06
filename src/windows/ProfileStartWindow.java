package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javafx.scene.Parent;
import profiles.Account;
import profiles.ChildProfile;
import profiles.ParentProfile;

/**
 * 
 * @author Henrik The initial profile window
 */
public class ProfileStartWindow extends JPanel {

	private Account account;
	private JButton prof1, prof2, prof3, prof4, prof5, prof6, prof7, prof8, prof9, prof10;
	private ArrayList<JButton> inactiveButtons;
	private ArrayList<JButton> activeButtons;
	private GridBagLayout layout = new GridBagLayout();
	private JPanel profilePanel = new JPanel(layout);
	private ActionListener actionListener;
	private ClientController clientController;

	public ProfileStartWindow(ClientController clientController) {
		this.account = clientController.getAccount();
		this.clientController = clientController;

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < activeButtons.size(); i++) {
					if (e.getSource() == activeButtons.get(i)) {
						if (isParent(activeButtons.get(i).getText())) {
							clientController.setParentProfile(activeButtons.get(i).getText());
							clientController.setViewParentHomeWindow();
						} else {
							clientController.setChildProfile(activeButtons.get(i).getText());
							clientController.setViewChildHomeWindow();
						}
						break;
					}
				}
			}

		};
		initialize();
	}

	private Boolean isParent(String string) {
		Boolean res = false;
		for (int i = 0; i < account.getParentProfileList().size(); i++) {
			if (account.getParentProfileList().get(i).getName().equals(string)) {
				res = true;
				break;
			}
		}

		return res;
	}

	// Initialize the graphical user interface
	private void initialize() {
		this.setBounds(0, 0, 400, 600);
		prof1 = new JButton();
		prof2 = new JButton();
		prof3 = new JButton();
		prof4 = new JButton();
		prof5 = new JButton();
		prof6 = new JButton();
		prof7 = new JButton();
		prof8 = new JButton();
		prof9 = new JButton();
		prof10 = new JButton();
		inactiveButtons = new ArrayList<JButton>();
		activeButtons = new ArrayList<JButton>();
		inactiveButtons.add(prof1);
		inactiveButtons.add(prof2);
		inactiveButtons.add(prof3);
		inactiveButtons.add(prof4);
		inactiveButtons.add(prof5);
		inactiveButtons.add(prof6);
		inactiveButtons.add(prof7);
		inactiveButtons.add(prof8);
		inactiveButtons.add(prof9);
		inactiveButtons.add(prof10);
		this.setBackground(Color.YELLOW);
		profilePanel.setBackground(Color.YELLOW);
		LinkedList<ParentProfile> parents = account.getParentProfileList();
		LinkedList<ChildProfile> children = account.getChildProfileList();

		for (int i = 0; i < parents.size(); i++) {
			activeButtons.add(inactiveButtons.get(0));
			activeButtons.get(i).setPreferredSize(new Dimension(130, 130));
			activeButtons.get(i).addActionListener(actionListener);
			inactiveButtons.remove(0);
			activeButtons.get(i)
					.setIcon(new ImageIcon(clientController.getPictures().getImage(parents.get(i).getImageString())));
			activeButtons.get(i).setText(parents.get(i).getName());
			activeButtons.get(i).setVerticalTextPosition(SwingConstants.BOTTOM);
			activeButtons.get(i).setHorizontalTextPosition(SwingConstants.CENTER);

		}
		for (int i = 0; i < children.size(); i++) {
			JButton button = inactiveButtons.get(0);
			button.setPreferredSize(new Dimension(130, 130));
			button.addActionListener(actionListener);
			inactiveButtons.remove(0);

			button.setIcon(new ImageIcon(clientController.getPictures().getImage(children.get(i).getImageString())));
			button.setText(children.get(i).getName());
			button.setVerticalTextPosition(SwingConstants.BOTTOM);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			activeButtons.add(button);
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		for (int i = 0; i < activeButtons.size(); i++) {
			if (c.gridx >= 2) {
				c.gridx = 0;
				c.gridy++;
			}
			profilePanel.add(activeButtons.get(i), c);
			c.gridx++;
		}

		add(profilePanel, BorderLayout.CENTER);
	}
}
