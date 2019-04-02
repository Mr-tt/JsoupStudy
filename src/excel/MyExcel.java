package excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyExcel {
	static Map<String,Student> data = new HashMap<String,Student>();
	
	public static void main(String[] args) throws Exception {
		read();
//		
		while(true){
			Scanner in = new Scanner(System.in);
			Student s = search(in.nextLine());
//			System.err.println(s);
			if(s!=null){
				write(s);
			}else{
				System.err.println("未查询到该考生！");
			}
		}
	
		
		
	}
	
	
	
	
	
	/**
	 * 读取excel文件，将其读入内存中以Map的形式  key-身份证号   value-student对象的所有数据
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public  static void read() throws InvalidFormatException, IOException {
		 // 获取Excel模板文件
        File file = new File("/Users/yehaitao/Desktop/test/test.xlsx");
//        File file = new File("D:\\test.xlsx");
        // 读取Excel模板
        XSSFWorkbook wb = new XSSFWorkbook(file);
        // 读取了模板内sheet的内容
        XSSFSheet sheet = wb.getSheetAt(0);
        
        
        
        // 在相应的单元格进行（读取）赋值 行列分别从0开始
        for (int rowNum = 1; rowNum < sheet.getLastRowNum()+1; rowNum++) {
        	//一行代表一个学生对象
        	Student student = new Student();
        	
        	XSSFRow row  = sheet.getRow(rowNum);
        	
//        	XSSFCell cell0 = row.getCell(0);
        	XSSFCell cell1 = row.getCell(1);
        	XSSFCell cell2 = row.getCell(2);
        	XSSFCell cell3 = row.getCell(3);
//        	student.setId(cell0.getStringCellValue());
        	student.setName(cell1.getStringCellValue());
        	student.setWork(cell2.getStringCellValue());
        	student.setIdNumber(cell3.getStringCellValue());
        	System.err.println(student.getIdNumber());
        	
        	data.put(student.getIdNumber(), student);
        }
        System.err.println(data);
        // 关闭相应的流
		wb.close();
	}
	
	/**
	 * 通过key找到对应的学生信息
	 * @param key
	 * @return
	 */
	public static Student search(String key){
		Student s = new Student();
		s = data.get(key);
		return s;
	}
	
	
	
	/**
	 * 向excel中追加数据
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public static void write(Student student) throws InvalidFormatException, IOException{
		 // 获取Excel模板文件
        File file = new File("/Users/yehaitao/Desktop/test/write.xlsx");
//        File file = new File("D:\\write.xlsx");
        // 读取Excel模板
        XSSFWorkbook wb = new XSSFWorkbook(file);
        // 读取了模板内sheet的内容
        XSSFSheet sheet = wb.getSheetAt(0);
        
        int a = sheet.getLastRowNum();
        XSSFRow row = sheet.createRow(a+1);
        System.err.println(student.getId());
        
        row.createCell(0).setCellValue(student.getId());
        row.createCell(1).setCellValue(student.getName());
        row.createCell(2).setCellValue(student.getWork());
        row.createCell(3).setCellValue(student.getIdNumber());
        row.createCell(4).setCellValue("/Users/yehaitao/Desktop/程序截图/"+student.getId()+".png");
        //备份文件
        FileOutputStream out = new FileOutputStream("/Users/yehaitao/Desktop/审核通过名单.xlsx");
//        FileOutputStream out = new FileOutputStream("D:\\write1.xlsx");
    	// 关闭相应的流
    	wb.write(out);
    	out.close();
        
        wb.close();
	}
	
}
