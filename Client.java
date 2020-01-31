/**
 * File Name:       Client.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         The class Client has the main method for launching of client window.  
 * Class list:      Client
 */

//Client.java

import java.awt.*;
import javax.swing.*;

/**
 * This class is responsible for launching client.
 * 
 * @author Madhav Sachdeva,Isha Isha
 * @version 1
 * @since 1.8.1
 *
 */
public class Client {

	/**
	 * The method is main method for launching client
	 * 
	 * @param args
	 *            - arguments for running client
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				ClientChatUI chat = new ClientChatUI("Isha's ClientChatUI");
				chat.setMinimumSize(new Dimension(588, 500));
				chat.setLocationByPlatform(true);
				chat.setVisible(true);
				chat.setResizable(false);
				chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			}
		});
	}

}
