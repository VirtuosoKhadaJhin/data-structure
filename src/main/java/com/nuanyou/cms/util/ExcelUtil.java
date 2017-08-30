package com.nuanyou.cms.util;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.util.*;


public class ExcelUtil {

    /**
     * 根据输入的数据生成一个XSSFWorkbook
     */
    public static <T> XSSFWorkbook generateXlsxWorkbook(LinkedHashMap<String, String> titleMap, Collection<T> dataSet) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("ny-sheet");
        sheet.setDefaultColumnWidth(20);

        XSSFCellStyle headerStyle = getHeaderStyle(workbook);
        XSSFCellStyle contentStyle = getContentStyle(workbook);

        XSSFRow row = sheet.createRow(0);
        int i = 0;
        for (String key : titleMap.keySet()) {
            sheet.autoSizeColumn(i,true);
            XSSFCell cell = row.createCell(i++);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(new XSSFRichTextString(titleMap.get(key)));
        }

        int rowNo = 1;
        for (T data : dataSet) {
            row = sheet.createRow(rowNo++);

            int cellNo = 0;
            for (String property : titleMap.keySet()) {
                XSSFCell cell = row.createCell(cellNo++);
                cell.setCellStyle(contentStyle);
                String textValue = null;


                Object value = data;
                String[] names = property.split("\\.");
                for (String name : names) {
                    if (value != null) {
                        if (value instanceof Collection) {
                            StringBuilder sb = new StringBuilder();
                            for (Object o : (Collection) value) {
                                sb.append(BeanUtils.getValue(o, name)).append(",");
                            }
                            if (sb.length() > 0)
                                value = sb.deleteCharAt(sb.length() - 1).toString();
                            else
                                value = "";
                        } else {
                            value = BeanUtils.getValue(value, name);
                        }
                    }
                }

                if (value != null) {
                    if (value instanceof Date) {
                        textValue = DateUtils.format((Date) value);
                    } else {
                        textValue = value.toString();
                    }
                }

                cell.setCellValue(new XSSFRichTextString(textValue));


            }

        }
        return workbook;
    }

    /**
     * 生成一个标题style
     */
    public static XSSFCellStyle getHeaderStyle(Workbook workbook) {
        return getHeaderStyle(workbook, Color.WHITE, IndexedColors.BLACK.getIndex());
    }

    /**
     * 生成一个指定颜色的标题style
     */
    public static XSSFCellStyle getHeaderStyle(Workbook workbook, Color foregroundColor, short fontColor) {

        // 生成一个样式（用于标题）
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(new XSSFColor(foregroundColor));
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setColor(fontColor);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        return style;
    }

    /**
     * 生成一个用于内容的style
     */
    public static XSSFCellStyle getContentStyle(Workbook workbook) {
        // 生成并设置另一个样式（用于内容）
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        //style.setFillForegroundColor(new XSSFColor(Color.YELLOW));
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);

        return style;
    }

    //测试：
//    public static void main(String[] args) {
//        List dataSet = new ArrayList<>();
//
//        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
//        //propertyHeaderMap.put("id", "唯一标识"); //注释掉，不导出id
//        propertyHeaderMap.put("name", "姓名");
//        propertyHeaderMap.put("age", "年龄");
//        propertyHeaderMap.put("sexName", "性别"); //直接获取Student中的sexName，而不是sex
//        propertyHeaderMap.put("birthday", "生日");
//
//        try {
//            XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, dataSet);
//            OutputStream out = new FileOutputStream("F://student3.xlsx");
//            ex.write(out);
//            System.out.println("导出成功！");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}