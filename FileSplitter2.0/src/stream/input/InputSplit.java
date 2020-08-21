package stream.input;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import logic.FileElement;

/**
 * Class specialized for split a file via {@link #splitFile}, extends the {@link #InputCore} class.
 * Reconstruct a file with all the parts
 * @author Meschio
 *
 */
public class InputSplit extends InputCore{

	/**
	 * The BufferedOutputStream where to write the reconstructed file
	 */
	private BufferedOutputStream fileOutputStream;

	/**
	 * Constructor of InputSplit, just call the super constructor of {@link #OutputCore}
	 * @param srcPath
	 * @param data
	 * @throws IOException
	 */
	public InputSplit(String srcPath, FileElement data) throws IOException{
		super(srcPath,data);
	}

	/**
	 * Specialized get method of InputSplit, return the {@link #fileOutputStream}.
	 * Used by {@link #writeParts}
	 */
	protected OutputStream getOutputStream(){
		return fileOutputStream;
	}

	/**
	 * Specialized set method of InputSplit, set the {@link #fileOutputStream}.
	 * Used by {@link #writeParts}
	 */
	protected void setOutputStream(String srcPathOut) throws FileNotFoundException{
		fileOutputStream = new BufferedOutputStream(new FileOutputStream(srcPathOut + File.separator + headerInfo.getFileNamePure() + headerInfo.getFileExtension()));
	}

	/**
	 * splitFile Function for reconstruct the actual FileElement from the InputSplit Constructor
	 * Will perform the action only if {@link #successfulFlag} is set to true,
	 * otherwise will update the {@link #data} status to "Error"
	 * @throws IOException
	 */
	public void splitFile() throws Exception {
		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
		} else {
			setFlagFalse();
			data.setStatus("Error");
		}
	}

	/**
	 * Specialized start set operation of cycle in {@link #writeParts}
	 */
	protected void handleMethodStartOperation(int partIndex){
		//nothing to do for splimethod
	}

	/**
	 * Specialized end set operation of cycle in {@link #writeParts}
	 * For OutputSplit close the actual {@link #fileOutputStream}
	 */
	protected void handleMethodEndOperation() throws IOException{
		//nothing to do for splimethod
	}


}
