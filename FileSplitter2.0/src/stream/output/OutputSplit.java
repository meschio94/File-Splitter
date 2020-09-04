package stream.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import logic.FileElement;
/**
 * Class specialized for split a file via {@link #splitFile}, extends the {@link stream.output.OutputCore} class.
 * Split an Element in parts
 * @author Meschio
 *
 */
public class OutputSplit extends OutputCore{

	/**
	 * The BufferedOutputStream where to write the file parts
	 */
	private BufferedOutputStream fileOutputStream;

	/**
	 * Constructor of OutputSplit, just call the super constructor of {@link stream.output.OutputCore}
	 * @param srcPath source output path
	 * @param data element
	 * @throws IOException IOException
	 */
	public OutputSplit(String srcPath, FileElement data) throws IOException{
		super(srcPath,data);
	}

	/**
	 * Specialized get method of OutputSplit, return the {@link #fileOutputStream}.
	 * Used by {@link #writeParts}
	 */
	protected OutputStream getOutputStream(){
		return fileOutputStream;
	}

	/**
	 * Specialized set method of OutputSplit, set the {@link #fileOutputStream}.
	 * Used by {@link #writeParts}
	 */
	protected void setOutputStream(String srcPathOut, String fileName, int index ) throws FileNotFoundException{
		fileOutputStream = new BufferedOutputStream(new FileOutputStream(srcPathOut + File.separator + fileName +"." + index + ".sp"));
	}

	/**
	 * splitFile Function for write the actual FileElement from the OutSplit Constructor
	 * Will perform the action only if successfulFlag value is set to true,
	 * otherwise will update the data status to "Error"
	 * @throws IOException IOException
	 */
	public void splitFile() throws Exception {

		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
		} else {
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
		getOutputStream().close();
	}
}
