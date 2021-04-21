package com.example.quickstart.service.impl;

import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 日志服务接口实现
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-29 11:12
 */
@Service
@Slf4j
public class LogServiceImpl implements ILogService {

    /**
     * 日志保存路径
     */
    @Value("${logging.file.path}")
    private String loggerPath;

    /**
     * 应用程序名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public R<List<String>> queryTimeByDate(String date) {
        try {
            String path = loggerPath.concat("/").concat(applicationName);
            File file = new File(path);
            File[] files = file.listFiles();
            //文件截取索引位置，因为文件名称长度一致
            int index = 13;
            log.info("查询系统日志信息，日志路径：{}", path);
            if (files != null && files.length > 0) {
                List<String> fileNames = new ArrayList<>(files.length);
                for (File f : files) {
                    if (f.getName().startsWith(date)) {
                        fileNames.add(f.getName().substring(index - 2, index));
                    }
                }
                return R.success(ResultConstant.QUERY_SUCCESS, fileNames);
            }
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return R.fail(ResultConstant.QUERY_FAIL);
    }


    @MethodRunLog(methodName = "查询日志", result = false)
    @Override
    public R<List<String>> queryLogFileInfo(HttpServletRequest request) {
        String data = request.getParameter("date");
        String time = request.getParameter("time");

        String path = loggerPath.concat("/").concat(applicationName).concat("/").concat(data + "-" + time + ".log");
        File file = new File(path);
        String tempString;
        List<String> logList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                tempString = tempString.replaceAll("&", "&amp;")
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("\"", "&quot;");
                //处理等级
                tempString = tempString.replace("DEBUG", "<span style='color: blue;'>DEBUG</span>");
                tempString = tempString.replace("INFO", "<span style='color: green;'>INFO</span>");
                tempString = tempString.replace("WARN", "<span style='color: orange;'>WARN</span>");
                tempString = tempString.replace("ERROR", "<span style='color: red;'>ERROR</span>");
                logList.add(tempString + "<br/>");
            }
            return R.success(ResultConstant.QUERY_SUCCESS, logList);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
        return R.fail(ResultConstant.QUERY_FAIL);
    }

    @Override
    public R<String> getFileSize(HttpServletRequest httpServletRequest) {
        try {
            String data = httpServletRequest.getParameter("date");
            String time = httpServletRequest.getParameter("time");
            String path = loggerPath.concat("/").concat(applicationName).concat("/").concat(data + "-" + time + ".log");
            File file = new File(path);
            return R.success(getFileSize(file));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取文件大小错误：" + e.getMessage());
        }
    }

    private String getFileSize(File file) {
        String size;
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }

}
