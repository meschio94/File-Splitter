package job.input;

/**
 * Interface for handle the different input type of job : Split, Decompress and Decrypt
 * @author Meschio
 *
 */
public interface JobInInterface {

	/**
	 * Start the FileElement Manipulation
	 * @return true if the job operated without error, false otherwise
	 * @throws Exception
	 */
	boolean startJob() throws Exception;

	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	boolean isSuccessful();


}
