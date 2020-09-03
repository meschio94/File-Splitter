package job.output;



import logic.FileElement;
import stream.output.OutputSplit;

/**
 * Specialized class of {@link job.output.JobOutInterface} for split an element via {@link stream.output.OutputSplit}
 * @author Meschio
 *
 */
public class JobOutSplit implements JobOutInterface{

	/**
	 * The Input Job
	 */
	private OutputSplit job;

	/**
	 * Constructor of the class for initialize the {@link stream.output.OutputSplit}
	 * @param srcPath output path
	 * @param data element
	 * @throws Exception Exception
	 */
	public JobOutSplit(String srcPath, FileElement data) throws Exception {

		this.job = new OutputSplit(srcPath, data);

	}

	/**
	 * Method for start the split procedure
	 * @return true if operation was successful
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
