
/**
 * File Name:       ServerChatUI.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class ServerChatUI has all the working and GUI of CLient window.  
 * Class list:      ServerChatUI    
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.net.Socket;

/**
 * This class is responsible for working of server.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 * 
 *
 */
public class ServerChatUI extends JFrame implements Accessible {

	/** Serial Version for GUI */
	private static final long serialVersionUID = 2019L;
	/** Field for message typing */
	private JTextField message;
	/** Send button for messages */
	private JButton sendButton;
	/** Area for displaying of messages */
	private JTextArea display;
	/** Instance of class ObjectOutputStream */
	private ObjectOutputStream outputStream;
	/** Instance of class Socket */
	private Socket socket;
	/** Instance of class ConnectionWrapper */
	private ConnectionWrapper connection;

	/**
	 * Initial constructor. Initiates the fields of the class.
	 * 
	 * @param title
	 *            String for the title of client window
	 */
	public ServerChatUI(Socket socket) {
		this.socket = socket;
		setFrame(createUI());
		runClient();

	}

	/**
	 * The method is used for the creation of buttons with the some basic
	 * properties.
	 * 
	 * @return JTextArea area - for display of chats
	 */
	public JTextArea getDisplay() {
		return display;
	}

	/**
	 * This class is responsible for conterolling client window.
	 * 
	 * @author Madhav Sachdeva,Isha Isha
	 * @version 1
	 * @since 1.8.1
	 * 
	 *
	 */
	private class WindowController extends WindowAdapter {

		/**
		 * The method is used for closing the server window
		 * 
		 * @param event
		 *            Instance of WindowEvent
		 */
		public void windowClosing(WindowEvent event) {
			super.windowClosing(event);
			System.out.println("ServerUI Window closing!");
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACEMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dispose();
				System.out.println("Chat closed!");
			}
			dispose();
			System.exit(0);
		}

		/**
		 * The method is used for printing that the server window is closed
		 * 
		 * @param event
		 *            Instance of WindowEvent
		 */
		public void windowClosed(WindowEvent event) {
			super.windowClosed(event);
			System.out.println("Server UI Closed!");
		}

	}

	/**
	 * This class is responsible for working of actions on server window.
	 * 
	 * @author Madhav Sachdeva,Isha Isha
	 * @version 1
	 * @since 1.8.1
	 * 
	 *
	 */
	private class Controller implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String str = event.getActionCommand();
			if (str.equals("send")) {
				send();
			}
		}

		/**
		 * The method is used for sending chats through server window
		 */
		private void send() {
			String sendMessage = message.getText();
			display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(
						ChatProtocolConstants.DISPLACEMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				display.setText(e.getMessage());
			}
		}
	}

	/**
	 * The method is used for creation of GUI for client window
	 * 
	 * @return JPanel
	 */
	public JPanel createUI() {
		JPanel main = new JPanel();
		JPanel messagePanel = new JPanel();
		JPanel chatDisplay = new JPanel();
		JPanel top = new JPanel();
		message = new JTextField(41);
		sendButton = new JButton();
		TitledBorder border;
		display = new JTextArea(30, 45);
		JScrollPane scroll = new JScrollPane(display);
		Controller controller = new Controller();

		top.setLayout(new BorderLayout());
		main.setLayout(new BorderLayout());
		main.setBackground(Color.GREEN);

		display.setBackground(Color.WHITE);
		display.setEditable(false);

		messagePanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		message.setText("Type Message");
		messagePanel.add(message);
		messagePanel.add(sendButton);

		sendButton.setPreferredSize(new Dimension(81, 19));
		sendButton.setMnemonic('S');
		sendButton.setText("Send");
		sendButton.addActionListener(controller);
		sendButton.setActionCommand("send");

		border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY");
		border.setTitleJustification(TitledBorder.CENTER);
		chatDisplay.setLayout(new BorderLayout());
		chatDisplay.setBorder(border);
		chatDisplay.add(scroll);

		top.add(messagePanel, BorderLayout.SOUTH);
		main.add(top, BorderLayout.NORTH);
		main.add(chatDisplay, BorderLayout.CENTER);

		return main;

	}

	/**
	 * The method is used for setting teh frame for server window
	 * 
	 * @param panel
	 */
	public final void setFrame(JPanel panel) {
		setContentPane(panel);
		addWindowListener(new WindowController());
	}

	/**
	 * The method is used for running the client window
	 */
	private void runClient() {
		connection = new ConnectionWrapper(socket);
		try {
			connection.createStreams();
			outputStream = connection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runnable runnable = new ChatRunnable<ServerChatUI>(this, connection);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * The method is used for closing the chat.
	 */
	public void closeChat() {
		try {
			connection.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dispose();
	}
}
