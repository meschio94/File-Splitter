package stream.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import error.AlertMaker;
import gui.progress.ColumnProgress;
import javafx.application.Platform;
import logic.FileElement;
import logic.header.FSHeadingReader;
import stream.StreamCore;

/**
 * Abstract class which extends the generic {@link stream.StreamCore}. Used for write
 * with {@link stream.StreamCore#writeParts} and for read the header file. Update the
 * successfulFlag if some error occur
 *
 * @author Meschio
 *
 */
public abstract class InputCore extends StreamCore {

	protected FSHeadingReader headerInfo;

	/**
	 * Constructor of InputCore, call the parent constructor
	 * {@link stream.StreamCore}, set the fileProgress value and read the headerFile.
	 *
	 * @param srcPath source output path
	 * @param data element
	 * @throws IOException IOException
	 */
	public InputCore(String srcPath, FileElement data) throws IOException {
		super(srcPath, data);

		this.headerInfo = new FSHeadingReader(data);
		if (headerInfo.isSuccessful() == false) {
			setFlagFalse();
		}

		this.fileProgress = new ColumnProgress(data.getProgressProperty(), getTotalBytesToWrite());

	}

	/******************************************************/
	/**************** | ABSTRACT METHOD | *****************/
	/******************************************************/
	
	/**
	 * Method for close the current stream and open the next part
	 *
	 * @param index of part
	 * @throws IOException IOException
	 */
	abstract public void openNextFile(int index) throws IOException;
	
	/**
	 * Method used by {@link stream.StreamCore.#writeParts} for get the OutputStream, specialized
	 * in the inherited class
	 *
	 * @return the OutputStream
	 */
	protected abstract OutputStream getOutputStream();

	/**
	 * Method used by {@link stream.StreamCore.#writeParts} for set the OutputStream, specialized
	 * in the inherited class
	 *
	 * @param srcPathOut
	 * @param fileName
	 * @param index
	 * @throws FileNotFoundException
	 */
	protected abstract void setOutputStream(String srcPathOut) throws FileNotFoundException;

	/**
	 * Method used by {@link stream.StreamCore.#writeParts} for set the start cycle write
	 * operation
	 *
	 * @param partIndex, index of the cycle
	 * @throws IOException
	 */
	protected abstract void handleMethodStartOperation(int partIndex) throws IOException;

	/**
	 * Method usedby {@link stream.StreamCore.#writeParts} for set the final cycle write operation
	 *
	 * @param partIndex, index of the cycle
	 * @throws IOException
	 */
	protected abstract void handleMethodEndOperation() throws IOException;

	/******************************************************/
	/****************** | GET METHODs | *******************/
	/******************************************************/

	/**
	 * get method specialized of file lenght
	 *
	 * @return file lenght long value
	 */
	public long getTotalFileLenght() {
		long fileLenght = ((handleNrOfParts() * handleBytesPerParts()) + handleBytesFinalPart());
		return fileLenght;
	}

	/**
	 * Specialized method for inputCore for returning the total bytes to write
	 *
	 * @return lenght of the file
	 */
	public long getTotalBytesToWrite() {
		return getTotalFileLenght();
	}

	/**
	 * Specialized method for inputCore for returning the Nr Of Parts from the
	 * header
	 *
	 * @return NrOfParts
	 */
	public long handleNrOfParts() {
		return headerInfo.getNrOfParts();
	}

	/**
	 * Specialized method for inputCore for returning the Bytes Per Parts from
	 * the header
	 *
	 * @return BytesPerPart
	 */
	public long handleBytesPerParts() {
		return headerInfo.getBytesPerPart();
	}

	/**
	 * Specialized method for inputCore for returning the Bytes Last Part from
	 * the header
	 *
	 * @return BytesLastPart
	 */
	public long handleBytesFinalPart() {
		return headerInfo.getBytesLastPart();
	}

	/******************************************************/
	/*************** | OPERATION METHODs | ****************/
	/******************************************************/
	
	/**
	 * Method for create a custom output folder where the result of the manipulation will be put
	 * @param data element
	 * @param srcPath output path
	 * @return path of the output file folder
	 * @throws IOException
	 */
	protected String createFolderOutput(FileElement data, String srcPath) throws IOException {

		String directoryName = srcPath + File.separator + data.getFileName() + " Reconstructed from " + data.getType();

		try {
			File directory = new File(directoryName);
			if (directory.exists() == false) {
				directory.mkdir();

			} else if (directory.exists() == true) {

				setFlagFalse();
				Platform.runLater(() -> AlertMaker.showErrorMessage("File Directory Error",
						"The directory of file: " + data.getFileName() + "  already exist"));
			}
		} catch (Exception e) {
			Platform.runLater(() -> AlertMaker.showErrorMessage("Error Directory", "An error occured trying to open the source directory ")); // forse

			directoryName = "";
			setFlagFalse();
			e.printStackTrace();
		}
		return directoryName;
	}
	
	

	/**
	 * writeParts specialized class of {@link #InputCore} for chop a file with every part of it.<p>
	 *
	 * Update the {@link #data} status of the operation for the
	 * TableView set the {@link #successfulFlag} to false if some error occur.
	 * rely of the set {@link #setOutputStream} and get {@link #getOutputStream}
	 * specialized in the inherited class, and rely to
	 * {@link #handleMethodStartOperation} and {@link #handleMethodEndOperation}
	 * for cycle operations specialized in the inherited class
	 */
	public void writeParts() throws Exception {

		long nrOfParts = handleNrOfParts();
		setOutputStream(srcPathOut); // set the output Stream
		OutputStream outputStream = getOutputStream(); // get the outputstream

		for (int i = 1; i <= nrOfParts; i++) {

			openNextFile(i);

			if (isSuccessful() == true) {// flag control operation
				handleMethodStartOperation(i);
				super.buffer(outputStream);
				handleMethodEndOperation();
			}
		}

		if (isSuccessful() == true) {// flag control operation
			long bytesLastPart = handleBytesFinalPart();
			if (bytesLastPart > 0) {

				openNextFile((int) handleNrOfParts() + 1);

				if (isSuccessful() == true) {
					handleMethodStartOperation((int) handleNrOfParts() + 1);
					writeBytes(outputStream, bytesLastPart);
					handleMethodEndOperation();
				}
			}
		}

		//stream.close();
		//getOutputStream().close();


	}


}
