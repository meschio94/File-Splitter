package gui.logic;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * This class pourpose is to handle the textField input for some user cases, this class is primary used to handle the informartion input
 * for check the input, disallow non digits input, and enable/disable some GUI feature
 * @author Meschio
 *
 */

public class FSTextfieldLogic {

	private TextField textField;

	/**
	 * Constructor of the class take in input the textfield to handle
	 * @param inputTextField textField to hook
	 */
	public FSTextfieldLogic (TextField inputTextField){
		this.textField = inputTextField;
	}


	/**
	 * Method for adding a new EventFilter to the textField for enable only digits input via {@link #inputTextFieldNumeric}
	 * @param maxLenght of numbers admitted
	 */
	public void handleNumericInput(int maxLenght){
		textField.addEventFilter(KeyEvent.KEY_TYPED , inputTextFieldNumeric(maxLenght));
	}

	/**
	 * Add a input filter for allow only input of numbers [0-9.] without any other math symbol
	 * @param maxLengh of numbers admitted
	 * @return new EventHandler<KeyEvent>
	 */
	private EventHandler<KeyEvent> inputTextFieldNumeric(final Integer maxLengh) {
	    return new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent e) {
	            TextField txt_TextField = (TextField) e.getSource();
	            if (txt_TextField.getText().length() >= maxLengh) {
	                e.consume();
	            }
	            if(e.getCharacter().matches("[0-9.]")){
	                if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
	                    e.consume();
	                }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
	                    e.consume();
	                }
	            }else{
	                e.consume();
	            }
	        }
	    };
	}

	/**
	 * Method for adding a new EventFilter to the textField for disable a button if the textfield is empty (null) {@link #inputTextFieldNull}
	 * @param btnToDisable button do disable
	 */
	public void handleNullInput(Button btnToDisable){
		textField.addEventFilter(KeyEvent.KEY_TYPED , inputTextFieldNull(btnToDisable));
	}

	/**
	 * Add a input filter for disable a button if the input is empty
	 * @param btnToDisable
	 * @return new EventHandler<KeyEvent>
	 */
	private EventHandler<KeyEvent> inputTextFieldNull(Button btnToDisable){
		return new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent e) {
	            TextField txt_TextField = (TextField) e.getSource();
	            if (txt_TextField.getText().length() > 0) {
	            	btnToDisable.setDisable(false);
	            } else {
	            	btnToDisable.setDisable(true);
	            }
	        }
	    };
	}

	/**
	 * method for check if the textField is empty (null)
	 * @return true if is empty, false otherwise
	 */
	public boolean isTextFieldEmpty(){
		if(textField.getText() == null){
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Method for get the information numeric value, perform some checks of the input
	 * If the value is equal and minus 0 or null will return 1
	 * @return the int information
	 */
	public int getFilteredInformartion(){
		int information;

		if (textField.getText().matches("[0-9.]+") == false){//check if the input isn't numeric, if the user copy/paste some text for example
			information = 1;
		} else {
			information = Integer.parseInt(textField.getText());

			if ((information <= 0) || (isTextFieldEmpty() == true)){//check for 0 or null input
				information = 1;
			}

		}


		return information;
	}


}
