package stream.input;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import logic.FileElement;

/**
 * Class specialized for decompress a file via {@link #decompressFile}, extends the {@link #InputCore} class.
 *
 * @author Meschio
 *
 */

public class InputDecompress extends InputCore{

	/**
	 * The OutputStream where to write the reconstructed file
	 */
	private BufferedOutputStream fileOutputStream;

	/**
	 * Constructor of InputDecompress, just call the super constructor of {@link #InputCore}
	 * @param srcPath
	 * @param data
	 * @throws IOException
	 */
	public InputDecompress(String srcPath, FileElement data) throws IOException{
		super(srcPath,data);
	}

	/**
	 * get method of OutputCompress, return the {@link #fileInputStream}.in a ZipOutputStream
	 * Used by {@link #writeParts}
	 */
	protected OutputStream getOutputStream(){
		return fileOutputStream;
	}

	/**
	 * Specialized set method of OutputCompress, set the {@link #fileInputStream}.
	 * Used by {@link #writeParts}
	 */
	protected void setOutputStream(String srcPathOut) throws FileNotFoundException{

		System.out.println("header extension pure :" + headerInfo.getFileExtension() );//aka
		fileOutputStream = new BufferedOutputStream(new FileOutputStream(srcPathOut + File.separator + headerInfo.getFileNamePure() + headerInfo.getFileExtension()));
	}

	/**
	 * decompressFile Function for extract the Files parts in the outputStream
	 *
	 * @throws IOException
	 */
	public void decompressFile() throws Exception {
		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
		} else {
			data.setStatus("Error"); System.out.println("cambio stato error");//aka
		}
	}

	/**
	 * Specialized start set operation of cycle in {@link #writeParts}
	 * For InputCompress set the stream input used by {@link #writeBytes} from an extracted zip
	 */
	protected void handleMethodStartOperation(int partIndex) throws IOException{
		stream = setNewStream(stream);
	}

	/**
	 * Specialized end set operation of cycle in {@link #writeParts}
	 *
	 */
	protected void handleMethodEndOperation() throws IOException{
		//nothing to do for splimethod
	}

	/**
	 * Method for extract from the zip file, the raw byte in a byte array
	 * @param stream of the file
	 * @return byte array of the zip file
	 * @throws IOException
	 */
	private byte[] extractInBufferInput(InputStream stream) throws IOException{
		ByteArrayOutputStream fileExtractedStream = new ByteArrayOutputStream();
		BufferedInputStream bufferInStream = new BufferedInputStream(stream); //input buffer of the actual part
		ZipInputStream zipStream = new ZipInputStream(bufferInStream);//zipStream of the part
		ZipEntry zipEntry = zipStream.getNextEntry();

		while (zipEntry != null) {
			byte[] buffer = new byte[writeBytesCostant];
			int len;

			while ((len = zipStream.read(buffer)) > 0) {
            	System.out.println("len value " + len);
            	fileExtractedStream.write(buffer, 0, len); //write in the support bytearray
           }


            zipStream.closeEntry(); //close the zipstream
            zipEntry = zipStream.getNextEntry(); //close the zip entry
		}
		zipStream.closeEntry();//close zipstream entry
		zipStream.close();//close the zipstream

		System.out.println("size  fileExtractedStream " + fileExtractedStream.size());

		return fileExtractedStream.toByteArray();
	}

	/**
	 * Method for set the new inputStream where to read the file
	 * @param stream
	 * @return new stream
	 * @throws IOException
	 */
	private ByteArrayInputStream setNewStream(InputStream stream) throws IOException{
		byte[] byteArray = extractInBufferInput(stream); //support byteArray, extract the array from the ByteArrayOutputStream

		ByteArrayInputStream fileExtractedInputStream = new ByteArrayInputStream(byteArray);

		return fileExtractedInputStream;
	}



}
