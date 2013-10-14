package com.socix.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecretUtility {
	
	public static final int BUFFER_SIZE = 1024;
	
	private static SecretUtility instance;
	
	private Key mKey;
	private Cipher mEncryptCipher;  
	private Cipher mDecryptCipher;  
	
	private SecretUtility() {
	}
	
	public static SecretUtility getInstance() {
		if(instance == null)
			instance = new SecretUtility();
		return instance;
	}
	
	public boolean init(String key) {
		if(key == null || key.getBytes().length < 8) 
			return false;
		byte[] keyByte = key.getBytes();
		byte[] tmpByte = new byte[8];
		for(int i = 0; i < keyByte.length && i < tmpByte.length; i++) {
			tmpByte[i] = keyByte[i];
		}
		mKey = new SecretKeySpec(tmpByte, "DES");
		try {
			mEncryptCipher = Cipher.getInstance("DES");
			mEncryptCipher.init(Cipher.ENCRYPT_MODE, mKey);  
			mDecryptCipher = Cipher.getInstance("DES");  
			mDecryptCipher.init(Cipher.DECRYPT_MODE, mKey);  
			return true;
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (NoSuchPaddingException e) {
			return false;
		} catch (InvalidKeyException e) {
			return false;
		}  
	}
	
	public void doEncrypt(InputStream is, OutputStream os) {  
        if(is == null || os == null)
        	return;  
        try {  
            CipherInputStream cis = new CipherInputStream(is, mEncryptCipher);  
            byte[] bytes = new byte[BUFFER_SIZE];  
            int len = -1;  
            while((len=cis.read(bytes))>0) {  
                os.write(bytes, 0, len);  
            }  
            os.flush();  
            os.close();  
            cis.close();  
            is.close();  
        } catch (Exception e) {  
        }  
    }
	
	public void doDecrypt(InputStream is, OutputStream os) {
        if(is == null || os == null)  
        	return;
        try {  
            CipherInputStream cis = new CipherInputStream(is, mDecryptCipher);  
            byte[] bytes = new byte[BUFFER_SIZE];  
            int len = -1;  
            while((len=cis.read(bytes))>0) {  
                os.write(bytes, 0, len);  
            }  
            os.flush();  
            os.close();  
            cis.close();  
            is.close();  
        } catch (Exception e) {  
        }  
    }

}
