package com.insaic.toolkit.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 * Created by FansenSen on 2018/10/25 0025.
 */
public class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 根据指定大小压缩图片
     *
     * @param srcPath     源图片地址
     * @param desPath     目标图片地址
     * @param desFileSize 指定图片大小，单位kb
     * @return
     */
    public static String commpressPicForSize(String srcPath, String desPath, long desFileSize) {
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {
            return null;
        }
        if (!new File(srcPath).exists()) {
            return null;
        }
        try {
            File srcFile = new File(srcPath);
            long srcFileSize = srcFile.length();
            LOGGER.info("源图片：" + srcPath + "，大小：" + srcFileSize / 1024 + "kb");
            // 递归压缩，直到目标文件大小小于desFileSize
            Thumbnails.of(srcPath).scale(1f).toFile(desPath);//最高精度copy一份到目的地址
            commpressPicCycle(desPath, desFileSize);
            File desFile = new File(desPath);
            LOGGER.info("目标图片：" + desPath + "，大小" + desFile.length() / 1024 + "kb");
            LOGGER.info("图片压缩完成！");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return desPath;
    }

    private static void commpressPicCycle(String desPath, long desFileSize) throws IOException {
        File srcFileJPG = new File(desPath);
        long srcFileSizeJPG = srcFileJPG.length();
        // 2、判断大小，如果小于设定大小，不压缩；如果大于等于设定大小，压缩
        if (srcFileSizeJPG <= desFileSize * 1024) {
            return;
        }
        Thumbnails.of(desPath).scale(0.8f).toFile(desPath);//按比例缩小
        commpressPicCycle(desPath, desFileSize);
    }
}
