package gui.controller;

import logic.FSlistFileElement;
import logic.FileElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import conccurent.refresh.TablePeriodicRefresh;
import gui.logic.FSMenuBarLogic;
import gui.logic.FSNodesInteration;
import gui.logic.FSTableViewLogic;
import gui.logic.FSTextfieldLogic;
import gui.progress.GlobalProgress;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jobStarter.JobListSelector;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.*;

/**
 * This is the Controller of {@link @FSMainWindow}, for handle all the GUI operations the user need for use the program
 * @author Meschio
 *
 */
public class FSMainController extends Controller implements Initializable {
	/**
	 * The list to manipulate
	 */
	private FSlistFileElement list;

	/**
	 * The directory where to output the result of the processing elements
	 */
	private String directoryFolder;

	/**
	 * The obeservable value used by {@link #M_GlobalProgress} to display the users the progress, and track the total tasks progress
	 */
	private SimpleDoubleProperty updateProgress = new SimpleDoubleProperty(.0);

	/**
	 * The Table methods for handle all the function of {@link #M_Table}
	 */
	private FSTableViewLogic tableViewLogic;

	/**
	 * The MenuBar method for handle all the function of {@link #M_Menu}
	 */
	private FSMenuBarLogic menuBarLogic;

	/**
	 * The inputInformation Text field for handle all the function of {@link #M_InTe}
	 */
	private FSTextfieldLogic textFieldLogic;

	/**
	 * method for enable/block some nodes of the GUI during some state of the program
	 */
	private FSNodesInteration nodesInterationLogic;

	/**
	 * Type Selection list displayed by {@link #M_ChBx}
	 */
	@FXML
	private ObservableList<String> typeSelection = FXCollections.observableArrayList("Dimension",
			"Dimension and Encrypt", "Dimension and Compress", "Nr of Parts");
	/**
	 * Context menu of {@link #M_Table} that host: {@link #ContextItemDelete}, {@link #ContextItemEdit}
	 */
	@FXML
	private ContextMenu ContextMenuTable;
	/**
	 * Option for delete an item in the list via {@link #handleFileDeleteOption}
	 */
	@FXML
	private MenuItem ContextItemDelete;
	/**
	 * Option for edit an item in the list via {@link #handleFileEditOption}
	 */
	@FXML
	private MenuItem ContextItemEdit;
	/**
	 * MenuBar of the program Main Window taht host: {@link #M_MeBaEdClear}, {@link #M_MeBaFiDirectory}
	 */
	@FXML
	private MenuBar M_Menu;
	/**
	 * Option for flush the list via {@link #handleClearQueue}
	 */
	@FXML
	private MenuItem M_MeBaEdClear;
	/**
	 * Option for change the {@link #directoryFolder}, via {@link  #handleChangeDirectoryFolder}
	 */
	@FXML
	private MenuItem M_MeBaFiDirectory;

	/**
	 * The TableView for display the list
	 */
	@FXML
	private TableView<FileElement> M_Table;
	@FXML
	private TableColumn<FileElement, String> FileColumn;
	@FXML
	private TableColumn<FileElement, String> TypeColumn;
	@FXML
	private TableColumn<FileElement, String> InformationColumn;
	@FXML
	private TableColumn<FileElement, String> StatusColumn;
	@FXML
	private TableColumn<FileElement, Double> ProgressColumn;

	/**
	 * Input {@link logic.FileElement#information} TextField
	 */
	@FXML
	private TextField M_InTe;

	/**
	 * Input {@link logic.FileElement#password} TextField
	 */
	@FXML
	private TextField M_InputPassword;

	/**
	 * Button for adding multiple files via {@link #addFile}
	 */
	@FXML
	private Button M_Add;

	/**
	 * Button for start processing the list via {@link #handleStartQueueSplit}
	 */
	@FXML
	private Button M_BtStart;

	/**
	 * Choice box for select the type of operation
	 */
	@FXML
	private ChoiceBox<String> M_ChBx;

	/**
	 * Choice box for select the size of information
	 */
	@FXML
	private ChoiceBox<String> M_ChBoSize;


	/**
	 * Mode of the list, split or chop, INCOMPLETO
	 */
	@FXML
	private Label M_LabelMode;

	/**
	 * Global ProgressBar of the operation processing in the list
	 */
	@FXML
	private ProgressBar M_GlobalProgress;

	/**
	 * Method  called during the loading of the scene, set some GUI elements
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nodesInterationLogic = new FSNodesInteration(M_Menu,M_Add,M_BtStart,M_Table,ProgressColumn);

		M_InputPassword.setDisable(true);

		nodesInterationLogic.setIdleMode();

		setTableColumn();

		menuBarLogic = new FSMenuBarLogic();

		setTextField();

		M_GlobalProgress.progressProperty().bind(updateProgress);// bind the Global bar to the update Value


	};

	/**
	 * Function for set the list and some elements before loading the scene GUI
	 *
	 * @param masterList
	 *            to manipulate
	 */
	public void setValue(ObservableList<FileElement> masterList, String outputPath) {
		this.list = new FSlistFileElement(masterList); // set the list

		this.tableViewLogic = new FSTableViewLogic(M_Table, list); // set the
																	// table
		tableViewLogic.handleRowOrderTable();

		this.directoryFolder = outputPath;// set the directory output
	}

	/**
	 * method for set the textfield features and controls
	 */
	private void setTextField() {
		textFieldLogic = new FSTextfieldLogic(M_InTe);

		textFieldLogic.handleNumericInput(7);// control of the user input
		textFieldLogic.handleNullInput(M_Add);// handle null input

		M_InTe.setText("10");
	}

	/**
	 * Method for format and bind values to the table column
	 */
	private void setTableColumn() {
		FileColumn.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		TypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
		InformationColumn.setCellValueFactory(cellData -> cellData.getValue().getInformationPrintProperty());
		StatusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());

		ProgressColumn.setCellValueFactory(cellData -> cellData.getValue().getProgressProperty().asObject());
		ProgressColumn.setCellFactory(ProgressBarTableCell.<FileElement>forTableColumn());
	}

	/**
	 * Function for "Add Files", open the file chooser where the user select the
	 * files to input
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void addFile(ActionEvent event) throws IOException {

		List<File> tempList = new ArrayList<File>();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Files");
		tempList = chooser.showOpenMultipleDialog(new Stage());
		if (tempList != null) {
			String type = M_ChBx.getValue().toString();
			int information = textFieldLogic.getFilteredInformartion();
			String size = M_ChBoSize.getValue().toString();
			String password = M_InputPassword.getText();

			list.addMultipleItems(tempList, type, information, size, password);
			M_Table.setItems(list.getList());

			if (list.getModeSplit() == false) {
				InformationColumn.setVisible(false);// disable information
													// column
			} else {
				InformationColumn.setVisible(true);// renable information column
			}
		}

	}

	/**
	 * Control of type choicebox selector Disable the size choicebox if the type
	 * "nr of parts" is selected Otherwise Reable the size choicebox if the type
	 * "nr of parts" is not selected
	 *
	 * @param event
	 */
	@FXML
	private void choiceBoxControl(ActionEvent event) {
		if (M_ChBx.getValue().toString().compareTo("Nr of Parts") == 0) {
			M_ChBoSize.setDisable(true);
		} else {
			M_ChBoSize.setDisable(false);
		}
		if (M_ChBx.getValue().toString().compareTo("Dimension and Encrypt") != 0) {
			M_InputPassword.setDisable(true);
		} else {
			M_InputPassword.setDisable(false);
		}
	}

	/**
	 * Method for the context menu in the table view with the edit file feature,
	 * open a new popup window with the scene FSChangeFile.FXML
	 *
	 * @param event
	 */
	@FXML
	private void handleFileEditOption(ActionEvent event) {
		tableViewLogic.handleFileEditOption(event);
	}

	/**
	 * Method for the context menu in the table view with the delete item
	 * feature
	 *
	 * @param event
	 */
	@FXML
	private void handleFileDeleteOption(ActionEvent event) {
		tableViewLogic.handleFileDeleteOption(event, M_Table.getSelectionModel().getSelectedItem());
	}

	/**
	 * Method to handle the clear queue command in the File Menu > Edit
	 *
	 * @param event
	 */
	@FXML
	private void handleClearQueue(ActionEvent event) {
		menuBarLogic.handleClearQueue(event, list);
	}

	/**
	 * Method to handle the change Directory Folder output in the File Menu >
	 * File
	 *
	 * @param event
	 */
	@FXML
	private void handleChangeDirectoryFolder(ActionEvent event) {
		directoryFolder = menuBarLogic.handleChangeDirectoryFolder(event, directoryFolder);
	}

	/**
	 * Function for start processing the list
	 *
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void handleStartQueueSplit(ActionEvent event) throws Exception {

		ProgressColumn.setVisible(true);

		 JobListSelector startJobs = new JobListSelector(directoryFolder, list.getList(), list.getModeSplit());

		 GlobalProgress globalBarClass = new GlobalProgress(updateProgress, list);

		TablePeriodicRefresh refreshTable = new TablePeriodicRefresh(M_Table, globalBarClass);

		Thread tableThread = new Thread(refreshTable); // start the refresh
														// Table thread
		tableThread.setDaemon(true);
		tableThread.start();

		startJobs.startProcessList(globalBarClass, nodesInterationLogic);// start to select the list

	}

	// ******************************************
	// ***************** DEBUG CODE ************
	// ******************************************

	@FXML
	private Button TempDebugPrint;

	@FXML
	private static final TextArea textArea = new TextArea();

	private int first = 0;
	private int last = 0;

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

	@FXML
	private void printConsoleList(ActionEvent event) {
		M_Table.refresh();

		System.out.println("inizio");
		System.out.println("--------------");
		for (FileElement each : list.getList())// ciclo ObservableList
		// for (FileElement each: masterList.getItems())//ciclo ListVIew

		{
			System.out.println("filename: " + each.getFileName() + " | file source path :" + each.getSourcePath());

			System.out.println(each.getFileName().toString());
			System.out.println("password :" + each.getPassword());
		}
		System.out.println("--------------");
		System.out.println("fine");
		System.out.println("    ");
	}

}
