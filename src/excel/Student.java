package excel;

public class Student {
	private String id; //序号 考号
	private String name; //姓名
	private String work; //岗位
	private String idNumber;  //身份证号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	@Override
	public String toString() {
		return "找到考生 [考号=" + id + ", 姓名=" + name + ", 岗位=" + work + ", 身份证号=" + idNumber + "]";
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	
}
