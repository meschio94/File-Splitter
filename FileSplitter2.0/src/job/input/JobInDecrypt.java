package job.input;


import java.util.concurrent.CountDownLatch;

import error.AlertMaker;
import gui.controller.FSPasswordInputController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.FSPasswordHash;
import logic.FileElement;
import stream.input.InputDecrypt;

/**
 * Specialized class of {@link @JobInInterface} for process a compressed elements and and extract it via {@link @InputDecrypt} and check the relative password
 * @author Meschio
 *
 */
public class JobInDecrypt implements JobInInterface{

	/**
	 * The Input Job
	 */
	private InputDecrypt job;

	/**
	 * The element to process
	 */
	private FileElement data;

	/**
	 * Password used for start the decrypJob via {@link @stream.input.InputDecrypt.decryptFile}
	 */
	private String password;

	/**
	 * Value for check if the user aborted the custom password input via {@link #callPasswordWindow}
	 */
	private boolean aborted;

	/**
	 * Value for check if the user input a password via {@link #callPasswordWindow}
	 */
	private boolean passwordInput;

	/**
	 * Set the hash Code from the header if the crypted file have a custom password
	 */
	private String hashCode;






	public JobInDecrypt(String srcPath, FileElement inputData) throws Exception {

		this.job = new InputDecrypt(srcPath, inputData);
		this.data = inputData;
		this.password = inputData.getPassword();


	}

	public boolean startJob() throws Exception {

		if (password.equals("default") == true){//branch password custom

			password = "x*RCe3iWt!doW8"; //set the dafault password

		} else {//branch custom password

			hashCode = password;//set the filehashcode

			CountDownLatch lock = new CountDownLatch(1);//set the lock value of the thread
			callPasswordWindow(lock);//prompt the user with the custom password input
			lock.await();//the thread stop of lock is >0


		}

		if((job.isSuccessful() == true) || (aborted == false)){
			job.decryptFile(password); //start decryption method
		}
			return job.isSuccessful();

	}




	/**
	 * call the PasswordInput window popup for input the custom password the user set during the crypt fase
	 * @param lock CountDownLatch to unlock for continue the thread job
	 */
	public void callPasswordWindow(CountDownLatch lock){


		Platform.runLater(new Runnable() {//can't load a UI in a non JAVAFX thread
		    @Override
		    public void run() {
		    	Parent root;
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/FSPasswordInput.fxml"));
					root = (Parent)loader.load();

					FSPasswordInputController mpc = (FSPasswordInputController)loader.getController();

					mpc.setData(data);//set the controller

			        Stage stage = new Stage();
			        stage.setResizable(false);//disable resizible window and fullscreen
			        stage.setTitle("Password Input");
			        stage.setScene(new Scene(root));

			        stage.showAndWait(); //block the main window if the file edit window is open

			        aborted = mpc.isAborted();
			        passwordInput = mpc.isPasswordInserted();
			        password = mpc.getPassword();

			        if (aborted == true) {//if the user abort the operation
			        	data.setStatus("Aborted by user");
			        	job.setFlagFalse();
			        	lock.countDown();//reset the lock value for restart the main computational thread
			        } else if (passwordInput == true) {//if the user press ok

			        	if(checkPassword(password) == true){//check password
			        		data.setStatus("Working...");
			        		lock.countDown();//reset the lock value for restart the main computational thread
			        	} else {
			        		AlertMaker.showErrorMessage("Password Error", "Password file of : " + data.getFileName() + " is wrong, please input the correct password ");
			        		mpc.setPasswordInserted(false);
			        		callPasswordWindow(lock);//recursive recall if the user close the window
			        	}

			        } else {
			        	callPasswordWindow(lock);//recursive recall if the user close the window
			        }


				 	} catch (Exception e) {
				 		e.printStackTrace();
				 	}
		    }

		});

	}

	/**
	 * Method for check the user inputPassword from {@link #callPasswordWindow} using the generated hash with the header hash
	 * @param inputPassword
	 * @return true if the password matches the header hash
	 * @throws Exception
	 */
	private boolean checkPassword(String inputPassword) throws Exception{
		FSPasswordHash crypt = new FSPasswordHash(inputPassword);
		int hashInt = crypt.getHashCode();
		String hashTempCode = String.valueOf(hashInt);

		if(hashCode.equals(hashTempCode) == true){
			return true;
		} else {
			return false;
		}



	}

	/**
	 * Method for check the operation status
	 * @return the successful flag
	 */
	public boolean isSuccessful() {
        return job.isSuccessful();
    }



}

