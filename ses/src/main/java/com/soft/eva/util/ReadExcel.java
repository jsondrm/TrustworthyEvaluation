package com.soft.eva.util;

import com.soft.eva.domain.SoftData;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    public  List<SoftData> getAllData(String path){
        try{
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row = null;
            List<SoftData> softDataList = new ArrayList<>();
            for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
                row = sheet.getRow(i);
                SoftData softData = new SoftData();
                Field[] fields = softData.getClass().getDeclaredFields();
                for(int j = 0; j < row.getPhysicalNumberOfCells(); j++){
                    fields[j+1].setAccessible(true);
                    if(j == 11 && row.getCell(j).getNumericCellValue() == 0){
                        return null;
                    }else{
                        row.getCell(j).setCellType(CellType.STRING);
                        fields[j+1].set(softData,row.getCell(j).getStringCellValue());
                    }
                }
                softDataList.add(softData);
            }
            return softDataList;

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("没有找到文件");
            return null;
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }


}
