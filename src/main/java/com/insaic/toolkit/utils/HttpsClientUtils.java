package com.insaic.toolkit.utils;

import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.constants.ToolkitConstants;
import com.insaic.toolkit.enums.EncodingEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HttpsClientUtils https请求工具类
 * Created by leon_zy on 2018/10/25
 */
public final class HttpsClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpsClientUtils.class);

    private static final int CONNECT_TIMEOUT = 60000;
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * https协议get请求
     * @param url 路径
     * @param encoding 字符编码
     * @param params 输出参数
     * @return response
     */
    public static String httpsGet(String url, String encoding, Map<String, String> params) {
        return httpsRequest(url, RequestMethod.GET, null, encoding, mapToRequestGetMethodStr(params, encoding));
    }

    /**
     * https协议post请求
     * @param url 路径
     * @param contentType 报文类型
     * @param encoding 字符编码
     * @param outputStr 输出参数
     * @return response
     */
    public static String httpsPost(String url, String contentType, String encoding, String outputStr) {
        return httpsRequest(url, RequestMethod.POST, contentType, encoding, outputStr);
    }

    /**
     * https协议方式请求服务器
     * @param url 请求地址
     * @param requestMethod GET/POST
     * @param outputStr POST为json参数，GET为xxx=xxx&xxx=xxx参数
     * @return response
     */
    private static String httpsRequest(String url, RequestMethod requestMethod, String contentType, String encoding, String outputStr) {
        DataOutputStream out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        logger.info("调用HttpsClientUtils.httpsRequest["+ requestMethod +"]方法, 服务端请求Url：" + url + ", 请求参数：" + outputStr);
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod.name());
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            if(RequestMethod.GET.equals(requestMethod)){
                conn.setDoInput(true);
                conn.setUseCaches(false);
            }else if(RequestMethod.POST.equals(requestMethod)){
                String conType;
                String encode = StringUtil.isBlank(encoding) ? EncodingEnum.UTF_8.getCode() : encoding;
                if (StringUtil.isBlank(contentType)) {
                    conType = "application/x-www-form-urlencoded;charset=" + encode;
                } else {
                    conType = contentType + ";charset=" + encode;
                }
                conn.setRequestProperty(CONTENT_TYPE, conType);
            }
            conn.connect();
            //往服务器端写内容
            if (StringUtil.isNotBlank(outputStr)) {
                out = new DataOutputStream(conn.getOutputStream());
                out.write(outputStr.getBytes(EncodingEnum.UTF_8.getCode()));
                // 刷新、关闭
                out.flush();
                out.close();
            }
            //读取服务器端返回的内容
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while (StringUtil.isNotBlank(line = in.readLine())) {
                result.append(line);
            }
            byte[] b = result.toString().getBytes();
            result = new StringBuilder(new String(b, EncodingEnum.UTF_8.getCode()));
        } catch (Exception e) {
            logger.error("发送https["+ requestMethod +"]请求出现异常！", e);
        } finally {
            //关闭输出流、输入流
            closeDataStream(out, in);
        }
        return result.toString();
    }

    /**
     * 关闭输出流、输入流
     * @param out 输出流
     * @param in 输入流
     */
    private static void closeDataStream(DataOutputStream out, BufferedReader in){
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            logger.error("关闭输出流、输入流出现异常！", ex);
        }
    }

    /**
     * map转换为get方法的字符串
     * @param map 集合
     * @param valEncode 值的编码
     * @return str
     */
    private static String mapToRequestGetMethodStr(Map<String, String> map, String valEncode) {
        String str = ToolkitConstants.EMPTY_STR;
        if (null != map && !map.isEmpty()) {
            StringBuilder paramStr = new StringBuilder();
            Boolean firstFlag = true;
            String encode = StringUtil.isBlank(valEncode) ? EncodingEnum.UTF_8.getCode() : valEncode;
            for (String key : map.keySet()) {
                String value = ToolkitConstants.EMPTY_STR;
                try {
                    value = URLEncoder.encode(StringUtil.toString(map.get(key)), encode);
                } catch (Exception e) {
                    logger.error("mapToRequestGetMethodStr执行异常！", e);
                }
                if(!firstFlag){
                    paramStr.append(ToolkitConstants.AND_STR);
                }
                paramStr.append(key).append(ToolkitConstants.EQUAL_STR).append(value);
                firstFlag = false;
            }
            str = paramStr.toString();
            if (ToolkitConstants.AND_STR.equals(StringUtil.toString(str.charAt(str.length() - 1)))) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    /**
     * 验证证书
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    /**
     * 验证证书
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
