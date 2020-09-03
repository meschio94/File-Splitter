package jobStarter.output;


import job.output.JobOutInterface;
import job.output.JobOutSplit;
import job.output.JobOutCompress;
import job.output.JobOutCrypt;
import jobStarter.JobStarterInterface;
import logic.FileElement;

/**
 * Class that implements {@link jobStarter.JobStarterInterface} for output element to the right type of job
 * @author Meschio
 *
 */
public class JobOutStarter implements JobStarterInterface{

	/**
	 * The output folder path
	 */
	private String srcPath;

	/**
	 * The generic job to process
	 */
	private JobOutInterface job;

	/**
	 * Constructor of the jobStarter Class
	 * @param inputSrcPath String of the output directory
	 */
	public JobOutStarter (String inputSrcPath){

		this.srcPath = inputSrcPath;
	}

	/**
	 * Start a new job from the different type selected
	 * @return true if the operation was successful, false otherwise
	 * @throws Exception Exception
	 */
	public boolean selectJob(FileElement data) throws Exception{
		switch(data.getType()){
			case "Dimension":
				job = new JobOutSplit(srcPath, data);

				break;
			case "Dimension and Encrypt" :
				job = new JobOutCrypt(srcPath, data);

				break;
			case "Dimension and Compress" :
				job = new JobOutCompress(srcPath, data);

				break;
			case "Nr of Parts" :
				job = new JobOutSplit(srcPath, data);

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
