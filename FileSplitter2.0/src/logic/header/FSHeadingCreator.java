package logic.header;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import error.AlertMaker;

/**
 * Class for create the File Header Information
 * @author Meschio
 *
 */
public class FSHeadingCreator{

	/**
	 * The file name with the extension
	 */
	private String fileName;

	/**
	 * The file name without the extension
	 */
	private String fileNamePure;

	/**
	 * The type of the operation
	 */
	private String operation;
	/**
	 * The password info, the hash if the password is custom, or "default" otherwise
	 */
	private String passwordInfo;

	/**
	 * The nr of parts the file is splitted
	 */
	private String nrOfParts;

	/**
	 * The bytes per Part
	 */
	private String bytesPerPart;

	/**
	 * The bytes of the last part
	 */
	private String bytesLastPart;

	/**
	 * The output Folder
	 */
	private String srcPath;

	/**
	 * The File Header
	 */
	private File headerFile;

	/**
	 * Flag for check the operation
	 */
	private boolean successfulFlag;

	/**
	 * Constructor of the heading Document Creator, used to create the info file document with all the information required by {@link @FSHeadingReader} for reconstruct the file
	 * @param inputSrcPath, output Folder
	 * @param filename
	 * @param operation, type of operation
	 * @param nrOfParts, nr of parts of the elements
	 * @param bytesPerPart
	 * @param bytesLastPart
	 * @param password
	 * @throws IOException
	 */
	public FSHeadingCreator(String inputSrcPath, String filename, String operation, long nrOfParts, long bytesPerPart, long bytesLastPart, String password) throws IOException{
		this.successfulFlag = true;
		this.fileName = filename;
		this.fileNamePure =  filename.replaceFirst("[.][^.]+$", ""); //

		this.operation = operation;
		this.nrOfParts = Long.toString(nrOfParts);
		this.bytesPerPart = Long.toString(bytesPerPart);
		this.bytesLastPart = Long.toString(bytesLastPart);
		this.srcPath = inputSrcPath;

		if ((password != "") && (password != null) && (password != "default")){
			passwordInfo = password;
		} else {
			passwordInfo = "default";
		}

	}

	/**
	 * Creating header file function, if the file already exist will update the successfulFlag to false
	 * @throws IOException
	 */
	public void createHeadingDocument() throws IOException{
		if (handleFileExistence() == true){//the header alredy exist
			AlertMaker.showSimpleAlert("Header File Error", "The header file : " + fileNamePure + ".0.sp" +" already exist in the location!");
			successfulFlag = false;
			return;
		}
		File header = new File(srcPath + File.separator + fileNamePure + ".0.sp");
		FileWriter writer = new FileWriter(header);

		writer.write("File Name : " + fileName + '\n'); //0 row, the FileName
		writer.write("Operation type : " + operation + '\n');//1 row, the Type of operation
		writer.write("Nr of parts : " + nrOfParts + '\n');//2 row, The nr of parts
		writer.write("Bytes per part : " + bytesPerPart + '\n'); //3 row, the byte per part
		writer.write("Bytes last part : " + bytesLastPart + '\n'); //4 row, the byte last aprt
		writer.write("Passwrod Info : " + passwordInfo + '\n'); //5 row, the apssword info

		successfulFlag = true;
		writer.close();//close the stream
	}

	/**
	 * Check the existence of the header file in the output directory
	 * @return true if already exist, false otherwise
	 */
	private boolean handleFileExistence(){
		headerFile = new File(srcPath + File.separator + fileNamePure + ".0.sp", "UTF-8" );
		if(headerFile.exists() == true){
			return true;
		}

		return false;
	}

	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	public boolean isSuccessful() {
        return successfulFlag;
    }

}
