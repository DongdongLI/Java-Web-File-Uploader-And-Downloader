package JDBC;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
//generic class
public class DAO<T> {
	//can simply run the query without much effort
	private QueryRunner queryRunner=new QueryRunner();
	//the base class
	private Class<T> clazz;
	
	public DAO(){
		Type superClass=getClass().getGenericSuperclass();
		// get the super class
		if(superClass instanceof ParameterizedType){
			// and if it really has the generic class
			ParameterizedType parameterizedType=(ParameterizedType)superClass;
			Type[] typeArgs=parameterizedType.getActualTypeArguments();
			//get the generic classes
			if(typeArgs!=null && typeArgs.length>0){
				if(typeArgs[0] instanceof Class){
					clazz=(Class<T>)typeArgs[0];
				}
			}
		}
	}
	// get connection, put sql in, 
	public <E> E getForValue(String sql,Object ... args)
	{ 
		Connection connection=null;
		try {
			connection=JdbcUtils.getConnection();
			return (E)queryRunner.query(connection, sql, new ScalarHandler(), args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
			
		}
		return null;
	}
	
	public List<T> getForList(String sql,Object ... args){

		Connection connection=null;
		
		try {
			connection=JdbcUtils.getConnection();
			List<T> list=queryRunner.query(connection, sql, new BeanListHandler<>(clazz), args);
			//System.out.println("the list "+list.size());
			return list;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JdbcUtils.releaseConnection(connection);
			//System.out.println("here2");
		}
		return null;
	}
	
		
	public void update(String sql,Object ... args){
		Connection connection=null;
		
		try {
			connection=JdbcUtils.getConnection();
			queryRunner.update(connection,sql,args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public T get(String sql,Object...args){
		Connection connection=null;
		
		try {
			connection=JdbcUtils.getConnection();
			return queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
		}
		return null;
	}
}
