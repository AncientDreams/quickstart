package com.example.quickstart.controller.developmenttool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.quickstart.bo.ResultBody;
import com.example.quickstart.constant.MessageConstant;
import com.example.quickstart.constant.SystemUrlConstant;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DevelopmentToolController {

    private DataSource dataSource;

    public DevelopmentToolController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value = SystemUrlConstant.VIEW)
    public ModelAndView view(){
        return new ModelAndView("tool/sql");
    }

    @PostMapping("/executeQuery")
    @ResponseBody
    public ResultBody executeQuery(HttpServletRequest request) throws SQLException {
        String sql = request.getParameter("sql");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

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
            return new ResultBody(true, MessageConstant.QUERY_SUCCESS, jsonArray);
        } catch (SQLException e) {
            return new ResultBody(false, e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
