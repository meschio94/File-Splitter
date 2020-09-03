package error;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


/**
 * Class for display windows Alert to the user
 * @author Meschio
 *
 */
public class AlertMaker {

	/**
	 * Create a custom window error Alert for the user
	 * @param title of the error
	 * @param content of the error
	 */
	public static void showSimpleAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

	/**
	 * Create an Error window for the user
	 * @param title of the error
	 * @param content of the error
	 */
	public static void showErrorMessage(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        //styleAlert(alert);
        alert.showAndWait();
    }

	/**
	 * Method for confirmation custom window
	 * @param title of the scene
	 * @param content of the scene
	 * @return true if the user press OK, false if cancel is pressed or closed the popup window
	 */
	public boolean showConfirmationMessage(String title, String content){
		 Alert alert = new Alert(AlertType.	CONFIRMATION);
		 alert.setTitle("Confirmation");
	     alert.setHeaderText(title);
	     alert.setContentText(content);

	     Optional<ButtonType> result = alert.showAndWait();
	     	if(result.get() == ButtonType.OK){
	     		return true;
	     	}
	     	else if(result.get() == ButtonType.CANCEL){
	     		return false;
	     	}
	     	return false;
	}

}
