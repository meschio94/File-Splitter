package application;


import java.io.File;

import gui.controller.FSMainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.FileElement;

/**
 * Main class of the FileSplitter Program
 * Create the output folder of the program
 * create an observable list and pass it to the controller of the main window
 * and launch the main windows.fxml file
 * @author Meschio
 *
 */
public class FileSplitterMain extends Application{

	private ObservableList<FileElement> list;

	/**
	 * Start method for launch the main window, set the outputfolder and observable list to pass to the main controller
	 */
	  @Override
      public void start(Stage primaryStage) throws Exception {

		  String outputPath = createOuputPath();


		  list = FXCollections.observableArrayList();

		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/FSMainWindow.fxml"));
		  Parent root = loader.load();

		  FSMainController controller = loader.getController();
		  controller.setValue(list,outputPath);

		  primaryStage.setResizable(false);//disable resizible window and fullscreen


		  primaryStage.setTitle("File Splitter");
          primaryStage.setScene(new Scene(root));
		  primaryStage.show();

  }

  public static void main(String[] args) {
      Application.launch(args);
  }

  /**
   * Method for create if not exist the output folder of the program
   * @return
   */
  public String createOuputPath(){
	  String d = System.getProperty("user.home");
	  String dir = d + File.separator+ "documents" +File.separator + "FSfolder";
	  File outFolder = new  File(dir);
	  outFolder.mkdirs();
	  return dir;

  }


}
