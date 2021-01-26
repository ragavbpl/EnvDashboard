package Utilities;

/**
 * @author Raghvendra Gupta
 * @author Last updated by Raghvendra Gupta: $
 * @version : $
 */


import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class PoIExcelUtils {

    private static XSSFSheet ExcelWSheet;

    private static XSSFWorkbook ExcelWBook;

    private static XSSFCell Cell;

    private static XSSFRow Rows;
    private static DataFormatter formatter = new DataFormatter();


    public static void setExcelFile(String Path, String SheetName) throws Exception {

        try {

            // Open the Excel file

            FileInputStream ExcelFile = new FileInputStream(Path);

            // Access the required test data sheet

            ExcelWBook = new XSSFWorkbook(ExcelFile);

            ExcelWSheet = ExcelWBook.getSheet(SheetName);

        } catch (Exception e) {

            throw (e);

        }

    }

    //This method is to read the test data from the Excel cell, in this we are passing parameters as Rows num and Col num

    public static Object[][] get_data(String file, String sheetName, int cols) throws Exception {
        setExcelFile(file, sheetName);
        int records = ExcelWSheet.getLastRowNum() - 1;
        int currentPosition = 2;
        Object[][] values = new Object[records][cols];
        for (int i = 0; i < records; i++, currentPosition++) {
            for (int j = 0; j < cols; j++) {
                //values[i][j] = ExcelWSheet.getRow(currentPosition).getCell(j).getStringCellValue();
                values[i][j] = formatter.formatCellValue(ExcelWSheet.getRow(currentPosition).getCell(j));

            }
        }
        //ExcelWBook.close();
        return values;


    }


    public static void write_data(String file, String sheetName, String colName, String envName, String Result) throws Exception {
        setExcelFile(file, sheetName);
        int records = ExcelWSheet.getLastRowNum();
        int finalRow = 0;
        int finalCol = 0;
        int sysGenEndCol = variablesProvider.systemGeneratedColsEnd;
        for (int i = 0; i <= records; i++) {
            if (i == 1) {
                for (int j = 0; j <= sysGenEndCol; j++) {
                    String colNameinExcel = ExcelWSheet.getRow(i).getCell(j).getStringCellValue();
                    if (colNameinExcel.equalsIgnoreCase(colName)) {
                        finalCol = j;
                        break;
                    }
                }
            }

            String envNameinExcel = ExcelWSheet.getRow(i).getCell(0).getStringCellValue();
            if (envNameinExcel.equalsIgnoreCase(envName)) {
                finalRow = i;
                break;
            }

        }
        Rows = ExcelWSheet.getRow(finalRow);
        Cell = Rows.getCell(finalCol, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (Cell == null) {

            Cell = Rows.createCell(finalCol);

            Cell.setCellValue(Result);

        } else {

            Cell.setCellValue(Result);

        }
        try {
            FileOutputStream fileOut = new FileOutputStream(file);

            ExcelWBook.write(fileOut);

            fileOut.flush();

            fileOut.close();

        } catch (Exception e) {

            throw (e);

        }


    }

    public static void cleanSystemGeneratedDataInExcel(String file, String sheetName, String envName) throws Exception {

        write_data(file, sheetName, "URL Reachable?", envName, "");
        write_data(file, sheetName, "Basic Nodepool Running?", envName, "");
        write_data(file, sheetName, "Collector Nodepool Running?", envName, "");
        write_data(file, sheetName, "PromoWS Nodepool Running?", envName, "");
        write_data(file, sheetName, "PromoNG Nodepool Running?", envName, "");
        write_data(file, sheetName, "Current SCPO Build Date", envName, "");
        write_data(file, sheetName, "Current JDA Platform Build Date", envName, "");
        write_data(file, sheetName, "Current SCPO Install Type", envName, "");
        write_data(file, sheetName, "Current Platform Installation Type", envName, "");
        write_data(file, sheetName, "SCPO Build Label", envName, "");
        write_data(file, sheetName, "Platform Build Label", envName, "");
    }

    public static void convertXLXStoCSV(String file, String sheetName) throws IOException {
        try {
            setExcelFile(file, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        XSSFRow row = null;
        XSSFCell cell = null;
        String finalCSVFile = "";
        for (int i = 1; i <= ExcelWSheet.getLastRowNum(); i++) {
            row = ExcelWSheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                //finalCSVFile += "\"" + row.getCell(j) + "\",";
                cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                //cell=formatter.formatCellValue(j,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if(i>1 && j==1)
                {
                    finalCSVFile+= "<a href='"+row.getCell(j)+"'>"+row.getCell(j)+"</a>"+",";
                    continue;
                }
                if (cell == null && j == row.getLastCellNum() - 1) {
                    finalCSVFile += row.getCell(j);
                    continue;
                } else if (cell == null) {
                    finalCSVFile += row.getCell(j) + " ,";
                } else if (j == row.getLastCellNum() - 1) {
                    finalCSVFile += row.getCell(j);
                } //else if()
                else
                    finalCSVFile += row.getCell(j) + ",";
//                if (cell == null && j == row.getLastCellNum() - 1) {
//                    finalCSVFile += row.getCell(j);
//                    continue;
//                } else if (cell == null) {
//                    finalCSVFile += row.getCell(j) + " ,";
//                } else if (j == row.getLastCellNum() - 1) {
//                    finalCSVFile += row.getCell(j);
//                } //else if()
//                else
//                    finalCSVFile += row.getCell(j) + ",";
            }
            finalCSVFile += "\n";
        }

        File myFoo = new File("src\\main\\resources\\EnvList.csv");
        boolean fileExists = myFoo.exists();
        if (!fileExists) {
            myFoo.createNewFile();
        }
        FileWriter fooWriter = new FileWriter(myFoo, false); // true to append
        // false to overwrite.
        fooWriter.write(finalCSVFile);
        fooWriter.close();
    }

    public static void copyFile() throws IOException {
        File src = new File("src/main/resources/EnvList.csv");
        File dest = new File("C:/Tools/XAMPP/htdocs/www/data/EnvList.csv");
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}