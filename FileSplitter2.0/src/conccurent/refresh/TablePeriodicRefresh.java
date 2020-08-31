package conccurent.refresh;

import gui.progress.GlobalProgress;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import logic.FileElement;

/**
 * Task Class for update the tableview in the main controller at a certain ms,
 * useful for update the tablecell progressbar of the elements
 *
 * @author Meschio
 *
 */
public class TablePeriodicRefresh extends Task<Void> {

	/**
	 * The table to refresh
	 */
	private TableView<FileElement> table;

	/**
	 * The ms refresh rate
	 */
	private int refresh = 500;

	/**
	 * The global bar to check for completition
	 */
	private GlobalProgress globalProgress;

	/**
	 * Constructor of the TablePeriodicRefresh
	 *
	 * @param inputTable
	 * @param inputGlobalProgress
	 */
	public TablePeriodicRefresh(TableView<FileElement> inputTable, GlobalProgress inputGlobalProgress) {
		this.table = inputTable;
		this.globalProgress = inputGlobalProgress;
	}

	/**
	 * Start the task for refreshing the table at the {@link #refresh} value.
	 * Will automatic stop when the globabar is at 100%
	 */
	@Override
	protected Void call() throws Exception {

		while (globalProgress.isDone() == false) {
			try {
				Thread.sleep(refresh);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			Platform.runLater(() -> table.refresh()

			);
		}

		return null;
	}
}
