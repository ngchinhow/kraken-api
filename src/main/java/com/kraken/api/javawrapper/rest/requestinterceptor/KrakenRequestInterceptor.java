package com.kraken.api.javawrapper.rest.requestinterceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class KrakenRequestInterceptor implements RequestInterceptor {

    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String HMAC_ALGORITHM = "HmacSHA512";
    private final String apiKey;
    private final String privateKey;

    public KrakenRequestInterceptor(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (Objects.isNull(apiKey) || Objects.isNull(privateKey)) return;
        // Step 1. Append nonce to urlencoded form payload
        long nonce = System.currentTimeMillis();
        String nonceAsParam = "nonce=" + nonce;
        byte[] body = requestTemplate.body();
        if (Objects.isNull(body)) return;
        Charset charset = requestTemplate.requestCharset();
        String bodyString = new String(body, charset);
        if (ObjectUtils.isEmpty(bodyString))
            bodyString = nonceAsParam;
        else
            bodyString = nonceAsParam  + "&" + bodyString;
        requestTemplate.body(bodyString);

        MessageDigest digestSHA256;
        String signature;
        try {
            // Step 2. Concatenate nonce and payload and hash, then concatenate with URL path
            digestSHA256 = MessageDigest.getInstance(SHA256_ALGORITHM);
            byte[] toBeEncrypted = ArrayUtils.addAll(
                requestTemplate.path().getBytes(charset),
                digestSHA256.digest((nonce + bodyString).getBytes(charset))
            );

            // Step 3. Encrypt byte array with HMAC-SHA-512 and encode to base 64
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(privateKey), HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            signature = Base64.encodeBase64String(mac.doFinal(toBeEncrypted));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        // Step 4. Append headers to Kraken request
        requestTemplate.header("API-Key", apiKey);
        requestTemplate.header("API-Sign", signature);
    }
}
