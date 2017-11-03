package couriersystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilityfunctions.checkCustomerClass;
import utilityfunctions.establishConnection;

/**
 * Servlet implementation class AddCustomer
 */
@WebServlet("/AddCustomer")
public class AddCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCustomer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		Connection myconn;
		ResultSet res,res1;
		PreparedStatement prep=null;
		HttpSession session=null;
		String custname,pass,dob,phonenumber,address,pin,id;
		custname=request.getParameter("name");
		pass=request.getParameter("pass");
		dob=request.getParameter("dob");
		phonenumber=request.getParameter("phonenumber");
		address=request.getParameter("address");
		pin=request.getParameter("pin");
		session=request.getSession();
		try {
			if(!checkCustomerClass.verifyCustomer(custname)) {  //if doesn't exist
				myconn=establishConnection.establishConnectionToDatabase();
				if(myconn!=null) {
					prep=myconn.prepareStatement("INSERT INTO login_info(`customer_name`, `encrypted_password`) VALUES (?,?)");
					prep.setString(1, custname);
					prep.setString(2,pass);
					if(prep.executeUpdate()>0) {
						prep=myconn.prepareStatement("select customer_id from login_info where customer_name=?");
						prep.setString(1, custname);
						res=prep.executeQuery();
						if(res.next()) {
							id=res.getString("customer_id");
							prep=myconn.prepareStatement("INSERT INTO `customer` (`customer_id`, `customer_name`, `phone_number`, `dob`, `address`, `pin_code`) VALUES (?, ?, ?,?, ?, ?)");								
							prep.setString(1, id);
							prep.setString(2, custname);
							prep.setString(3, phonenumber);
							prep.setDate(4, Date.valueOf(dob));
							prep.setString(5, address);
							prep.setString(6, pin);
							if(!(prep.executeUpdate()>0)) {
								System.out.println("Some error while inserting the values to customer table");
							}
							else {
								session.setAttribute("courier.successfullyaddedcustomer","yes");
								response.sendRedirect("adminhomepage.jsp");
							}
						}
					}
				}
			}
			else {
				session.setAttribute("courier.successfullyaddedcustomer","no");
				response.sendRedirect("addCustomer.jsp");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
