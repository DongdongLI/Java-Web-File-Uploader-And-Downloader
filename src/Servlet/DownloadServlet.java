package Servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Business.FileBean;
import JDBC.UploadFileDao;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("*.download")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static UploadFileDao dao=new UploadFileDao();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath= request.getServletPath();
		//get the path in order to get that particular part of the path, like addCustomer, query.
		String methodString=servletPath.substring(1).substring(0, servletPath.length()-10);
		try {
			//find the method
			Method method=getClass().getDeclaredMethod(methodString, HttpServletRequest.class,HttpServletResponse.class);
			//invoke the method
			method.invoke(this, request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void showAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//System.out.println("show All");
		List<FileBean> beans=dao.getFiles();
		request.setAttribute("files", beans);
		
		request.getRequestDispatcher("/download.jsp").forward(request, response);
	}
	protected void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//the new download name
		String fileName=request.getParameter("name");
		// get the file path in the server
		String filePath=request.getParameter("path");
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
		// the input and the output
		OutputStream out=response.getOutputStream();
		InputStream in=new FileInputStream(filePath);
		
		byte[] buffer=new byte[1024];
		int len=0;
		while((len=in.read(buffer))!=-1){
			out.write(buffer,0,len);
		}
		in.close();
	}
}
