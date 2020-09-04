package stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import error.AlertMaker;
import gui.progress.ColumnProgress;
import javafx.application.Platform;
import logic.FileElement;
import stream.input.InputCore;
import stream.output.OutputCore;

/**
 * StreamCore Abstract class, the heart of the stream package, is the generic abstract class which handle the specialized class {@link InputCore} and {@link OutputCore}
 * This class handle the {@link #successfulFlag} boolean operator, which update the status of all the stream operation and inform the class about the state of the file the queue is working on
 * StreamCore host some generic utility info about the file opened, some logic calculation for the file data (a converter {@link #sizeConverter} for the type of information) and most important
 * the {@link #writeBytes} and a {@link #buffer} used to write into a new file
 * @author Meschio
 *
 */
public abstract class StreamCore {

	/**
	 * The source path of the output manipulation
	 */
	protected String srcPathOut;

	/**
	 * The raw FileElement info to work on
	 */
	protected FileElement data;

	/**
	 * The file to open
	 */
	protected File file;

	/**
	 * The type of the operation the file was manipulated
	 */
	protected String type;

	/**
	 * the information for manipulate the file, will be converted with the {@link #size}
	 */
	protected long information;

	/**
	 * The size of the information, converted by {@link #sizeConverter}
	 */
	protected int size;

	/**
	 * the stream of the file to read in the {@link #writeBytes}
	 */
	protected InputStream stream;

	/**
	 * password string extrapolated from the element
	 */
	protected String password;

	/**
	 * Flag used to check the file status operation, in the constructor start to true, will change to false if some error occur
	 */
	protected boolean successfulFlag;

	/**
	 * Int costant used by {@link #buffer} for read/write chunk of the part
	 */
	protected int writeBytesCostant = 2048; //costant bytes to write in the buffer

	/**
	 * ColumnProgress used for update the {@link logic.FileElement.progress} obeservable value in {@link #writeBytes}
	 */
	protected ColumnProgress fileProgress;


	/**
	 * Constructor of StreamCore, get in input the output Directory and a FileElement
	 * Will start to construct the custom directory output and add the data of the file in the class variables
	 * Handle the errors in this process, advise the user abut the error via custom error message and update the FileElement status
	 *
	 * @param srcPath output path
	 * @param data element to process
	 * @throws FileNotFoundException FileNotFoundException
	 */
	public StreamCore(String srcPath, FileElement data) throws FileNotFoundException{
		setFlagTrue();


		try{

			this.srcPathOut = createFolderOutput(data, srcPath);

			this.file = new File (data.getSourcePath());

			if(file.exists() == false){

				Platform.runLater(() -> AlertMaker.showErrorMessage("No File found", "The file: " + data.getFileName() + " can't be found ")); //forse conviene un return false e mettere l'allert fuori

				data.setStatus("Error");//update status
				setFlagFalse();
			}

		} catch(Exception e){
			Platform.runLater(() -> AlertMaker.showErrorMessage("No File found", "The file: " + data.getFileName().toString() + " can't be found ")); //forse conviene un return false e mettere l'allert fuori
			setFlagFalse();
			e.printStackTrace();
		}

		if (isSuccessful() == true){
			this.type = data.getType();
			this.information = data.getInformation();
			this.size = sizeConverter(data);
			this.data = data;
			this.password = data.getPassword();



			this.stream = new FileInputStream(this.file);
		}
	}

	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	public boolean isSuccessful() {
        return successfulFlag;
    }

	/******************************************************/
	/****************** | SET METHODs | *******************/
	/******************************************************/

	/**
	 * Method for set the successfulFlag to true
	 */
	public void setFlagTrue(){
		successfulFlag = true;
	}

	/**
	 * Method for set the successfulFlag to false
	 */
	public void setFlagFalse(){
		successfulFlag = false;
	}

	/**
	 * set function of status
	 * @param inputStatus String of the file
	 */
	public void setStatus(String inputStatus){
		data.setStatus(inputStatus);
	}

	/******************************************************/
	/****************** | GET METHODs | *******************/
	/******************************************************/

	/**
	 * get method of file length
	 * @return file length long value
	 */
	public long getFileLenght(){
		return file.length();
	}

	/**
	 * Get function of status
	 * @return Status of the Element
	 */
	public String getStatus(){
		return data.getStatus();
	}

	/**
	 * Get the path of the folder created where the file will be put
	 * @return directory folder output
	 */
	public String getSrcPathOut(){
		return srcPathOut;
	}

	/*******************************************************/
	/**************** | ABSTRACT METHOD | ******************/
	/******************************************************/

	/**
	 * method for calculating the nr of file parts, don't count the last part if exist
	 * specialized in {@link InputCore} and {@link OutputCore}
	 * @return int of nr of parts
	 */
	abstract public long handleNrOfParts();


	/**
	 * method for calculating the bytes per part
	 * specialized in {@link InputCore} and {@link OutputCore}
	 * @return partBytes long value
	 */
	abstract public long handleBytesPerParts();

	/**
	 * method for calculating the bytes of the final parts if exist
	 * specialized in {@link InputCore} and {@link OutputCore}
	 * @return the bytes of the last part is a long value
	 */
	abstract public long handleBytesFinalPart();


	/**
	 * writeParts method for writing a file, specialized in {@link InputCore} and {@link OutputCore}
	 * @throws Exception Exception
	 */
	abstract public void writeParts() throws Exception;

	/**
	 * Method to get for the total bytes to write, differ from input and output implementation
	 * Used by {@link #fileProgress} to update the progress in the GUI
	 * @return Total Bytes to write for the element
	 */
	abstract public long getTotalBytesToWrite();
	
	/**
	 * Method for create a custom output folder where the result of the manipulation will be put
	 */
	abstract protected String createFolderOutput(FileElement data, String srcPath) throws IOException;

	/******************************************************/
	/*************** | OPERATION METHODs | ****************/
	/******************************************************/

	/**
	 * Method that return the multiplication of byte of the size selected
	 * @param data The element to convert
	 * @return the byte multiplication or -1 if the file has a by parts division (-2 if occur an error)
	 */
	public int sizeConverter(FileElement data){
		if (data.getType().compareTo("Nr of Parts") == 0){
			return -1;
		}

		switch(data.getSize()) {
		  case "Kb":
			return 1000;

		  case "Mb":
			return 1000000;

		  case "Gb":
			return 1000000000;

		  default:
			  Platform.runLater(() -> AlertMaker.showErrorMessage("Error", "unexpected error in the size file value"));
			  setFlagFalse();
			  return -2;
		}
	}



	/**
	 * Buffer method for handle big size part to write in output, splitting in chunks
	 * called by {@link #writeParts}
	 * @param outputStream outputStream
	 * @throws IOException
	 */
	protected void buffer(OutputStream outputStream) throws Exception {

		if(isSuccessful() == true){
			long bytesPart = handleBytesPerParts();

			if (bytesPart > writeBytesCostant){

				long readBytes = bytesPart/writeBytesCostant;
				long readRemainBytes = bytesPart % writeBytesCostant;

				for (int numberOfReadBytes = 0; numberOfReadBytes < readBytes ; numberOfReadBytes++){
					writeBytes(outputStream, writeBytesCostant);
				}

				if (readRemainBytes > 0) {
					writeBytes(outputStream,readRemainBytes);
				}
			} else {
				writeBytes(outputStream,bytesPart);
			}
		}
	}


	/**
	 * Write Bytes method to handle the {@link #buffer}, will also update the column bar progress
	 * @param outputStream outputStream
	 * @param part part to write
	 * @throws IOException
	 */
	protected void writeBytes(OutputStream outputStream, long part) throws Exception {
		if(isSuccessful() == true){

			fileProgress.incColumnBar();//update the progress of the element

			byte[] buffer = new byte[(int) part];
			if(stream.read(buffer) != -1){
				outputStream.write(buffer);
			}

		}
	}




}
