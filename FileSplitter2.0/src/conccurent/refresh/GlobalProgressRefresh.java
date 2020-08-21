package conccurent.refresh;

import gui.logic.FSNodesInteration;
import gui.progress.GlobalProgress;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import logic.FileElement;

/**
 * This class handle the refresh of all {@link logic.FileElement#progress} in the list during the processing.
 * update {@link gui.progress.GlobalProgress} class and refresh the globalbar in the UI
 * @author mesch
 *
 */
public class GlobalProgressRefresh extends Task<Void>{

	/**
	 * list to process
	 */
	private ObservableList<FileElement> list;

	/**
	 * the class to update the globalProgress bar
	 */
	private GlobalProgress globalProgress;

	/**
	 * refresh rate of the thread, ms value
	 */
	private int refresh = 500; //ms

	/**
	 * array of the list to process, the list can host different status elements
	 * the {@link #GlobalProgressRefresh} task need to filter the elements for upgrading the globalProgress
	 */
	private int listProcessing[];

	private FSNodesInteration nodesInterationLogic;


	/**
	 * Constructor of the class, take in input the list to process and the progressBar class attached to it
	 * @param inputList
	 * @param inputGlobalProgress
	 */
	public GlobalProgressRefresh (ObservableList<FileElement> inputList, GlobalProgress inputGlobalProgress,FSNodesInteration nodesInterationLogic){
		this.list = inputList;
		this.globalProgress = inputGlobalProgress;
		this.listProcessing = createProcessingArray(inputList);
		this.nodesInterationLogic = nodesInterationLogic;
	}

	/**
	 * Create the array of elements to parse to the relative list Index
	 * @param inputList
	 * @return
	 */
	private int[] createProcessingArray(ObservableList<FileElement> inputList){
		int listProcessing[] = new int[processingSize(list)];

		int listIndex = 0;
		int arrayIndex = 0;
		for (FileElement each: list) {
			if((each.getStatus() != "Done") && (each.getStatus() != "Error")){
				listProcessing[arrayIndex] = listIndex;
				arrayIndex++;
			}
			listIndex++;
		}
		return listProcessing;
	}


	/**
	 * get the numbers of elements to parse
	 * @param inputList
	 * @return the numbers of elements to parse
	 */
	private int processingSize(ObservableList<FileElement> inputList){
		int index = 0;
		for (FileElement each: list) {
			if((each.getStatus() != "Done") && (each.getStatus() != "Error")){
				index++;
			}
		}

		return index;
	}


	/**
	 * start the task, loop the list of elements to parse, untile they have all done, updating it every {@link #refresh}
	 * milliseconds
	 */
	@Override
	protected Void call() throws Exception {
		int arrayIndex = 0;
		double totProgress = 0;

		int size = processingSize(list);

		Platform.runLater(() -> nodesInterationLogic.setWorkingMode());//block the GUI

		while (globalProgress.isDone() == false) {

			if(arrayIndex >= size){
				globalProgress.updateGlobalBar(totProgress);
				arrayIndex = 0; //reset index
				totProgress = 0; //reset progress
				Thread.sleep(refresh); //pause the thread for not overload the cpu
			}


			totProgress += list.get(listProcessing[arrayIndex]).getProgress();


			arrayIndex++;
		}

		Platform.runLater(() -> nodesInterationLogic.setIdleMode());//re-enable the GUI

		return null;
	}
}
