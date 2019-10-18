package com.example.wincloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Controller
public class FileController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam(value = "file") MultipartFile file) {

        BufferedInputStream is = null;
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        log.debug("文件大小{}", fileSize);

        File saveFile = new File("./data" + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            is = new BufferedInputStream(file.getInputStream());
            fileOutputStream = new FileOutputStream(saveFile);
            int length;
            byte[] bytes = new byte[1024];
            while((length = is.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return "Success";
    }
}
