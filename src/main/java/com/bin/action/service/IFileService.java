package com.bin.action.service;

import com.bin.action.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by zhangbin on 18/1/30.
 */
public interface IFileService {
    String upload(MultipartFile multipartFile, String path);
}
