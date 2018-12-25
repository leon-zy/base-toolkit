package com.insaic.toolkit.utils;

import com.insaic.base.exception.ExceptionUtil;
import com.insaic.base.utils.Collections3;
import com.insaic.base.utils.DateStyle;
import com.insaic.base.utils.StringUtil;
import com.insaic.toolkit.constants.ToolkitConstants;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel工具类
 */
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private static final String ERROR_TITLE_STR = "错误信息";
    public static final String SHEET_NAME = "sheet1";
    private final static String METHOD_GET = "get";

    public List<List<String>> readExcel(MultipartFile file) throws IOException {
        String path = file.getOriginalFilename();
        int index = path.lastIndexOf(".");
        String lastStr = path.substring(index + 1);
        if (path == null || ("").equals(path)) {
            return null;
        } else {
            if (lastStr.equals("xls")) {
                return readXls(file);
            } else if (lastStr.equals("xlsx")) {
                return readXlsx(file);
            } else {
                System.out.println(path + ": Not the Excel file!");
            }
        }
        return null;
    }


    public List<List<String>> readExcelAll(MultipartFile file) throws IOException {
        String path = file.getOriginalFilename();
        int index = path.lastIndexOf(".");
        String lastStr = path.substring(index + 1);
        if (path == null || ("").equals(path)) {
            return null;
        } else {
            if (lastStr.equals("xls")) {
                return readXlsAll(file);
            } else if (lastStr.equals("xlsx")) {
                return readXlsxAll(file);
            } else {
                System.out.println(path + ": Not the Excel file!");
            }
        }
        return null;
    }

    public List<List<String>> readExcelNew(MultipartFile file) throws IOException {
        String path = file.getOriginalFilename();
        int index = path.lastIndexOf(".");
        String lastStr = path.substring(index + 1);
        if (path == null || ("").equals(path)) {
            return null;
        } else {
            if (lastStr.equals("xls")) {
                return readXlsNew(file);
            } else if (lastStr.equals("xlsx")) {
                return readXlsxNew(file);
            } else {
                System.out.println(path + ": Not the Excel file!");
            }
        }
        return null;
    }

    public List<List<String>> readXlsxAll(MultipartFile file) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();

        // 只读第一个Sheet
        int i = 0;
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null)
                continue;

            if (i != 0) {
                break;
            }

            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);

                if ((xssfRow.getCell(0) == null || "".equals(xssfRow.getCell(0).toString().trim()))
                        && (xssfRow.getCell(1) == null || "".equals(xssfRow.getCell(1).toString().trim()))
                        ) {
                    continue;
                }

                int mixColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = mixColIx; colIx < maxColIx; colIx++) {
                    Cell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                list.add(rowList);
            }
            i++;
        }

        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                }
            }
        }
        return list;
    }

    public List<List<String>> readXlsx(MultipartFile file) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();

        // 只读第一个Sheet
        int i = 0;
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null)
                continue;

            if (i != 0) {
                break;
            }

            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);

                if ((xssfRow.getCell(0) == null || "".equals(xssfRow.getCell(0).toString().trim()))
                        && (xssfRow.getCell(1) == null || "".equals(xssfRow.getCell(1).toString().trim()))
                        ) {
                    continue;
                }

                int mixColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = mixColIx; colIx < maxColIx; colIx++) {
                    Cell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                list.add(rowList);
            }
            i++;
        }

        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                }
            }
        }
        return list;
    }

    public List<List<String>> readXlsxNew(MultipartFile file) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();

        // 只读第一个Sheet
        int i = 0;
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null)
                continue;

            if (i != 0) {
                break;
            }
            Row xssfRowHeader = xssfSheet.getRow(0);
            int mixColIx = xssfRowHeader.getFirstCellNum();
            int maxColIx = xssfRowHeader.getLastCellNum();
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);

                if (xssfRow == null || ((xssfRow.getCell(0) == null || "".equals(xssfRow.getCell(0).toString().trim()))
                        && (xssfRow.getCell(1) == null || "".equals(xssfRow.getCell(1).toString().trim())))
                        ) {
                    continue;
                }

                List<String> rowList = new ArrayList<String>();
                for (int colIx = mixColIx; colIx < maxColIx; colIx++) {
                    Cell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        rowList.add(" ");
                    } else {
                        rowList.add(getStringVal(cell));
                    }
                }
                list.add(rowList);
            }
            i++;
        }

        return list;
    }

    public List<List<String>> readXlsAll(MultipartFile file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);

                if ((hssfRow.getCell(0) == null || "".equals(hssfRow.getCell(0).toString().trim()))
                        && (hssfRow.getCell(1) == null || "".equals(hssfRow.getCell(1).toString().trim()))
                        ) {
                    continue;
                }

                int minColIx = hssfRow.getFirstCellNum();
                int maxColIx = hssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    HSSFCell cell = hssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                list.add(rowList);
            }
        }
        return list;
    }

    public List<List<String>> readXls(MultipartFile file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);

                if ((hssfRow.getCell(0) == null || "".equals(hssfRow.getCell(0).toString().trim()))
                        && (hssfRow.getCell(1) == null || "".equals(hssfRow.getCell(1).toString().trim()))
                        ) {
                    continue;
                }

                int minColIx = hssfRow.getFirstCellNum();
                int maxColIx = hssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    HSSFCell cell = hssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                list.add(rowList);
            }
        }
        return list;
    }

    public List<List<String>> readXlsNew(MultipartFile file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            HSSFRow hssfRowHeader = hssfSheet.getRow(0);
            int minColIx = hssfRowHeader.getFirstCellNum();
            int maxColIx = hssfRowHeader.getLastCellNum();
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);

                if (hssfRow == null || ((hssfRow.getCell(0) == null || "".equals(hssfRow.getCell(0).toString().trim()))
                        && (hssfRow.getCell(1) == null || "".equals(hssfRow.getCell(1).toString().trim())))
                        ) {
                    continue;
                }

                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    HSSFCell cell = hssfRow.getCell(colIx);
                    if (cell == null) {
                        rowList.add(" ");
                    } else {
                        rowList.add(getStringVal(cell));
                    }
                }
                list.add(rowList);
            }
        }
        return list;
    }

    private String getStringVal(Cell cell) {
        String res = "";
        if (null == cell) {
            return "";
        }
//		res=cell.getRichStringCellValue().toString();

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字/日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    res = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {

                    BigDecimal value = new BigDecimal(cell.getNumericCellValue());
                    String str = value.toString();
                    if (str.contains(".")) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        res = df.format(cell.getNumericCellValue());
                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        res = df.format(cell.getNumericCellValue());
                    }


                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                res = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                res = booleanValue.toString();
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                res = "";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                res = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                res = "";
                break;
            default:
                System.out.println("未知类型");
                break;
        }
        return res;
    }

    private String getStringVal(Cell cell, String dateType) {
        String res = "";
        if (null == cell) {
            return "";
        }
//		res=cell.getRichStringCellValue().toString();

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字/日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    res = new SimpleDateFormat(StringUtil.isBlank(dateType) ? DateStyle.YYYY_MM_DD.getValue() : dateType).format(cell.getDateCellValue());
                } else {

                    BigDecimal value = new BigDecimal(cell.getNumericCellValue());
                    String str = value.toString();
                    if (str.contains(".")) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        res = df.format(cell.getNumericCellValue());
                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        res = df.format(cell.getNumericCellValue());
                    }


                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                res = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                res = booleanValue.toString();
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                res = "";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                res = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                res = "";
                break;
            default:
                System.out.println("未知类型");
                break;
        }
        return res;
    }


    /**
     * 读取Excel文件，数字原样读入，不做精度控制
     * @param file
     * @return
     * @throws IOException
     */
    public List<List<String>> readExcelSource(MultipartFile file) throws IOException {
        String path = file.getOriginalFilename();
        int index = path.lastIndexOf(".");
        String lastStr = path.substring(index + 1);
        if (path == null || ("").equals(path)) {
            return null;
        } else {
            if (lastStr.equals("xls")) {
                return readXlsSource(file);
            } else if (lastStr.equals("xlsx")) {
                return readXlsxSource(file);
            } else {
                logger.info(path + ": Not the Excel file!");
            }
        }
        return null;
    }

    public List<List<String>> readXlsSource(MultipartFile file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            HSSFRow hssfRowHeader = hssfSheet.getRow(0);
            int minColIx = hssfRowHeader.getFirstCellNum();
            int maxColIx = hssfRowHeader.getLastCellNum();
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);

                if (hssfRow == null || ((hssfRow.getCell(0) == null || "".equals(hssfRow.getCell(0).toString().trim()))
                        && (hssfRow.getCell(1) == null || "".equals(hssfRow.getCell(1).toString().trim())))
                        ) {
                    continue;
                }

                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    HSSFCell cell = hssfRow.getCell(colIx);
                    if (cell == null) {
                        rowList.add(" ");
                    } else {
                        rowList.add(getSourceStringVal(cell));
                    }
                }
                list.add(rowList);
            }
        }
        return list;
    }

    private String getSourceStringVal(Cell cell) {
        String res = "";
        if (null == cell) {
            return "";
        }
//		res=cell.getRichStringCellValue().toString();

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字/日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    res = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {

                    BigDecimal value = new BigDecimal(cell.getNumericCellValue());
                    res = value.toString();
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                res = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                res = booleanValue.toString();
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                res = "";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                res = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                res = "";
                break;
            default:
                System.out.println("未知类型");
                break;
        }
        return res;
    }

    public List<List<String>> readXlsxSource(MultipartFile file) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        List<List<String>> list = new ArrayList<List<String>>();

        // 只读第一个Sheet
        int i = 0;
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null)
                continue;

            if (i != 0) {
                break;
            }
            Row xssfRowHeader = xssfSheet.getRow(0);
            int mixColIx = xssfRowHeader.getFirstCellNum();
            int maxColIx = xssfRowHeader.getLastCellNum();
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = xssfSheet.getRow(rowNum);

                if (xssfRow == null || ((xssfRow.getCell(0) == null || "".equals(xssfRow.getCell(0).toString().trim()))
                        && (xssfRow.getCell(1) == null || "".equals(xssfRow.getCell(1).toString().trim())))
                        ) {
                    continue;
                }

                List<String> rowList = new ArrayList<String>();
                for (int colIx = mixColIx; colIx < maxColIx; colIx++) {
                    Cell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        rowList.add(" ");
                    } else {
                        rowList.add(getSourceStringVal(cell));
                    }
                }
                list.add(rowList);
            }
            i++;
        }

        return list;
    }

    public static String exportErrorInfoToExcel(String excelPath, String titles, List<List<String>> excelList) {

        String excelFilePath;

        String path = excelPath;

        File errorCsvDir = new File(excelPath);
        //如果文件夹不存在则创建
        if (!errorCsvDir.exists() && !errorCsvDir.isDirectory()) {
            logger.info(excelPath + "不存在");
            errorCsvDir.mkdirs();
        }

        String dateStr = String.valueOf(new Date().getTime());
        File filePath = new File(path);
        File errorFile = new File(filePath, "errorExport" + dateStr + ".xls");
        excelFilePath = path + "/errorExport" + dateStr + ".xls";
        OutputStream os = null;
        try {
            os = new FileOutputStream(errorFile);
            HSSFWorkbook wb = new HSSFWorkbook();
            String[] titleArrays = titles.split(",");
            HSSFSheet sheet = wb.createSheet("sheet1");
            HSSFRow row1 = sheet.createRow(0);
            for (int i = 0; i < titleArrays.length; i++) {
                HSSFCell cell = row1.createCell(i);
                //设置头信息内容
                cell.setCellValue(titleArrays[i]);
            }

            for (int i = 0; i < excelList.size(); i++) {
                HSSFRow rowadd = sheet.createRow(i + 1);
                for (int j = 0; j < excelList.get(i).size(); j++) {
                    HSSFCell cell1 = rowadd.createCell(j);
                    cell1.setCellValue(excelList.get(i).get(j));
                }
            }
            wb.write(os);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return excelFilePath;
    }

    /**
     * 获取文件的全部属性，并返回
     * @param file 文件
     * @param name sheet名称
     * @param dateType 日期格式
     * @return list
     * @throws IOException 异常
     */
    public List<List<String>> readXlsSheetNameAllFile(MultipartFile file, String name, String dateType) throws IOException, InvalidFormatException {
        List<List<String>> list = new ArrayList<List<String>>();
        Row row;
        Cell cell;
        Sheet sheet;
        Row firstRow;
        int firstColIx = 0;
        int lastColIx = 0;
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            sheet = wb.getSheetAt(numSheet);
            if(null != sheet && (StringUtil.isBlank(name) || StringUtil.toString(sheet.getSheetName()).contains(name))){
                if(firstColIx == 0 && lastColIx == 0){
                    firstRow = sheet.getRow(0);
                    firstColIx = firstRow.getFirstCellNum();
                    lastColIx = firstRow.getLastCellNum();
                }
                //文件第一行中文title不取
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    row = sheet.getRow(rowNum);
                    if (null != row && (StringUtil.isNotBlank(StringUtil.toString(row.getCell(0)))
                            || StringUtil.isNotBlank(StringUtil.toString(row.getCell(1))))) {
                        List<String> rowList = new ArrayList<String>();
                        for (int colIx = firstColIx; colIx < lastColIx; colIx++) {
                            cell = row.getCell(colIx);
                            rowList.add(getStringVal(cell, dateType));
                        }
                        list.add(rowList);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 错误信息导出
     * @param excelList 文件信息
     * @param fileNameList 属性名称
     * @param errorExcelPath 错误文件地址
     * @return str
     */
    public static String exportErrorInfoToExcel(List excelList, List<String> fileNameList, String errorExcelPath, String titleStr, String errorMsg) {
        String excelFilePath = null;
        if(Collections3.isNotEmpty(excelList) && Collections3.isNotEmpty(fileNameList)
                && StringUtil.isNotBlank(errorExcelPath) && StringUtil.isNotBlank(titleStr)){
            File errorExcelDir = new File(errorExcelPath);
            //如果文件夹不存在则创建
            if (!errorExcelDir.exists() && !errorExcelDir.isDirectory()) {
                logger.info(errorExcelPath + "不存在");
                errorExcelDir.mkdirs();
            }
            String dateStr = String.valueOf(new Date().getTime());
            File filePath = new File(errorExcelPath);
            File errorFile = new File(filePath, "errorExport" + dateStr + ".xls");
            excelFilePath = errorExcelPath + "/errorExport" + dateStr + ".xls";
            OutputStream os = null;
            try {
                os = new FileOutputStream(errorFile);
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("sheet1");
                HSSFRow row1 = sheet.createRow(0);
                //设置头信息内容
                String[] titles = titleStr.split(ToolkitConstants.COMMA_EN);
                for (int i = 0; i < titles.length; i++) {
                    HSSFCell cell = row1.createCell(i);
                    cell.setCellValue(titles[i]);
                }
                if(StringUtil.isNotBlank(errorMsg)){
                    HSSFCell cell = row1.createCell(titles.length);
                    cell.setCellValue(ERROR_TITLE_STR);
                }
                //单元格信息
                Method method;
                String upperName;
                for (int i = 0; i < excelList.size(); i++) {
                    HSSFRow rowAdd = sheet.createRow(i + 1);
                    for(int j = 0; j < fileNameList.size(); j++){
                        upperName = fileNameList.get(j).substring(0, 1).toUpperCase() + fileNameList.get(j).substring(1);
                        method = excelList.get(i).getClass().getMethod(METHOD_GET + upperName);
                        HSSFCell cell1 = rowAdd.createCell(j);
                        cell1.setCellValue(StringUtil.toString(method.invoke(excelList.get(i))));
                    }
                    if(StringUtil.isNotBlank(errorMsg)){
                        HSSFCell cell1 = rowAdd.createCell(fileNameList.size());
                        cell1.setCellValue(errorMsg);
                    }
                }
                wb.write(os);
            } catch (Exception e) {
                logger.error("错误信息导出异常", e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return excelFilePath;
    }

    /**
     * 导出Excel
     * @param titleStr 标题
     * @param entryList 内容
     * @param fileNameList 属性名称
     * @return wb
     */
    public static HSSFWorkbook getHSSFWorkbook(String titleStr, List entryList, List<String> fileNameList){
        HSSFWorkbook wb;
        try{
            // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
            wb = new HSSFWorkbook();
            // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet(SHEET_NAME);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
            HSSFRow row = sheet.createRow(0);
            //声明列对象
            HSSFCell cell;
            //创建标题
            String[] titles = titleStr.split(ToolkitConstants.COMMA_EN);
            for(int i=0;i<titles.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
            }
            //创建内容
            if(Collections3.isNotEmpty(entryList) && Collections3.isNotEmpty(fileNameList)){
                Method method;
                String upperName;
                for (int i = 0; i < entryList.size(); i++) {
                    HSSFRow rowAdd = sheet.createRow(i + 1);
                    for(int j = 0; j < fileNameList.size(); j++){
                        upperName = fileNameList.get(j).substring(0, 1).toUpperCase() + fileNameList.get(j).substring(1);
                        method = entryList.get(i).getClass().getMethod(METHOD_GET + upperName);
                        cell = rowAdd.createCell(j);
                        cell.setCellValue(StringUtil.toString(method.invoke(entryList.get(i))));
                    }
                }
            }
        } catch (Exception e) {
            wb = null;
            logger.error("信息导出excel异常", e);
        }
        return wb;
    }
    /**
     * 导出Excel
     * @param titleStr 标题
     * @return wb
     */
    public static HSSFWorkbook getEmptyHSSFWorkbook(String titleStr){
        HSSFWorkbook wb;
        try{
            // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
            wb = new HSSFWorkbook();
            // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet(SHEET_NAME);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
            HSSFRow row = sheet.createRow(0);
            //声明列对象
            HSSFCell cell;
            //创建标题
            String[] titles = titleStr.split(ToolkitConstants.COMMA_EN);
            for(int i=0;i<titles.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
            }
        } catch (Exception e) {
            wb = null;
            logger.error("信息导出excel异常", e);
        }
        return wb;
    }
    /**
     * 导出Excel
     * @param sheet excel中的要充填的页签
     * @param fileNameList 属性名称
     * @return wb
     */
    public static void addRowsToExcel(HSSFSheet sheet, List<String> fileNameList, List entryList){
        try{
            //创建内容
            if(Collections3.isNotEmpty(entryList) && Collections3.isNotEmpty(fileNameList)){
                Method method;
                String upperName;
                for (int i = 0; i < entryList.size(); i++) {
                    HSSFRow rowAdd = sheet.createRow(sheet.getLastRowNum() + 1);
                    for(int j = 0; j < fileNameList.size(); j++){
                        upperName = fileNameList.get(j).substring(0, 1).toUpperCase() + fileNameList.get(j).substring(1);
                        method = entryList.get(i).getClass().getMethod(METHOD_GET + upperName);
                        HSSFCell cell = rowAdd.createCell(j);
                        cell.setCellValue(StringUtil.toString(method.invoke(entryList.get(i))));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("信息导出excel异常", e);
        }
    }



    /**
     * 发送响应流方法
     * @param response 返回信息
     * @param name 文件名称
     */
    public static void setResponseHeader(HttpServletResponse response, String name) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = new String(name.getBytes("utf-8"), "iso-8859-1") + sdf.format(new Date()) + ".xls";
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/msexcel");
        } catch (Exception e) {
            logger.error("发送响应流格式异常", e);
        }
    }
}
