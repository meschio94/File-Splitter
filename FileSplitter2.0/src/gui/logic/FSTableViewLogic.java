package gui.logic;

import java.util.Optional;

import error.AlertMaker;
import gui.controller.FSChangeFileDataController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.FSlistFileElement;
import logic.FileElement;

/**
 * Class for handle the feaure and the logic (manipulate the list) of the TableView in the GUI
 * @author Meschio
 *
 */
public class FSTableViewLogic extends TableView<FileElement> {

	/**
	 * The tableView to handle
	 */
	private TableView<FileElement> table;

	/**
	 * THe list used in the GUI to manipulate
	 */
	private FSlistFileElement list;

	/**
	 * Constructor of the class, take in input the tableview to handle the logic & features, and the list to manipulate
	 * @param inputTable
	 * @param inputList
	 */
	public FSTableViewLogic(TableView<FileElement> inputTable, FSlistFileElement inputList){
		this.table = inputTable;
		this.list = inputList;
	}

	/**
	 * Method to handle row order in the tableview with drag&drop mouse click feature
	 */
	public void handleRowOrderTable(){

		DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

		table.setRowFactory(tv -> {
            TableRow<FileElement> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    FileElement draggedFileElement = table.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = table.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    table.getItems().add(dropIndex, draggedFileElement);

                    event.setDropCompleted(true);
                    table.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });
	}

	/**
	 * Method for the context menu in the table view with the delete item feature
	 * @param event
	 */

	public void handleFileDeleteOption(ActionEvent event, FileElement selectedForDeletion) {
		//FileElement selectedForDeletion = fileElement;
		if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No File selected", "Please select a File for deletion.");
            return;
        }

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		 alert.setTitle("Deleting File");
	     alert.setContentText("Are you sure want to delete :" + selectedForDeletion.getFileName() + "?");
	     Optional<ButtonType> answer = alert.showAndWait();
	     if (answer.get() == ButtonType.OK) {
	    	 	list.remove(selectedForDeletion);
	         } else {
	            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
	        }
	}

	/**
	 * Method for the context menu in the table view with the edit file feature,
	 *  open a new popup window with the scene FSChangeFile.FXML
	 * @param event
	 */
	public void handleFileEditOption(ActionEvent event) {
		FileElement selectedForEdit = table.getSelectionModel().getSelectedItem();
		if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No File selected", "Please select a File to delete.");
            return;
        }
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/FSChangeFileData.fxml"));
            Parent parent = loader.load();

            FSChangeFileDataController mpc = loader.getController();
            mpc.setData(selectedForEdit);

            Stage stage = new Stage();
            stage.setResizable(false);//disable resizible window and fullscreen
            stage.setTitle("Edit File");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL); //set for block the main window
            stage.showAndWait(); //block the main window if the file edit window is open

            if (mpc.isEdited()) {
                table.refresh(); // or update the table by other means
            }


            } catch (Exception e) {
        	e.printStackTrace();
		}
	}

	/**
	 * Get table method
	 * @return the table
	 */
	public TableView<FileElement> getTable(){
		return table;
	}

}
