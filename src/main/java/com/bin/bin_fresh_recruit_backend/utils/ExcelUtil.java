package com.bin.bin_fresh_recruit_backend.utils;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author: hongxiaobin
 * @Time: 2024/4/17 1:29
 */
@Slf4j
public class ExcelUtil {
    /**
     * 用户信息导出类
     *
     * @param response   响应
     * @param fileName   文件名
     * @param columnList 每列的标题名
     * @param dataList   导出的数据
     */
    public static void uploadExcelAboutUser(HttpServletResponse response, String fileName, List<String> columnList, List<List<String>> dataList) {
        //声明输出流
        OutputStream os = null;
        //设置响应头
        setResponseHeader(response, fileName);
        try {
            //获取输出流
            os = response.getOutputStream();
            //内存中保留1000条数据，以免内存溢出，其余写入硬盘
            Workbook wb = new XSSFWorkbook();
            //获取该工作区的第一个sheet
            Sheet sheet1 = wb.createSheet("sheet1");
            int excelRow = 0;
            //创建标题行
            Row titleRow = sheet1.createRow(excelRow++);
            for (int i = 0; i < columnList.size(); i++) {
                //创建该行下的每一列，并写入标题数据
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columnList.get(i));
            }
            //设置内容行
            if (dataList != null && dataList.size() > 0) {
                //序号是从1开始的
                int count = 1;
                //外层for循环创建行
                for (List<String> strings : dataList) {
                    Row dataRow = sheet1.createRow(excelRow++);
                    //内层for循环创建每行对应的列，并赋值
                    for (int j = -1; j < dataList.get(0).size(); j++) {
                        Cell cell = dataRow.createCell(j + 1);
                        if (j == -1) {
                            cell.setCellValue(count++);
                        } else {//其余列是数据列，将数据库中读取到的数据依次赋值
                            cell.setCellValue(strings.get(j));
                        }
                    }
                }
            }
            //将整理好的excel数据写入流中
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置浏览器下载响应头
     *
     * @param response
     * @param fileName
     */
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void uploadExcelByEasyExcel(HttpServletResponse response, String fileName, List<List<String>> columnList, List<List<String>> dataList) {
        // 设置请求头
        setResponseHeaderByEasyExcel(response,fileName);
        // 导出数据
        try {
            EasyExcel.write(response.getOutputStream()).head(columnList).sheet("投递记录").doWrite(dataList);
        } catch (IOException e) {
            log.error("导出数据失败！错误信息为：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setResponseHeaderByEasyExcel(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        // 响应类型,编码
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }
}
