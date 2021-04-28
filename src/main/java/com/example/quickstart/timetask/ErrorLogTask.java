package com.example.quickstart.timetask;

import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.entity.SystemErrorLog;
import com.example.quickstart.service.ISystemErrorLogService;
import com.example.quickstart.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * <p>
 * 系统错误日志扫描任务
 * <p>
 * ConditionalOnProperty 注解确定本类是否生效
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2021/4/23 0023 13:57
 */
@Configuration
@ConditionalOnProperty(prefix = "filter", name = "ErrorLogTaskFilter", havingValue = "true")
@Slf4j
public class ErrorLogTask {

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

    private final ISystemErrorLogService iSystemErrorLogService;

    public ErrorLogTask(ISystemErrorLogService iSystemErrorLogService) {
        log.info("ErrorLogTask 已启动");
        this.iSystemErrorLogService = iSystemErrorLogService;
    }

    /**
     * 每个小时执行一次 "0 0 * * * ?"
     */
    @Scheduled(cron = "0 0 * * * ?")
    @MethodRunLog(methodName = "系统错误日志监控任务", result = false, parameters = false)
    public void task() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
        String logFileName = dtf2.format(localDateTime.minusHours(1));
        String path = loggerPath.concat("/").concat(applicationName).concat("/").concat(logFileName + ".log");
        File file = new File(path);
        log.info("【系统错误日志监控任务】读取文件{}，文件大小{}", file.getName(), file.length());
        String tempString;
        int count = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("Exception") || tempString.contains("ERROR")) {
                    if (tempString.length() > 500) {
                        tempString = tempString.substring(500);
                    }
                    SystemErrorLog systemErrorLog = new SystemErrorLog();
                    systemErrorLog.setErrorInfo(tempString);
                    systemErrorLog.setErrorLine(count);
                    systemErrorLog.setLogFileName(file.getName());
                    systemErrorLog.setServerIp(HttpUtil.getLocaliP());
                    iSystemErrorLogService.save(systemErrorLog);
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}