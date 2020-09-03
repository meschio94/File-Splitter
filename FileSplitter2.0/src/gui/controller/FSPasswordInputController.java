package gui.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.FileElement;

/**
 * Controller for {@link @FSPasswordInput.fxml}, launched by {@link job.input.JobInDecrypt}  for input the custom password during decrypting an element
 * @author Meschio
 *
 */
public class FSPasswordInputController extends Controller{

	/**
	 * Flag for report if the user cancelled the operation
	 */
	private boolean aborted;

	/**
	 * Flag for report if the user inserted the password
	 */
	private boolean passwordInserted;

	/**
	 * The input password
	 */
	private String password;

	/**
	 * The element to manipulate
	 */
	private FileElement data;

	/**
	 * The button for check the inserted password via {@link #okButton}
	 */
	@FXML
	private Button P_OkBt;

	/**
	 * The button for cancel the operation via {@link #closeButton}
	 */
    @FXML
    private Button P_CaBt;

    /**
     * The input PAssword TextField
     */
    @FXML
    private TextField P_InputPassword;

    /**
     * The label for display the FileName
     */
    @FXML
    private Label P_LableFileName;


    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * method that return if the operation was aborted
     * @return true if it is
     */
    public boolean isAborted() {
        return aborted;
    }

    /**
     * Method for return if the user input a password
     * @return true if it is
     */
    public boolean isPasswordInserted(){
    	return passwordInserted;
    }

    /**
     * Method that set the file name label for the popupinput password window
     * @param selectedItem to manipulate
     */
    public void setData(FileElement selectedItem) {
        if (selectedItem == null) {
           throw new IllegalArgumentException();
        }
        this.data = selectedItem;
        P_LableFileName.setText(data.getFileName()); //set the label file name

        aborted = false;
    	passwordInserted = false;
    }

    /**
     * Set the boolean {@link #passwordInserted}
     * @param value
     */
    public void setPasswordInserted(Boolean value){
    	passwordInserted = value;
    }

    /**
     * Method for cancel the operation
     * @param event
     */
    @FXML
    void closeButton(ActionEvent event) {
    	if(handleAbortedOperation() == true){
    		aborted = true;
    		P_CaBt.getScene().getWindow().hide();
    	}
    }

    /**
     * Method for confirm the input password
     * @param event
     */
    @FXML
    void okButton(ActionEvent event){
    	password = P_InputPassword.getText();
    	if (( password != "") || ( password != null)){
    		passwordInserted = true;
	    	P_OkBt.getScene().getWindow().hide();
    	}
    }

    /**
     * get method for retrive password input by the user
     * @return
     */
    public String getPassword(){
    	return password;
    }

    /**
     * error message for handle the close button {@link #CloseButton} final advice
     * @return true if the user want to abort the operation and skip the item
     */
    private boolean handleAbortedOperation(){
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	boolean result;
		alert.setTitle("Cancel Operation");
	    alert.setContentText("Do you want to cancel the password input and skip the decrypt ?");
	    Optional<ButtonType> answer = alert.showAndWait();
	    if (answer.get() == ButtonType.OK) {
	    	result = true;
	    } else {
	    	result = false;
	     }
	    return result;
    }


}
