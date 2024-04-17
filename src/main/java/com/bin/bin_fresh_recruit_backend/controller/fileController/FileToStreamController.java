package com.bin.bin_fresh_recruit_backend.controller.fileController;

import com.bin.bin_fresh_recruit_backend.interceptor.IgnoreAuth;
import com.bin.bin_fresh_recruit_backend.utils.FileSwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件路径转为文件流
 *
 * @Author: hongxiaobin
 * @Time: 2024/4/18 0:25
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileToStreamController {

    @IgnoreAuth
    @GetMapping("/url2stream")
    public void fileUrlToStream(HttpServletResponse response, @RequestParam("file_url") String fileUrl) {
        try {
            String fileName = "test.pdf";
            File file = FileSwitchUtils.urlToFlie(fileUrl, fileName);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.getOutputStream().write(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
