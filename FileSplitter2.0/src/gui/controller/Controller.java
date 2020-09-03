package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.FileSplitterMain;
import javafx.fxml.Initializable;

/**
 * Abstract class of the Controllers Class
 * @author Meschio
 *
 */
public abstract class Controller implements Initializable{
	protected FileSplitterMain main;

	/**
	 * Set  the main
	 * @param main set the main app
	 */
    public void setMainApp(FileSplitterMain main) {
        this.main = main;
    }

    /**
     * initialize method of the controller
     */
    @Override
    public abstract void initialize(URL location, ResourceBundle bundle);
}
