package logic.header;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import error.AlertMaker;
import logic.FileElement;

/**
 * Class for reading the header file information and store it
 *
 * @author Meschio
 *
 */
public class FSHeadingReader {

	/**
	 * Element to process
	 */
	private FileElement data;

	/**
	 * The header to read
	 */
	private File file;

	/**
	 * The file name extrapolated from the header
	 */
	private String fileName;

	/**
	 * The type of job extrapolated from the header
	 */
	private String type;

	/**
	 * The nrOfParts extrapolated from the header
	 */
	private String nrOfParts;

	/**
	 * The bytes per part extrapolated from the header
	 */
	private String bytesPerPart;

	/**
	 * The bytes last part extrapolated from the header
	 */
	private String bytesLastPart;

	/**
	 * The password info extrapolated from the header
	 */
	private String passwordInfo;

	/**
	 * Flag for check the status of the operations
	 */
	private boolean successfulFlag = true;

	/*
	 * constant of the max row the file handle, we count the 0
	 */
	private int fileNrRow = 5;

	/**
	 * Constructor of FSHeadingReader
	 * @param inputData the header file to read
	 * @throws FileNotFoundException FileNotFoundException
	 * @throws IOException IOException
	 */
	public FSHeadingReader(FileElement inputData) throws FileNotFoundException, IOException{

		this.data = inputData;

		try{
			this.file = new File (data.getSourcePath());//try to open the header
		} catch (Exception e){
			AlertMaker.showSimpleAlert("Header File Error", "Can't read the header file");
			setFlagFalse();
		}

		//set the elements to null
		fileName = null;
		type = null;
		nrOfParts = null;
		bytesPerPart = null;
		bytesLastPart = null;
		passwordInfo = null;

		readFile();


	}

	/**
	 * read file method for store the file information in the value of the constructor
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readFile() throws FileNotFoundException, IOException{
		int lineIndex = 0; // row File index

		FileReader fileReader = new FileReader(file);
		try (BufferedReader br = new BufferedReader(fileReader)) {
		    String rawLine;
		    String refinedLine;
		    while ((rawLine = br.readLine()) != null) {
		    	if(isSuccessful() != false){
		    		refinedLine = processRawLine(rawLine);
		    		storeRefinedLine(lineIndex, refinedLine);
		    	} else {
		    		setFlagFalse();
		    		AlertMaker.showSimpleAlert("Header File Error", "Read row error, can't process the file");
		    	}
		    	lineIndex++;
		    	if (lineIndex > 7){//handle some read error and loop, the file always had 6 line
		    		return;
		    	}
		    }
		    br.close();
		}
		fileReader.close();


	}

	/**
	 * method for clear the raw line from the header file, remove the foreword of the raw
	 * @param line
	 * @return
	 */
	private String processRawLine(String line){
		StringBuilder temp = new StringBuilder(line);

		int iend = temp.indexOf(":");
		if (iend == -1){ // can't find ":" error
			setFlagFalse();
			return null;
		}

		temp.delete(0,iend + 2);//delete the : and the space char

		if ((temp.toString() == "")  || (temp == null)){
			setFlagFalse();
		}

		return temp.toString();
	}

	/**
	 * method for fill the file info for the row readed
	 * @param i the row readed
	 * @param refinedLine the String with the information
	 */
	private void storeRefinedLine(int i, String refinedLine){

		if((i>= 0) && (i <= fileNrRow)){
			switch(i){
			case 0:
				fileName = String.copyValueOf(refinedLine.toCharArray());

				break;
			case 1:
				type = String.copyValueOf(refinedLine.toCharArray());

				break;
			case 2:
				nrOfParts = String.copyValueOf(refinedLine.toCharArray());

				break;
			case 3:
				bytesPerPart = String.copyValueOf(refinedLine.toCharArray());

				break;
			case 4:
				bytesLastPart = String.copyValueOf(refinedLine.toCharArray());

				break;
			case 5:
				passwordInfo = String.copyValueOf(refinedLine.toCharArray());

				break;
			}
		} else {
			setFlagFalse();
		}

	}

	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	public boolean isSuccessful() {
        return successfulFlag;
    }

	/**
	 * Method for set the successfulFlag to false
	 */
	public void setFlagFalse(){
		successfulFlag = false;
	}

	/**
	 * Method for set the successfulFlag to true
	 */
	public void setFlagTrue(){
		successfulFlag = true;
	}


	/**
	 * Extract the file Name without extension
	 * @return fila name without extension
	 */
	public String getFileNamePure(){
		String namePure = fileName.replaceFirst("[.][^.]+$", "");


		return namePure;
	}

	/**
	 * Extract the extension of the file
	 * @return the Strin extension
	 */
	public String getFileExtension(){
		setFlagTrue();
		String namePure = getFileName();
		StringBuilder temp = new StringBuilder(namePure).reverse(); // reverse the string
		int iend = temp.indexOf("."); //get the index of the firt '.' dot from the reversed string
		if (iend == -1){
			setFlagFalse();
			return null;} //'.'didn't found error, the file don't exist

		String extension = temp.subSequence(0,iend + 1).toString();
		StringBuilder temp2 = new StringBuilder(extension).reverse();
		extension = temp2.toString();


		return extension;

	}

	/**
	 * Method to return the location of the index part
	 * @param index the part name to return
	 * @return the String of the srcpath + location
	 */
	public String getFileNextPartLocation(int index){
		setFlagTrue();
		String namePure = getFileNamePure(); //controllare

		String location = data.getSourcePathFolder() + File.separator + namePure + "." + index + ".sp";

		return location;
	}

	/**
	 * Get method for FileName
	 * @return the name file with original extension
	 */
	public String getFileName(){
		return fileName;
	}

	/**
	 * get method for type
	 * @return the type of the operation
	 */
	public String getType(){
		return type;
	}

	/**
	 * Get method for nrOfParts
	 * @return the nr of parts
	 */
	public long getNrOfParts(){
		return Long.valueOf(nrOfParts).longValue();
	}

	/**
	 * Get method for bytesPerParts
	 * @return the bytes per part
	 */
	public long getBytesPerPart(){
		return Long.valueOf(bytesPerPart).longValue();
	}

	/**
	 * Get method for bytesLastPart
	 * @return the bytes of the last part
	 */
	public long getBytesLastPart(){
		return Long.valueOf(bytesLastPart).longValue();
	}

	/**
	 * Get method for passwordInfo
	 * @return the password info
	 */
	public String getPasswordInfo(){

		if(passwordInfo.equals("default") == true){
			return "default";
		} else {
			return passwordInfo;
		}



	}

}
