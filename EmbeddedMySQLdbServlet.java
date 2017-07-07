package com.nr.examples;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/EmbeddedMySQLdbServlet")
public class EmbeddedMySQLdbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SQL = "SELECT * FROM city";
	DataSource ds = null;

    public void init(ServletConfig config) throws ServletException {
    	
    		try {
				Context ctx = (Context) new InitialContext().lookup("java:comp/env");
				ds = (DataSource) ctx.lookup("jdbc/world");
				if (ds == null) throw new ServletException("ds is null!");
			} catch (NamingException e) {
				System.out.println("something went wrong at line 25 EmbeddedMySQLdbServlet");
				e.printStackTrace();
			}
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//need a connection
		Connection connect = null;
		PreparedStatement statement = null;		
		
		
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<html><head><title>JDBC in a Webapp using DataSource</title></head>");
		printWriter.println("<body><h1>JDBC in a Webapp using DataSource</h1><table width=30%>");
		//Content will go here
		try {
			connect = ds.getConnection();
			statement = connect.prepareStatement(SQL);
			ResultSet resultSet = statement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			printWriter.println("<tr>");
			for (int j = 1; j <= columnCount; j++) {
				printWriter.println("<td bgcolor=lightblue>"+resultSetMetaData.getColumnName(j)+"</td>");
			}
			
			printWriter.println("</tr>");
			while (resultSet.next()) {
				printWriter.println("<tr>");
				for(int k = 1; k <= columnCount; k++) {
					printWriter.println("<td bgcolor=lightgrey>"+resultSet.getString(k)+"</td>");
				}
				
				printWriter.println("</tr>");


				
				
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				if (connect != null) connect.close();
				if (statement != null) statement.close();
			} catch (SQLException e) {
				throw new ServletException(e.getMessage());
			}
		}
		printWriter.println("</table></body></html>");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
