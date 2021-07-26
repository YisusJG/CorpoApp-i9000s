package com.corpogas.corpoapp.TanqueLleno;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5 {
    public static byte [] encryptMD5(byte[] data)  throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5.digest();
    }
}