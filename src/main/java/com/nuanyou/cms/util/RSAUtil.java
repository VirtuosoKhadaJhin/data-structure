package com.nuanyou.cms.util;

import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;

/**
 * 数字签名工具
 */
public class RSAUtil {
    public static String sign(byte[] data, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(spec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encodeToString(signature.sign());
    }

    public static String sign(byte[] data, String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(spec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encodeToString(signature.sign());
    }

    public static boolean verify(byte[] data, String modulus, String publicExponent, String sign) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(spec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decodeFromString(sign));
    }

    public static String toPrivateKeyString(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(spec);
        return Base64Utils.encodeToString(privateK.getEncoded());
    }

    public static String toPublicKeyString(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return Base64Utils.encodeToString(publicKey.getEncoded());
    }

    public static RSAPublicKey toPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buffer = Base64.getDecoder().decode(key);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        X509EncodedKeySpec  keySpec= new X509EncodedKeySpec(buffer);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey toPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buffer = Base64.getDecoder().decode(key);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encryptWithPublicKey(String key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher= Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, toPublicKey(key));
        return cipher.doFinal(data);
    }

    public static byte[] decryptWithPublicKey(String key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher= Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, toPublicKey(key));
        return cipher.doFinal(data);
    }

    public static byte[] encryptWithPrivateKey(String key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher= Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, toPrivateKey(key));
        return cipher.doFinal(data);
    }

    public static byte[] decryptWithPrivateKey(String key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher= Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, toPublicKey(key));
        return cipher.doFinal(data);
    }

    public static String encryptTradeNo(String tradeNo) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIywbisfO1pA09Ww1nqzLhuYlBPJP6F1WlhzulJZdEtRh8umYwHhnb+zUVZ5Yy58JFAabsgAyJZHYrw0LZdKmYeohygm/8BewOUXgWJ0u02BmTBpzFYCKPSaKacY8HOG1gxFFWGmfuvH3mZ2fClmygmlUnkC5KncSep3ITheBIXdAgMBAAECgYAhKZVOZNZAt/c277AezA2aQ6Tj5RQyos7g+qZ4ss1O07EGt/muf9gHT+VkVMikNG760sA6NLUTd1A2vLXia5s0nTMi3+e0J80J7jad6zdP3IqVuO60CoRd8BZx8xAeb/5Zfd8bLzu9NCwE2XrjKoZkN6O+uEcDSDegUK5VjDlrAQJBAMy4Q38JRlLmkNqwRXRY7ACaSICAzPjcBi0MfEwpkfPcbbSxBcm0g8Y0cfjR6SbmoVHRNaRitAwYXbJ2Bb947+kCQQCv7i3rRH7TwRyAr4DuiGDo5J5oOjfhhYhnNUMJMj22Bx4HstM01mWfJCvQyw1iVOlaZj7LhVkfsIv3u2atrQHVAkAaUttC+4NSZ5latW3I3yJBlZUbCPTSTyBwLHnTJUlCLSbiJB0VaaPEGbxJ/VvYh7FYnukh4Rs7ruplZy2oCDn5AkB56qyXpVfelqyR4Q2QP6IowoWj5PRkrIziON7DuLVbN4NwAlSrAQhbSE2024Hi6sTriH9runUQqN/WRV/aa1IhAkBIIPLZ7MujzK5s3AK5NqnvegZq2Kk1m5KhWu9xOQm6GFPFlwNz2jsi+44LY5iEelr5f2cqAFgEvgEc1g3E+AUZ";
        byte[] s = tradeNo.getBytes("UTF-8");
        byte[] s2 = encryptWithPrivateKey(privateKey, s);

        String result = Base64.getEncoder().encodeToString(s2);
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        String tradeno = "2012133";
        byte[] s = tradeno.getBytes("UTF-8");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMsG4rHztaQNPVsNZ6sy4bmJQTyT+hdVpYc7pSWXRLUYfLpmMB4Z2/s1FWeWMufCRQGm7IAMiWR2K8NC2XSpmHqIcoJv/AXsDlF4FidLtNgZkwacxWAij0mimnGPBzhtYMRRVhpn7rx95mdnwpZsoJpVJ5AuSp3EnqdyE4XgSF3QIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIywbisfO1pA09Ww1nqzLhuYlBPJP6F1WlhzulJZdEtRh8umYwHhnb+zUVZ5Yy58JFAabsgAyJZHYrw0LZdKmYeohygm/8BewOUXgWJ0u02BmTBpzFYCKPSaKacY8HOG1gxFFWGmfuvH3mZ2fClmygmlUnkC5KncSep3ITheBIXdAgMBAAECgYAhKZVOZNZAt/c277AezA2aQ6Tj5RQyos7g+qZ4ss1O07EGt/muf9gHT+VkVMikNG760sA6NLUTd1A2vLXia5s0nTMi3+e0J80J7jad6zdP3IqVuO60CoRd8BZx8xAeb/5Zfd8bLzu9NCwE2XrjKoZkN6O+uEcDSDegUK5VjDlrAQJBAMy4Q38JRlLmkNqwRXRY7ACaSICAzPjcBi0MfEwpkfPcbbSxBcm0g8Y0cfjR6SbmoVHRNaRitAwYXbJ2Bb947+kCQQCv7i3rRH7TwRyAr4DuiGDo5J5oOjfhhYhnNUMJMj22Bx4HstM01mWfJCvQyw1iVOlaZj7LhVkfsIv3u2atrQHVAkAaUttC+4NSZ5latW3I3yJBlZUbCPTSTyBwLHnTJUlCLSbiJB0VaaPEGbxJ/VvYh7FYnukh4Rs7ruplZy2oCDn5AkB56qyXpVfelqyR4Q2QP6IowoWj5PRkrIziON7DuLVbN4NwAlSrAQhbSE2024Hi6sTriH9runUQqN/WRV/aa1IhAkBIIPLZ7MujzK5s3AK5NqnvegZq2Kk1m5KhWu9xOQm6GFPFlwNz2jsi+44LY5iEelr5f2cqAFgEvgEc1g3E+AUZ";
        byte[] s2 = encryptWithPrivateKey(privateKey, s);


        String content = Base64.getEncoder().encodeToString(s2);
//        System.out.println(content);
//        byte[] s3 = decryptWithPublicKey(publicKey, s2);
//        System.out.println(new String(s3, "UTF-8"));
//
//        System.out.println(File.separatorChar);
//        System.out.println(File.pathSeparatorChar);
    }
}
