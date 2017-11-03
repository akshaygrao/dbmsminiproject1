package couriersystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginresponsecourier
 */
@WebServlet("/loginresponsecourier")
public class loginresponsecourier extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	File log;
	FileWriter logw;
	static BufferedWriter logwriter;
	
	public static void writelogerrors(String err) throws IOException {
		logwriter.newLine();
		logwriter.write((new java.util.Date()).toString());
		logwriter.newLine();
		logwriter.write(err);
		logwriter.close();
	}
	public loginresponsecourier() throws IOException {
		super();
		log=new File("C:\\Users\\user\\eclipse-workspace\\Dbmsminiproject\\WebContent\\server.log");
		logw=new FileWriter(log,true);
		logwriter=new BufferedWriter(logw);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		Connection myconn=null;
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String username=null,password=null,logtype=null;
		String dburl="jdbc:mysql://localhost:3306/dbms_mini_project";
		String user="root";
		 String driver = "com.mysql.jdbc.Driver";
		String dbpassword="KX117@gaway@goh90";
		HttpSession session=null;
		ResultSet res;
		PreparedStatement prep=null;
		System.out.println("got it");
		try {
			
			Class.forName(driver);
			myconn=DriverManager.getConnection(dburl, user, dbpassword);

			System.out.println("connected to database successfully");


		}
		catch(SQLException s) {
			s.printStackTrace();
			System.out.println(s.getMessage());
			writelogerrors(s.getMessage());
		} /*catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/ catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//makes sure that the homepage.jsp is requesting it
		if(request.getHeader("Referer").contains("homepage.jsp")) {
			System.out.println("got Signin request");
			if((username=request.getParameter("uname"))!=null & (password=request.getParameter("password"))!=null &(logtype=request.getParameter("logtype"))!=null) {
				try {
				if(logtype.equals(new String("Customer"))) {
					prep=myconn.prepareStatement("select encrypted_password from login_info where customer_name=?");
					prep.setString(1,username);
					res=prep.executeQuery();
					System.out.println("check customer");
					if(res.next()) {
					System.out.println(res.getString(1));
					if(res.getString(1).equals(password)) {
						session=request.getSession();
						session.setMaxInactiveInterval(60*2);
						session.setAttribute("courier.passwordisset","yes");
						session.setAttribute("courier.currentuser",username);
						System.out.println("password right");
						request.setAttribute("courier.passwordisset","yes");
						System.out.println("password right");
						response.sendRedirect("customerhomepage.jsp");
//						request.getRequestDispatcher("product.jsp").forward(request, response);
					}
					else {
/*						PrintWriter pw=response.getWriter();
						pw.println("<html><body>You Ente");*/
						System.out.println("Password wrong");
						session=request.getSession();
						session.setAttribute("courier.passwordisset","no");
						request.setAttribute("courier.passwordisset","no");
                        response.sendRedirect("homepage.jsp");
						
						}
					}
					else {
						/*PrintWriter pw=response.getWriter();
						pw.println("<html><body>You Ente");*/
						System.out.println("Password wrong");
						session=request.getSession();
						session.setAttribute("courier.passwordisset","no");
						request.setAttribute("courier.passwordisset","no");
						response.sendRedirect("homepage.jsp");
												
					}
				}
				else if(logtype.equals(new String("Employee"))) {
					prep=myconn.prepareStatement("select admin_password from admin_info where admin_name=?");
					prep.setString(1,username);
					res=prep.executeQuery();
					System.out.println("check admin");
					if(res.next()) {
					System.out.println(res.getString(1));
					if(res.getString(1).equals(password)) {
						session=request.getSession();
						session.setMaxInactiveInterval(60*2);
						session.setAttribute("courier.passwordisset","yes");
						session.setAttribute("courier.currentuser",username);
						System.out.println("password right");
						request.setAttribute("courier.passwordisset","yes");
						System.out.println("password right");
						response.sendRedirect("adminhomepage.jsp");
//						request.getRequestDispatcher("product.jsp").forward(request, response);
					}
					else {
/*						PrintWriter pw=response.getWriter();
						pw.println("<html><body>You Ente");*/
						System.out.println("Password wrong");
						session=request.getSession();
						session.setAttribute("courier.passwordisset","no");
						request.setAttribute("courier.passwordisset","no");
                        response.sendRedirect("homepage.jsp");
						
						}
					}else {
/*						PrintWriter pw=response.getWriter();
						pw.println("<html><body>You Ente");*/
						System.out.println("Password wrong");
						session=request.getSession();
						session.setAttribute("courier.passwordisset","no");
						request.setAttribute("courier.passwordisset","no");
                        response.sendRedirect("homepage.jsp");
						
						}

				}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					writelogerrors(e.getMessage());
				}

			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}
}
