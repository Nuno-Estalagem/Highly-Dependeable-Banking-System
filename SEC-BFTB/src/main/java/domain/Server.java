package domain;

import cipher.CipherHandler;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

/**
 *
 */
public class Server {

    private static Server INSTANCE = null;
    public static final String KEY_STORE_ALIAS = "server";
    public static final String KEY_STORE_TYPE = "JKS";
    private static final String KEYSTORE_PATH = "/src/main/resources/serverkeystore";
    private HashMap<String, SecretKey> sessionKeys = new HashMap<>();
    private final String keyStorePassword;
    private final KeyStore keyStore;


    public static Server getInstance() throws Exception {
        if (INSTANCE == null) {
            Server.create(new File("").getAbsolutePath().concat(KEYSTORE_PATH), "password");
        }
        return INSTANCE;
    }

    /**
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @since 2.0
     */
    public static Server create(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        INSTANCE = new Server(keyStorePath, keyStorePassword);
        return INSTANCE;

    }

    /**
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @since 1.0
     */
    private Server(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        this.keyStorePassword = keyStorePassword;
        keyStore = CipherHandler.getKeyStore(keyStorePath, keyStorePassword, KEY_STORE_TYPE);
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }


}
