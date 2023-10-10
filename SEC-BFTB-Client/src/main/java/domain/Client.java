package domain;

import cipher.CipherHandler;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * Handles Client-Server connectivity as well as transmitting and receiving of information
 */
public final class Client {

	private KeyStore keyStore;
	private static Client INSTANCE;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Map<String,Key> serverPubKeys;

	/**
	 * Returns the Client singleton instance
	 * @return Client singleton
	 */
	public static Client getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Client constructor
	 * @param address-server address
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @since 1.0
	 */
	private Client(String path, String keystore, String keystorePass, String alias) throws NumberFormatException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		Path cp = Paths.get("");
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream fis = new FileInputStream(keystore);
		try {
			keyStore.load(fis, keystorePass.toCharArray());
		}catch (IOException e){
			System.out.println("Incorrect password for username " + alias);
		}
		privateKey = CipherHandler.getPrivateKeyFromKeyStorePath(keystore,keystorePass, alias, "JKS");
		publicKey =	 CipherHandler.getPublicKeyFromPathToKeyStore(keystore, keystorePass, alias, "JKS");

		serverPubKeys= new HashMap<>();
		String regex="server_[0-9][0-9][0-9][0-9].cer";
		Pattern p= Pattern.compile(regex);
		File dir = new File(path);
		if(dir.isDirectory()) {
			File[] ficheiros = dir.listFiles((File file) -> p.matcher(file.getName()).matches());
			for (File file : ficheiros)
				serverPubKeys.put(file.getPath().split("_")[1].split("\\.")[0], CipherHandler.getCertificateFromTrustStore(file.getPath()).getPublicKey());
		}

	}
	
	/**
	 * Returns the Client singleton instance after creating the Client
	 * @return Client singleton
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @since 1.0
	 */
	public static Client loadClient(String truststore, String keystore, String keystorePass, String alias) throws NumberFormatException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		INSTANCE = new Client(truststore,keystore, keystorePass, alias);
		return INSTANCE;
	}
	/**
	 * 
	 * @return keyStore
	 * 
	 */
	public KeyStore getKeyStore() {
		return keyStore;
	}
	/**
	 * 
	 * @return publicKey
	 * 
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}
	/**
	 * 
	 * @return privateKey
	 * 
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	/**
	 * 
	 * @return serverPubKey
	 * 
	 */
	public Map<String,Key> getServerPubKeys() {
		return serverPubKeys;
	}




}
