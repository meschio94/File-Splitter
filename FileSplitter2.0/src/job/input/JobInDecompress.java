package job.input;

import logic.FileElement;
import stream.input.InputDecompress;

/**
 * Specialized class of {@link job.input.JobInInterface} for process a compressed elements and and extract it via {@link stream.input.InputDecompress}
 * @author Meschio
 *
 */
public class JobInDecompress implements JobInInterface{

	/**
	 * The Input Job
	 */
	private InputDecompress job;

	/**
	 * Constructor of the class for initialize the {@link stream.input.InputDecompress}
	 * @param srcPath source path
	 * @param data Element
	 * @throws Exception Exception
	 */
	public JobInDecompress(String srcPath, FileElement data) throws Exception {

		this.job = new InputDecompress(srcPath, data);

	}

	/**
	 * Method for start the decompress procedure
	 */
	public boolean startJob() throws Exception {


		if(job.isSuccessful() == true){
			job.decompressFile();
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
