/**
 * This package manage the Header Logic of the program, when a file is processed by the program is created an header File with all the info of the operation
 * This header have the extension ".0.sp" and is a generic txt with 6 rows (starting from 0) with this fields: <p>
 *
 * @author Meschio
 *
 */


package logic.header;


/**
 * <p>
 * 0 --> File Name : The FileName with the extension<p>
 * 1 --> Operation type : The type of operation of the file<p>
 * 2 --> Nr of parts : The nr of parts the file was splitted<p>
 * 3 --> Bytes per part : Bytes per Part<p>
 * 4 --> Bytes last part : Bytes last part<p>
 * 5 --> Passwrod Info : "default" if it was used the default password, if it was a custom password the is stored the hash of the password<p>
 */