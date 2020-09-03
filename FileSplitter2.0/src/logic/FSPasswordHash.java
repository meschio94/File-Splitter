package logic;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class generate the chiper used for write/read the file by {@link #OutputCrypt},  {@link #InputDecrypt}
 * @author Meschio
 *
 */
public class FSPasswordHash {

	/**
	 * The password used for generate the {@link #chiper}
	 */
	private String password;

	/**
	 * The <Chiper> for crypt/decrypt the file
	 */
	private Cipher cipher;

	/**
	 * The default password used if the {@link #password} is null or empty
	 */
	private String defaultPassword = "x*RCe3iWt!doW8";

	/**
	 * The salt useb for the password
	 */
	private byte[] salt;

	/**
	 * IvParameterSpec for the initialization of the {@link #chiper}
	 */
	private IvParameterSpec ivParam;

	/**
	 * Secret key generated with AES
	 */
	private SecretKey aesKey;

	/**
	 * Constructor of the class, take in input a password for generate a {@link #chiper}
	 * @param inputPassword
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 */
	public FSPasswordHash(String inputPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException{
		if ((inputPassword == null) || (inputPassword == "")){
			this.password = defaultPassword;
		} else {
			this.password = inputPassword;
		}
		this.salt = makeSalt(password);
		this.cipher = makeCipher(salt);


	}

	/**
	 * Generate the salt for the password
	 * @param password
	 * @return
	 */
	private byte[] makeSalt(String password){
		byte[] saltBytes = password.getBytes();
		byte[] salt = new byte[16];
		for(int i=0; i< saltBytes.length ;i++){
			salt[i] = saltBytes[i];
		}

		return salt;
	}


	/**
	 * Create a generic Chiper after salted the {@link #password}, generate an aesKey and IvParameterSpec and store it in {@link #aesKey} and in {@link #ivParam}
	 * @param salt to use for the password
	 * @return the Chiper
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 */
	private Cipher makeCipher(byte [] salt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException{
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 1024, 256);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		SecretKey key = keyFactory.generateSecret(keySpec);
		cipher = Cipher.getInstance("AES/CFB8/NoPadding");
		ivParam = new IvParameterSpec(salt);
		aesKey = new SecretKeySpec(key.getEncoded(), "AES");
		return cipher;
	}

	/**
	 * Create and return the Crypt Chiper for Crypt a file
	 * @return Crypt Chiper
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public Cipher getEncryptChiper() throws InvalidKeyException, InvalidAlgorithmParameterException{
		cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParam);
		return cipher;
	}

	/**
	 * Create and return the decrypt Chiper for decrypt a file
	 * @return Decrypt Chiper
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public Cipher getDecryptChiper() throws InvalidKeyException, InvalidAlgorithmParameterException{
		cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParam);
		return cipher;
	}

	/**
	 * Return the #defaultPassword
	 * @return
	 */
	public String getDefaultPassword(){
		return defaultPassword;
	}

	/**
	 * return the hash code of the {@link #aesKey}
	 * @return
	 */
	public int getHashCode(){
		return aesKey.hashCode();
	}

}