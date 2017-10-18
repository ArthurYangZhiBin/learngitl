package com.synnex.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.epiphany.shr.util.exceptions.UserException;

/**
 *
 * @author jellyh
 * @Since 2013-3-11
 */
public class SimpleJDBCUtil {
	public static DataSource getDataSource(String wmwhseID) throws UserException{
		InitialContext initialContext = null;
		try{
			initialContext = new InitialContext();
			//10.1的JNDI格式
			return (DataSource) initialContext.lookup("java:jdbc/" + wmwhseID.toUpperCase());
		}catch (NamingException e) {
			try {
				//10.2的JNDI格式
				return (DataSource) initialContext.lookup("java:/jdbc/" + wmwhseID.toUpperCase());
			} catch (NamingException e1) {
				throw new UserException(e1.getMessage(), new Object[] {});
			}
		}
	} 
	public static Object executeQuery(DataSource dataSource,String sql,RowMapper mapper,Object ... params) throws UserException{
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return executeQuery(connection,sql,mapper,params);
		} catch (SQLException e) {
			throw new UserException(e.getMessage(),new Object[]{});
		}finally {
           try {
        	   if(connection != null){
        		   connection.close();
        	   }
           } catch (SQLException e) {
               e.printStackTrace();
           }
	    }
	}
	public static Object executeQuery(Connection connection,String sql,RowMapper mapper,Object ... params) throws UserException{
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
           statement = connection.prepareStatement(sql);
           if(params != null){
        	   for(int i=0;i<params.length;i++){
        		   if(params[i] instanceof String){
        			   statement.setString(i+1, (String)params[i]);
        		   }else if(params[i] instanceof Double){
        			   statement.setDouble(i+1, (Double)params[i]);
        		   }else if(params[i] instanceof Long){
        			   statement.setLong(i+1, (Long)params[i]);
        		   }else {
        			   statement.setString(i+1, params[i].toString());
        		   }
        	   }
           }
           rs = statement.executeQuery();
           return mapper.mapping(rs);
        } catch (SQLException e) {
        	throw new UserException(e.getMessage(),new Object[]{});
        } finally {
           try {
        	   if(statement != null){
        		   statement.close();
        	   }
        	   if(rs != null){
        		   rs.close();
        	   }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        }
	}
	public static Object executeUpdate(DataSource dataSource,String sql,Object ... params) throws UserException{
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return executeUpdate(connection,sql,params);
		} catch (SQLException e) {
			throw new UserException(e.getMessage(),new Object[]{});
		}finally {
           try {
        	   if(connection != null){
        		   connection.close();
        	   }
           } catch (SQLException e) {
               e.printStackTrace();
           }
	    }
	}
	public static int executeUpdate(Connection connection,String sql,Object ... params) throws UserException{
		int count = 0;
		PreparedStatement statement = null;
        try {
           statement = connection.prepareStatement(sql);
           if(params != null){
        	   for(int i=0;i<params.length;i++){
        		   if(params[i] instanceof String){
        			   statement.setString(i+1, (String)params[i]);
        		   }else if(params[i] instanceof Double){
        			   statement.setDouble(i+1, (Double)params[i]);
        		   }else if(params[i] instanceof Long){
        			   statement.setLong(i+1, (Long)params[i]);
        		   }else {
        			   statement.setString(i+1, params[i].toString());
        		   }
        	   }
           }
           count = statement.executeUpdate();
        } catch (SQLException e) {
        	throw new UserException(e.getMessage(),new Object[]{});
        } finally {
           try {
        	   if(statement != null){
        		   statement.close();
        	   }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        }
		return count;
	}
	public static void release(Connection conn,Statement statement,ResultSet resultSet) throws UserException
	{
		try {
			if(null != resultSet){
				resultSet.close();;
			}
			if(null != statement){
				statement.close();
			}
			if(null != conn){
				conn.close();
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage(),new Object[]{});
		}
	}
}
