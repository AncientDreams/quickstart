package com.example.quickstart.controller.developmenttool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.quickstart.annotation.MethodRunLog;
import com.example.quickstart.bo.R;
import com.example.quickstart.constant.ResultConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.*;

/**
 * <p>
 * 系统开发工具控制器
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-08-03 15:07
 */
@Controller
@RequestMapping(value = "/tool")
@AllArgsConstructor
public class DevelopmentToolController {

    private final DataSource dataSource;

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view() {
        return new ModelAndView("tool/sql");
    }

    @PostMapping("/executeQuery")
    @ResponseBody
    @MethodRunLog
    public R<JSONArray> executeQuery(HttpServletRequest request) {
        String sql = request.getParameter("sql");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject(16, true);
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    int index = i + 1;
                    String columnName = resultSetMetaData.getColumnLabel(index);
                    jsonObject.put(columnName, resultSet.getObject(index));
                }
                jsonArray.add(jsonObject);
            }
            return R.success(ResultConstant.QUERY_SUCCESS, jsonArray);
        } catch (SQLException e) {
            return R.fail(e.getMessage());
        }
    }

}
