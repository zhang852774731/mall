package com.bin.action.service.impl;

import com.bin.action.common.ServerResponse;
import com.bin.action.service.IFileService;
import com.bin.action.util.FtpUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zhangbin on 18/1/30.
 */
@Service(value = "iFileServiceImpl")
public class FileServiceImpl implements IFileService {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String upload(MultipartFile multipartFile, String path) {
        String fileOriginalName = multipartFile.getOriginalFilename();
        String extendName = fileOriginalName.substring(fileOriginalName.lastIndexOf(".")+1);
        String newName = UUID.randomUUID().toString()+"."+extendName;
        File targetDir = new File(path);
        if (!targetDir.exists()){
            targetDir.setWritable(true);
            targetDir.mkdirs();
        }
        File targetFile = new File(path+newName);
        try {
            //将文件上传到upload目录
            multipartFile.transferTo(targetFile);
            FtpUtil ftpUtil = new FtpUtil();
            ftpUtil.upload(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
