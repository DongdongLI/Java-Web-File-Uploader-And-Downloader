package JDBC;

import java.sql.Connection;
import java.util.List;


import Business.FileBean;

public class UploadFileDao extends DAO<FileBean>{
	public List<FileBean> getFiles() {
		
		Connection conn=null;
		
		try {
			conn=JdbcUtils.getConnection();
			String sql="select id,fileName,filePath,fileDesc from upload_files";
			return getForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.releaseConnection(conn);
		}
		return null;
	}
	public FileBean getFile(int id){
		
		return null;
	}
	
	public void save(List<FileBean> beans){
		Connection conn=null;
		
		try {
			conn=JdbcUtils.getConnection();
			String sql="insert into upload_files(fileName,filePath,fileDesc) values "
					+ "(?,?,?)";
			
			for(FileBean bean:beans){
				System.out.println(bean.getFileName());
				System.out.println(bean.getFilePath());
				System.out.println(bean.getFileDesc());
				update(sql,bean.getFileName(),bean.getFilePath(),bean.getFileDesc());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.releaseConnection(conn);
		}
	}
}
