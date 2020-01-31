
/**
 * File Name:       ChatRunnable.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class ChatRunnable is responsible for connecting server and client for chat.  
 * Class list:      ChatRunnable                
 */
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * This class is responsible for connecting server and client.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 *
 */
public class ChatRunnable<T extends JFrame & Accessible> implements Runnable {
	/** Instance of type T */
	final T ui;
	/** Instance of class Socket */
	final Socket socket;
	/** Instance of class InputStream */
	final ObjectInputStream inputStream;
	/** Instance of class OutputStream */
	final ObjectOutputStream outputStream;
	/** Area for display of chat */
	final JTextArea display;

	/**
	 * Initial constructor. Initiates the fields of the class.
	 * 
	 * @param ui
	 * @param connection
	 */
	public ChatRunnable(T ui, ConnectionWrapper connection) {
		socket = connection.getSocket();
		inputStream = connection.getInputStream();
		outputStream = connection.getOutputStream();
		this.ui = ui;
		display = ui.getDisplay();
	}

	/**
	 * The method is used for running the socket
	 */
	public void run() {
		String strin = "";

		while (true) {
			if (socket.isClosed() != true) {
				try {
					strin = (String) inputStream.readObject();
					LocalDateTime time = LocalDateTime.now();
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, HH:mm a");
					String str = format.format(time);

					if (strin.trim().equals(ChatProtocolConstants.CHAT_TERMINATOR)) {
						final String terminate;
						terminate = ChatProtocolConstants.DISPLACEMENT + str + ChatProtocolConstants.LINE_TERMINATOR
								+ strin;
						display.append(terminate);
						break;
					} else {
						final String append;
						append = ChatProtocolConstants.DISPLACEMENT + str + ChatProtocolConstants.LINE_TERMINATOR
								+ strin;
						display.append(append);
					}
				} catch (EOFException | SocketTimeoutException ex) {
					break;
				} catch (ClassNotFoundException | IOException e) {
					// e.printStackTrace();
					break;
				}

			} else {
				break;
			}
		}
		if (socket.isClosed() != true) {
			try {
				outputStream.writeBytes(ChatProtocolConstants.DISPLACEMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ui.closeChat();
	}
}
