package excel;
import java.io.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class demo01 {
	public static void ExcelRead() throws Exception {
		//确定要操作的是c:/1.xls
		 XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("/Users/yehaitao/Desktop/test.xlsx")));
//		HSSFWorkbook workbook = new HSSFWorkbook();
		//取第0个单元表
		XSSFSheet sheet = workbook.getSheetAt(0);
		//sheet.getPhysicalNumberOfRows();求出所有行数
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			//取一行操作
			XSSFRow row = sheet.getRow(i);
			//row.getPhysicalNumberOfCells();求出本行的单元格数，也就是列数
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
					System.out.print(row.getCell(j) + "\t");
			}
			System.out.println();
		}
	}
 
	public static void main(String[] args) throws Exception {
				ExcelRead();
		ExcelWrite();
	}
	
	
	
	public static void setBoderStyle(XSSFCellStyle style) {
		style.setBorderTop(BorderStyle.DOUBLE); // 上边框为双线
		style.setBorderRight(BorderStyle.DASH_DOT_DOT); // 右边框为虚线
		style.setBorderBottom(BorderStyle.DASHED); // 底边框为单线
		style.setBottomBorderColor((short)1);// 底边框为红色
	}
 
	public static void setFontStyle(XSSFWorkbook workbook, XSSFCellStyle style) {
		XSSFFont font = workbook.createFont();// 要设置字体样式先要创建字体
		font.setFontHeightInPoints((short) 16);// 字号
		font.setBold(true);// 加粗
		font.setItalic(true);// 斜体
		font.setColor((short)8);// 字体颜色是红色
		style.setFont(font); // 把这个设置好的字体样色压入样式
	}
 
	public static void allColumnAutoSize(XSSFSheet sheet) {
		// 遍历所有单元格，把单元格皆设置为最优列宽。
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sheet.getRow(i);
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				sheet.autoSizeColumn(j);
			}
		}
	}
 
	public static void ExcelWrite() {
 
		// 创建一个webbook，对应一个Excel文件
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 在webbook中添加一个Excel单元表sheet，并设置单元表的问题
		XSSFSheet sheet = workbook.createSheet("单元表标题");
 
		// 在sheet中添加第0行，注意老版本poi对Excel的行数列数是有限制
		XSSFRow row = sheet.createRow(0);
		// 创建一个居中样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		// 在于这个居中样式的基础上，添加表格边框样式
		setBoderStyle(style);
		// 创建第0个单元格
		XSSFCell cell = row.createCell(0);
		// 设置这个单元格的内容为“一”
		cell.setCellValue("一");
		// 设置这个单元格的格式为上面设置好的居中样式+表格边框样式
		cell.setCellStyle(style);
		// 同理创建第1个单元格并且设置好样式，下面以此类推
		cell = row.createCell(1);
		cell.setCellValue("二");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("三");
		cell.setCellStyle(style);
 
		// 创建第1行
		row = sheet.createRow(1);
		// 清空上面设置好的居中样式+表格边框样式
		style = workbook.createCellStyle();
		// 设置字体样式
		setFontStyle(workbook, style);
		cell = row.createCell(0);
		cell.setCellValue("111");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("222");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("333");
		cell.setCellStyle(style);
		// 自动调整列宽
		allColumnAutoSize(sheet);
 
		// 将文件存到指定位置
		try {
			//false代表覆盖输出
			FileOutputStream fileOutputStream = new FileOutputStream(
					"/Users/yehaitao/Desktop/demo.xlsx", false);
			workbook.write(fileOutputStream);
			//人走带门
			fileOutputStream.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}}
