package com.insaic.toolkit.utils;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.insaic.toolkit.enums.EncodingEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * MD5加密解密工具类
 */
public class MD5Utils {

    private final static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    public static String MD5(String sourceStr, String charset) {
        logger.info("加密前数据：" + sourceStr + "加密字符集：" + charset);

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes(charset));
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
        }
        logger.info("加密后数据：" + result);
        return result;
    }

    public static String MD5(String sourceStr) {
        return MD5(sourceStr, EncodingEnum.UTF_8.getCode());
    }

    public static String getImgMd5Code(String imgFile) {
        String imgMd5Str = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(imgFile);
            imgMd5Str = DigestUtils.md5Hex(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return imgMd5Str;
    }
}
