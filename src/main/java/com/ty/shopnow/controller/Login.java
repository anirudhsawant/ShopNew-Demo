package com.ty.shopnow.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ty.shopnow.dao.ConnectionPool;

@WebServlet("/login")
public class Login extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("uemail");
		String password = req.getParameter("upwd");
		
		Connection conn = ConnectionPool.giveConnection();
		
		try {
			PreparedStatement pstm=conn.prepareStatement("SELECT * FROM register WHERE email=?");
			pstm.setString(1, email);
			
			ResultSet rs = pstm.executeQuery();
			
			if (rs.next()) {
				String pwd = rs.getString(3);
				if (password.equals(pwd)) {
					RequestDispatcher rd=req.getRequestDispatcher("homepage.html");
					rd.forward(req, resp);
				} else {
					req.setAttribute("msg", "Invalid Password");
					RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
					rd.forward(req, resp);
				}
				
			}else {
				req.setAttribute("msg", "Not Registered");
				RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
				rd.forward(req, resp);
			}
			
		} catch (SQLException e) {
			req.setAttribute("msg", "Something went wrong please try again later");
			RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
			rd.forward(req, resp);
			e.printStackTrace();
		}
	}
}
