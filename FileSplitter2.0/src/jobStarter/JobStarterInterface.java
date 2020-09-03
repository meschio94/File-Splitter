package jobStarter;

import logic.FileElement;

/**
 * Interface for the input or output job the elements need to be processed
 * @author Meschio
 *
 */
public interface JobStarterInterface {

	/**
	 * Select the right Type of job the elements is setted
	 * @param data, the element to process
	 * @return true if oepration was successful
	 * @throws Exception Exception
	 */
	boolean selectJob(FileElement data) throws Exception;

}
