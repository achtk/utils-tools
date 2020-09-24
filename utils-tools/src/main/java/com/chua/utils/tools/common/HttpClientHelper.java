package com.chua.utils.tools.common;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * http部分工具
 * @author CH
 * @since 1.0
 */
public class HttpClientHelper {

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    /**
     * 创建默认 HostnameVerifier
     * @return
     */
    public static HostnameVerifier createDefaultHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslsession) {
                return true;
            }
        };
    }
    /**
     * 创建 默认SslContext
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createDefaultSslContext() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        return createSslContext("SSL","SunJSSE");
    }
    /**
     * 创建SslContext
     * @param protocol 协议
     * @param provider 生产者
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createSslContext(final String protocol, final String provider) throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException {
        return createSslContext(protocol, provider, new TrustAllCerts());
    }
    /**
     * 创建SslContext
     * @param protocol 协议
     * @param provider 生产者
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createSslContext(final String protocol, final String provider, final X509TrustManager x509TrustManager) throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = null;
        if(null == provider) {
            sslcontext = SSLContext.getInstance(protocol);
        } else {
            sslcontext = SSLContext.getInstance(protocol, provider);
        }
        sslcontext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        return sslcontext;
    }

    /**
     * 创建带参数的url
     * @param url url
     * @param bodyer 消息体
     * @return
     */
    public static String createUrlWithParameters(String url, Map<String, Object> bodyer) {
        if(null == url) {
            return url;
        }
        String urlParameter = createWithParameters(bodyer);
        return url + "?" + urlParameter;
    }
    /**
     * 创建带参数的url
     * @param bodyer 消息体
     * @return
     */
    public static String createWithParameters(Map<String, Object> bodyer) {
        return StringHelper.join(bodyer, "=", "&");
    }

    /**
     * 用于信任所有证书
     */
    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
