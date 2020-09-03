package gui.progress;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import logic.FSlistFileElement;
import logic.FileElement;

/**
 * This class pourpose is to update the GUI GlobalProgress Bar with the right amount of progress from {@link conccurent.refresh.GlobalProgressRefresh} in the GUI
 * @author Meschio
 *
 */
public class GlobalProgress {

	/**
	 * progress observable value binded to {@link gui.controller.FSMainController#M_GlobalProgress} progressbar
	 */
	private SimpleDoubleProperty progress;

	/**
	 * this is the size of the processing elements of the list
	 */
	private int size;

	/**
	 * Flag for report all the completed processed elements
	 */
	private boolean isDone;

	/**
	 * Constructor of the GlobalProgress class
	 * @param inputProgress the observable value to update
	 * @param inputList the list to process
	 */
	public GlobalProgress (SimpleDoubleProperty inputProgress, FSlistFileElement inputList){
		this.progress = inputProgress;
		this.size = nrProcessingElements(inputList.getList());
		this.isDone = false;
	}


	/**
	 * class for update the global bar, take in input the sum of all the process and dive it to the {@link #size}
	 * for obtain the global progress value
	 * @param update value
	 */
	public void updateGlobalBar(double update){
		double progressUpdate = update/size;
		progress.setValue(progressUpdate);
		if (progressUpdate >= 1){
			isDone = true;
		}
	}

	/**
	 * get method for the {@link #isDone} flag
	 * @return true if the operation is done
	 */
	public boolean isDone(){
		return isDone;
	}

	/**
	 * Method for calculating the numbers of processing elements
	 * @param list
	 * @return
	 */
	private int nrProcessingElements(ObservableList<FileElement> list){
		int tot = 0;
		for (FileElement each: list) {

			if(each.getStatus() == "Ready to start"){
				tot++;
			}
		}
		return tot;
	}

	/**
	 * Method for reset the {@link #progress} value and the flag {@link #isDone}
	 */
	public void resetGlobalProgress(){
		isDone = false;
		progress.setValue(0);
	}


}
