package org.indexlm.auth.config.rest;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 登录RestTemplate配置
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Configuration
public class LoginRestTemplateConfig {
    /**
     * 基于OkHttp3配置RestTemplate
     *
     * @return
     */
    @Bean("loginRestTemplate")
    public RestTemplate restTemplate() {
        TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{
                new X509ExtendedTrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                    }
                }
        };
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //设置连接超时
        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS);
        //设置读超时
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);
        //设置写超时
        okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //支持所有类型https请求
                return true;
            }
        });
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("SSL");
//            sc.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        SSLSocketFactory socketFactory = sc.getSocketFactory();
//        okHttpBuilder.sslSocketFactory(socketFactory);
        //允许请求重定向，默认是true
        okHttpBuilder.followRedirects(true);
        OkHttpClient build = okHttpBuilder.build();
        OkHttp3ClientHttpRequestFactory httpRequestFactory = new OkHttp3ClientHttpRequestFactory(build);
      httpRequestFactory.setConnectTimeout(10 * 1000);
        httpRequestFactory.setReadTimeout(10 * 1000);
        httpRequestFactory.setWriteTimeout(10 * 1000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
