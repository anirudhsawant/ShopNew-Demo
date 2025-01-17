package com.ty.shopnow.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ty.shopnow.dao.ConnectionPool;

@WebServlet("/register")
public class Register extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("uname");
		String email = req.getParameter("uemail");
		String password = req.getParameter("upassword");
		
		Connection connection = ConnectionPool.giveConnection();
		
		String reg="INSERT INTO register VALUES(?,?,?)";
		
		try {
			PreparedStatement pstm = connection.prepareStatement(reg);
			pstm.setString(1, name);
			pstm.setString(2, email);
			pstm.setString(3, password);
			
			int update = pstm.executeUpdate();
			
			ConnectionPool.submitConnection(connection);
			
			RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
			if (update>0) {
				req.setAttribute("msg", "Registered Successfully");
				rd.forward(req, resp);
			} else {
				req.setAttribute("msg", "Registration Failed");
				rd.forward(req, resp);
			}
			
		} catch (SQLException e) {
			req.setAttribute("msg", "Already Registered");
			RequestDispatcher rd=req.getRequestDispatcher("register.jsp");
			rd.forward(req, resp);
			e.printStackTrace();
		}
	}
}
