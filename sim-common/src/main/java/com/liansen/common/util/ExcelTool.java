package com.liansen.common.util;

import com.liansen.common.vo.FieldTransition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 解析（Excel导入和导出）
 * 入口 readExcel()
 * @Author: cdy
 * @Date: 2018/12/28 15:36
 * @Version 1.0
 */
public class ExcelTool {
    public static final String XLSX = ".xlsx";
    public static final String XLS=".xls";

    //    public static void main(String[] args) throws IOException, InvalidFormatException {
//        File f1 = new File("D:\\工艺信息导入模板.xlsx");
//        JSONArray jsonArray = ExcelTool.readExcel(f1);
//        List<Process> processList = jsonArray;
//        System.out.println(processList.size());
//    }
    /**
     * 获取Excel文件（.xls和.xlsx都支持）
     * @param file
     * @return  解析excle后的Json数据
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public static JSONArray readExcel(MultipartFile file) throws FileNotFoundException, IOException, InvalidFormatException {
        int res = checkFile(file);
        if (res == 0) {
            throw new NullPointerException("the file is null.");
        }else if (res == 1) {
            return readXLSX(file);
        }
//        else if (res == 2) {
//            return readXLS(file);
//        }
        throw new IllegalAccessError("the file["+file.getOriginalFilename()+"] is not excel file");
    }

    /**
     * 判断File文件的类型
     * @param file 传入的文件
     * @return 0-文件为空，1-XLSX文件，2-XLS文件，3-其他文件
     */
    public static int checkFile(MultipartFile file){
        if (file==null) {
            return 0;
        }
        String flieName = file.getOriginalFilename();
        if (flieName.endsWith(XLSX)) {
            return 1;
        }
        if (flieName.endsWith(XLS)) {
            return 2;
        }
        return 3;
    }

    /**
     * 读取XLSX文件
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static JSONArray readXLSX(MultipartFile file) throws InvalidFormatException, IOException {
        Workbook book = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = book.getSheetAt(0);
        return read(sheet, book);
    }

    /**
     * 读取XLS文件
     * @param file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
//    public static JSONArray readXLS(MultipartFile file) throws FileNotFoundException, IOException{
//        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
//        Workbook book = new HSSFWorkbook(poifsFileSystem);
//        Sheet sheet = book.getSheetAt(0);
//        return read(sheet, book);
//    }

    /**
     * 解析数据
     * @param sheet 表格sheet对象
     * @param book 用于流关闭
     * @return
     * @throws IOException
     */
    public static JSONArray read(Sheet sheet, Workbook book) throws IOException{
        int rowStart = sheet.getFirstRowNum();	// 首行下标
        int rowEnd = sheet.getLastRowNum();	// 尾行下标
        // 如果首行与尾行相同，表明只有一行，直接返回空数组
        if (rowStart == rowEnd) {
            book.close();
            return new JSONArray();
        }
        // 获取第一行JSON对象键
        Row firstRow = sheet.getRow(rowStart);
        int cellStart = firstRow.getFirstCellNum();
        int cellEnd = firstRow.getLastCellNum();
        Map<Integer, String> keyMap = new HashMap<Integer, String>();

        //导入excel所有字段
        for (int j = cellStart; j < cellEnd; j++) {
            keyMap.put(j, FieldTransition.transition(getValue(firstRow.getCell(j), rowStart, j, book, true)));

        }
        // 获取每行JSON对象的值
        JSONArray array = new JSONArray();
        for(int i = rowStart+1; i <= rowEnd ; i++) {
            Row eachRow = sheet.getRow(i);
            JSONObject obj = new JSONObject();
            StringBuffer sb = new StringBuffer();
            for (int k = cellStart; k < cellEnd; k++) {
                if (eachRow != null) {
                    String val = getValue(eachRow.getCell(k), i, k, book, false);
                    sb.append(val);		// 所有数据添加到里面，用于判断该行是否为空
                    obj.put(keyMap.get(k),val);
                }
            }
            if (sb.toString().length() > 0) {
                array.add(obj);
            }
        }
        book.close();
        return array;
    }

    /**
     * 获取每个单元格的数据
     * @param cell 单元格对象
     * @param rowNum 第几行
     * @param index 该行第几个
     * @param book 主要用于关闭流
     * @param isKey 是否为键：true-是，false-不是。 如果解析Json键，值为空时报错；如果不是Json键，值为空不报错
     * @return
     * @throws IOException
     */
    public static String getValue(Cell cell, int rowNum, int index, Workbook book, boolean isKey) throws IOException{

        // 空白或空
        if (cell == null || cell.getCellType()==Cell.CELL_TYPE_BLANK ) {
            if (isKey) {
                book.close();
                throw new NullPointerException(String.format("the key on row %s index %s is null ", ++rowNum,++index));
            }else{
                return "";
            }
        }

        // 0. 数字 类型
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return df.format(date);
            }
            String val = cell.getNumericCellValue()+"";
            val = val.toUpperCase();
//            if (val.contains("E")) {
//                val = val.split("E")[0].replace(".", "");
//            }
            val = val.replace(".0", "");
            return val;
        }

        // 1. String类型
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String val = cell.getStringCellValue();
            if (val == null || val.trim().length()==0) {
                if (book != null) {
                    book.close();
                }
                return "";
            }
            return val.trim();
        }

        // 2. 公式 CELL_TYPE_FORMULA
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getStringCellValue();
        }

        // 4. 布尔值 CELL_TYPE_BOOLEAN
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue()+"";
        }

        // 5.	错误 CELL_TYPE_ERROR
        return "";
    }

    /**
     * 导出excel
     * @param excel_name 导出的excel路径（需要带.xlsx)
     * @param headList  excel的标题备注名称
     * @param fieldList excel的标题字段（与数据中map中键值对应）
     * @param dataList  excel数据
     * @throws Exception
     */
    public static void createExcel(String excel_name, String[] headList,
                                   String[] fieldList, List<Map<String, Object>> dataList)
            throws Exception {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值
        XSSFSheet sheet = workbook.createSheet();
        // 在索引0的位置创建行（最顶端的行）
        XSSFRow row = sheet.createRow(0);
        // 设置excel头（第一行）的头名称
        for (int i = 0; i < headList.length; i++) {

            // 在索引0的位置创建单元格（左上端）
            XSSFCell cell = row.createCell(i);
            // 定义单元格为字符串类型
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            // 在单元格中输入一些内容
            cell.setCellValue(headList[i]);
        }
        // ===============================================================
        //添加数据
        for (int n = 0; n < dataList.size(); n++) {
            // 在索引1的位置创建行（最顶端的行）
            XSSFRow row_value = sheet.createRow(n + 1);
            Map<String, Object> dataMap = dataList.get(n);
            // ===============================================================
            for (int i = 0; i < fieldList.length; i++) {

                // 在索引0的位置创建单元格（左上端）
                XSSFCell cell = row_value.createCell(i);
                // 定义单元格为字符串类型
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                // 在单元格中输入一些内容
                cell.setCellValue((dataMap.get(fieldList[i])).toString());
            }
            // ===============================================================
        }
        // 新建一输出文件流
        FileOutputStream fos = new FileOutputStream(excel_name);
        // 把相应的Excel 工作簿存盘
        workbook.write(fos);
        fos.flush();
        // 操作结束，关闭文件
        fos.close();
    }
}
