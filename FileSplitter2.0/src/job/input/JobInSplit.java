package job.input;

import logic.FileElement;
import stream.input.InputSplit;

/**
 * Specialized class of {@link job.input.JobInInterface} for process a splitted element and reconstruct it {@link stream.input.InputSplit}
 * @author Meschio
 *
 */
public class JobInSplit implements JobInInterface{
	/**
	 * The Input Job
	 */
	private InputSplit job;

	/**
	 * Constructor of the class for initialize the {@link stream.input.InputSplit}
	 * @param srcPath source path
	 * @param data element
	 * @throws Exception Exception
	 */
	public JobInSplit(String srcPath, FileElement data) throws Exception {

		this.job = new InputSplit(srcPath, data);

	}


	/**
	 * Method for start the reconstruction procedure
	 */
	public boolean startJob() throws Exception {
		if(job.isSuccessful() == true){
			job.splitFile();
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
