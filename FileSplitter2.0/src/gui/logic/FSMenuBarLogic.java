package gui.logic;

import java.io.File;
import java.util.Optional;

import error.AlertMaker;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import logic.FSlistFileElement;

/**
 * Class for handle the method used by the item of the MenuBar
 * @author Meschio
 *
 */
public class FSMenuBarLogic {

	/**
	 * Method to handle the clear queue command in the File Menu > Edit
	 * @param event
	 */
	public void handleClearQueue(ActionEvent event, FSlistFileElement list){

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deleting Queue");
	    alert.setContentText("Do you want to delete and clear the queue?");
	    Optional<ButtonType> answer = alert.showAndWait();
	    if (answer.get() == ButtonType.OK) {
	    	list.clear();
	    }else {
	         AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
	    }
	}

	/**
	 * Method to handle the change Directory Folder output in the File Menu > File
	 * @param event
	 */
	public String handleChangeDirectoryFolder(ActionEvent event, String directoryFolder){
		String newDirectoryFolder = null;
		try{
			DirectoryChooser chooser = new DirectoryChooser();
			File initialDirectory = new File(directoryFolder);
			chooser.setInitialDirectory(initialDirectory);//set the actual directory to open
			chooser.setTitle("Select Directory");
			File directory = chooser.showDialog(new Stage());
			if(directory != null){
				newDirectoryFolder = directory.getAbsolutePath();
			}
		} catch (Exception e) {
        	e.printStackTrace();
		}
		return newDirectoryFolder;
	}
}
