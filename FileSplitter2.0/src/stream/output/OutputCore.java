package stream.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import gui.progress.ColumnProgress;
import logic.FileElement;
import logic.header.FSHeadingCreator;
import stream.StreamCore;

/**
 * Abstract class which extends the generic {@link #StreamCore}. Used for write
 * with {@link #writeParts} and for create the header file. Update the
 * {@link #successfulFlag} if some error occur
 *
 * @author Meschio
 *
 */

public abstract class OutputCore extends StreamCore {

	/**
	 * Constructor of OutputCore, call the parent constructor
	 * {@link #StreamCore}, set the {@link #fileProgress} and create the headerFile.
	 *
	 * @param srcPath
	 * @param data
	 * @throws IOException
	 */
	public OutputCore(String srcPath, FileElement data) throws IOException {
		super(srcPath, data);
		if (isSuccessful() == true) {// check if the previous operation was successful

			createHeaderFile(data, getSrcPathOut());
		}

		this.fileProgress = new ColumnProgress(data.getProgressProperty(), getTotalBytesToWrite());
	}

	/*******************************************************/
	/**************** | ABSTRACT METHOD | ******************/
	/******************************************************/

	/**
	 * Method used by {@link #writeParts} for get the OutputStream, specialized
	 * in the inherited class
	 *
	 * @return the OutputStream
	 */
	protected abstract OutputStream getOutputStream();

	/**
	 * Method used by {@link #writeParts} for set the OutputStream, specialized
	 * in the inherited class
	 *
	 * @param srcPathOut
	 * @param fileName
	 * @param index
	 * @throws FileNotFoundException
	 */
	protected abstract void setOutputStream(String srcPathOut, String fileName, int index) throws FileNotFoundException;

	/**
	 * Method used by {@link #writeParts} for set the start cycle write
	 * operation
	 *
	 * @param partIndex, index of the cycle
	 * @throws IOException
	 */
	protected abstract void handleMethodStartOperation(int partIndex) throws IOException;

	/**
	 * Method usedby {@link #writeParts} for set the final cycle write operation
	 *
	 * @param partIndex, index of the cycle
	 * @throws IOException
	 */
	protected abstract void handleMethodEndOperation() throws IOException;


	/******************************************************/
	/****************** | GET METHODs | *******************/
	/******************************************************/

	/**
	 * Specialized method for outputCore for returning the total bytes to write
	 */
	public long getTotalBytesToWrite() {
		return getFileLenght();
	}

	/**
	 * method for calculating the bytes per part
	 *
	 * @return partBytes long value
	 */
	public long handleBytesPerParts() {
		long partBytes;
		if (this.size > 0) {
			partBytes = information * size; // multiplicator for size
			if (partBytes > getFileLenght()) {
				return getFileLenght(); // return getFileLenght if the file
										// dimension < of part
			}
		} else {
			partBytes = Math.floorDiv(getFileLenght(), information); // division for nr of parts
		}
		return partBytes;
	}

	/**
	 * method for calculating the nr of file parts Warning! don't count the last
	 * part if the division have a reminder Warning! we start to count to 0
	 *
	 * @return int of nr of parts to split the file
	 */
	public long handleNrOfParts() {
		if (size > 0) {
			long nrParts = (getFileLenght() / handleBytesPerParts());
			return nrParts;
		}
		return information - 1;// if the split is nr of parts i return
								// information minus the last part
	}

	/**
	 * method for calculating the bytes of the final parts if exist
	 *
	 * @return the bytes of the last part in a long value
	 */
	public long handleBytesFinalPart() {
		if (size == -1) { // case nr of parts input
			return (handleBytesPerParts() + (getFileLenght() % (handleNrOfParts()))); // Sum the bytes of the parts and the reminder of the division
		}
		if (handleNrOfParts() > 0) {
			System.out.println(
					"handleBytesFinalPart " + (getFileLenght() - (handleBytesPerParts() * (handleNrOfParts())))); // aka
			return (getFileLenght() - (handleBytesPerParts() * (handleNrOfParts())));
		}
		if (handleNrOfParts() == 0) { // case the file is composed of only 1
										// part
			return (handleBytesPerParts());
		}
		return 0; // no parts
	}

	public String getOutputPath(File file) {
		return srcPathOut + File.separator + file.getName();
	}

	/******************************************************/
	/*************** | OPERATION METHODs | ****************/
	/******************************************************/

	/**
	 * Create the header file document with the element data
	 *
	 * @param data
	 * @param srcPath
	 * @return true is the operation was successful, false otherwise
	 * @throws IOException
	 */
	protected boolean createHeaderFile(FileElement data, String srcPath) throws IOException {
		FSHeadingCreator header = new FSHeadingCreator(srcPath, file.getName(), type, handleNrOfParts(),
				handleBytesPerParts(), handleBytesFinalPart(), this.password);
		header.createHeadingDocument();
		if (header.isSuccessful() == false) {
			return false;
		}
		return true;
	}



	/**
	 * writeParts specialized class of {@link #OutputCore} for split a file and
	 * write on it. <p>
	 *
	 * Update the {@link #data} status of the operation for the
	 * TableView set the {@link #successfulFlag} to false if some error occur.
	 * rely of the set {@link #setOutputStream} and get {@link #getOutputStream}
	 * specialized in the inherited class, and rely to
	 * {@link #handleMethodStartOperation} and {@link #handleMethodEndOperation}
	 * for cycle operations specialized in the inherited class
	 */
	public void writeParts() throws Exception {
		System.out.println("nr parti : " + handleNrOfParts());// aka
		System.out.println("Byte prima parte :  " + handleBytesPerParts());// aka
		System.out.println("Byte parte finale" + handleBytesFinalPart());// aka

		long nrOfParts = handleNrOfParts();
		for (int i = 1; i <= nrOfParts; i++) {
			if (isSuccessful() == true) {// flag control operation
				setOutputStream(srcPathOut, data.getFileNamePure(), i);
				OutputStream outputStream = getOutputStream();
				handleMethodStartOperation(i);
				super.buffer(outputStream);
				handleMethodEndOperation();
			}
		}
		long bytesLastPart = handleBytesFinalPart();
		if (bytesLastPart > 0) {
			if (isSuccessful() == true) {// flag control operation
				setOutputStream(srcPathOut, data.getFileNamePure(), (int) handleNrOfParts() + 1);
				OutputStream outputStream = getOutputStream();
				handleMethodStartOperation((int) handleNrOfParts() + 1);
				writeBytes(outputStream, bytesLastPart);
				handleMethodEndOperation();
			}
		}
		stream.close();// close the inputstream
		getOutputStream().close();// close the outputstream

	}

}
