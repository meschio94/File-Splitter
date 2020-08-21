package logic;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This Class set the type of element the list is composed, all the FileElements are Property value used by the MainWindow GUI TableView for autoupdate itself
 * if some field change, in this class there is the usual get and set method used by all the program classes
 * @author Meschio
 *
 */
public class FileElement {
	/**
	 * The File Path
	 */
	private final SimpleStringProperty file = new SimpleStringProperty("");

	/**
	 * The type of operation
	 */
	private final SimpleStringProperty type = new SimpleStringProperty(""); //stringa con lettere che rappresentano il tipo di operazione

	/**
	 * The information about the bytes of the aprt or the nr of parts to split
	 */
	private SimpleIntegerProperty information = new SimpleIntegerProperty(-1);  //numero che rappresenta il nr di aprti o grandezza delle parti in base al type

	/**
	 * The size of the information: KB, MB or GB
	 */
	private SimpleStringProperty size = new SimpleStringProperty(""); //peso conversione

	/**
	 * The status of the element, used to inform the user about the operation
	 */
	private SimpleStringProperty status = new SimpleStringProperty("Ready to start"); //status of the operation with default setting

	/**
	 * The password of the element used by crypt/decrypt operations
	 */
	private SimpleStringProperty password = new SimpleStringProperty(""); //status of the operation with default setting

	/**
	 * The progress of the processing elements, used by tableColumn
	 */
	private SimpleDoubleProperty progress = new SimpleDoubleProperty(0.0);

	/**
	 * Default constructor of FileElement
	 */
	public FileElement(){
		this("","",-1,"","");
	}

	/**
	 * Constructor of the FileElement
	 * @param srcPath the Path location of the File
	 * @param inputType the String of the operation
	 * @param inputInformation the bytes of operation
	 * @param inputSize the multiplicator of the information
	 * @param inputPassword the password of the element
	 */
	public FileElement (String srcPath, String inputType, int inputInformation, String inputSize, String inputPassword){
		setFile(srcPath);
		setType(inputType);
		setInformation(inputInformation);
		setSize(inputSize);
		setPassword(inputPassword);
		setProgress(0.0);

	}

	//********************************************************************//
	//***********************| SET FUNCTION |*****************************//
	//********************************************************************//

	/**
	 * set function of file
	 * @param src_path of the file
	 */
	public void setFile(String src_path){
		file.set(src_path);
	}

	/**
	 * set function of status
	 * @param inputStatus String of the file
	 */
	public void setStatus(String inputStatus){
		status.set(inputStatus);
	}

	/**
	 * set function of size
	 * @param inputSize of the file
	 */
	public void setSize(String inputSize){
		size.set(inputSize);
	}

	/**
	 * set function of Type
	 * @param inputType
	 */
	public void setType(String inputType){
		type.set(inputType);
	}

	/**
	 * set function of password
	 * @param password of the file
	 */
	public void setPassword(String inputPassword){
		password.set(inputPassword);
	}

	/**
	 * set function of information
	 * @param inputInformation
	 */
	public void setInformation(int inputInformation){
		information.setValue(inputInformation);
	}

	/**
	 * set function of progress
	 * @param inputProgress
	 */
	public void setProgress(double inputProgress){
		progress.setValue(inputProgress);
	}


	//********************************************************************//
	//***********************| GET FUNCTION |*****************************//
	//********************************************************************//

	/**
	 * get file size in bytes
	 * @return long value
	 */
	public long getFileSize(){
		File f = new File(getSourcePath());
		return f.length();
	}

	/**
	 * Get the source path  function
	 * @return return the File source path as String
	 */
	public String getSourcePath(){
		return file.get();
	}

	/**
	 * Get the path of the folder where the file is located
	 * @return directory folder
	 */
	public String getSourcePathFolder(){
		File file = new File(getSourcePath());
		String path = file.getParentFile().getAbsolutePath();
		return path;
	}

	/**
	 * Get Size function
	 * @return return as String
	 */
	public String getSize(){
		return size.get();
	}

	/**
	 * GetType function
	 * @return return the type as String
	 */
	public String getType(){
		return type.get();
	}

	/**
	 * GetType function
	 * @return return the status as Srting
	 */
	public String getStatus(){
		return status.get();
	}

	/**
	 * GetInformation function
	 * @return return the information as int
	 */
	public int getInformation(){
		return information.get();
	}

	/**
	 * GetPAssword function
	 * @return password as String
	 */
	public String getPassword(){
		return password.get();
	}

	/**
	 * GetProgress function
	 * @return return the progress as double
	 */
	public double getProgress(){
		return progress.get();
	}

	/**
	 * GetFile function for observable value
	 * @return return the File from the FileElement for observable element
	 */
	public StringProperty getFileProperty(){
		return file;
	}

	/**
	 * GetStatus function for observable value
	 * @return return the status from the FileElement for observable element
	 */
	public StringProperty getStatusProperty(){
		return status;
	}

	/**
	 * Get progress function for observable value
	 * @return the progress from the FileElement for observable element
	 */
	public DoubleProperty getProgressProperty(){
		return progress;
	}

	/**
	 * GetFile name function
	 * @return return the file name as string
	 */
	public String getFileName(){
		File f = new File(getSourcePath());
		return f.getName();
	}

	/**
	 * getFile name without extension
	 * @return the file name without extension String
	 */
	public String getFileNamePure(){
		File f = new File(getSourcePath());
		String fileName = f.getName().replaceFirst("[.][^.]+$", "");
		return fileName;
	}



	//********************************************************************//
	//*******************| GET PROPERTY FUNCTION |************************//
	//********************************************************************//

	/**
	 * GetFile name function for observable value
	 * @return return the File name from the FileElement for observable element
	 */
	public StringProperty getFileNameProperty(){
		File f = new File(getSourcePath());
		SimpleStringProperty fName = new SimpleStringProperty("");
		fName.set(f.getName());
		return fName;
	}

	/**
	 * GetSize function for observable value
	 * @return return the size from the FileElement for observable element
	 */
	public StringProperty getSizeProperty(){
		return size;
	}

	/**
	 * GetType function for observable value
	 * @return type from the FileElement for observable element
	 */
	public StringProperty getTypeProperty(){
		return type;
	}

	/**
	 * GetPassword function for observable value
	 * @return password from the FileElement for observable element
	 */
	public StringProperty getPasswordProperty(){
		return password;
	}

	/**
	 * Method for returning a print of the information + size if type != nr of parts, only information otherwise
	 * useful for the information column in a tableview
	 * @return an observable string
	 */
	public StringProperty getInformationPrintProperty(){
		String string;
		SimpleStringProperty observableString = new SimpleStringProperty("");
		if (this.getType().compareTo("Nr of Parts") == 0){
			string = String.valueOf(this.getInformation());
		} else {
			string = this.getInformation() + " " + this.getSize();
		}
		observableString.set(string);

		return observableString;
	}

	/**
	 * GetInformation function for observable value
	 * @return return the information from the FileElement for observable element
	 */
	public IntegerProperty getInformationProperty(){
		return information;
	}

}
