package windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;

import client.Client;
import profiles.Account;
import profiles.ChildProfile;
import profiles.ParentProfile;
import tasks.Task;

/**
 * 
 * @author Henrik Sigeman
 *
 */
public class DisplayWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private JPanel panel;
	private JPanel updatePanel = new JPanel();
	private JButton updateBtn = new JButton("Update");
	private JTextField updateText = new JTextField();
	private Dimension boxDimension = new Dimension(128, 64);
	private ChildTaskWindow ctw;
	private ChildRewardWindow2 crw;
	private ChildProfileWindow cpw;
	private ProfileStartWindow psw;
	private ParentHomeWindow phw;
	private ParentEditTaskWindow petw;
	private ParentTaskWindow ptw;
	private Account account;
	private Client client;

	private ChildProfile childProfile;
	private ParentProfile parentProfile;

	public DisplayWindow(Account account, Client client) {
		this.client = client;
		this.account = account;
		try {
			ctw = new ChildTaskWindow(this);
			crw = new ChildRewardWindow2(this);
			cpw = new ChildProfileWindow(this);
			psw = new ProfileStartWindow("ProfileStartWindow", this);
			phw = new ParentHomeWindow(this);
			petw = new ParentEditTaskWindow(this);
			ptw = new ParentTaskWindow(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updatePanel.setPreferredSize(new Dimension(128, 128));
		this.setPreferredSize(new Dimension(400, 600));
		setLayout(new BorderLayout());
		updateBtn.addActionListener(this);
		updateBtn.setPreferredSize(boxDimension);
		updateText.setPreferredSize(boxDimension);
		updatePanel.setLayout(new BorderLayout());
		updatePanel.add(updateBtn, BorderLayout.NORTH);
		updatePanel.add(updateText, BorderLayout.SOUTH);
		panel = updatePanel;
		add(panel, BorderLayout.CENTER);
		pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public Account getAccount() {
		return account;
	}

	private void setPanel(JPanel panel) {
		this.remove(this.panel);
		this.panel = panel;
		add(this.panel, BorderLayout.CENTER);
		this.revalidate();
		this.panel.updateUI();
	}

	public void setViewParentTaskWindow() {
		setPanel(ptw);
	}

	public void setViewChildTaskWindow() {
		setPanel(ctw);
	}

	public void setViewChildRewardWindow() {
		setPanel(crw);
	}

	public void setViewChildProfileWindow() {
		setPanel(cpw);
	}

	public void setViewProfileStartWindow() {
		setPanel(psw);
	}

	public void setViewParentHomeWindow() {
		setPanel(phw);
	}

	public void setViewParentEditTaskWindow() {
		setPanel(petw);

	}

	public void setParentProfile(String name) {
		for (int i = 0; i < account.getParentProfileList().size(); i++) {
			if (account.getParentProfileList().get(i).getName().equals(name)) {
				parentProfile = account.getParentProfileList().get(i);
			}
		}
	}

	public void setChildProfile(String name) {
		for (int i = 0; i < account.getChildProfileList().size(); i++) {
			if (account.getChildProfileList().get(i).getName().equals(name)) {
				childProfile = account.getChildProfileList().get(i);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateBtn) {
			setPanel(psw);
			try {
				account.setTaskList(client.getTasksFromServer(account));
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// DisplayWindow displayWindow = new DisplayWindow();
	}

}
