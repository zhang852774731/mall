package com.bin.action.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangbin on 18/1/30.
 */
public class FtpUtil {
    private Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.ip");
    private static String ftpUsername = PropertiesUtil.getProperty("ftp.username");
    private static String ftppass = PropertiesUtil.getProperty("ftp.pass");
    private static String remotePath = PropertiesUtil.getProperty("ftp.remotePath");

    private FTPClient ftpClient;
    public boolean upload(List<File> files) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        if (createConnection()){
            ftpClient.setBufferSize(1024);
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                for (File file : files){
                    fileInputStream = new FileInputStream(file);
                    ftpClient.storeFile(remotePath,fileInputStream);
                }
            } catch (IOException e) {
                logger.error("ftp连接异常",e);
                uploaded = false;
                return uploaded;
            }finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private boolean createConnection(){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpIp);
            isSuccess = ftpClient.login(ftpUsername,ftppass);
        } catch (IOException e) {
            logger.error("ftp连接异常",e);
            return isSuccess;
        }
        return isSuccess;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
