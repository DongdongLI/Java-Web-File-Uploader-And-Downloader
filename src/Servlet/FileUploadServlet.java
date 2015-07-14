package Servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Business.FileAppProperty;
import Business.FileBean;
import Exception.InvalidExtException;
import JDBC.UploadFileDao;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/fileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String FILE_PATH="/WEB-INF/files/";
	private static UploadFileDao dao=new UploadFileDao();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//all the setup
		ServletFileUpload upload=getServletFileUpload();
		//the page after this
		String path=null;
		try {
			Map<String, FileItem> uploadFiles=new HashMap<String, FileItem>();
			//get all the items
			List<FileItem> items=upload.parseRequest(request);
			//0 build FileBean set
			List<FileBean> beans=buildFieldUploadBeans(items,uploadFiles);
			//1
			//2 extend name
			//System.out.println(beans);//file name wrong
			validateExtName(beans);
			//3 check the size(just get the result by the Exception)
			//4 upload
			upload(uploadFiles);
			//5 save to database
			saveBeans(beans);
			
			path="/success.jsp";
		} catch (Exception e) {
			path="/upload.jsp";
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		finally{
			request.getRequestDispatcher(path).forward(request, response);
		}
	}
	
	private void saveBeans(List<FileBean> beans) {
		System.out.println("saveBeans: ");
		dao.save(beans);
	}



	private void upload(Map<String, FileItem> uploadFiles) throws IOException {
		for(Map.Entry<String, FileItem> uploadFile: uploadFiles.entrySet()){
			String path=uploadFile.getKey();
			FileItem item=uploadFile.getValue();
			
			upload(path,item.getInputStream());
		}
	}



	private void upload(String path, InputStream inputStream) throws IOException{
		OutputStream out=out = new FileOutputStream(path);
		
		byte [] buffer=new byte[1024];
		int len=0;
		
		while((len=inputStream.read(buffer))!=-1){
			out.write(buffer, 0, len);
		}
			inputStream.close();
			out.close();
		
	}



	private void validateExtName(List<FileBean> beans) {
		String exts=FileAppProperty.getInstance().getProperty("exts");
		List<String> extList=Arrays.asList(exts.split(","));
		//System.out.println(extList);
		for(FileBean bean:beans){
			String fileName=bean.getFileName();//file name not correct here
			String extName=fileName.substring(fileName.indexOf(".")+1);
			if(!extList.contains(extName)){
				throw new InvalidExtException(fileName+"has invalid extention name");
			}
		}
	}



	private List<FileBean> buildFieldUploadBeans(List<FileItem> items, Map<String, FileItem> uploadFiles) {
		//save detail of files
		List<FileBean> beans=new ArrayList<FileBean>();
		
		//1 traverse items, get desc (Map<descX>)
		Map<String, String> descs=new HashMap<String,String>();
		
		for(FileItem item:items){
			// if a form field
			if(item.isFormField())
			{
				//String a=item.getFieldName();
				//String b=item.getString();
				descs.put(item.getFieldName(), item.getString());
				//System.out.println(a+" "+b);
			}
		}
		//System.out.println(descs);
		//2 traverse again, get FileItem
			//create a FileBean for each one of them
			// get fileName, filePath, and get teh desc from the map
		for(FileItem item:items){
			// it is a file
			if(!item.isFormField()){
				String fieldName=item.getFieldName();
				String index=fieldName.substring(fieldName.length()-1);
				
				String fileName=item.getName();
				String desc=descs.get("desc"+index);
				String filePath=getFilePath(fileName);
				
				FileBean bean=new FileBean(fileName,filePath,desc);
				beans.add(bean);
				
				uploadFiles.put(filePath, item);
				//System.out.println(fieldName+" "+index+" "+fileName+" "+desc
				//		+" "+filePath);
			}
		}
		
		return beans;
	}



	private String getFilePath(String fileName) {
		String extName=fileName.substring(fileName.lastIndexOf("."));
		
		Random rand=new Random();
		int randNum=rand.nextInt(100000);
		
		//String filePath="/Users/lidongdong/Documents/MATLAB/"+System.currentTimeMillis()+randNum+extName;//getServletContext().getRealPath(FILE_PATH)+"/"+System.currentTimeMillis()+randNum+extName;
		//String filePath="/Users/lidongdong/Documents/workspaceJ2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/FileUpload/WEB-INF/files/"+randNum+extName;//no exception but nothing
		String filePath=getServletContext().getRealPath(FILE_PATH)+System.currentTimeMillis()+extName;
		return filePath;
	}



	private ServletFileUpload getServletFileUpload(){
		String exts=FileAppProperty.getInstance().getProperty("exts");
		String fileSize=FileAppProperty.getInstance().getProperty("fileMaxSize");
		String totalSize=FileAppProperty.getInstance().getProperty("totalMaxSize");
		
		DiskFileItemFactory factory=new DiskFileItemFactory();
		
		factory.setSizeThreshold(1024*500);
		File tempDir=new File("/Users/lidongdong/Desktop/temp");
		factory.setRepository(tempDir);
		
		ServletFileUpload upload=new ServletFileUpload(factory);
		
		upload.setSizeMax(Integer.parseInt(totalSize));
		upload.setFileSizeMax(Integer.parseInt(fileSize));
		return upload;
	}
	
}
