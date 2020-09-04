package stream.output;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import logic.FileElement;

/**
 * Class specialized for Compress a file via {@link stream.output.OutputCompress#compressFile}, extends the {@link stream.output.OutputCore} class.
 *
 * @author Meschio
 *
 */

public class OutputCompress extends OutputCore{

	/**
	 * The OutputStream where to write the file parts
	 */
	private ZipOutputStream fileOutputStream;

	/**
	 * Constructor of OutputCompress, just call the super constructor of {@link stream.output.OutputCore}
	 * @param srcPath source output path
	 * @param data element
	 * @throws IOException IOException
	 */
	public OutputCompress(String srcPath, FileElement data) throws IOException{
		super(srcPath,data);
	}

	/**
	 * Specialized get method of OutputCompress, return the {@value fileOutputStream}.in a OutputStream value
	 * Used by {@link #writeParts}
	 */
	protected OutputStream getOutputStream(){
		return fileOutputStream;
	}

	/**
	 * get method of OutputCompress, return the {@value fileOutputStream}.in a ZipOutputStream
	 * Used by {@link stream.StreamCore#writeParts}
	 */
	protected ZipOutputStream getZipOutputStream(){
		return fileOutputStream;
	}

	/**
	 * Specialized set method of OutputCompress, set the {@value fileOutputStream}.
	 * Used by {@link stream.StreamCore#writeParts}
	 */
	protected void setOutputStream(String srcPathOut, String fileName, int index ) throws FileNotFoundException{
		fileOutputStream = new ZipOutputStream(new FileOutputStream(srcPathOut + File.separator + data.getFileNamePure() +"." + index + ".sp"));
	}

	/**
	 * compressFile Function for write the actual FileElement from the OutSplit Constructor and compress it
	 * Will perform the action only if {@link stream.StreamCore#successfulFlag} is set to true,
	 * otherwise will update the data status to "Error"
	 * @throws Exception Exception
	 */
	public void compressFile() throws Exception{
		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
			getZipOutputStream().close();
		} else {
			data.setStatus("Error");
		}
	}

	/**
	 * Specialized start set operation of cycle in {@link stream.StreamCore#writeParts}
	 * For OutputCompress open the entry of the actual {@value fileOutputStream} via {@link getZipOutputStream},
	 * set the method of compression to DEFLATE, the level to 9 (the maximun)
	 */
	protected void handleMethodStartOperation(int partIndex) throws IOException{
		ZipOutputStream zip = getZipOutputStream();
		zip.setMethod(ZipOutputStream.DEFLATED);
		zip.setLevel(9);
		zip.putNextEntry(new ZipEntry(data.getFileName()));
	}

	/**
	 * Specialized end set operation of cycle in {@link stream.StreamCore#writeParts}}
	 * For OutputCompress close the entry of the actual {@value fileOutputStream} via {@link getZipOutputStream}
	 */
	protected void handleMethodEndOperation() throws IOException{
		ZipOutputStream zip = getZipOutputStream();
		zip.closeEntry();
        zip.finish();
        getOutputStream().close();
	}

}
