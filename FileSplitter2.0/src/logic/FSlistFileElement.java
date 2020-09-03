package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import error.AlertMaker;
//import FSlistElementInspector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import logic.header.FSHeadingReader;


/**
 * This Class host the {@link @FileElement} wich the list is composed, handle some control/filter when an item is added and set the type of mode of the list
 *
 *
 * @author Meschio
 *
 */
public class FSlistFileElement {
	/**
	 * The input List to manipulate
	 */
	private ObservableList<FileElement> list = FXCollections.observableArrayList();

	/**
	 * the mode value of the queue: true is split, false is chop
	 * Used to handle the logic of the {@link #modeBoolEngine}
	 */
	public boolean modeSplit;

	/**
	 * the flag of the extension used by actual mode, True if we are working with .sp files
	 * Used to handle the logic of the {@link #modeBoolEngine}
	 */
	public boolean extensionSp;

	/*
	 * The constructor of the FSlistFileElement
	 * take in input the ObservableList<FileElement> list to manipulate
	 *
	 * and set the modeSplit and extensionSp Flag
	 */
	public FSlistFileElement(ObservableList<FileElement> listView){
		list = listView;
		modeSplit = true;
		extensionSp = false;
	}


	/**
	 * The default method for add item/items to the list, get in input a list<FileElement> and cycle it one by one
	 * @param tempList the list of elements to add
	 * @param type of operation
	 * @param information
	 * @param size
	 * @param password
	 * @throws IOException
	 */
	public void addMultipleItems(List<File> tempList, String type,int information, String size, String password)throws IOException{

		for (int i = 0; i < tempList.size(); i++){
			String path = tempList.get(i).getAbsolutePath();
			addItem(path,type,information,size,password);
		}
	}

	/**
	 * add a single item in the list, check for duplicate and division split math
	 * if a file is a .sp extension and the mode meet the requirement will change the mode of the queue with modeBoolEngine
	 * @param inputSrcPath
	 * @param inputType
	 * @param inputInformation
	 * @param inputSize
	 * @param inputPassword
	 */
	public void addItem(String inputSrcPath, String inputType, int inputInformation, String inputSize, String inputPassword )throws IOException{
		String password;
		if ((inputPassword == "") || (inputPassword == null)){
			password = null;
		}
		password = inputPassword;
		FileElement data = new FileElement(inputSrcPath,inputType ,inputInformation,inputSize,password);//set the file element

		FSlistElementInspector dataCheck = new FSlistElementInspector(data);//set the ListInspector for some check

		int addFlag = modeBoolEngine(data); //addflag control, -1 the file will don't be added to the list

		if((addFlag != -1) && (modeSplit == false)){
			updateFileInfoChopMode(data);
		}

		if ((dataCheck.filePartsCheck() == true) && (dataCheck.fileListExist(list) == false) && (dataCheck.fileIsNotEmptyCheck() == true) && (addFlag != -1)){ //integrity add queue item check
			list.add(data);

		}

	}

	/**
	 * method for remove an item
	 * @param data
	 */
	public void remove(FileElement data){
		list.remove(data);
	}

	/**
	 * method for flush the list
	 */
	public void clear(){
		list.clear();
	}

	/**
	 * method for get the list
	 * @return list
	 */
	public ObservableList<FileElement> getList(){
		return list;
	}


	/**
	 * Function to select the right mode to change if the condition of mode and .sp file detected are meeted
	 * is a boolean math check, update the queue to the user choice if all the operation and check performed true
	 *
	 *in case of a .sp file revealed (extension = TRUE) will be performed a check if the file i an header (.0.sp extension) and if the header is readable without error
	 *
	 *<p>
	 *  extension |  mode<p>
	 * -------------------<p>
	 *    TRUE    |  TRUE  -> change to chop<p>
	 *    TRUE    |  FALSE -> remain chop<p>
	 *   FALSE    |  TRUE  -> change to split<p>
	 *   FALSE    |  TRUE  -> remain split<p>
	 * @param data
	 * @return int 0 = nothing happen the item is added, 1 = add item and mode is changed, -1 = nothing happen but the item don't will be added to queue
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private int modeBoolEngine(FileElement data) throws FileNotFoundException, IOException{
		FSlistElementInspector dataCheck = new FSlistElementInspector(data);

		boolean extension = dataCheck.handleExtensionCheck();
		int result = 0;

		if((extensionSp != extension) && (result == 0)){ // check if the extension is different with the extension flag for the actual mode selected

			if((extension == false) && (modeSplit == false)){
				handleInputSplitMode(data);
				if(extensionSp == extension) {
					result = 1;
				}else {result = -1;}
			}

			if((extension == true) && (modeSplit == true)){
				handleInputChopMode(data);

				handleHeaderExistence(data);
				if(data.getSourcePath() == null){ //the .sp file is not an header
					result = -1;
				} else {
					if(extensionSp == extension) {
						result = 1;
					}else {result = -1;}
				}
			}

		} else if(extension == true){// check extension and selector
			handleHeaderExistence(data); //check and correct if the file is not an header
			if(data.getSourcePath() == null){ //the .sp file is not an header
				result = -1;
			} else { //the file is an header
				FSHeadingReader headerInfo = new FSHeadingReader(data);


		    	if( headerInfo.isSuccessful() == true){//check the successful flag of headerReader

		    		if(modeSplit == true){ list.clear();} //clear the queue if the mode is split
		    		modeSplit = false; //set the new mode
			    	extensionSp = true; //set the new sp extension flag

			    	result = 1;}
		    	else{
		    		AlertMaker.showSimpleAlert("Operation Aborted", "The header file of :" + data.getFileName() + " isn't readable");
		    		result = -1;}

			}
		}

		return result;
	}

	/**
	 * Chop mode selector used by {@link #modeBoolEngine(FileElement)}
	 * if a .sp file extension detected, ask the user if want to flush the queue, add the item and change queue mode and flag extension flag
	 * if the user cancelled the action or close the window nothing will happen
	 * @param data
	 *
	 */
	private void handleInputChopMode(FileElement data){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		if(list.size() >= 1){
			alert.setTitle("Split File Extension Detected");
		    alert.setContentText("Do you want to flush the queue, add : " + data.getFileName() + " and change the queue mode to Chop ?");
		    Optional<ButtonType> answer = alert.showAndWait();
		    if (answer.get() == ButtonType.OK) {
		    	list.clear();
		    	modeSplit = false; //set the new mode
		    	extensionSp = true; //set the new sp extension flag
		    } else {
		         AlertMaker.showSimpleAlert("Operation aborted", "change of queue mode cancelled");
		     }
		} else {
			list.clear();
			modeSplit = false; //set the new mode
	    	extensionSp = true; //set the new sp extension flag
		}
	}

	/**
	 * Split mode selector used by {@link #modeBoolEngine(FileElement)}
	 * if a .sp file extension detected, ask the user if want to flush the queue, add the item and change queue mode and flag extension flag
	 * if the user cancelled the action or close the window nothing will happen
	 * @param data
	 */
	private void handleInputSplitMode(FileElement data){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		if(list.size() >= 1){
			alert.setTitle("Split File Extension Missed");
		    alert.setContentText("Do you want to flush the queue, add : " + data.getFileName() + " and change the queue mode to Split ?");
		    Optional<ButtonType> answer = alert.showAndWait();
		    if (answer.get() == ButtonType.OK) {
		    	 list.clear();
		    	 modeSplit = true; //set the new mode
		    	 extensionSp = false; //set the new sp extension flag
		    } else {
		         AlertMaker.showSimpleAlert("Operation aborted", "change of queue mode cancelled");
		    }
		} else {
			list.clear();
			 modeSplit = true; //set the new mode
	    	 extensionSp = false; //set the new sp extension flag
		}
	}

	/**
	 * Method for check if the .sp file is an header (.0.sp) if not, will ask the user if he want to automatic search it in the folder.
	 * If the user don't want to search the header or the header searched can't be found, the element File field will be set to null.
	 * @param data
	 */
	private void handleHeaderExistence(FileElement data){
		FSlistElementInspector dataCheck = new FSlistElementInspector(data);


		if (dataCheck.handleHeaderExtensionCheck() != true){ //the file is eligible


			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

				alert.setTitle("Split File is not the header");
			    alert.setContentText("File is a .sp extension, but is not the header, the 0 part, do you want to check for the existence of the header in the folder and add it ?");
			    Optional<ButtonType> answer = alert.showAndWait();
			    if (answer.get() == ButtonType.OK) {
			    	 if(dataCheck.checkHeaderFileExistence() == true){
			    		 data.setFile(dataCheck.headerLocationCalculator());

			    	 } else {
			    		 AlertMaker.showSimpleAlert("Missing Header File", "the header file : " + data.getFileNamePure() + ".0.sp don't exist");
			    		 data.setFile(null);
			    	 }

			    } else {
			         AlertMaker.showSimpleAlert("Operation Aborted", "The file :" + data.getFileName() + " will not be added");
			         data.setFile(null);
			    }

		}

	}

	/**
	 * return the actual mode of the queue
	 * @return true if is in split state, false if not (Chop status)
	 */
	public boolean getModeSplit(){
		return modeSplit;
	}

	/**
	 * If a .sp file is added in the queue this method will update the FileElement Type and password item
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void updateFileInfoChopMode(FileElement data) throws FileNotFoundException, IOException{
		FSHeadingReader headerInfo = new FSHeadingReader(data);

    	if( headerInfo.isSuccessful() == true){

    		data.setPassword(headerInfo.getPasswordInfo());//set passwordInfo
        	data.setType(headerInfo.getType()); //set the new type
    	}

	}

	/**
	 * get method of the nr of elements in the list
	 * @return the nr elements
	 */
	public int getListSize(){
		return list.size();
	}


}
