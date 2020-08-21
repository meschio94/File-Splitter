package conccurent;

import javafx.concurrent.Task;
import jobStarter.JobStarterInterface;
import logic.FileElement;

/**
 * Class for set the Task to input in a thread, for processing a single element from the list
 * @author Meschio
 *
 */
public class JobTask extends Task<Void>{

	/**
	 * The element to process
	 */
	private FileElement data;

	/**
	 * The job interface of the queue
	 */
	private JobStarterInterface job;

	/**
	 * Constructor of the class, take in input the FileElement to process and the type of job the queue is in
	 * @param inputData
	 * @param inputJob
	 */
	public JobTask(FileElement inputData, JobStarterInterface inputJob){
		this.data = inputData;
		this.job = inputJob;

	}

	/**
	 * Start to process the task, updating the relative status
	 */
	 @Override
	    protected Void call() throws Exception{

		 data.setStatus("Working...");

		 try {
			if(job.selectJob(data) == true){
				data.setStatus("Done");
			} else {

				data.setStatus("Error");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		 	data.setProgress(1);

			return null;
		}



}
