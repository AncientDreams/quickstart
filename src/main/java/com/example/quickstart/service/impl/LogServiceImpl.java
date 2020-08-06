package com.example.quickstart.service.impl;

import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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
public class LogServiceImpl implements ILogService {

    private Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

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
    public ResultBody queryTimeByDate(String date) {
        try {
            String path = loggerPath.concat("/").concat(applicationName);
            File file = new File(path);
            File[] files = file.listFiles();
            //文件截取索引位置，因为文件名称长度一致
            int index = 13;
            logger.info("查询系统日志信息，日志路径：{}", path);
            if (files != null && files.length > 0) {
                List<String> fileNames = new ArrayList<>(files.length);
                for (File f : files) {
                    if (f.getName().startsWith(date)) {
                        fileNames.add(f.getName().substring(index - 2, index));
                    }
                }
                return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS, fileNames);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
        return new ResultBody<>(false, MessageConstant.QUERY_FAIL);
    }


    @MethodRunLog(methodName = "查询日志", result = false)
    @Override
    public ResultBody queryLogFileInfo(HttpServletRequest request) {
        String data = request.getParameter("date");
        String time = request.getParameter("time");

        String path = loggerPath.concat("/").concat(applicationName).concat("/").concat(data + "-" + time + ".log");
        File file = new File(path);
        String tempString;
        List<String> log = new ArrayList<>();
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
                log.add(tempString + "<br/>");
            }
            return new ResultBody<>(true, MessageConstant.QUERY_SUCCESS, log);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return new ResultBody<>(false, MessageConstant.QUERY_FAIL);
    }


}
