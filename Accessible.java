
/**
 * File Name:       Accessible.java
 * Author:          Madhav Sachdeva(040918899),Isha Isha(040912862)
 * Course:          CST8221 - JAP, Lab Section: 313(Madhav),311(Isha)
 * Assignment:      2, Part 2
 * Date:            December 06,2019
 * Professor:       Daniel Cormier
 * Purpose:         This is just an interface for the program.  
 * Class list:      none
 */
import javax.swing.JTextArea;

/**
 * This is the interface of the program
 * 
 * @author Isha Isha, Madhav Sachdeva
 * @version 1.0
 * @since 1.8.1
 */
public interface Accessible {

	/**
	 * get method for Display
	 * 
	 * @return JTextArea
	 */
	JTextArea getDisplay();

	/**
	 * method to close the Chat UI.
	 */
	void closeChat();

}
