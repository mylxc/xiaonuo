package com.jackli.web.core.util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 *  非对称数据加解密
 * @Author:luoweicheng
 **/
public class RSAUtils {


    /**
     * 使用私钥加密，返回Base64编码的密文字符串
     * @param plainText
     * @param base64PrivateKey
     * @return
     * @throws Exception
     */
    public static String encryptWithPrivateKey(String plainText, String base64PrivateKey) throws Exception {
        byte[] decodedPrivateKey = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 使用公钥解密，返回明文字符串
    public static String decryptWithPublicKey(String encryptedText, String base64PublicKey) throws Exception {
        //byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        //String originalText = new String(decodedBytes, "UTF-8");

        byte[] decodedPublicKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

        return new String(decryptedBytes);
    }
}
