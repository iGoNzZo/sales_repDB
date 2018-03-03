import java.sql.*;

public class project2_db {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "cs157a"; // insert your MySQL password
	private static Connection conn = null;
	private static PreparedStatement preparedStatement = null;
	private static Statement statement = null;
	private static ResultSet rs = null;
	private static String sql = null;
	private static project2_db db;

	public static void main(String[] args) throws SQLException {
		db = new project2_db();
	}
	
	public project2_db() throws SQLException {
		try	{
			Class.forName(JDBC_DRIVER); //Register JDBC Driver
			
			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    System.out.println("Connected database successfully...");
		    
			createDatabase();
			createTables();
			loadIntoTables();
			
			//STEP 4: Execute a query
		    System.out.println("Creating statement...");
		    statement = conn.createStatement();
		    System.out.println("\n==============================Create View====================================");
		    ResultSet result = createView();
		    printCustomer(result);
		    
		    //createTrigger();
		    System.out.println("\n==============================Two-Way-Join====================================");
		    twowayjoin();
		    System.out.println("\n\n==============================Three-Way-Join====================================");
		    threeWayJoin();
System.out.println("\n\n==============================Aggregation====================================");
		    System.out.println("\t Count");
		    countAggregate();
		    System.out.println("\n\t Min");
		    minAggregate();
		    System.out.println("\n\t Max");
		    maxAggregate();
		    System.out.println("\t AVG");
		    avgAggregate();
		    System.out.println("\t SUM");
		    sumAggregate();
System.out.println("\n==============================Inserting====================================");
		    insert();
		    //System.out.println("\n\tInsert2 method:");
		    //insertTwo();
		    System.out.println("\nDepartment table after insert");
		    printDepartmentTable();
		    System.out.println("\nEmployee table after insert");
		    printEmployeeTable();
		    System.out.println("\nCustomer table after insert");
		    printCustomerTable();
System.out.println("\n\n==============================Updating====================================");
		    update();
		    //System.out.println("\tUpdate2 method");
		    //updateTwo();
		    System.out.println("\nDepartment table after Update");
		    printDepartmentTable();
		    System.out.println("\nEmployee table after Update");
		    printEmployeeTable();
		    System.out.println("\ncustomer table after Update");
		    printCustomerTable();
System.out.println("\n\n==============================Deleting====================================");
		    delete();
		    //System.out.println("\tDelete2\n");
		    //deleteTwo();
		    System.out.println("\nDepartment table after Delete");
		    printDepartmentTable();
		    System.out.println("\nEmployee table after Delete");
		    printEmployeeTable();
		    System.out.println("\nCustomer table after Delete");
		    printCustomerTable();
System.out.println("\n\n==============================Transaction====================================");
		    System.out.println("With Constraint Violation: ");
		    transactionWithViolation();
		    System.out.println("No Changes.... nothing to print..\n\n Without Violation");
		    transactionNoViolation();
		    System.out.println("After Transaction Insert on Employee table");
		    printEmployeeTable();
		    System.out.println("\n");
System.out.println("\n\n==============================NestedQueries====================================");
		    nestedQueries();
			rs.close();
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(statement!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	   System.out.println("\nGoodbye!");	
	}
	
	private static void createDatabase() throws SQLException {
		String queryDrop = "DROP DATABASE IF EXISTS project2_db;";
		Statement stmtDrop = conn.createStatement();
		stmtDrop.execute(queryDrop);
		
		sql = "SET foreign_key_checks = 0; ";
		statement = conn.createStatement();
		statement .execute(sql);

		System.out.println("Creating database...");	// Create a database
		String sql = "CREATE DATABASE project2_db;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Database created successfully...");

		conn = DriverManager.getConnection(DB_URL+"project2_db", USER, PASS);
	}
	
	private static void createTables() throws SQLException	{
		String queryDrop = "DROP TABLE IF EXISTS department";
		Statement stmtDrop = conn.createStatement();
		stmtDrop.execute(queryDrop);
		String createTableSQL = "CREATE TABLE department("
				+ "dept_name VARCHAR(40) NOT NULL PRIMARY KEY, "
				+ "dept_address VARCHAR(30), "
				+ "dept_city VARCHAR(40), "
				+ "dept_state VARCHAR(40), "
				+ "dept_country VARCHAR(40), "
				+ "dept_lead VARCHAR(40)) ";
		preparedStatement = conn.prepareStatement(createTableSQL);
		preparedStatement.executeUpdate(); 
		System.out.println("Table called department created successfully...");
		
		queryDrop = "DROP TABLE IF EXISTS employee";
		stmtDrop = conn.createStatement();
		stmtDrop.execute(queryDrop);
		createTableSQL = "CREATE TABLE employee( "
				+ "f_name VARCHAR(20) NOT NULL, "
				+ "l_name VARCHAR(20) NOT NULL, "
				+ "emp_id INTEGER NOT NULL PRIMARY KEY, "
				+ "title VARCHAR(30) NOT NULL, "
				+ "birthdate VARCHAR(10) DEFAULT '1/1/1988', "
				+ "hire_date VARCHAR(10) DEFAULT '2/19/2011', "
				+ "address VARCHAR(40), "
				+ "city VARCHAR(20), "
				+ "addr_state VARCHAR(20), "
				+ "country VARCHAR(25), "
				+ "postal_code INTEGER, "
				+ "phone VARCHAR(20), "
				+ "email VARCHAR(40), "
				+ "dept VARCHAR(35), "
				+ "FOREIGN KEY (dept) REFERENCES department(dept_name)); ";
		preparedStatement = conn.prepareStatement(createTableSQL);
		preparedStatement.executeUpdate(); 
		System.out.println("Table called employee created successfully...");
		
		queryDrop = "DROP TABLE IF EXISTS customer";
		stmtDrop = conn.createStatement();
		stmtDrop.execute(queryDrop);
		createTableSQL = "CREATE TABLE customer( "
				+ "customer_id INTEGER NOT NULL PRIMARY KEY, "
				+ "f_name VARCHAR(20), "
				+ "l_name VARCHAR(20), "
				+ "address VARCHAR(40), "
				+ "city VARCHAR(20), "
				+ "addr_state VARCHAR(20), "
				+ "country VARCHAR(25), "
				+ "postal_code INTEGER, "
				+ "phone VARCHAR(20), "
				+ "email VARCHAR(40), "
				+ "support_rep INTEGER, "
				+ "FOREIGN KEY (support_rep) REFERENCES employee(emp_id) "
				+ "ON UPDATE CASCADE "
				+ "ON DELETE CASCADE); ";
		preparedStatement = conn.prepareStatement(createTableSQL);
		preparedStatement.executeUpdate(); 
		System.out.println("Table called customer created successfully...");
	}
	
	public static void loadIntoTables() throws SQLException	{
	
		String path = System.getProperty("user.dir").replace("\\", "\\\\") + "/departments.txt";
		System.out.println("Loading data from file departments.txt");
		String loadDataSQL = "LOAD DATA LOCAL INFILE '" + path + "' INTO TABLE  department "
				+ "LINES TERMINATED BY '\r\n'"; // need to add lines terminated if on windows
		preparedStatement = conn.prepareStatement(loadDataSQL);
		preparedStatement.execute(); 
		
		path = System.getProperty("user.dir").replace("\\", "\\\\") + "/employees.txt";
		System.out.println("Loading data from employees.txt");
		loadDataSQL = "LOAD DATA LOCAL INFILE '" + path + "' INTO TABLE employee "
				+ "LINES TERMINATED BY '\r\n'"; // need to add lines terminated if on windows
		preparedStatement = conn.prepareStatement(loadDataSQL);
		preparedStatement.execute();
		
		path = System.getProperty("user.dir").replace("\\", "\\\\") + "/customers.txt";
		System.out.println("Loading data from file customers.txt");
		loadDataSQL = "LOAD DATA LOCAL INFILE '" + path + "' INTO TABLE customer "
				+ "LINES TERMINATED BY '\r\n'"; // need to add lines terminated if on windows
		preparedStatement = conn.prepareStatement(loadDataSQL);
		preparedStatement.execute();
	}
	
	public static void countAggregate()	throws SQLException	{
		sql = null;
		rs = null;
		
		sql = "SELECT employee.f_name, employee.l_name, support_rep AS rep_id, COUNT(support_rep) "
				+ "FROM customer JOIN employee "
				+ "ON customer.support_rep = employee.emp_id "
				+ "GROUP BY support_rep "
				+ "ORDER BY support_rep ASC; ";
		
		try	{
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				String f_name = rs.getString("f_name");
				String l_name = rs.getString("l_name");
				int rep_id = rs.getInt("rep_id");
				int count = rs.getInt("COUNT(support_rep)");
				
				System.out.println(f_name + " "  + l_name + " " + rep_id + " " + count);
			}
		}catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void minAggregate() throws SQLException	{
		sql = null;
		rs = null;
		
		sql = "SELECT rep_id, MIN(myCount) "
				+ "FROM (SELECT support_rep AS rep_id, COUNT(support_rep) AS myCount "
					+ "FROM customer "
					+ "GROUP BY support_rep) AS countFunction; ";
		
		try	{
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				int rep_id = rs.getInt("rep_id");
				int minCount = rs.getInt("MIN(myCount)");
				
				System.out.println("sales rep  MIN(count): " + minCount);
			}
		}catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void maxAggregate() throws SQLException	{
		sql = null;
		rs = null;
		
		sql = "SELECT rep_id, MAX(myCount) "
				+ "FROM (SELECT support_rep AS rep_id, COUNT(support_rep) AS myCount "
				+ "FROM customer "
				+ "GROUP BY support_rep) AS countFunction; ";
		
		try	{
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				int rep_id = rs.getInt("rep_id");
				int maxCount = rs.getInt("MAX(myCount)");
				
				System.out.println("Sales Rep with the most Customer Contracts, Max(count): " + maxCount + "\n");
			}
		}catch (SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void avgAggregate() throws SQLException	{
		sql = null;
		rs = null;
		
		sql = "SELECT rep_id, AVG(myCount) "
				+ "FROM (SELECT support_rep AS rep_id, COUNT(support_rep) AS myCount "
					+ "FROM customer "
					+ "GROUP BY support_rep) AS countFunction; ";
		
		try	{
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				int rep_id = rs.getInt("rep_id");
				int avgCount = rs.getInt("AVG(myCount)");
				
				System.out.println("sales_reps have number of average customers , AVG(count): " + avgCount + "\n");
			}
		}catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void sumAggregate() throws SQLException	{
		sql = null;
		rs = null;
		
		sql = "SELECT rep_id, SUM(myCount) "
				+ "FROM (SELECT support_rep AS rep_id, COUNT(support_rep) AS myCount "
					+ "FROM customer "
					+ "GROUP BY support_rep) AS countFunction; ";
		
		try	{
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				int rep_id = rs.getInt("rep_id");
				int sumCount = rs.getInt("SUM(myCount)");
				
				System.out.println("total customer contracts , SUM(count): " + sumCount + "\n");
			}
		}catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void nestedQueries()	{
		sql = null;
		
		try	{
			sql = "SELECT f_name, l_name from customer "
					+ "WHERE f_name IN (SELECT f_name FROM customer WHERE f_name LIKE 'A%'); ";
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				String f_name = rs.getString("f_name");
				String l_name = rs.getString("l_name");
				
				System.out.println("First_Name: " + f_name + ", Last_Name: " + l_name);
			}
			System.out.println("\n");
			
			sql = "SELECT f_name, l_name, emp_id FROM employee "
					+ "WHERE emp_id IN (SELECT emp_id FROM employee WHERE emp_id MOD 2 = 0);";
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				String f_name = rs.getString("f_name");
				String l_name = rs.getString("l_name");
				int emp_id = rs.getInt("emp_id");
				
				System.out.println("First_Name: " + f_name + ", Last_Name: " + l_name + ", emp_id: " + emp_id);
			}
			System.out.println("\n");
			
			sql = "SELECT dept_name, dept_address, dept_city, dept_state, dept_lead "
					+ "FROM department WHERE dept_state IN (SELECT dept_state FROM department WHERE dept_state = 'California');";
			preparedStatement = conn.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())	{
				String dept_name = rs.getString("dept_name");
				String dept_address = rs.getString("dept_address");
				String dept_city = rs.getString("dept_city");
				String dept_state = rs.getString("dept_state");
				String dept_lead = rs.getString("dept_lead");
				
				System.out.println("dept_name: " + dept_name + ", address: " + dept_address + ", city: " + dept_city + ", state: " + dept_state
						+ ", dept_lead: " + dept_lead);
			}
		} catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void insert()	throws SQLException {
		sql = null;
	
		try{
			//insert 1
			sql = "INSERT INTO employee (f_name, l_name, emp_id, title,  address, city, addr_state, country, postal_code, phone, email, dept) "
					+ "VALUES ('Steven', 'Gonzalez', 120, 'Software Engineer', '1 Washington Square Hall',  'San Jose', 'California', 'United States', 95023, '1-(831)801-8637', 's.gonzalez0250@yahoo.com', 'Software'); ";
			statement.executeUpdate(sql);
			System.out.println("Inserted an employee in the employee table...");
			
			//insert 2
			sql = "INSERT INTO project2_db.customer (`customer_id`, `f_name`, `l_name`, `address`, `city`, `addr_state`, `country`, `postal_code`, `phone`, `email`) "
		      		+ "VALUES ('1989', 'Peter ', 'Pan', '52127 Del Mar Crossing', 'Arvada', 'Colorado', 'United States', '19160', '1-(201)927-1717', 'wrobertsd123@bluehost.com');";
		    statement.executeUpdate(sql);
			//STEP 2: Extract data from result set
		    System.out.println("Inserted an Customer into into the table...");
		    
		    //insert 3
		    sql = "INSERT into Department (dept_name, dept_address, dept_city, dept_state, dept_country, dept_lead) VALUES ('electronics', 'Miller Rd', 'Hollister', 'California', 'US', 'Jim Jones');";
		    statement.executeUpdate(sql);
		    System.out.println("Inserted into department table... ");
		    
		    //insert 4
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO customer(customer_id,f_name,l_name,address,city,addr_state,country,postal_code,phone,email,support_rep)" + "VALUES (1302,'Udit','Manocha','One Washington Sq', 'San Jose','California','United States',95112,'1-(424)288-0824','udit.manocha@sjsu.edu',56)");
			System.out.println("Inserted into customer table... ");
		}catch (SQLException e) {e.printStackTrace();}
	}
	
	public static void update()	throws SQLException	{
		sql = null;
		
		try	{
			//update 1
			sql = "UPDATE project2_db.customer SET `address`='111 Hoffman Trail' WHERE `customer_id`='1989';";
		    statement.executeUpdate(sql);
		    System.out.println("UPDATED an Customer's address into into the table...");
		    
		    //update 2
		    sql = "UPDATE employee SET l_name = 'Barrientos' where f_name = 'Rose'; "; 
			statement.executeUpdate(sql);
			System.out.println("UPDATED an Customer's lastname into from customer table......");
			
			//update 3
		    sql = "UPDATE department SET dept_lead = 'Babe Ruth' WHERE dept_name = 'electronics' "; 
			statement.executeUpdate(sql);
			System.out.println("UPDATED a department lead from the department table...");
			
			//update 4
			statement = conn.createStatement();
			statement.executeUpdate("UPDATE customer SET l_name = 'Sharma' WHERE customer_id = 1989");
			System.out.println("UPDATED an Customer's last name in the customer table...");
		} catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void delete()	throws SQLException	{
		sql = null;
		
		try	{
			//delete 1
		    sql = "DELETE FROM employee WHERE emp_id = 2; " ;
			statement.executeUpdate(sql);			
			System.out.println("DELETED an employee from employee table...");
			
			//delete 2
			sql = "DELETE FROM project2_db.customer WHERE `customer_id`='1989';";
		    statement.executeUpdate(sql);
			//STEP 2: Extract data from result set
		    System.out.println("DELETED an Customer from customer table...");
		    
		    //delete 3
		    sql = "DELETE FROM department WHERE `dept_name` = 'electronics';";
		    statement.executeUpdate(sql);
		    System.out.println("DELETED a department from department table...");
		    
		    //delete 4
			statement = conn.createStatement();
			statement.executeUpdate("DELETE FROM customer WHERE customer_id = 1971");
			System.out.println("DELETED an Customer from customer table...");
		} catch(SQLException e)	{	e.printStackTrace();	}
	}
	
	public static void twowayjoin() throws SQLException	{
		//STEP 1: Execute a query
		try{
	      sql = "SELECT customer.customer_id, customer.f_name, customer.l_name, employee.emp_id, employee.f_name, employee.l_name "
		      		+ "FROM project2_db.`customer` INNER JOIN project2_db.`employee` ON customer.support_rep = employee.emp_id;";
	      rs = statement.executeQuery(sql);
		    
	      //STEP 2: Extract data from result set
	      while(rs.next()){
			  //Retrieve by column name
		      int cus_id  = rs.getInt("customer.customer_id");
		      String cus_first = rs.getString("customer.f_name");
		      String cus_last = rs.getString("customer.l_name");
		      int emp_id = rs.getInt("employee.emp_id");
		      String emp_first = rs.getString("employee.f_name");
		      String emp_last = rs.getString("employee.l_name");

		      //Display values
		      System.out.print("customer_id: " + cus_id);
		      System.out.print(", customer_FirstName: " + cus_first);
		      System.out.print(", customer_LastName: " + cus_last);
		      System.out.print(", emp_id: " + emp_id);
		      System.out.print(", Emp_First: " + emp_first);
		      System.out.println(", Emp_Last: " + emp_last);
	     }
	   }catch(SQLException se)	{	se.printStackTrace();	}
	}     
	
	public static void threeWayJoin() throws SQLException	{
		sql = null;
		rs = null;
		
		try	{
			sql = "SELECT customer.f_name, customer.l_name, employee.f_name, employee.l_name, department.dept_name, department.dept_lead "
					+ "FROM employee "
					+ "JOIN customer ON employee.emp_id = customer.support_rep "
					+ "JOIN department ON department.dept_name = employee.dept "
					+ "ORDER BY dept_name; ";
			rs = statement.executeQuery(sql);
			
			while(rs.next()){
				String c_f_name = rs.getString("customer.f_name");
				String c_l_name = rs.getString("customer.l_name");
				String e_f_name = rs.getString("employee.f_name");
				String e_l_name = rs.getString("employee.l_name");
				String dept_name = rs.getString("department.dept_name");
				String dept_lead = rs.getString("department.dept_lead");
				
				System.out.println("Customer name: " + c_f_name + " " + c_l_name
						+ " , Employee name: " + e_f_name + " " + e_l_name 
						+ " , Department: " + dept_name + " , Department Lead: " + dept_lead);
			}
		}catch(SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void transactionWithViolation() throws SQLException	{
		try	{
			conn.setAutoCommit(false); //turn off auto-commit
			statement = conn.createStatement();
			
			sql = "INSERT INTO employee (f_name, l_name, emp_id, title,  address, city, addr_state, country, postal_code, phone, email, dept) "
					+ "VALUES ('Steven', NULL, 120, NULL, '1 Washington Square Hall',  'San Jose', 'California', 'United States', 95023, '1-(831)801-8637', 's.gonzalez0250@yahoo.com', 'Software');";
			statement.executeUpdate(sql);
			
			//Never reached because first insert statement trips a constraint violation and jumpt to catch block
			sql = "INSERT INTO employee (f_name, l_name, emp_id, title,  address, city, addr_state, country, postal_code, phone, email, dept) "
					+ "VALUES ('Steven', NULL, 122, NULL, '1 Washington Square Hall',  'San Jose', 'California', 'United States', 95023, '1-(831)801-8637', 's.gonzalez0250@yahoo.com', 'Software');";
			statement.executeUpdate(sql);
			
			conn.commit(); // never reached
		}catch(SQLException e)	{
			//constraint violation... so rollback changes
			System.out.println("Constraint violation - Columns 'l_name' and 'title' in employee table cannot be NULL. processing Rollback");
			System.out.println("Processing Rollback");
			conn.rollback();
			System.out.println("Rollback complete...");
		}
	}
	
	public static void transactionNoViolation() throws SQLException	{
		try	{
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			
			sql = "INSERT INTO employee (f_name, l_name, emp_id, title,  address, city, addr_state, country, postal_code, phone, email, dept) "
					+ "VALUES ('Joe', 'Rogan', 122, 'Engineer', '1 Washington Square Hall',  'San Jose', 'California', 'United States', 95023, '1-(831)801-9999', 'joe111110@yahoo.com', 'Software');";
			statement.executeUpdate(sql);
			
			conn.commit();
			System.out.println("Successful Transaction commit");
		}catch(SQLException e)	{
			//Never reached, no violation in try block
			System.out.println("Constraint violation -  processing Rollback");
			conn.rollback();
			System.out.println("Rollback complete...");
		}
	}
	
	public static void printCustomerTable()	throws SQLException {
		rs = null;
		try	{
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * from customer; ");
		}catch(SQLException e)	{	e.printStackTrace();	}
		
		while(rs.next())	{
			int customer_id = rs.getInt("customer_id");
			String f_name = rs.getString("f_name");
			String l_name = rs.getString("l_name");
			String address = rs.getString("address");
			String city = rs.getString("city");
			String state = rs.getString("addr_state");
			String country = rs.getString("country");
			int zipcode = rs.getInt("postal_code");
			String phone = rs.getString("phone");
			String email = rs.getString("email");
			int support_rep = rs.getInt("support_rep");
			
			System.out.println(customer_id + " " + f_name + " " + l_name + " " +  address + " " 
					+ city + " " + state + " " + country + " " + zipcode + " " + phone + " "
					+ email + " " + support_rep);
		}
	}
	
	public static void printEmployeeTable()	throws SQLException{
		rs = null;
		try	{
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * from employee; ");
		}catch(SQLException e)	{	e.printStackTrace();	}
		
		while(rs.next())	{
			String f_name = rs.getString("f_name");
			String l_name = rs.getString("l_name");
			int employee_id = rs.getInt("emp_id");
			String title = rs.getString("title");
			String birthdate = rs.getString("birthdate");
			String hire_date = rs.getString("hire_date");
			String address = rs.getString("address");
			String city = rs.getString("city");
			String state = rs.getString("addr_state");
			String country = rs.getString("country");
			int zipcode = rs.getInt("postal_code");
			String phone = rs.getString("phone");
			String email = rs.getString("email");
			String department = rs.getString("dept");
			
			System.out.println(f_name + " " + l_name + " " + employee_id + " " + title + " " 
					+ birthdate + " " + hire_date + " " + address + " " + city + " " + state + " "
					+ country + " " + zipcode + " " + phone + " " + email + " " + department);
		}
	}
	
	public static void printDepartmentTable() throws SQLException	{
		rs = null;
		try	{
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * from department; ");
		}catch(SQLException e)	{	e.printStackTrace();	}
		
		while(rs.next())	{
			String department_name = rs.getString("dept_name");
			String department_address = rs.getString("dept_address");
			String department_city = rs.getString("dept_city");
			String department_state = rs.getString("dept_state");
			String department_country = rs.getString("dept_country");
			String department_lead = rs.getString("dept_lead");
			
			System.out.println(department_name + " " + department_address + " " + department_city + " "
					+ department_state + " " + department_country + " " + department_lead);
		}
	}
	
	private static void printCustomer(ResultSet rs)	{
		  try {
			while(rs.next())	{
			    	System.out.print(rs.getString("customer_id") + " " + rs.getString("l_name") + "\n");
			    }
		} catch (SQLException e) {	e.printStackTrace();	}
	  }
	   
	   private static ResultSet createView()	{
		   System.out.println("creating view.....");
		   ResultSet rs = null;
		   try	{
			   statement = conn.createStatement();
			   statement.executeUpdate("CREATE OR REPLACE VIEW v AS SELECT * FROM customer WHERE customer.addr_state = 'Utah';");
			   rs = statement.executeQuery("SELECT * from v");
			   System.out.println("creatview() - complete");
		   } 
		   catch (SQLException e)	{	e.printStackTrace();	}
		   return rs;
	   }
}


