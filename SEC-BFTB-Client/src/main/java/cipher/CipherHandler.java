package cipher;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * 
 * Handles operation related to ciphers, keys and signatures
 * @author 
 *   
 */
public class CipherHandler {
	/**
	 * Returns keystore given a path a password and a keystore type
	 * @param path path where the keyStore is
	 * @param pass password to the keystore
	 * @param type type of keystore
	 * @return keyStore
	 * @throws NoSuchAlgorithmException 
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws KeyStoreException
	 *    
	 */
	public static KeyStore getKeyStore(String path, String pass, String type) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException {
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(new FileInputStream(path), pass.toCharArray());
		return ks;
	}
	/**
	 * Encrypt an array of bytes
	 * @param arr data to encrypt
	 * @param key encryption key
	 * @return Encrypt data
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 *    
	 */
	public static byte[] encrypt(byte[] arr, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher c = Cipher.getInstance(key.getAlgorithm());
		c.init(Cipher.ENCRYPT_MODE, key);
		return c.doFinal(arr);
	}
	/**
	 * Decrypt an array of bytes
	 * @param arr data to decrypt
	 * @param key Decryption key
	 * @return Decrypted data
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 *    
	 */
	public static byte[] decrypt(byte[] arr, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher c = Cipher.getInstance(key.getAlgorithm());
		c.init(Cipher.DECRYPT_MODE, key);
		return c.doFinal(arr);
	}
	/**
	 * Generates a key given an algorithm
	 * @param algortihm to generate key
	 * @return new key
	 * @throws NoSuchAlgorithmException
	 *    
	 */
	public static SecretKey generateKey(String algortihm) throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(algortihm);
		kg.init(128);
		return kg.generateKey();
	}
	/**
	 * Constructs a secret key from the given byte array
	 * @param key data array
	 * @param algortihm associated with the key
	 * @return secretKey
	 *    
	 */
	public static SecretKey getKeyFromArrayByte(byte[] key, String algortihm) {
		 return new SecretKeySpec(key, algortihm);
	}
	/**
	 * Gets a certificate from a keystore
	 * @param pathToKeyStore path to key store
	 * @param pass password to keystore
	 * @param alias certificate alias
	 * @param typeKeyStore keyStore type
	 * @return Certificate
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 *    
	 */
	public static Certificate getCertificate(String pathToKeyStore, String pass, String alias, String typeKeyStore) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		FileInputStream kfile = new FileInputStream(pathToKeyStore);
		KeyStore kstore = KeyStore.getInstance(typeKeyStore);
		kstore.load(kfile,pass.toCharArray());
		return kstore.getCertificate(alias); 
	}
	/**
	 * Gets a public key from a given keystore
	 * @param pathToKeyStore path to key store
	 * @param pass password to keystore
	 * @param alias certificate alias
	 * @param typeKeyStore keyStore type
	 * @return publicKey 
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 *    
	 */
	public static PublicKey getPublicKeyFromPathToKeyStore(String pathToKeyStore, String pass, String alias, String typeKeyStore) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		Certificate cert = getCertificate(pathToKeyStore,pass, alias, typeKeyStore);
		return cert.getPublicKey();
	}
	/**
	 * Get certificate from file
	 * @param path path to file
	 * @return Certificate
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 *    
	 */
	public static Certificate getCertificateFromTrustStore(String path) throws FileNotFoundException, CertificateException {
		FileInputStream fis = new FileInputStream(path);         
		CertificateFactory fac = CertificateFactory.getInstance("X509");
		return fac.generateCertificate(fis);
	}
	

	/**
	 * Gets a private from a keyStore
	 * @param pathToKeyStore path to keyStore
	 * @param pass password to the keyStore
	 * @param alias alias of the key
	 * @param typeKeyStore type of keyStore
	 * @return privateKey
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 *    
	 */
	public static PrivateKey getPrivateKeyFromKeyStorePath(String pathToKeyStore, String pass,  String alias,
			String typeKeyStore) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
		FileInputStream kfile = new FileInputStream(pathToKeyStore);
		KeyStore kstore = KeyStore.getInstance(typeKeyStore);
		kstore.load(kfile,pass.toCharArray());
		return  (PrivateKey) kstore.getKey(alias, pass.toCharArray( ));
	}
	/**
	 * return private key from store
	 * @param store keyStore
	 * @param pass password for the keystore
	 * @param alias privateKey alias
	 * @return PrivateKey
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 *    
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(KeyStore store, String pass,  String alias) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
		return (PrivateKey) store.getKey(alias, pass.toCharArray( ));
	}
	/**
	 * wraps a key with another key
	 * @param key key which will be wrapped
	 * @param pk public key 
	 * @return array of bytes representing a wrapped key
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 *    
	 */
	public static byte[] encryptKey(Key key, Key pk) throws InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher c = Cipher.getInstance(pk.getAlgorithm());
		//System.out.println("Cipher algorithm: " + c.getAlgorithm());
		c.init(Cipher.WRAP_MODE, pk);
		return c.wrap(key);
		
	}
	/**
	 * Unwraps a key
	 * @param key to unwrap
	 * @param type type of wrapped key
	 * @param decryptKey key used to unwrap the other key
	 * @param algortihm unwrapping algortithm
	 * @return key
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 *    
	 */
	public static Key decryptKey(byte[] key, int type, Key decryptKey,String algortihm) throws InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher c = Cipher.getInstance(decryptKey.getAlgorithm());
		c.init(Cipher.UNWRAP_MODE, decryptKey);
		return c.unwrap(key,algortihm,type);
	}
	public static byte[] hashMessage(byte[] msg) throws NoSuchAlgorithmException {
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	md.update(msg);
		return md.digest();
    }

	public static boolean verifyHash(byte[] msg, byte[] hashedMsg) throws NoSuchAlgorithmException {
		byte[] md = CipherHandler.hashMessage(msg);


		String md64= Base64.getEncoder().encodeToString(md);
		String hashedm64=Base64.getEncoder().encodeToString(hashedMsg);

		return md64.contentEquals(hashedm64);
	}
	
}
