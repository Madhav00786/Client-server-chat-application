
/**
 * File Name:       ClientChatUI.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class ClientChatUI has all the working and GUI of CLient window.  
 * Class list:      ClientChatUI
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is responsible for working of client.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 * 
 *
 */
public class ClientChatUI extends JFrame implements Accessible {
	/** Serial Version for GUI */
	private static final long serialVersionUID = 6345732L;
	/** Field for message typing */
	JTextField message;
	/** Send button for messages */
	JButton sendButton;
	/** Area for displaying of messages */
	JTextArea display;
	/** Instance of class ObjectOutputStream */
	ObjectOutputStream outputStream;
	/** Instance of class Socket */
	Socket socket;
	/** Instance of class ConnectionWrapper */
	ConnectionWrapper connection;
	/** Connect button for connecting to port */
	JButton connectButton;
	/** Area for host name */
	JTextField hostMessage;
	/** Combo Box for ports */
	JComboBox<String> comboBox;

	/**
	 * Initial constructor. Initiates the fields of the class.
	 * 
	 * @param title
	 *            String for the title of client window
	 */
	public ClientChatUI(String title) {

		setTitle(title);
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
	 * The method is used for closing the chat.
	 */
	public void closeChat() {
		if (socket.isClosed() != true) {
			try {
				connection.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			enableConnectButton();
		}
	}

	/**
	 * The method is used for enabling the connect button.
	 */
	private void enableConnectButton() {
		connectButton.setEnabled(true);
		connectButton.setBackground(Color.RED);
		sendButton.setEnabled(false);
		hostMessage.requestFocus();
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
		 * The method is used for closing the client window
		 * 
		 * @param event
		 *            Instance of WindowEvent
		 */
		public void windowClosing(WindowEvent event) {
			super.windowClosing(event);

			try {
				if (socket.isClosed() != true) {
					outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}

	/**
	 * This class is responsible for working of actions on client window.
	 * 
	 * @author Madhav Sachdeva,Isha Isha
	 * @version 1
	 * @since 1.8.1
	 * 
	 *
	 */
	private class Controller implements ActionListener {

		/**
		 * The method is used for closing the client window
		 * 
		 * @param host
		 *            String host of client
		 * @param port
		 *            int port number
		 * @return boolean
		 */
		public boolean connect(String host, int port) {
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), 60000);
				socket.setSoTimeout(60000);

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);

				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);

				display.append(String.format("Connection established address = %s, port  %d and local port = %d\n",
						socket.getInetAddress(), socket.getPort(), socket.getLocalPort()));
				connection = new ConnectionWrapper(socket);
				connection.createStreams();
				outputStream = connection.getOutputStream();
				return true;
			} catch (IllegalArgumentException e) {
				display.append(e.getMessage());
				return false;
			} catch (UnknownHostException e1) {
				display.append("ERROR: Unknown Host");
				return false;
			} catch (IOException e2) {
				display.append(e2.getMessage());
				return false;
			}
		}

		/**
		 * The method is used for handling the actions performed on client window
		 * 
		 * @param event
		 *            Instance of ActionEvent
		 */
		public void actionPerformed(ActionEvent event) {
			boolean connected = false;

			String str = event.getActionCommand();

			if (str.equals("connect")) {
				String host = hostMessage.getText();

				int port = 65535;
				try {
					port = Integer.parseInt((String) comboBox.getSelectedItem());
				} catch (NumberFormatException e) {
					display.append(e.getMessage());
				}
				connected = connect(host, port);

				if (connected) {
					connectButton.setEnabled(false);
					connectButton.setBackground(Color.BLUE);
					sendButton.setEnabled(true);
					message.requestFocus();
					Runnable runnable = new ChatRunnable<ClientChatUI>(getInstance(), connection);
					Thread thread = new Thread(runnable);
					thread.start();
				}
			}

			if (str.equals("send")) {
				send();
			}
		}

	}

	/**
	 * The method is used for sending chats through client window
	 */
	private void send() {
		String sendMessage = message.getText();

		display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
		try {
			outputStream.writeObject(
					ChatProtocolConstants.DISPLACEMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);

		} catch (IOException e) {
			enableConnectButton();
			display.setText(e.getMessage());
		}
	}

	/**
	 * The method is used for creation of GUI for client window
	 * 
	 * @return JPanel
	 */
	public JPanel createClientUI() {
		JPanel main = new JPanel();
		JPanel top = new JPanel();
		JPanel connection = new JPanel();
		JPanel messagePanel = new JPanel();
		JPanel chatDisplay = new JPanel();
		JPanel hostPanel = new JPanel();
		JLabel hostLabel = new JLabel("Host: ");
		JPanel portPanel = new JPanel();
		JLabel portLabel = new JLabel("Port: ");
		connectButton = new JButton("Connect");
		sendButton = new JButton("Send");
		String[] portNumbers = { "", "8089", "65000", "65535" };
		comboBox = new JComboBox<String>(portNumbers);
		message = new JTextField(41);
		hostMessage = new JTextField(45);
		display = new JTextArea(30, 45);
		JScrollPane scroll = new JScrollPane(display);
		TitledBorder border;
		Controller controller = new Controller();

		display.setBackground(Color.WHITE);
		display.setEditable(false);

		hostLabel.setPreferredSize(new Dimension(35, 30));
		hostLabel.setDisplayedMnemonic('H');
		hostLabel.setLabelFor(hostMessage);

		hostPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		hostPanel.add(hostLabel);
		hostPanel.add(hostMessage);

		portLabel.setPreferredSize(new Dimension(35, 30));
		portLabel.setDisplayedMnemonic('P');
		portLabel.setLabelFor(comboBox);

		portPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		portPanel.add(portLabel);
		portPanel.add(comboBox);
		portPanel.add(connectButton);

		hostMessage.setText("localhost");
		hostMessage.setCaretPosition(0);
		hostMessage.requestFocus();
		hostMessage.setBorder(BorderFactory.createCompoundBorder(hostMessage.getBorder(),
				BorderFactory.createEmptyBorder(0, 5, 0, 0)));

		connection.setLayout(new BorderLayout());
		connection.add(hostPanel, BorderLayout.NORTH);
		connection.add(portPanel, BorderLayout.SOUTH);
		connection.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 10), "CONNECTION"));

		connectButton.setPreferredSize(new Dimension(100, 20));
		connectButton.setBackground(Color.RED);
		connectButton.setMnemonic('C');
		connectButton.addActionListener(controller);
		connectButton.setActionCommand("connect");

		comboBox.setPreferredSize(new Dimension(100, 20));
		comboBox.setBackground(Color.WHITE);
		comboBox.setEditable(true);
		comboBox.addActionListener(controller);

		top.setLayout(new BorderLayout());
		top.add(messagePanel, BorderLayout.SOUTH);
		top.add(connection, BorderLayout.CENTER);

		messagePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		messagePanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		message.setText("Type Message");
		messagePanel.add(message);
		messagePanel.add(sendButton);

		sendButton.setPreferredSize(new Dimension(81, 19));
		sendButton.setMnemonic('S');
		sendButton.setEnabled(false);
		sendButton.addActionListener(controller);
		sendButton.setActionCommand("send");

		border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY");
		border.setTitleJustification(TitledBorder.CENTER);
		chatDisplay.setLayout(new BorderLayout());
		chatDisplay.setBorder(border);
		chatDisplay.add(scroll);

		main.setLayout(new BorderLayout());
		main.setBackground(Color.GREEN);
		main.add(top, BorderLayout.NORTH);
		main.add(chatDisplay, BorderLayout.CENTER);

		return main;

	}

	/**
	 * The method is used for running the client window
	 */
	private void runClient() {
		setContentPane(createClientUI());
		addWindowListener(new WindowController());
	}

	/**
	 * The method is used for getting the instance of this class
	 * 
	 * @return ClientChatUI
	 */
	private ClientChatUI getInstance() {
		return this;
	}
}
