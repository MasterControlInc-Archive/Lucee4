/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package lucee.runtime.db.driver;

import java.lang.reflect.InvocationTargetException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import lucee.commons.lang.ExceptionUtil;
import lucee.runtime.exp.PageRuntimeException;
import lucee.runtime.op.Caster;

public class ConnectionProxy implements Connection {
	
	private Connection conn;
	private Factory factory;

	public ConnectionProxy(Factory factory,Connection conn){
		this.conn=conn;
		this.factory=factory;
	}
	
	public Connection getConnection() {
		return conn;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return factory.createStatementProxy(this,conn.createStatement());
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return factory.createStatementProxy(this,conn.createStatement(resultSetType, resultSetConcurrency));
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return factory.createStatementProxy(this,conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return factory.createCallableStatementProxy(this,conn.prepareCall(sql),sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return factory.createCallableStatementProxy(this,conn.prepareCall(sql, resultSetType, resultSetConcurrency),sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return factory.createCallableStatementProxy(this,conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql, autoGeneratedKeys),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql, columnIndexes),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql, columnNames),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql, resultSetType, resultSetConcurrency),sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return factory.createPreparedStatementProxy(this, conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability),sql);
	}
	
	
	
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return conn.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return conn.unwrap(iface);
	}

	@Override
	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		conn.close();
	}

	@Override
	public void commit() throws SQLException {
		conn.commit();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return conn.createArrayOf(typeName, elements);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return conn.createStruct(typeName, attributes);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return conn.getClientInfo(name);
	}

	@Override
	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return conn.isValid(timeout);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return conn.nativeSQL(sql);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
	}

	@Override
	public void rollback() throws SQLException {
		conn.rollback();
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		conn.setCatalog(catalog);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		conn.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		conn.setClientInfo(name, value);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		conn.setHoldability(holdability);
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		conn.setReadOnly(readOnly);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		conn.setTransactionIsolation(level);
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		conn.setTypeMap(map);
	}




	public void setSchema(String schema) throws SQLException {
		// used reflection to make sure this work with Java 5 and 6
		try {
			conn.getClass().getMethod("setSchema", new Class[]{String.class}).invoke(conn, new Object[]{schema});
		}
		catch (Throwable t) {
        	ExceptionUtil.rethrowIfNecessary(t);
			if(t instanceof InvocationTargetException && ((InvocationTargetException)t).getTargetException() instanceof SQLException)
				throw (SQLException)((InvocationTargetException)t).getTargetException();
			throw new PageRuntimeException(Caster.toPageException(t));
		}
	}

	public String getSchema() throws SQLException {
		// used reflection to make sure this work with Java 5 and 6
		try {
			return Caster.toString(conn.getClass().getMethod("getSchema", new Class[]{}).invoke(conn, new Object[]{}));
		}
		catch (Throwable t) {
        	ExceptionUtil.rethrowIfNecessary(t);
			if(t instanceof InvocationTargetException && ((InvocationTargetException)t).getTargetException() instanceof SQLException)
				throw (SQLException)((InvocationTargetException)t).getTargetException();
			throw new PageRuntimeException(Caster.toPageException(t));
		}
	}

	public void abort(Executor executor) throws SQLException {
		// used reflection to make sure this work with Java 5 and 6
		try {
			conn.getClass().getMethod("abort", new Class[]{Executor.class}).invoke(conn, new Object[]{executor});
		}
		catch (Throwable t) {
        	ExceptionUtil.rethrowIfNecessary(t);
			if(t instanceof InvocationTargetException && ((InvocationTargetException)t).getTargetException() instanceof SQLException)
				throw (SQLException)((InvocationTargetException)t).getTargetException();
			throw new PageRuntimeException(Caster.toPageException(t));
		}
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// used reflection to make sure this work with Java 5 and 6
		try {
			conn.getClass().getMethod("setNetworkTimeout", new Class[]{Executor.class,int.class}).invoke(conn, new Object[]{executor,milliseconds});
		}
		catch (Throwable t) {
        	ExceptionUtil.rethrowIfNecessary(t);
			if(t instanceof InvocationTargetException && ((InvocationTargetException)t).getTargetException() instanceof SQLException)
				throw (SQLException)((InvocationTargetException)t).getTargetException();
			throw new PageRuntimeException(Caster.toPageException(t));
		}
	}

	public int getNetworkTimeout() throws SQLException {
		// used reflection to make sure this work with Java 5 and 6
		try {
			return Caster.toIntValue(conn.getClass().getMethod("getNetworkTimeout", new Class[]{}).invoke(conn, new Object[]{}));
		}
		catch (Throwable t) {
        	ExceptionUtil.rethrowIfNecessary(t);
			if(t instanceof InvocationTargetException && ((InvocationTargetException)t).getTargetException() instanceof SQLException)
				throw (SQLException)((InvocationTargetException)t).getTargetException();
			throw new PageRuntimeException(Caster.toPageException(t));
		}
	}
}
