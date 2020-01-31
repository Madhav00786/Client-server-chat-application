
/**
 * File Name:       ConnectionWrapper.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class ConnectionWrapper has the functionality needed to set the streams for the connection.  
 * Class list:      ConnectionWrapper
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is responsible for working of streams.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 * 
 *
 */
public class ConnectionWrapper {

	/** Instance of class ObjectOutputStream */
	private ObjectOutputStream outputStream;
	/** Instance of class ObjectInputStream */
	private ObjectInputStream inputStream;
	/** Instance of class Socket */
	private Socket socket;

	/**
	 * Initial constructor. Initiates the fields of the class.
	 * 
	 * @param socket
	 */
	public ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}

	/**
	 * getter for Socket
	 * 
	 * @return Socket
	 * 
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * getter for ObjectOutputStream
	 * 
	 * @return ObjectOutputStream
	 * 
	 */
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * getter for ObjectInputStream
	 * 
	 * @return ObjectInputStream
	 * 
	 */
	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Method is used to create ObjectInputStream
	 * 
	 * @param ObjectInputStream
	 * 
	 */
	ObjectInputStream createObjectIStreams() throws IOException {
		inputStream = new ObjectInputStream(socket.getInputStream());
		return inputStream;
	}

	/**
	 * Method is used to create ObjectOutputStream
	 * 
	 * @param ObjectOutputStream
	 * 
	 */
	ObjectOutputStream createObjectOStreams() throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		return outputStream;
	}

	/**
	 * Method is used to create streams for connection
	 * 
	 */
	void createStreams() throws IOException {
		createObjectOStreams();
		createObjectIStreams();

	}

	/**
	 * Method is used to close the connection
	 * 
	 */
	public void closeConnection() throws IOException {
		try {
			if (outputStream != null) {
				// && socket != null && !socket.isClosed()) {
				outputStream.close();
			}
		} catch (IOException e) {

		}
		try {
			if (inputStream != null) {
				// && !socket.isClosed() && socket != null) {
				inputStream.close();
			}
		} catch (IOException e) {

		}
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}
}
