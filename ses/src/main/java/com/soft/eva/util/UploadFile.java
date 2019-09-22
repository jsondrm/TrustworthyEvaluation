package com.soft.eva.util;

import com.soft.eva.domain.SoftData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class UploadFile {
    public String upload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\") != -1){
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        String filePath = "src/main/resources/static/files/";
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(filePath+fileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }
}
