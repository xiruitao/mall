package com.mall.util;

import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @Description: 图片上传工具类
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 13:50
 */
public class ImgUpUtil {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(ImgUpUtil.class);

    /**
     * 写文件
     * @param file 文件
     * @return 文件路径
     * @throws BusinessException 业务异常
     * @throws IOException 文件IO异常
     */
    public static String writeFile(MultipartFile file) throws BusinessException, IOException {
        if (file.isEmpty()) {
            logger.info("传入文件为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择上传文件");
        }
        String originalFilename = file.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString();
        int index = originalFilename.lastIndexOf(".");
        //文件扩展名
        String extensions = originalFilename.substring(index);
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        //文件目录
        String filePath = "C:\\Users\\Administrator\\Documents\\MyDocuments\\testPages\\img\\" + nowDate;

        File folderPath = new File(filePath);
        if (!folderPath.exists() && !folderPath .isDirectory()) {
            folderPath.mkdir();
        }
        //文件绝对路径
        filePath += "\\" + randomUUID + extensions;
        file.transferTo(new File(filePath));

        return filePath;
    }
}
