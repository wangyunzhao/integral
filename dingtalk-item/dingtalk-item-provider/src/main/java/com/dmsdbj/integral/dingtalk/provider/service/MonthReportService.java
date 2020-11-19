package com.dmsdbj.integral.dingtalk.provider.service;

import com.dmsdbj.integral.dingtalk.model.MonthReportModel;
import com.dmsdbj.integral.dingtalk.provider.common.Constants;
import com.dmsdbj.integral.dingtalk.provider.dao.MonthReportDao;
import com.tfjybj.framework.json.JsonHelper;
import com.tfjybj.framework.log.LogCollectManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.*;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 导出月报
 *
 * @author 崔晓鸿
 * @since 2020年6月11日08:57:48
 */
@Service
@RefreshScope
public class MonthReportService {
    @Autowired
    private MonthReportDao monthReportDao;
    @Value("${monthReport.excelTitle}")
    private String[] excelTitle;
    @Value("${monthReport.excelFileName}")
    private String excelFileName;
    @Value("${monthReport.excelSheetName}")
    private String excelSheetName;

    private static final String INDEX = "monthReport";


    /**
     * 根据组织id(相对应的期数)与时间导出月报
     *
     * @author 崔晓鸿
     * @since 2020年6月8日09:42:04
     */
    public void monthReportDownload(HttpServletResponse response, String organizationId, String startTime, String endTime) {
        LogCollectManager.common(MessageFormat.format(Constants.BEGIN + " 进入导出月报方法", ""), Constants.DING_INDEX + INDEX);
        Map<String, Object> map = new HashMap<>(16);
        map.put("organizationId", organizationId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 查询月报数据方法开始 param <> {0}", JsonHelper.toJson(map)), Constants.DING_INDEX + INDEX);
        List<MonthReportModel> monthReportModels = new ArrayList<>();
        // 获取月报数据
        try {
            monthReportModels = selectMonthReport(organizationId, startTime, endTime);
        } catch (Exception e) {
            LogCollectManager.common(MessageFormat.format(Constants.ERR + " service <> {0} 调用查询月报数据方法出现异常  message <> {1}", INDEX, e.getMessage()), Constants.DING_ERROR);
        }

        LogCollectManager.common(MessageFormat.format(Constants.LOG + " 查询月报数据方法结束 result <> {0}", JsonHelper.toJson(monthReportModels)), Constants.DING_INDEX + INDEX);
        // 将月报数据放到表格导出
        exportExcel(response, monthReportModels);
        LogCollectManager.common(MessageFormat.format(Constants.END + " 导出月报方法结束", JsonHelper.toJson("")), Constants.DING_INDEX + INDEX);
    }

    /**
     * 获取月报数据，查询请假与违纪数据
     *
     * @return List<MonthReportModel>
     * @author 崔晓鸿
     * @since 2020年6月8日09:45:15
     */
    public List<MonthReportModel> selectMonthReport(String organizationId, String startTime, String endTime) {
        // 查询请假与违纪数据
        List<MonthReportModel> monthReportModels = monthReportDao.selectMonthReport(organizationId, startTime, endTime);
        // 若没有查到数据
        if (monthReportModels.size() == 0) {
            List<MonthReportModel> monthReportModelsNone = new ArrayList<>();
            return monthReportModelsNone;
        }
        return monthReportModels;
    }


    /**
     * 设置Excel标题、文件名、sheet名，将查到的月报数据转化为表格用到的二维数组，以及导出
     *
     * @author 崔晓鸿
     * @since 2020年6月8日09:42:04
     */
    public void exportExcel(HttpServletResponse response, List<MonthReportModel> monthReportModels) {
        // Excel标题
        String[] title = excelTitle;
        // Excel文件名
        String fileName = excelFileName;
        // sheet名
        String sheetName = excelSheetName;
        // 声明一个二维数组
        String[][] content = new String[monthReportModels.size()][title.length];
        MonthReportModel monthReportModel = new MonthReportModel();
        // 获取对象属性
        String[] filedNames = getFiledName(monthReportModel);
        // 获取对象属性对应的get方法
        List<Method> fieldMethods = getFieldMethod(filedNames, monthReportModel);
        // 将数据放到数组中
        for (int i = 0; i < monthReportModels.size(); i++) {
            monthReportModel = monthReportModels.get(i);
            int j = 0;
            for (Method method : fieldMethods) {
                Object value = null;
                try {
                    value = method.invoke(monthReportModel, new Object[]{});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                content[i][j] = (String) value;
                j++;
            }
        }


        // 导出Excel
        try {
            HSSFWorkbook hssfWorkbook = getHssfWorkbook(sheetName, title, content, null);
            setResponseHeader(response, fileName);
            OutputStream outputStream = response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成表格
     *
     * @return HSSFWorkbook
     * @author 崔晓鸿
     * @since 2020年6月8日09:42:04
     */
    public HSSFWorkbook getHssfWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook workbook) {
        // 创建一个HSSFWorkbook，对应一个Excel文件
        if (workbook == null) {
            workbook = new HSSFWorkbook();
        }
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 在sheet中添加表头第0行
        HSSFRow row = sheet.createRow(0);
        // 创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 声明列对象
        HSSFCell cell = null;
        // 创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(cellStyle);
        }
        // 创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                // 将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return workbook;

    }

    /**
     * 发送响应流
     *
     * @author 崔晓鸿
     * @since 2020年6月8日09:43:22
     */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象属性
     *
     * @return String[]
     * @author 崔晓鸿
     * @since 2020年6月8日10:34:01
     */
    public String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 用反射根据属性名称获取属性get方法
     *
     * @return List<Method>
     * @author 崔晓鸿
     * @since 2020年6月8日10:34:01
     */
    public List<Method> getFieldMethod(String[] fieldNames, Object o) {
        List<Method> methods = new ArrayList<Method>();
        for (String fieldName : fieldNames) {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = null;
            try {
                method = o.getClass().getMethod(getter, new Class[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            methods.add(method);
        }
        return methods;
    }


}
