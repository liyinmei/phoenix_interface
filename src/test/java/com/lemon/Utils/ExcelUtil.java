package com.lemon.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.lemon.pojo.Case;
import com.lemon.pojo.Rest;
import com.lemon.pojo.VariableConfigration;
import com.lemon.pojo.WriteBackData;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class ExcelUtil {
	//caseId与行索引map
	public static Map<String, Integer> rowIdentifierRowNumMapping=new HashMap<String, Integer>();
	//cellName与列索引map
	public static Map<String ,Integer> cellNameCellNumMapping=new HashMap<String, Integer>();
	//声明一个集合，保存writeBackData对象
	public static List<WriteBackData> writeBackDatas=new ArrayList<WriteBackData>();
	static{
		loadRowNumCellNumMappings(PropertiesUtil.getCaseFilePath(),"用例");
	}
	public static Object[][] read(String sheetName,String filePath,int startRowNum,int endRowNum,int startCellNum,int endCellNum){
		//首先获取一个Workbook对象
		InputStream iStream = null;
		//先声明数组
		Object[][] datas=null;
		try {
			iStream = new FileInputStream(new File(filePath));
			Workbook workbook=WorkbookFactory.create(iStream);
			//拿到一个表单（Sheet）对象
			Sheet sheet=workbook.getSheet(sheetName);
			//拿到要操作的Row对象（行）
			//初始化数组
			datas=new Object[endRowNum-startRowNum+1][endCellNum-startCellNum+1];
			//循环取出行
			for(int i=startRowNum;i<=endRowNum;i++){
				Row row=sheet.getRow(i-1);
				//拿到要操作的Cell对象（列）循环取出列
				for (int j = startCellNum; j <=endCellNum; j++) {
					Cell cell=row.getCell(j-1,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					//将列设置为字符串类型
					cell.setCellType(CellType.STRING);
					//取出当前列的值
					String value=cell.getStringCellValue();
					//将数据保存到数组中
					datas[i-startRowNum][j-startCellNum]=value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (iStream!=null) {
				try {
					iStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return datas;
	}
	
	/**加载行与行号（行索引），列名与列号（列索引）map映射关系
	 * @param filePath 文件路径
	 * @param sheetName 表单名
	 */
	public static void loadRowNumCellNumMappings(String filePath, String sheetName) {
		InputStream iStream=null;
		try {
		    iStream= new FileInputStream(new File(filePath));
			Workbook workbook=WorkbookFactory.create(iStream);
			Sheet sheet=workbook.getSheet(sheetName);
			//列名与列号映射
			Row row=sheet.getRow(0);
			int lastCellNum=row.getLastCellNum();
			for (int i = 0; i < lastCellNum; i++) {
				Cell cell = row.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				//获取列名
				String title=cell.getStringCellValue();
				title=title.substring(0, title.indexOf("("));
				//获取列索引
				int cellNum=cell.getAddress().getColumn();
				//添加到map中
				cellNameCellNumMapping.put(title, cellNum);
			}
			//行与行索引对应映射map
			int lastRowNum=sheet.getLastRowNum();
			for (int i = 1; i <= lastRowNum; i++) {
				//获取数据行
				Row dataRow=sheet.getRow(i);
				//获取每个数据行对应的（caseId）列
				Cell cell = dataRow.getCell(0,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				//获取行标识
				String rowIdentifier=cell.getStringCellValue();
				//获取行标识对应的行号
				int rownum=cell.getAddress().getRow();
				//添加到map中
				rowIdentifierRowNumMapping.put(rowIdentifier, rownum);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (iStream!=null) {
				try {
					iStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**从Excel读取数据
	 * @param sheetName 表单名
	 * @param filePath 文件路径
	 * @param cellNames 要读取的列名（数组）
	 * @return
	 */
	public static Object[][] read2(String sheetName,String filePath,String[] cellNames) {
		InputStream iStream=null;
		ArrayList<ArrayList<String>> groups=null;
		try {
		    iStream=new FileInputStream(new File(filePath));
			Workbook workbook=WorkbookFactory.create(iStream);
			Sheet sheet=workbook.getSheet(sheetName);
			//利用map保存列名与索引的映射关系
			Map<String,Integer>  cellNameAndCellNumMap=new HashMap<String, Integer>();
			//获取所有的标题数据以及每一个标题所在的列索引
			Row titleRow=sheet.getRow(0);
			//取出表单中列的个数
			int lastCellNum=titleRow.getLastCellNum();
			//循环取出标题行的每一列，即每一个标题
			for (int i = 0; i < lastCellNum; i++) {
				Cell cell=titleRow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				//标题
				String title=cell.getStringCellValue();
				//取出当前标题列的索引
				int cellNum=cell.getAddress().getColumn();
				cellNameAndCellNumMap.put(title, cellNum);
			//	logger.info("将{标题列："+title+"，索引："+cellNum+"}添加到map集合成功");
			}
			//取出所有行，标题行除外
			//获取最后一行的索引
			int lastRowNum=sheet.getLastRowNum();
			//groups保存多组数据即多少行数据
			groups=new ArrayList<ArrayList<String>>();
			for (int i = 1; i <= lastRowNum; i++) {
				//每一行的数据放入一个集合中
				ArrayList<String> cellValuesPerRow=new ArrayList<String>();
				Row row=sheet.getRow(i);
				if (isEmpty(row)) {
					continue;
				}
				//取出此行上面对应的列数据
				for (int j = 0; j < cellNames.length; j++) {
					String cellName=cellNames[j];
					//根据列名，从map中获取列索引
					int cellNum=cellNameAndCellNumMap.get(cellName);
					//logger.info("根据列名：【"+cellName+"】得到的列索引为：【"+cellNum+"】");
					Cell cell=row.getCell(cellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String value=cell.getStringCellValue();
					//logger.info("第"+i+"行第"+j+"列的单元格的值为：【"+value+"】");
					//将值放入到集合中
					cellValuesPerRow.add(value);
				}
				groups.add(cellValuesPerRow);
			}
		} catch (Exception e) {
			//logger.info("解析Excel用例文件出错");
			e.printStackTrace();
		}finally {
			if (iStream!=null) {
				try {
					iStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return listToArray(groups);
		
	}
	
	/**将集合转换为数组
	 * @param groups
	 * @return
	 */
	private static Object[][] listToArray(ArrayList<ArrayList<String>> groups){
		int size1=groups.size();//取出所有组数
		int size2=groups.get(0).size();//每一组的数据个数
		Object[][] datas=new Object[size1][size2];
		for (int i = 0; i < size1; i++) {
			ArrayList<String> group=groups.get(i);
			for (int j = 0; j < size2; j++) {
				String value=group.get(j);
				datas[i][j]=value;
				//logger.info("数组data["+i+"]["+j+"]=【"+value+"】");
			}
		}
		return datas;
	}
	
	/**判断是否为空行
	 * @param row
	 * @return
	 */
	private static boolean isEmpty(Row row){
		int lastCellNum=row.getLastCellNum();
		for (int i = 0; i<lastCellNum; i++) {
			Cell cell=row.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellType(CellType.STRING);
			String value=cell.getStringCellValue();
			if (value!=null&&value.trim().length()>0) {//"  "有空格情况
				return false;
			}
		}
		return true;
	}

	/**解析Excel，封装数据
	 * @param filePath 文件路径
	 * @param sheetName 表单名
	 */
	public static void loadDatas(String filePath, String sheetName,Class clazz) {
		InputStream istream =null;
		try {
			istream = new FileInputStream(new File(filePath));
			//获取工作簿
			Workbook workbook=WorkbookFactory.create(istream);
			//获取表单对象
			Sheet sheet=workbook.getSheet(sheetName);
			//获取标题行
			Row titleRow=sheet.getRow(0);
			//获取所有列个数
			int lastCellNum=titleRow.getLastCellNum();
			//用字符串数组保存截取后的标题行，用于后边拼接方法名   例：ApiName(接口名称)--->ApiName
			String [] title=new String[lastCellNum];
			for (int i = 0; i < lastCellNum; i++) {
				//
				Cell cell=titleRow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//设置单元格类型
				cell.setCellType(CellType.STRING);
				//获取单元格的值
			    String value=cell.getStringCellValue();
			    //将截取后的标题值保存到数组中
				title[i]=value.substring(0, value.indexOf("("));
			}
			//获取所有数据行
			int lastRowNum=sheet.getLastRowNum();
			for (int i = 1; i <=lastRowNum ; i++) {
				Object object=clazz.newInstance();
				Row dataRow=sheet.getRow(i);
				for (int j = 0; j < lastCellNum; j++) {
					Cell cell=dataRow.getCell(j,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String value=cell.getStringCellValue();
					//拼接方法名
					String methodName="set"+title[j];
					//通过反射获取方法对象
					Method method=clazz.getMethod(methodName, String.class);
					//调用方法，将值写入到对象中
					method.invoke(object, value);
				}
				//将接口对象添加到集合中
				//判断对象的类型
				if (object instanceof Case) {
					Case cs=(Case) object;
					CaseUtil.cases.add(cs);
				}else if (object instanceof Rest) {
					Rest rest=(Rest) object;
					RestUtil.rests.add(rest);
				}else if (object instanceof VariableConfigration) {
					VariableConfigration variableConfigration = (VariableConfigration) object;
					VariableUtil.variableConfigrations.add(variableConfigration);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (istream!=null) {
				try {
					istream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**单次写入数据到Excel
	 * @param filePath 文件路径
	 * @param sheetName 表单名
	 * @param caseId 用例编号
	 * @param cellName 列名
	 * @param result 响应数据
	 */
	public static void writeDatas(String filePath, String sheetName, String caseId, String cellName, String result) {
		InputStream iStream=null;
		OutputStream oStream=null;
		//根据caseId得到行号
		int rownum = rowIdentifierRowNumMapping.get(caseId);
		//根据cellName得到列号
		int cellnum=cellNameCellNumMapping.get(cellName);
		try {
			iStream=new FileInputStream(new File(filePath));
			Workbook workbook=WorkbookFactory.create(iStream);
			Sheet sheet=workbook.getSheet(sheetName);
			//要写入数据的行
			Row row =sheet.getRow(rownum);
			//要写入数据的列
			Cell cell = row.getCell(cellnum,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellType(CellType.STRING);
			//将值放到指定的单元格中
			cell.setCellValue(result);
			//将响应数据回写到Excel中
			oStream=new FileOutputStream(new File(filePath));
			workbook.write(oStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//关闭输入输出流
			try {
				if(oStream!=null) {
					oStream.close();
				}
				if (iStream!=null) {
					iStream.close();
				}
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	/**封装writeBackData对象并添加到集合中
	 * @param rowIdentifier 行标识
	 * @param cellName 列名
	 * @param result 响应数据
	 */
	public static void saveWriteBackDatas(String sheetName,String rowIdentifier,String cellName,String result){
		WriteBackData writeBackData=new WriteBackData();
		writeBackData.setRowIdentifier(rowIdentifier);
		writeBackData.setCellName(cellName);
		writeBackData.setResult(result);
		writeBackData.setSheetName(sheetName);
		writeBackDatas.add(writeBackData);
	}
	
	
	/**批量回写响应数据到Excel
	 * @param filePath 文件路径
	 */
	public static void batchWriteDatas(String filePath){
		InputStream iStream=null;
		OutputStream oStream=null;
		try {
			iStream = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(iStream);
			//从集合中依次取出writeBackData对象
			for (WriteBackData writeBackData : writeBackDatas) {
				Sheet sheet = workbook.getSheet(writeBackData.getSheetName());
				//通过writeBackData对象得到行标识
				String rowIdentifier = writeBackData.getRowIdentifier();
				//通过行标识与行号的映射关系得到要写入的行索引
				int rownum=rowIdentifierRowNumMapping.get(rowIdentifier);
				//通过writeBackData对象得到cellName
				String cellName = writeBackData.getCellName();
				//通过cellName与列号的映射关系得到要写入的列索引
				int cellnum=cellNameCellNumMapping.get(cellName);
				//通过writeBackData对象得到响应数据
				String result = writeBackData.getResult();
				//要写入的行
				Row row=sheet.getRow(rownum);
				//要写入的列
				Cell cell=row.getCell(cellnum,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				//将响应数据设置到对应的单元格中
				cell.setCellValue(result);
			}
			oStream = new FileOutputStream(new File(filePath));
			workbook.write(oStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (oStream!=null) {
					oStream.close();
				}
				if (iStream!=null) {
					iStream.close();
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
