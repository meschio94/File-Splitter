package jobStarter.input;

import job.input.JobInDecompress;
import job.input.JobInDecrypt;
import job.input.JobInInterface;
import job.input.JobInSplit;
import jobStarter.JobStarterInterface;
import logic.FileElement;

/**
 * Class that implements {@link @JobStarterInterface} for input elements to the right type of job
 * @author Meschio
 *
 */
public class JobInStarter implements JobStarterInterface{

	/**
	 * The output folder path
	 */
	private String srcPath;

	/**
	 * The generic job to process
	 */
	private JobInInterface job;

	/**
	 * Constructor of the jobStarter Class
	 * @param inputSrcPath String of the output directory
	 * @param inputData the FileElement to process
	 */
	public JobInStarter (String inputSrcPath){

		this.srcPath = inputSrcPath;
	}

	/**
	 * Start a new job from the different type selected
	 * @return true if the operation was successful, false otherwise
	 * @throws Exception
	 */
	public boolean selectJob(FileElement data) throws Exception{
		switch(data.getType()){
			case "Dimension":
				job = new JobInSplit(srcPath, data);

				break;
			case "Dimension and Encrypt" :
				job = new JobInDecrypt(srcPath, data);

				break;
			case "Dimension and Compress" :
				job = new JobInDecompress(srcPath, data);

				break;
			case "Nr of Parts" :
				job = new JobInSplit(srcPath, data);

				break;
		}


		boolean result;
		if(job.isSuccessful() == true){
			result =  job.startJob();
		} else {
			result = false;
			data.setStatus("Error");
		}

		return result;
	}



}
