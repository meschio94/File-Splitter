package job.output;



import logic.FileElement;
import stream.output.OutputSplit;

/**
 * Specialized class of {@link @JobOutInterface} for split an element via {@link @OutputSplit}
 * @author Meschio
 *
 */
public class JobOutSplit implements JobOutInterface{

	/**
	 * The Input Job
	 */
	private OutputSplit job;

	/**
	 * Constructor of the class for initialize the {@link @OutputSplit}
	 * @param srcPath
	 * @param data
	 * @throws Exception
	 */
	public JobOutSplit(String srcPath, FileElement data) throws Exception {

		this.job = new OutputSplit(srcPath, data);

	}

	/**
	 * Method for start the split procedure
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
