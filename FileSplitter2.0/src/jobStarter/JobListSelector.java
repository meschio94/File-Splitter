package jobStarter;

import conccurent.JobTask;
import conccurent.refresh.GlobalProgressRefresh;
import javafx.collections.ObservableList;
import jobStarter.input.JobInStarter;
import jobStarter.output.JobOutStarter;
import logic.FileElement;
import gui.logic.FSNodesInteration;
import gui.progress.GlobalProgress;

/**
 * Class for sort the type of job via {@link @JobStarterInterface} and create the task {@link @JobTask} for every elements in a thread
 * @author Meschio
 *
 */
public class JobListSelector {

	/**
	 * The list to process
	 */
	private ObservableList<FileElement> list;

	/**
	 * The mode of the queue, used to select the right Job branch
	 */
	private boolean modeSplit;

	/**
	 * Job interface for the type of job to start input or output
	 */
	private JobStarterInterface job;

	/**
	 * THe folder of output path
	 */
	private String srcPath;




	/**
	 * Constructor of the class
	 * @param inputSrcPath the output folder
	 * @param inputList the list to process
	 * @param inputMode the type of queue mode (split/chop)
	 *
	 */
	public JobListSelector(String inputSrcPath, ObservableList<FileElement> inputList , boolean inputMode){
		this.list = inputList;
		this.modeSplit = inputMode;
		this.srcPath = inputSrcPath;
	}

	/**
	 * Function for flow the list, start the right job of every elements in a thread and start the GlobalBar refresh
	 * @param updateProgress
	 * @param nodesInterationLogic
	 * @throws Exception
	 */
	public void startProcessList(GlobalProgress updateProgress, FSNodesInteration nodesInterationLogic) throws Exception{

		if (modeSplit == true){//the queue is in split mode
			job = new JobOutStarter(srcPath);
		} else { //the queue is in chop mode
			job = new JobInStarter(srcPath);

		}

		int threadSize = 0;//nrOfProcessingThreads

		for (FileElement each: list) { //calculate the nr of threads
			if((each.getStatus() != "Done") && (each.getStatus() != "Error")){
				threadSize++;
			}

		}

		Thread taskThread[] = new Thread[threadSize];//array of threads for list elements

		int i = 0;
		for (FileElement each: list) {

			if((each.getStatus() == "Ready to start")){//start the trhead task

				JobTask jobTask = new JobTask(each,job);
				taskThread[i] = new Thread(jobTask);
				taskThread[i].setDaemon(true);
			    taskThread[i].start();

				i++;

			}

		}

		GlobalProgressRefresh globalRefresh = new GlobalProgressRefresh(list,updateProgress,nodesInterationLogic); //initialize the task
		Thread globalRefreshThread = new Thread(globalRefresh);//start the globalBar task thread
		globalRefreshThread.setDaemon(true);
		globalRefreshThread.start();


	}



}
