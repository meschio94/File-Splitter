package job.output;

import logic.FSPasswordHash;
import logic.FileElement;
import stream.output.OutputCrypt;

public class JobOutCrypt implements JobOutInterface {

	/**
	 * The Output Job
	 */
	private OutputCrypt job;

	/**
	 * FSPasswordHash element to handle the encrypt stuff
	 */
	private FSPasswordHash crypt;

	/**
	 * Constructor of the class for initialize the {@link stream.output.OutputCrypt}
	 * @param srcPath output path
	 * @param data element
	 * @throws Exception Exception
	 */
	public JobOutCrypt(String srcPath, FileElement data) throws Exception {
		String password; //password to use for encrypt

		if ((data.getPassword() == "") || (data.getPassword() == null)){//if the apssword is empty
			data.setPassword(null);
			password = null;
			this.crypt = new FSPasswordHash(null);//generate the FSPasswordHash
		} else { //if the password is custom
			password = data.getPassword();//save the original password for the initialization of OutputCrypt
			this.crypt = new FSPasswordHash(data.getPassword());//generate the FSPasswordHash
			int hashCode = crypt.getHashCode(); //get the hash code
			data.setPassword(String.valueOf(hashCode));//set the hash code for the Header password row
		}

		this.job = new OutputCrypt(srcPath, data, crypt, password);

	}

	/**
	 * Method for start the crypt procedure
	 */
	public boolean startJob() throws Exception {
		if(job.isSuccessful() == true){
			job.cryptFile();
		}
		return job.isSuccessful();
	}


	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	public boolean isSuccessful() {
        return job.isSuccessful();
    }
}
