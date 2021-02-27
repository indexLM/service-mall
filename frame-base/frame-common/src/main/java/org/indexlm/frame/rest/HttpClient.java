package org.indexlm.frame.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * http客户端
 *
 * @author LiuMing
 * @since 2021/2/27
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    public static final RestTemplate REST_TEMPLATE;

    /**
     * 发送get请求
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String getRequest(String url, Object params, Object... uriVariables) {
        HttpHeaders headersMap = new HttpHeaders();
        //封装请求参数
        url = this.formatUrlParams(url, params);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.GET, uriVariables);
    }

    /**
     * 发送get请求 携带请求头
     *
     * @param url          请求url
     * @param headers      请求头
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String getRequest(String url, Map<String, String> headers, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        //封装请求头
        this.formatHeader(headers, headersMap);
        //封装请求参数
        url = this.formatUrlParams(url, params);
        HttpEntity<String> request = new HttpEntity<>(null, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.GET, uriVariables);
    }

    /**
     * 发送postJson请求 携带请求头
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String   postJsonRequest(String url, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.setContentType(MediaType.APPLICATION_JSON);
        //请求参数
        String paramJsonStr = JSONObject.toJSONString(params);
        HttpEntity<String> request = new HttpEntity<>(paramJsonStr, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postJson请求 携带请求头
     *
     * @param url          请求url
     * @param headers      请求头
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postJsonRequest(String url, Map<String, String> headers, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.setContentType(MediaType.APPLICATION_JSON);
        this.formatHeader(headers, headersMap);
        //请求参数
        String paramJsonStr = JSONObject.toJSONString(params);
        HttpEntity<String> request = new HttpEntity<>(paramJsonStr, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postUrlData请求
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postUrlDataRequest(String url, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.setContentType(MediaType.MULTIPART_FORM_DATA);
        //封装请求参数
        url = this.formatUrlParams(url, params);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postFormData请求
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postFormDataRequest(String url, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.setContentType(MediaType.MULTIPART_FORM_DATA);
        //请求参数
        LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        //构建请求体参数
        this.formatBodyParams(params, paramMap);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postFormData请求 携带请求头
     *
     * @param url          请求url
     * @param headers      请求头
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postFormDataRequest(String url, Map<String, String> headers, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        //请求格式
        headersMap.setContentType(MediaType.MULTIPART_FORM_DATA);
        //格式化请求头信息
        this.formatHeader(headers, headersMap);
        //请求参数
        LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        //构建请求体参数
        this.formatBodyParams(params, paramMap);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postUrlXW请求  x-www-urlencode
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postUrlXWRequest(String url, Object params, Object... uriVariables) {

        //请求头
        HttpHeaders headersMap = new HttpHeaders();
       // 设置参数
        headersMap.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //封装请求参数
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }


    /**
     * 发送postUrlXW请求  x-www-urlencode
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postSoapRequest(String url, Object params, Object... uriVariables) {


        if(params instanceof  String){
            String paramStr=(String)params;
            //请求头
            HttpHeaders headersMap = new HttpHeaders();
            //请求格式
            headersMap.setContentType(MediaType.TEXT_XML);

            HttpEntity<String> request = new HttpEntity<>(paramStr, headersMap);

            return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
        }else{
            return null;
        }
    }

    /**
     * 发送postUrlXW请求  x-www-urlencode
     *
     * @param url          请求url
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postXWRequest(String url, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //请求参数
        LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        //构建请求体参数
        this.formatBodyParams(params, paramMap);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    /**
     * 发送postUrlXW请求  x-www-urlencode
     *
     * @param url          请求url
     * @param headers      请求头
     * @param params       请求参数 可以map或者vo类
     * @param uriVariables 请求路径中的变量
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public String postXWRequest(String url, Map<String, String> headers, Object params, Object... uriVariables) {
        //请求头
        HttpHeaders headersMap = new HttpHeaders();
        //请求格式
        headersMap.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //格式化请求头信息
        this.formatHeader(headers, headersMap);
        //请求参数
        LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        //构建请求体参数
        this.formatBodyParams(params, paramMap);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headersMap);
        return this.httpRequest(url, params, request, HttpMethod.POST, uriVariables);
    }

    private String httpRequest(String url, Object params, HttpEntity request, HttpMethod httpMethod, Object... uriVariables) {
        log.info("开始发送请求,url为{},参数为:{}",url,params);
        ResponseEntity<String> entity;
        //发送请求
        if (uriVariables == null) {
            entity = REST_TEMPLATE.exchange(url, httpMethod, request, String.class);
        } else {
            entity = REST_TEMPLATE.exchange(url, httpMethod, request, String.class, uriVariables);
        }
        int value = entity.getStatusCode().value();
        if (value == HttpProperties.STATUS_CODE_OK) {
            String body = entity.getBody();
            log.info("发起{}请求成功:\n请求地址:{}\n请求参数:{}\n响应信息:{}",
                    httpMethod.name(), url, params.toString(), JSONObject.toJSONString(body));
            return body;
        } else {
            log.error("发起{}请求失败,失败信息:\n请求地址:{}\n请求参数:{}响应状态码:{}",
                    httpMethod.name(), url, params.toString(), entity.getStatusCodeValue());
            return null;
        }
    }

    /**
     * 格式化请求路径中拼接的参数
     *
     * @param url    url
     * @param params 请求的参数
     * @return {@link String} 返回的拼接后的带参数的路径
     * @author LiuMing
     * @since 2021/2/27
     */
    private String formatUrlParams(String url, Object params) {
        if (ObjectUtil.isNotEmpty(params)) {
            Map<String, Object> paramsMap;
            if (params instanceof Map) {
                paramsMap = (Map<String, Object>) params;
            } else {
                paramsMap = BeanUtil.beanToMap(params, false, true);
            }
            StringBuilder urlSb = new StringBuilder(url);
            urlSb.append("?");
            Set<Map.Entry<String, Object>> entrySet = paramsMap.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                urlSb.append(key).append("=").append(value).append("&");
            }
            return urlSb.substring(0, urlSb.length() - 1);
        } else {
            return url;
        }
    }

    /**
     * 格式化请求体参数
     *
     * @param params   传入参数
     * @param paramMap 发送的参数
     * @author LiuMing
     * @since 2021/2/27
     */
    private void formatBodyParams(Object params, LinkedMultiValueMap<String, Object> paramMap) {
        if (ObjectUtil.isNotEmpty(params)) {
            Map<String, Object> paramsMap;
            if (params instanceof Map) {
                paramsMap = (Map<String, Object>) params;
            } else {
                paramsMap = BeanUtil.beanToMap(params, false, true);
            }
            Set<Map.Entry<String, Object>> entrySet = paramsMap.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                paramMap.add(key, value);
            }
        }
    }

    /**
     * 格式化请求头
     *
     * @param headers    传入请求头
     * @param headersMap 发送的请求头
     * @author LiuMing
     * @since 2021/2/27
     */
    private void formatHeader(Map<String, String> headers, HttpHeaders headersMap) {
        if (ObjectUtil.isNotEmpty(headers)) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> stringObjectEntry : entrySet) {
                String key = stringObjectEntry.getKey();
                String value = stringObjectEntry.getValue();
                headersMap.add(key, value);
            }
        }
    }

    private HttpClient() {
    }


    /**
     * 获得远程调用单例对象
     *
     * @return {@link HttpClient } 远程调用单例对象
     * @author LiuMing
     * @since 2021/2/27
     */
    public static HttpClient getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * 内部类 单例模式
     */
    private static class SingleTonHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    static {
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
        okHttpBuilder.connectTimeout(20, TimeUnit.SECONDS);
        //设置读超时
        okHttpBuilder.readTimeout(20, TimeUnit.SECONDS);
        //设置写超时
        okHttpBuilder.writeTimeout(20, TimeUnit.SECONDS);
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
        httpRequestFactory.setConnectTimeout(20 * 1000);
        httpRequestFactory.setReadTimeout(20 * 1000);
        httpRequestFactory.setWriteTimeout(20 * 1000);
        REST_TEMPLATE = new RestTemplate(httpRequestFactory);
        REST_TEMPLATE.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
}
