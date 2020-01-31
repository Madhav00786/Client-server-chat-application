
/**
 * File Name:       Server.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class Server has the main function for launching the server window..  
 * Class list:      Server
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 * This class is responsible for launching server.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 * 
 *
 */
public class Server {

	/**
	 * The method is main method for server
	 * 
	 * @param args
	 *            - arguments for running client
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int portNum = 65535;
		if (args.length > 0) {
			portNum = Integer.parseInt(args[0]);
			System.out.println("Using the port : " + portNum);
		} else {
			System.out.println("Using the default port : " + portNum);
		}
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNum);
			int friend = 0;

			while (true) {
				Socket socket = serverSocket.accept();

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);

				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);

				System.out.printf(
						"Trying to establish a connection with a socket with address = %s, port = %d and local port = %d\n",
						socket.getInetAddress(), socket.getPort(), socket.getLocalPort());
				friend++;

				final String title = "Isha's Friend " + friend;
				launchClient(socket, title);
			}
		} catch (IOException e) {
			System.out.println("Couldn't create a ServerSocket at the port");
		}
	}

	/**
	 * The method is method for launching server
	 * 
	 * @param socket
	 * @param title
	 *            String title for server window
	 */
	public static void launchClient(Socket socket, String frameTitle) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				ServerChatUI serverChat = new ServerChatUI(socket);
				serverChat.setMinimumSize(new Dimension(588, 500));
				serverChat.setLocationRelativeTo(null);
				serverChat.setTitle(frameTitle);
				serverChat.setResizable(false);
				serverChat.setVisible(true);
				serverChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			}
		});
	}

}
