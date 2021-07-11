package ru.itis;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class EducationCentreDataSource implements DataSource {

	private Connection connection;
	private final Properties properties;

	public EducationCentreDataSource(Properties properties) {
		this.properties = properties;
		openConnection();
	}

	private void openConnection(){
		String url = this.properties.getProperty("db.url");
		String user = this.properties.getProperty("db.user");
		String password = this.properties.getProperty("db.password");
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()){
			openConnection();
		}
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password){
		throw new NotImplementedException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new NotImplementedException();
	}
}
