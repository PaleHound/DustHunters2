package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import profiles.Account;
import profiles.ChildProfile;
import profiles.ParentProfile;
import rewards.Reward;
import server.ServerRequest;
import tasks.Task;
import windows.ClientController;

/**
 * 
 * @author Henrik Sigeman
 *
 */
public class Client extends Thread {
	@SuppressWarnings("unused")
	private OutputStream os;
	private InputStream is;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private BufferedReader br;
	private String serverRequest;
	private Boolean isActive = false;
	private Observable observable = new Observable();
	private int port;
	private String host;

	/**
	 * Creates a client
	 * 
	 * @param user
	 *            the usercontroller
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(int port, String host) throws UnknownHostException, IOException {
		this.port = port;
		this.host = host;
	}

	public Boolean isActive() {
		return isActive;
	}

	private void setActive(String s) {
		System.out.println(s + " initiated.");
		isActive = true;
	}

	private void setInactive(String s) {
		System.out.println(s + " finished.");
		observable.notifyObservers();
		isActive = false;
	}

	public void addObserver(Observer o) {
		observable.addObserver(o);
	}

	private void openStreams() {
		try {
			socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			ois = new ObjectInputStream(is);
			oos = new ObjectOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// System.out.println("Streams open");
		} catch (IOException e) {
			System.out.println("Ojd�!");
			e.printStackTrace();
		}
	}

	/**
	 * Closes all of the streams.
	 * 
	 * @throws IOException
	 */
	private void closeStreams() throws IOException {
		oos.close();
		ois.close();
		br.close();
		os.close();
		is.close();
		socket.close();
		// System.out.println("Streams closed");
	}

	public void addPointsToChildProfile(Account account, String childProfileName, int pointsToAdd) {
		serverRequest = "addPoints";
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param account
	 *            The account to be registered.
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String sendRegisterToServer(Account account) throws UnknownHostException, IOException, InterruptedException {
		serverRequest = "Register";
		openStreams();
		// Send register request to server.
		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.flush();
		// Wait for response from server.
		String res = null;
		while (res == null) {
			Thread.sleep(500);
			res = br.readLine();
		}
		closeStreams();
		return res;
	}

	public LinkedList<Task> getTasksFromServer(Account account)
			throws UnknownHostException, IOException, ClassNotFoundException {
		setActive("Getting tasks ");
		serverRequest = "GetTasks";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.flush();
		LinkedList<Task> tasks = new LinkedList();
		tasks = (LinkedList<Task>) ois.readObject();
		closeStreams();
		setInactive("Getting tasks ");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}

	public void addRewardToServer(Account account) {

	}

	public void addChildProfileToServer(Account account, ChildProfile childProfile) throws IOException {
		setActive("Add child Profile ");
		serverRequest = "AddChildProfile";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(childProfile);
		oos.flush();

		closeStreams();
		setInactive("Add child Profile ");
	}

	public void removeParentProfileFromServer(Account account, ParentProfile parentProfile) throws IOException {
		setActive("Remove parentprofile");
		serverRequest = "RemoveParentProfile";

		openStreams();
		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(parentProfile);
		oos.flush();

		closeStreams();
		setInactive("Remove parentprofile ");
	}

	public void removeChildProfileFromServer(Account account, ChildProfile childProfile) throws IOException {
		setActive("Remove childprofile ");
		serverRequest = "RemoveChildProfile";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(childProfile);
		oos.flush();

		closeStreams();
		setInactive("Remove childprofile ");
	}

	public void addParentProfileToServer(Account account, ParentProfile parentProfile) throws IOException {
		setActive("Add parent Profile ");
		serverRequest = "AddParentProfile";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(parentProfile);
		oos.flush();

		closeStreams();
		setInactive("Add parent profile ");
	}

	public LinkedList<Reward> removeRewardFromServer(Account account, Reward reward)
			throws IOException, ClassNotFoundException {
		setActive("Remove reward ");
		serverRequest = "RemoveReward";
		LinkedList<Reward> rewardList = null;

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(reward);
		oos.flush();
		rewardList = (LinkedList<Reward>) ois.readObject();

		closeStreams();
		setInactive("Remove reward ");
		return rewardList;
	}

	public LinkedList<Reward> removeTaskFromServer(Account account, Task task) throws IOException {
		setActive("Remove task ");
		serverRequest = "RemoveTask";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(task);
		oos.flush();

		LinkedList<Reward> rewardList = null;
		try {
			rewardList = (LinkedList<Reward>) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeStreams();
		setInactive("Remove task ");

		return rewardList;
	}

	public LinkedList<Reward> addRewardToServer(Account account, Reward reward) throws IOException {
		setActive("Add reward ");
		serverRequest = "AddReward";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(reward);
		oos.flush();
		LinkedList<Reward> rewardList = null;

		try {
			rewardList = (LinkedList<Reward>) ois.readObject();
		} catch (ClassNotFoundException e) {

		}
		closeStreams();
		setInactive("Add Reward ");
		return rewardList;

	}

	public LinkedList<Task> addTaskToServer(Account account, Task task) throws UnknownHostException, IOException {
		setActive("Add task ");
		serverRequest = "AddTask";

		openStreams();

		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(task);
		oos.flush();

		LinkedList<Task> taskList = null;
		try {
			taskList = (LinkedList<Task>) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeStreams();

		setInactive("Add task ");
		return taskList;
	}

	/**
	 * 
	 * @param account
	 *            The account to be logged in to.
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Account sendLoginToServer(Account account) throws UnknownHostException, IOException, ClassNotFoundException {
		setActive("Logging in");
		Boolean ret = false;
		serverRequest = "Login";

		openStreams();
		// Send login request to server.
		oos.writeObject(new ServerRequest(account, serverRequest));
		// Wait for response from server
		Account res = null;
		while (res == null) {
			res = (Account) ois.readObject();
		}
		// Close streams.
		closeStreams();
		// printAccount(res);
		//ClientController clientController = new ClientController(res, this);
		setInactive("Logging in");
		return res;
	}

	public LinkedList<Task> setTaskAsCompleted(Account account, Task task, ChildProfile childProfile) {
		setActive("Setting task as completed");
		serverRequest = "SetTaskComplete";
		openStreams();

		try {
			oos.writeObject(new ServerRequest(account, serverRequest));
			oos.writeObject(task);
			oos.writeObject(childProfile);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkedList<Task> taskList = null;
		try {
			taskList = (LinkedList<Task>) ois.readObject();
			closeStreams();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setInactive("Setting task as completed");
		return taskList;
	}

	public LinkedList<Task> setTaskCompleted(Account account, Task task, ChildProfile childProfile) throws IOException {
		setActive("Completing task");
		serverRequest = "CompleteTask";

		openStreams();
		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(task);
		oos.writeObject(childProfile);
		oos.flush();
		LinkedList<Task> taskList = null;
		try {
			taskList = (LinkedList<Task>) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeStreams();
		for (ChildProfile c : account.getChildProfileList()) {
			if (c == childProfile) {
				c.addPoints(task.getTaskValue());
			}
		}
		setInactive("Completing task");
		return taskList;

	}

	public LinkedList<Reward> addPointsToReward(Reward reward, int points, ChildProfile childProfile, Account account)
			throws IOException {
		setActive("Add points to reward ");
		serverRequest = "AddRewardPoints";
		LinkedList<Reward> rewardList = null;

		openStreams();
		oos.writeObject(new ServerRequest(account, serverRequest));
		oos.writeObject(reward);
		oos.writeObject(childProfile);
		oos.writeInt(points);
		oos.flush();
		try {
			rewardList = (LinkedList<Reward>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return rewardList;
	}
}
