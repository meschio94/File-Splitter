package gui.progress;

import javafx.beans.property.DoubleProperty;

/**
 * This class pourpose is to update the progress value binded to the progress column of the {@link logic.FileElement} for the GUI TableView
 * @author Meschio
 *
 */
public class ColumnProgress {

	/**
	 * totalWrites Represent the total {@link stream.StreamCore#writeBytes} cycle the element need to do
	 */
	private double totalWrites;

	/**
	 * The reference to the observable value of {@link logic.FileElement#progress} for update purpose
	 */
	private DoubleProperty progress;

	/**
	 * The write byte costant used in {@link @StreamCore} to write bytes
	 */
	private int writeByteCostant = 2048;

	/**
	 * Count value used by {@link #incColumnBar} to update progress of 1%
	 */
	private double cycleCount;

	/**
	 * onePerCent represent the nr of cycle {@link stream.StreamCore#writeBytes} need to complete for upgrade {@link #progress} of 0.01
	 */
	private double onePerCent;//1% -> 0.01 progressbar

	/**
	 * Constructor of ColumnProgress, get the observable value binded to the progressbar and the file size
	 * @param inputProgress numeric progress
	 * @param inputSize size of the file
	 */
	public ColumnProgress (DoubleProperty inputProgress, long inputSize){
		this.progress = inputProgress;
		this.totalWrites = inputSize/writeByteCostant;

		this.onePerCent = totalWrites/100;
		this.cycleCount = 0;

	}

	/**
	 * Method to increase the {@link #progress} value of 0.01 when the cycleCount complete the value of onePerCent
	 * @throws Exception Exception
	 */
	public synchronized void incColumnBar() throws Exception{
		cycleCount++;
		if (cycleCount >= onePerCent){
			progress.setValue(progress.getValue() + 0.01);
			cycleCount = 0;//reset cyclecount
		}
	}





}
