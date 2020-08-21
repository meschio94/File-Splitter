package stream.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.*;

import logic.FSPasswordHash;
import logic.FileElement;
/**
 * Class specialized for Crypt a file via {@link #cryptFile}, extends the {@link #OutputCore} class.
 * Split a file and crypt all the parts
 * @author Meschio
 *
 */
public class OutputCrypt extends OutputCore{

	/**
	 * The password estrapulated via the {@link #data}
	 *
	 */
	private String password;

	/**
	 * The BufferedOutputStream where to write the file parts
	 */
	private BufferedOutputStream fileOutputStream;

	/**
	 * cipher element used for write via {@link #writeBytes},
	 * and manipulated via {@link #crypt}
	 */
	private Cipher cipher;

	/**
	 * FSPasswordHash element to handle the encrypt stuff
	 */
	private FSPasswordHash crypt;

	/**
	 * Constructor of OutputCrypt, call the super constructor of {@link #OutputCore},
	 * and create the {@link #crypt} from the {@link #password}
	 * @param srcPath
	 * @param data
	 * @throws Exception
	 */
	public OutputCrypt(String srcPath, FileElement data, FSPasswordHash inputCrypt, String inputPassword) throws Exception{
		super(srcPath,data);
		this.password = inputPassword;


		this.crypt = inputCrypt;
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
		fileOutputStream = new BufferedOutputStream(new FileOutputStream(srcPathOut + File.separator + data.getFileNamePure() +"." + index + ".sp"));
	}

	/**
	 * cryptFile Function for write the actual FileElement from the OutSplit Constructor and crypt it
	 * Will perform the action only if {@link #successfulFlag} is set to true,
	 * otherwise will update the {@link #data} status to "Error"
	 * @throws IOException
	 */
	public void cryptFile() throws Exception {

		if(isSuccessful()==true){//check all previous operation in stream core
			writeParts();
		} else {
			data.setStatus("Error");
		}


	}

	/**
	 * @Override method of writeBytes in {@link #StreamCore} to handle the crypt write
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
	 * For OutputCrypt is the creating of the password hash and set the cipher via {@link #FPasswordHash}
	 */
	protected void handleMethodStartOperation(int partIndex){

		System.out.println("method start operation 1");//aka

			try {
				crypt = new FSPasswordHash(this.password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 try {
			this.cipher = crypt.getEncryptChiper();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Specialized end set operation of cycle in {@link #writeParts}
	 * For OutputCrypt close the actual {@link #fileOutputStream}
	 */
	protected void handleMethodEndOperation() throws IOException{
		getOutputStream().close();
	}

}
