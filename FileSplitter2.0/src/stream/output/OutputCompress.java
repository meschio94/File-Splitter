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
 * Class specialized for Compress a file via {@link #compressFile}, extends the {@link #OutputCore} class.
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
	 * Constructor of OutputCompress, just call the super constructor of {@link #OutputCore}
	 * @param srcPath
	 * @param data
	 * @throws IOException
	 */
	public OutputCompress(String srcPath, FileElement data) throws IOException{
		super(srcPath,data);
	}

	/**
	 * Specialized get method of OutputCompress, return the {@link #fileOutputStream}.in a OutputStream value
	 * Used by {@link #writeParts}
	 */
	protected OutputStream getOutputStream(){
		return fileOutputStream;
	}

	/**
	 * get method of OutputCompress, return the {@link #fileOutputStream}.in a ZipOutputStream
	 * Used by {@link #writeParts}
	 */
	protected ZipOutputStream getZipOutputStream(){
		return fileOutputStream;
	}

	/**
	 * Specialized set method of OutputCompress, set the {@link #fileOutputStream}.
	 * Used by {@link #writeParts}
	 */
	protected void setOutputStream(String srcPathOut, String fileName, int index ) throws FileNotFoundException{
		fileOutputStream = new ZipOutputStream(new FileOutputStream(srcPathOut + File.separator + data.getFileNamePure() +"." + index + ".sp"));
	}

	/**
	 * compressFile Function for write the actual FileElement from the OutSplit Constructor and compress it
	 * Will perform the action only if {@link #successfulFlag} is set to true,
	 * otherwise will update the {@link #data} status to "Error"
	 * @throws IOException
	 */
	public void compressFile() throws Exception{
		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
			getZipOutputStream().close();
		} else {
			data.setStatus("Error"); System.out.println("cambio stato error");//aka
		}
	}

	/**
	 * Specialized start set operation of cycle in {@link #writeParts}
	 * For OutputCompress open the entry of the actual {@link #fileOutputStream} via {@link #getZipOutputStream},
	 * set the method of compression to DEFLATE, the level to 9 (the maximun)
	 */
	protected void handleMethodStartOperation(int partIndex) throws IOException{
		ZipOutputStream zip = getZipOutputStream();
		zip.setMethod(ZipOutputStream.DEFLATED);
		zip.setLevel(9);
		zip.putNextEntry(new ZipEntry(data.getFileName()));
	}

	/**
	 * Specialized end set operation of cycle in {@link #writeParts}
	 * For OutputCompress close the entry of the actual {@link #fileOutputStream} via {@link #getZipOutputStream}
	 */
	protected void handleMethodEndOperation() throws IOException{
		ZipOutputStream zip = getZipOutputStream();
		zip.closeEntry();
        zip.finish();

	}

}
