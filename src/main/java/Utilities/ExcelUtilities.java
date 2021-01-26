package Utilities;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;


public class ExcelUtilities {
	private static Workbook workbook;
	private static Sheet sheet;

	public static void setWorkbook(String file) {
		try {
			ExcelUtilities.workbook = Workbook.getWorkbook(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}

	public static void setSheet(String sheetName) {
		try {
			ExcelUtilities.sheet = workbook.getSheet(sheetName);
		} catch (Exception e) {
			System.out.println("****Sheet you passed might not be present");
			e.printStackTrace();
		}
	}

	public static Object[][] get_data(String file, String sheetName, int cols) throws Exception {
		setWorkbook(file);
		setSheet(sheetName);
		int records = sheet.getRows() - 1;
		int currentPosition = 1;
		Object[][] values = new Object[records][cols];
		for (int i = 0; i < records; i++, currentPosition++) {
			for (int j = 0; j < cols; j++) values[i][j] = sheet.getCell(j, currentPosition).getContents();
		}
		workbook.close();
		return values;
	}

	public static void write_data(String file, String sheetName, String colName, String envName, int cols) throws IOException {
		setWorkbook(file);
		setSheet(sheetName);
		int totalCols = sheet.getColumns();
		int totalRows = sheet.getRows();
		for (int row = 0; row < totalRows; row++) {
			for (int col = 0; col < totalCols; col++) {
				System.out.println(sheet.getCell(col, row).getContents());
				if(sheet.getCell(col,row).getContents().equalsIgnoreCase(envName))
				{

				}

			}
			System.out.println("\n");
		}

	}
}

