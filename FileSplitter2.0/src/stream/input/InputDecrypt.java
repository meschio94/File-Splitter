package stream.input;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;

import logic.FSPasswordHash;
import logic.FileElement;

/**
 * Class specialized for Decrypt and reconstruct a file via {@link #deryptFile}, extends the {@link #InputCore} class.
 *	Reconstruct a file with all the encrypted parts
 * @author Meschio
 *
 */
public class InputDecrypt extends InputCore{

	/**
	 * The BufferedOutputStream where to write the reconstructed file
	 */
	private BufferedOutputStream fileOutputStream;

	/**
	 * cipher element used for write via {@link #writeBytes},
	 * and manipulated via {@link #crypt}
	 */
	private Cipher cipher;

	/**
	 * FSPasswordHash element to handle the decrypt stuff
	 */
	private FSPasswordHash crypt;

	/**
	 * Constructor of InputCrypt, call the super constructor of {@link #InputCore},
	 * @param srcPath
	 * @param data
	 * @throws IOException
	 */
	public InputDecrypt(String srcPath, FileElement data) throws IOException{
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
	protected void setOutputStream(String srcPathOut) throws FileNotFoundException{
		fileOutputStream = new BufferedOutputStream(new FileOutputStream(srcPathOut + File.separator + headerInfo.getFileNamePure() + headerInfo.getFileExtension()));
	}

	/**
	 * decryptFile Function for reconstruct the file from its crypted parts
	 * Will perform the action only if {@link #successfulFlag} is set to true,
	 * otherwise will update the {@link #data} status to "Error"
	 * @throws IOException
	 */
	public void decryptFile(String password) throws Exception {
		if(isSuccessful()==true){//check all previous operation in stream core

			crypt = new FSPasswordHash(password);
			this.cipher = crypt.getDecryptChiper();
			writeParts();
		} else {
			data.setStatus("Error");
		}

	}

	/**
	 * @Override method of writeBytes in {@link #StreamCore} to handle the decrypt write
	 */
	@Override
    protected void writeBytes(OutputStream outputStream, long part) throws Exception {
		if(isSuccessful() == true){

			fileProgress.incColumnBar();//update the progress of the element

			byte[] buffer = new byte[(int) part];
			if(stream.read(buffer) >=0){
				byte[] crypt = cipher.update(buffer);

				outputStream.write(crypt);
			}
		}
    }

	/**
	 * Specialized start set operation of cycle in {@link #writeParts}
	 *
	 */
	protected void handleMethodStartOperation(int partIndex){
		//nothing to do for crypt
	}

	/**
	 * Specialized end set operation of cycle in {@link #writeParts}
	 *
	 */
	protected void handleMethodEndOperation() throws IOException{
		//nothing to do for crypt
	}



}
