package gui.controller;

import logic.FileElement;

import java.net.URL;
import java.util.ResourceBundle;

import gui.logic.FSTextfieldLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This is the Controller of (@link @FSPasswordInput.fxml} view
 * This window is used by the user for edit the setting of the file operation
 * @author Meschio
 *
 */
public class FSChangeFileDataController extends Controller implements Initializable{


	/**
	 * The Element to edit
	 */
	private FileElement data;

	/**
	 * Flag for check if the element was edited
	 */
	private boolean edited;

	/**
	 * TextField Information logic and filter
	 */
	private FSTextfieldLogic textFieldLogic;

	/**
	 * The confirmation button to save the changes
	 */
    @FXML
    private Button C_OkBt;

    /**
     * The cancel Button for abort the edit
     */
    @FXML
    private Button C_CaBt;

    /**
     * The Label with the File Name
     */
    @FXML
    private Label C_FileNameLabel;

    /**
     * The Choice Operation Box
     */
    @FXML
    private ChoiceBox<String> C_ChBx;

    /**
     * The Information TextField
     */
    @FXML
    private TextField C_InTe;

    /**
     * The Size of the file choice options
     */
    @FXML
    private ChoiceBox<String> C_ChBoSize;

    /**
     * Location path of the selected elements
     */
    @FXML
    private TextField C_SourcePathText;

    /**
     * Password TextField
     */
    @FXML
    private TextField C_InputPassword;

    /**
     * Initialize method of the Controller
     */
    @Override
	public void initialize(URL location, ResourceBundle resources) {

    	textFieldLogic = new FSTextfieldLogic(C_InTe);

		textFieldLogic.handleNumericInput(7);
		textFieldLogic.handleNullInput(C_OkBt);

    }

     /**
     * method that return the edited flag
     * @return true if the file was edited, false otherwise
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * method for closing the window without saving the changes
     * @param event
     */
    @FXML
    public void closeButton(ActionEvent event){
    	C_CaBt.getScene().getWindow().hide();
    }

    /**
     * Method that set the item for the popup Change File window for manipulation purpose
     * @param selectedItem to manipulate
     */
    public void setData(FileElement selectedItem) {
        if (selectedItem == null) {
            throw new IllegalArgumentException();
        }
        this.data = selectedItem;
        C_FileNameLabel.setText(data.getFileName()); //set the label file name
    	C_SourcePathText.setText(data.getSourcePath());//set the source path
        C_InTe.setText(Integer.toString(selectedItem.getInformation()));//set the input text
        C_ChBx.setValue(selectedItem.getType());


        if (C_ChBx.getValue().toString().compareTo("Nr of Parts") == 0) {
        	C_ChBoSize.setDisable(true);
        } else {
        	C_ChBoSize.setValue(selectedItem.getSize());
        }


        edited = false;
    }

    /**
     * Method that save the new setting of the file when ok button is pressed
     */
    @FXML
    public void save(ActionEvent event) {


    	data.setType(C_ChBx.getValue().toString()); //save the choice type box
        data.setInformation(textFieldLogic.getFilteredInformartion()); //save the information box
        data.setSize(C_ChBoSize.getValue().toString()); //save the size

        String inputPassword = C_InputPassword.getText();
        if ((inputPassword == "") || (inputPassword == null)){
        	data.setPassword(null);
		} else {
			data.setPassword(inputPassword);
		}

        edited = true;
        C_OkBt.getScene().getWindow().hide();
    }

	/**
	 * Control of type choicebox selector
	 * Disable the size choicebox if the type "nr of parts" is selected
	 * Otherwise Reable the size choicebox if the type "nr of parts" is not selected
	 * @param event
	 */
	@FXML
	private void choiceBoxControl(ActionEvent event){
		if (C_ChBx.getValue().toString().compareTo("Nr of Parts") == 0){
			C_ChBoSize.setDisable(true);
      } else{
    	  	C_ChBoSize.setDisable(false);
      }
		if (C_ChBx.getValue().toString().compareTo("Dimension and Encrypt") != 0){
			C_InputPassword.setDisable(true);
		} else{
    	  	C_InputPassword.setDisable(false);
      }
    }



}

