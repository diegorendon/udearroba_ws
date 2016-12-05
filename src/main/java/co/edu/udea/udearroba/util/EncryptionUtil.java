package co.edu.udea.udearroba.util;

import co.edu.udea.udearroba.i18n.Texts;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

/**
 * Encrypt or Decrypt the passwords.
 *
 * @author Diego Rendón
 */
public class EncryptionUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

    /**
     * Decrypts a user password.
     *
     * @param password The encrypted user password.
     * @param initializationVectorString The 16 bytes initialization vector string used to decrypt the data.
     * @param secretKey The 128-bits secret key used to decrypt the data.
     *
     * @return String The unencrypted password.
     */
    public static String decrypt(String password, String initializationVectorString, String secretKey) {
        String decryptedPassword = null;
        try {
            SecretKeySpec secretkeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec initialVector = new IvParameterSpec(initializationVectorString.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretkeySpec, initialVector);
            byte[] encryptedByteArray = (new Base64()).decode(password.getBytes());
            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
            decryptedPassword = new String(decryptedByteArray, "UTF8");
        } catch (Exception ex) {
            logger.error("{} - {}", Texts.getText("decryptionLogMessage"), ex.getMessage());
        }
        return decryptedPassword;
    }
}
