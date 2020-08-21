package job.output;

/**
 * Interface for handle the different output type of job : Split, Compress and Crypt
 * @author Meschio
 *
 */
public interface JobOutInterface{

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
