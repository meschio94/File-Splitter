package gui.logic;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.FileElement;

/**
 * This class handle the GUI nodes interations for the FSMainWindow, enabling and disabling components during some Tasks
 * @author Meschio
 *
 */
public class FSNodesInteration {

	/**
	 * The MenuBar in the GUI to handle
	 */
	private MenuBar menu;

	/**
	 * AddFiles button in the GUI
	 */
	private Button add;

	/**
	 * Start Button in the GUI
	 */
	private Button start;

	/**
	 * TableView to display list in the GUI
	 */
	private TableView<FileElement> table;

	/**
	 * Column BArPRogress from the Tableview
	 */
	private TableColumn<FileElement, Double> progressColumn;


	/**
	 * The constructor of the class, take in input the elements from the gui to disable during workingMode
	 * @param menu menuFile to hook
	 * @param add addFiles button to hook
	 * @param start start button to hook
	 * @param table tableView to hook
	 * @param progressColumn progress columns to hook
	 */
	public FSNodesInteration(MenuBar menu,Button add, Button start, TableView<FileElement> table, TableColumn<FileElement, Double> progressColumn ){
		this.menu = menu;
		this.add = add;
		this.start = start;
		this.table = table;
		this.progressColumn = progressColumn;
	}

	/**
	 * Set the standard Idle Mode GUI when the list isn't processing
	 */
	public void setIdleMode(){
		enableMouseIntercation(table);
		enableNode(menu);
		enableNode(add);
		enableNode(start);
		progressColumn.setVisible(false);
	}

	/**
	 * Set the the Working Mode GUI disabling some nodes from user input
	 */
	public void setWorkingMode(){
		disableMouseInteraction(table);
		disableNode(menu);
		disableNode(add);
		disableNode(start);
		progressColumn.setVisible(true);
	}

	/**
	 * Disable a node
	 * @param node
	 */
	private void disableNode(Node node){
		node.setDisable(true);
	}

	/**
	 * Enable a node
	 * @param node
	 */
	private void enableNode(Node node){
		node.setDisable(false);
	}

	/**
	 * Disable mouse interaction without greyout the node
	 * @param node
	 */
	private void disableMouseInteraction(Node node){
		node.setMouseTransparent(true);
	}

	/**
	 * Enable mouse interaction in a node
	 * @param node
	 */
	private void enableMouseIntercation(Node node){
		node.setMouseTransparent(false);
	}

}
