
package job.output;

import logic.FileElement;
import stream.output.OutputCompress;

/**
 * Specialized class of {@link job.output.JobOutInterface} for compress a file via {@link stream.output.OutputCompress}
 * @author Meschio
 *
 */
public class JobOutCompress implements JobOutInterface{

	/**
	 * The Output Job
	 */
	private OutputCompress job;

	/**
	 * Constructor of the class for initialize the {@link stream.output.OutputCompress}
	 * @param srcPath output path
	 * @param data element
	 * @throws Exception Exception
	 */
	public JobOutCompress(String srcPath, FileElement data) throws Exception {
		this.job = new OutputCompress(srcPath, data);

	}

	/**
	 * Method for start to compress the file
	 */
	public boolean startJob() throws Exception {
		if(job.isSuccessful() == true){
			job.compressFile();
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
