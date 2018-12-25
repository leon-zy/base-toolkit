package com.insaic.toolkit.utils;

import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.enums.FileTypeEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 加解密工具
 */
public class EncryptUtils {
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes("utf-8"));
        } catch (Exception e) {
            System.err.println("encrypt error:" + e);
        }
        return new String(Base64.encodeBase64(crypted));
    }

    public static String decrypt(String input, String key) {
        try {
            byte[] str = new BASE64Decoder().decodeBuffer(input);
            SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] bits = cipher.doFinal(str);
            return new String(bits, "utf-8");
        } catch (Exception e) {
            System.err.println("encrypt error:" + e);
            e.printStackTrace();
            return null;
        }
    }

    public static String Base64Encode(String param, String code) {
        byte[] bytes = null;
        try {
            bytes = param.getBytes(code);
        } catch (UnsupportedEncodingException e) {
        }
        String base64 = new String(new Base64().encode(bytes));
        return base64;
    }

    public static String encryptVuserInfo(String vuser_id, String vuser_pwd) {
        String loginInfo = vuser_id + ":" + vuser_pwd;
        try {
            loginInfo = new String(new BASE64Encoder().encode(loginInfo.getBytes("iso-8859-1")));
        } catch (UnsupportedEncodingException e) {
        }
        return loginInfo;
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgBase64Str(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * Base64转图片
     * @param baseStr  base64字符串
     * @param imagePath 生成的图片
     * @param compressImagePath 生成的压缩图片
     */
    public static void base64StrToImage(String baseStr,String imagePath, String compressImagePath){
        if (StringUtil.isNotBlank(baseStr)){
            OutputStream out = null;
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                // 解密
                byte[] b = decoder.decodeBuffer(baseStr);
                // 处理数据
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                out = new FileOutputStream(imagePath);
                //压缩JPG文件
                String fileName = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length());
                if (StringUtil.isNotBlank(imagePath) && (fileName.toLowerCase().endsWith(FileTypeEnum.jpg.getCode())
                        || fileName.toLowerCase().endsWith(FileTypeEnum.jpeg.getCode()))) {
                    ImageUtils.compressPicForSize(imagePath, compressImagePath, 500);
                }
                out.write(b);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(out);
            }
        }
    }

}
