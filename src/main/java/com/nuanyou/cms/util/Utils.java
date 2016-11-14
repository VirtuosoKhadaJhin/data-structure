package com.nuanyou.cms.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;


/**
 * Created by xiejing on 2016/1/27.
 */
public class Utils {

    public static String genNonceStr() {
        Random random = ThreadLocalRandom.current();
        return MD5Utils.encrypt(String.valueOf(random.nextInt(10000)));
    }

    public static String sign(byte[] data, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(spec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    public static void sign(List<NameValuePair> pairs, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        pairs.add(new BasicNameValuePair("timestamp", String.valueOf(System.currentTimeMillis())));
        pairs.add(new BasicNameValuePair("noncestr", String.valueOf(genNonceStr())));
        Collections.sort(pairs, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair o1, NameValuePair o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        String toSign = URLEncodedUtils.format(pairs, StandardCharsets.UTF_8);
        String sign = sign(toSign.getBytes(StandardCharsets.UTF_8), privateKey);
        pairs.add(new BasicNameValuePair("sign", sign));
    }

}