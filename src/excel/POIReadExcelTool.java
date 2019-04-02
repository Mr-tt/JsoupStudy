package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import excel.Student;

public class POIReadExcelTool {

    public static void main(String[] args) throws Exception {
//        List<Student> list = readXls();
//        for(Student stu : list){
//            System.out.println(stu.getId());
//            System.out.println(stu.getName());
//            System.out.println(stu.getWork());
//            System.out.println(stu.getIdNumber());
//        }
        
    	 // 获取Excel模板文件
        File file = new File("/Users/yehaitao/Desktop/test.xlsx");
        // 读取Excel模板
        XSSFWorkbook wb = new XSSFWorkbook(file);
        // 读取了模板内sheet的内容
        XSSFSheet sheet = wb.getSheetAt(0);
        // 在相应的单元格进行（读取）赋值 行列分别从0开始
        System.err.println(sheet.getLastRowNum());
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
        	System.err.print(sheet.getRow(rowNum).getCell(0)+"-");
        	System.err.print(sheet.getRow(rowNum).getCell(1)+"-");
        	System.err.println(sheet.getRow(rowNum).getCell(2));
        	
        }
    }
    
    
    public static void  read() throws Exception{
        // 获取Excel模板文件
        File file = new File("/Users/yehaitao/Desktop/test.xlsx");
        // 读取Excel模板
        XSSFWorkbook wb = new XSSFWorkbook(file);
        // 读取了模板内sheet的内容
        XSSFSheet sheet = wb.getSheetAt(0);
        // 在相应的单元格进行（读取）赋值 行列分别从0开始
        
        for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
        	
        	XSSFCell cell = sheet.getRow(0).getCell(0);
        	cell.setCellValue("张*****");
        	// 修改模板内容导出新模板
        	FileOutputStream out = new FileOutputStream("/Users/yehaitao/Desktop/座位分布图.xlsx");
        	// 关闭相应的流
        	wb.write(out);
        	out.close();
        	wb.close();
        	
        }
    }
    
    
    
    
    
    /**
     * HSSFWorkbook只能用于2007以下的版本
     * @return
     * @throws Exception
     */
    public static List<Student> readXls() throws Exception {
        InputStream is = new FileInputStream("/Users/yehaitao/Desktop/test.xlsx");

        HSSFWorkbook excel = new HSSFWorkbook(is);
        Student stu = null;
        List<Student> list = new ArrayList<Student>();
        
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < excel.getNumberOfSheets(); numSheet++) {
            HSSFSheet sheet = excel.getSheetAt(numSheet);
            if (sheet == null)
                continue;
            // 循环行Row
            for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow row = sheet.getRow(rowNum);
                if (row == null)
                    continue;
                stu = new Student();
                // 循环列Cell
                // 0学号 1姓名 2年龄 3生日
                /*System.out.println((int)cell0.getNumericCellValue());
                System.out.println(cell1.getStringCellValue());
                System.out.println((int)cell2.getNumericCellValue());
                System.out.println(cell3.getStringCellValue());*/
                /**console:
                 *         1
                        张三
                        1997-03-12
                        李四
                        1996-08-12
                 */
                HSSFCell cell0 = row.getCell(0);
                if (cell0 == null)
                    continue;
                stu.setId(cell0.getStringCellValue());
                HSSFCell cell1 = row.getCell(1);
                if (cell1 == null)
                    continue;
                stu.setName(cell1.getStringCellValue());
                HSSFCell cell2 = row.getCell(2);
                if (cell2 == null)
                    continue;
                stu.setWork(cell2.getStringCellValue());
                HSSFCell cell3 = row.getCell(3);
                if (cell3 == null)
                    continue;
                stu.setIdNumber(cell3.getStringCellValue());
                list.add(stu);
            }
        }

        return list;
    }


}