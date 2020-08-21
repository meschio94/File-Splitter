package logic;

import java.io.File;


import error.AlertMaker;
import javafx.collections.ObservableList;

/**
 * This is a Util class of {@link @FSlistFileElement} with some useful methods
 * @author Meschio
 *
 */
public class FSlistElementInspector {

	/**
	 * Element to analyze
	 */
	private FileElement data;

	/**
	 * Constructor of the class
	 * @param inputData
	 */
	public FSlistElementInspector(FileElement inputData){
		this.data = inputData;

	}

	/**
	 * method to check the file size and the nr of parts split
	 * @return true if you can split the file, false otherwise
	 */
	public boolean filePartsCheck(){
		File file = new File(data.getSourcePath());
		double size = file.length()/ data.getInformation();
		if((size < 1) && (data.getType().compareTo("Nr of Parts") == 0)){
			AlertMaker.showErrorMessage("File ", "The file: " + data.getFileName() + " size is too small for this type of split");
			return false;
		}

		return true;
	}

	/**
	 * Method to check if the file is empty
	 * @return false if the file is empty, true otherwise
	 */
	public boolean fileIsNotEmptyCheck(){
		File file = new File(data.getSourcePath());
		if (file.length() == 0) {
			AlertMaker.showErrorMessage("Empty file", "The file: " + data.getFileName() + " is empty ");
			return false;
		}
		return true;
	}

	/**
	 * method for check if the file is already in the queue
	 * @param list
	 * @return true if the file is already in the queue, false otherwise
	 */
	public boolean fileListExist(ObservableList<FileElement> list){
		for(FileElement each: list){
			if(each.getSourcePath().compareTo(data.getSourcePath()) == 0 ){
				AlertMaker.showErrorMessage("File Duplicate", "The file: " + data.getFileName() + " is already in the queue, select other file");
				return true;
			}
		}
		return false;
	}

	/**
	 * method to check if the file end with .sp extension
	 * @return true if end with ".sp", false otherwise
	 */
	public boolean handleExtensionCheck(){
		if(data.getFileName().endsWith(".sp")== true){
			return true;
		}
		return false;
	}

	/**
	 * method to check if the file end with .0.sp extension
	 * @return true if end with ".0.sp", false otherwise
	 */
	public boolean handleHeaderExtensionCheck(){
		if(data.getFileName().endsWith(".0.sp")== true){
			return true;
		}
		return false;
	}


	/**
	 * retrive the sourcepath of file header in a folder from the FileElement data in the constructor
	 * @return header file location, null if some error occur
	 */
	public String headerLocationCalculator(){

		String headerName = data.getFileNamePure();//get the filename without .sp extension

		StringBuilder temp = new StringBuilder(headerName).reverse(); // reverse the string
		int iend = temp.indexOf("."); //get the index of the firt '.' dot from the reversed string

		if (iend == -1){ return null;} //'.'didn't found error, the file don't exist

		temp.delete(0,iend + 1);//remove the char to the first dot

		headerName = new StringBuilder(temp).reverse().toString(); // reverse the string without the point or nr of parts element

		String location = data.getSourcePathFolder() + File.separator + headerName + ".0.sp";

		return location;
	}

	/**
	 * method to check the existence of the header file in the file folder
	 * @return true if exist, false otherwise
	 */
	public boolean checkHeaderFileExistence(){

		String location = headerLocationCalculator();
		if (location == null){ return false; }

		boolean headerCheck = new File(location).exists();
		System.out.println("header location " + location );//aka
		if (headerCheck == true){
			return true;
		}

		return false;
	}



}
