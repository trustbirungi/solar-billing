package com.trustbirungi.solar_billing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.time.DateUtils;

public class BillingServer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2245537117283746379L;

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/solar_billing";

	// Database credentials
	static final String USER = "db_username";_ //Change this to your database credentials
	static final String PASS = "db_password"; //Change this to your database credentials

	@SuppressWarnings("rawtypes")
	ArrayList clientOutputStreams;
	ArrayList<String> empData = new ArrayList<String>();
	ArrayList<String> clientData = new ArrayList<String>();
	ArrayList<String> loginData = new ArrayList<String>();
	ArrayList<String> paymentData = new ArrayList<String>();
	ArrayList<HashMap<String, String>> loggedInEmployees = new ArrayList<HashMap<String, String>>();

	// Client Data

	String name = "";
	String client_number = "";
	String phone_number = "";
	String district = "";
	String county = "";
	String village = "";
	Date installation_date = null;
	String energy_store = "";
	int service_level = 0;
	Date next_payment_date = null;
	int account_balance = 0;
	int months_owed = 0;
	int clientID = 0;
	int service_fee = 0;
	String month = "";
	Date paymentDate = null;
	int amount_paid = 0;
	int balance = 0;
	String serviceLevel = "";
	String searchData = "";
	int connection_fees = 0;
	int connection_fees_recd = 0;
	int connection_fees_balance = 0;
	
	static final int SL_1_FEE = 23000;
	static final int SL_2_FEE = 34000;
	static final int SL_3_FEE = 43000;
	static final int SL_4_FEE = 58000;

	int service_level_fee = 0;

	public static boolean isLoggedIn = false;
	public static boolean duePayments = false;

	// Socket sock;
	Socket clientSocket;
	PrintWriter writer;

	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;

		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(
						sock.getInputStream());
				reader = new BufferedReader(isReader);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			//monthlyAccountUpdate();
			String message;
			try {
				message = reader.readLine();
				if (message != null
						&& message.contains("EMPLOYEE_REGISTRATION")) {

					while ((message = reader.readLine()) != null) {

						empData.add(message);

						if (message.contains("END")) {
							addEmployee(empData);
							empData.clear();
						}

					}
					tellEveryone(message);
				} else if (message != null
						&& message.contains("CLIENT_REGISTRATION")) {
					while ((message = reader.readLine()) != null) {

						clientData.add(message);

						if (message.contains("END")) {
							addClient(clientData);
							clientData.clear();
						}

					}
				} else if (message != null
						&& message.contains("EMPLOYEE_LOGIN")) {
					while ((message = reader.readLine()) != null) {

						loginData.add(message);

						if (message.contains("END")) {
							login(loginData);
							loginData.clear();
						}
					}
				} else if (message != null
						&& message.contains("MONTHLY_PAYMENT")) {
					while ((message = reader.readLine()) != null) {

						paymentData.add(message);

						if (message.contains("END")) {
							monthlyPayment(paymentData);
							paymentData.clear();
						}
					}
				} else if (message != null && message.contains("SHOW_CLIENTS")) {

					showClients();

				} else if (message != null
						&& message.contains("SEARCH BY SERVICE LEVEL")) {
					while ((message = reader.readLine()) != null) {
						System.out.println("The service level is " + message);
						System.out.println("SEARCH BY SERVICE LEVEL");
						System.out.println("DATA READ");
						if (message.contains("1") || message.contains("2")
								|| message.contains("3")
								|| message.contains("4")) {

							showClients(Integer.parseInt(message));

						} else {
							PrintWriter writer = new PrintWriter(
									clientSocket.getOutputStream());
							writer.println("null");
							writer.flush();
						}
					}

				} else if (message != null
						&& message.contains("SEARCH BY NAME")) {
					while ((message = reader.readLine()) != null) {
						System.out.println("The NAME is " + message);
						System.out.println("SEARCH BY NAME");
						System.out.println("DATA READ");

						showClients(message);
					}

				} else if (message != null
						&& message.contains("SEARCH BY ENERGY STORE")) {
					while ((message = reader.readLine()) != null) {
						System.out.println("The ENERGY STORE is " + message);
						System.out.println("SEARCH BY ENERGY STORE");
						System.out.println("DATA READ");

						searchByEnergyStore(message);
					}

				} else if (message != null
						&& message.contains("SEARCH BY DISTRICT")) {
					while ((message = reader.readLine()) != null) {
						System.out.println("The DISTRICT is " + message);
						System.out.println("SEARCH BY DISTRICT");
						System.out.println("DATA READ");

						searchByDistrict(message);
					}

				} else if (message != null
						&& message.contains("SHOW DUE PAYMENTS")) {
					paymentReminder();
				} else if (message != null
						&& message.contains("DEBTORS REPORT")) {
					debtorsReport();
				} else if (message != null
						&& message.contains("PAID CLIENTS")) {
					paidClients();
				}else if (message != null
						&& message.contains("MONTHLY_BILLING_RECORDS")) {
					showBillingRecords();
				}else if (message != null
						&& message.contains("NAME_BILL")) {
					while ((message = reader.readLine()) != null) {
						System.out.println("The Name is " + message);
						searchData = message;
						System.out.println("Search Data = " + searchData);
						showBillingRecords(searchData);
						
					}
				}else {

					System.out.println("NO REQUEST WAS SENT ");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		EmployeeLogin.main(null);
		new BillingServer().go();
		 
		
	}

	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(8000);
			while (true) {
				clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(
						clientSocket.getOutputStream());
				clientOutputStreams.add(writer);

				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isLoggedIn() {
		if (isLoggedIn) {
			return true;
		} else {
			return false;
		}
	}

	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void addEmployee(ArrayList<String> empData) {
		if (empData.get(0).equals("EMPLOYEE_REGISTRATION")) {
			empData.remove(0);
		}

		System.out.println("******ADD EMPLOYEE HAS BEEN CALLED*****");
		Connection conn = null;

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement preparedStatement = conn
					.prepareStatement("insert into employees(name, phone_number, email, username, password) values (?, ?, ?, ?, ?)");

			// Parameters start with 1

			preparedStatement.setString(1, empData.get(0));
			preparedStatement.setString(2, empData.get(1));
			preparedStatement.setString(3, empData.get(2));
			preparedStatement.setString(4, empData.get(3));
			preparedStatement.setString(5, empData.get(4));

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addClient(ArrayList<String> clientData) {
		if (clientData.get(0).equals("CLIENT_REGISTRATION")) {
			clientData.remove(0);
		}

		System.out.println("******ADD CLIENT HAS BEEN CALLED*****");

		Connection conn = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO clients (name, client_number, phone_number, district, county, village, installation_date, energy_store, service_level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			// Parameters start with 1

			preparedStatement.setString(1, clientData.get(0));
			preparedStatement.setString(2, clientData.get(1));
			preparedStatement.setString(3, clientData.get(2));
			preparedStatement.setString(4, clientData.get(3));
			preparedStatement.setString(5, clientData.get(4));
			preparedStatement.setString(6, clientData.get(5));

			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date myDate = null;
			try {
				myDate = formatter.parse(clientData.get(6));
			} catch (ParseException e) {

				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());

			preparedStatement.setDate(7, sqlDate);

			preparedStatement.setString(8, clientData.get(7));
			preparedStatement.setInt(9, Integer.parseInt(clientData.get(8)));

			preparedStatement.executeUpdate();

			PreparedStatement prepACCID = conn
					.prepareStatement("UPDATE clients SET accountID = clientID");
			prepACCID.executeUpdate();

			// Add data to accounts table

			PreparedStatement accountPrep = conn
					.prepareStatement("INSERT INTO client_accounts(client_number, service_level, installation_date, connection_fees, connection_fees_recd, conn_fees_balance, next_payment_date) values (?, ?, ?, ?, ?, ?, ?)");
			
			accountPrep.setString(1, clientData.get(1));
			accountPrep.setInt(2, Integer.parseInt(clientData.get(8)));
			

			DateFormat date_formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date install_Date = null;
			try {
				install_Date = date_formatter.parse(clientData.get(6));
			} catch (ParseException e) {

				e.printStackTrace();
			}
			java.sql.Date mysqlDate = new java.sql.Date(install_Date.getTime());

			accountPrep.setDate(3, mysqlDate);
			
			accountPrep.setInt(4, Integer.parseInt(clientData.get(9)));
			accountPrep.setInt(5, Integer.parseInt(clientData.get(10)));
			accountPrep.setInt(6, (Integer.parseInt(clientData.get(9))) - (Integer.parseInt(clientData.get(10))));

			// Calculate next payment date
			Date next_pay_date = DateUtils.addDays(install_Date, 30);
			java.sql.Date my_pay_date = new java.sql.Date(
					next_pay_date.getTime());
			accountPrep.setDate(7, my_pay_date);

			accountPrep.executeUpdate();

			PreparedStatement prepClientID = conn
					.prepareStatement("UPDATE client_accounts SET clientID = accountID");
			prepClientID.executeUpdate();

			// Set the first date of payment

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void login(ArrayList<String> loginData) {
		if (loginData.get(0).equals("EMPLOYEE_LOGIN")) {
			loginData.remove(0);
		}

		String username = "";
		String password = "";
		String name = "";
		int employeeID = 0;
		String email = "";
		String phone_number = "";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM employees WHERE username = ? AND password = ?";

			preparedStatement = conn.prepareStatement(query);

			// Parameters start with 1
			preparedStatement.setString(1, loginData.get(0));
			preparedStatement.setString(2, loginData.get(1));

			loginData.clear();

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				username = rs.getString("username");
				password = rs.getString("password");
				email = rs.getString("email");
				phone_number = rs.getString("phone_number");
				name = rs.getString("name");
				employeeID = rs.getInt("employeeID");

				System.out.println("Username: " + username);
				System.out.println("Password: " + password);

				isLoggedIn = true;

			}

			if (isLoggedIn) {
				HashMap<String, String> emp = new HashMap<String, String>();
				emp.put(name, username);

				loggedInEmployees.add(emp);

				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println(name);
					writer.println(username);

					writer.println("null");
					writer.flush();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("***LOGIN FAILURE***");
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("Failed Login");

					writer.println("null");
					writer.flush();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showClients() {
		System.out.println("SHOW CLIENTS HAS BEEN CALLED");
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		clientDetails.clear();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
		
				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showClients(int service_level) {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		System.out.println("THE SERVICE LEVEL PASSED TO THE DATABASE IS "
				+ service_level);
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients WHERE service_level = ?";

			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, service_level);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");

				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("CLIENT DETAILS HAS A SIZE OF "
					+ clientDetails.size());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showClients(String name) {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients WHERE name = '" + name + "'";

			System.out.println(name);
			preparedStatement = conn.prepareStatement(query);
			// preparedStatement.setString(1, name);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");

				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("CLIENT DETAILS HAS A SIZE OF "
					+ clientDetails.size());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchByEnergyStore(String energy_store) {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients WHERE clients.energy_store = ?";

			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, energy_store);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");

				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("CLIENT DETAILS HAS A SIZE OF "
					+ clientDetails.size());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);
				
			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchByDistrict(String district) {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients WHERE district = ?";

			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, district);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
				
				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("CLIENT DETAILS HAS A SIZE OF "
					+ clientDetails.size());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void debtorsReport() {
		System.out.println("SHOW CLIENTS HAS BEEN CALLED");
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		clientDetails.clear();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.phone_number = rs.getString("phone_number");
				//client.address = rs.getString("address");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
				client.next_payment_date = rs.getDate("next_payment_date");
				client.account_balance = rs.getInt("account_balance");
				client.months_owed = rs.getInt("months_owed");

				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			for (int i = 0; i < clientDetails.size(); i++) {
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).phone_number);
				//writer.println(clientDetails.get(i).address);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);
				writer.println(clientDetails.get(i).next_payment_date);
				writer.println(clientDetails.get(i).account_balance);
				writer.println(clientDetails.get(i).months_owed);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void paidClients() {
		System.out.println("SHOW CLIENTS HAS BEEN CALLED");
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		clientDetails.clear();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients, client_accounts WHERE clients.clientID = client_accounts.clientID AND client_accounts.months_owed <= 0";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.phone_number = rs.getString("phone_number");
				//client.address = rs.getString("address");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
				client.next_payment_date = rs.getDate("next_payment_date");
				client.account_balance = rs.getInt("account_balance");
				client.months_owed = rs.getInt("months_owed");

				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			for (int i = 0; i < clientDetails.size(); i++) {
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).phone_number);
				//writer.println(clientDetails.get(i).address);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);
				writer.println(clientDetails.get(i).next_payment_date);
				writer.println(clientDetails.get(i).account_balance);
				writer.println(clientDetails.get(i).months_owed);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void monthlyPayment(ArrayList<String> paymentData) {
		int sl_fee = 0;
		String month = "";
		int balance = 0;
		
		if (paymentData.get(0).equals("MONTHLY_PAYMENT")) {
			paymentData.remove(0);
		}
		System.out.println("******MONTHLY PAYMENT HAS BEEN CALLED*****");
		Connection conn = null;

		System.out.println("NAME SENT THROUGH THE FORM IS "
				+ paymentData.get(0));
		
		
//		Payment calculations
		if(paymentData.get(3).equals("S1")) {
			sl_fee = SL_1_FEE;
		}else if(paymentData.get(3).equals("S2")) {
			sl_fee = SL_2_FEE;
		}else if(paymentData.get(3).equals("S3")) {
			sl_fee = SL_3_FEE;
		}else if(paymentData.get(3).equals("S4")) {
			sl_fee = SL_4_FEE;
		}
		
		month = paymentData.get(4) + "_" + paymentData.get(5);
		
		balance = (sl_fee - (Integer.parseInt(paymentData.get(7))));
		
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO billing_tracking (name, client_number, phone_number, service_level, service_fee, month, payment_date, amount_paid, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			// Parameters start with 1

			preparedStatement.setString(1, paymentData.get(0));
			preparedStatement.setString(2, paymentData.get(1));
			preparedStatement.setString(3, paymentData.get(2));
			preparedStatement.setString(4, paymentData.get(3));
			preparedStatement.setInt(5, sl_fee);
			preparedStatement.setString(6, month);
			
			DateFormat date_formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date payment_date = null;
			try {
				payment_date = date_formatter.parse(paymentData.get(6));
			} catch (ParseException e) {

				e.printStackTrace();
			}
			java.sql.Date mysqlDate = new java.sql.Date(payment_date.getTime());

			preparedStatement.setDate(7, mysqlDate);
			
			preparedStatement.setInt(8, Integer.parseInt(paymentData.get(7)));
			
			preparedStatement.setInt(9, balance);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	public void paymentReminder() {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients, client_accounts WHERE clients.clientID = client_accounts.clientID AND client_accounts.months_owed > 0";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.phone_number = rs.getString("phone_number");
				//client.address = rs.getString("address");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
				client.next_payment_date = rs.getDate("next_payment_date");
				client.account_balance = rs.getInt("account_balance");
				client.months_owed = rs.getInt("months_owed");

				clientDetails.add(client);

				duePayments = true;
				//isDuePayment();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

			System.out.println("CLIENT DETAILS HAS A SIZE OF "
					+ clientDetails.size());
			for (int i = 0; i < clientDetails.size(); i++) {
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).phone_number);
				//writer.println(clientDetails.get(i).address);
				writer.println(clientDetails.get(i).district);
				writer.println(clientDetails.get(i).county);
				writer.println(clientDetails.get(i).village);
				writer.println(clientDetails.get(i).installation_date);
				writer.println(clientDetails.get(i).energy_store);
				writer.println(clientDetails.get(i).service_level);
				writer.println(clientDetails.get(i).next_payment_date);
				writer.println(clientDetails.get(i).account_balance);
				writer.println(clientDetails.get(i).months_owed);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isDuePayment() {
		return duePayments;
	}

	public ArrayList<BillingServer> monthlyAccountUpdate() {
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM clients, client_accounts WHERE clients.clientID = client_accounts.clientID";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				BillingServer client = new BillingServer();

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();

				client.clientID = rs.getInt("clientID");
				client.name = rs.getString("name");
				client.phone_number = rs.getString("phone_number");
				//client.address = rs.getString("address");
				client.district = rs.getString("district");
				client.county = rs.getString("county");
				client.village = rs.getString("village");
				client.installation_date = rs.getDate("installation_date");
				client.energy_store = rs.getString("energy_store");
				client.service_level = rs.getInt("service_level");
				client.next_payment_date = rs.getDate("next_payment_date");
				client.account_balance = rs.getInt("account_balance");
				client.months_owed = rs.getInt("months_owed");

				if (date.compareTo(client.next_payment_date) >= 0) {

					switch (client.service_level) {
					case 1:
						client.account_balance -= SL_1_FEE;
						System.out.println("Account balance updated = "
								+ client.account_balance);
						break;
					case 2:
						client.account_balance -= SL_2_FEE;
						System.out.println("Account balance updated = "
								+ client.account_balance);
						break;
					case 3:
						client.account_balance -= SL_3_FEE;
						System.out.println("Account balance updated = "
								+ client.account_balance);
						break;
					case 4:
						client.account_balance -= SL_4_FEE;
						System.out.println("Account balance updated = "
								+ client.account_balance);
						break;
					}

					PreparedStatement prepstmt = conn
							.prepareStatement("UPDATE client_accounts SET account_balance = ?, next_payment_date = ?, months_owed = ? WHERE clientID = ?");

					prepstmt.setInt(1, client.account_balance);

					Date next_pay_date = DateUtils.addDays(
							client.next_payment_date, 31);
					java.sql.Date my_pay_date = new java.sql.Date(
							next_pay_date.getTime());

					prepstmt.setDate(2, my_pay_date);

					client.months_owed += 1;
					prepstmt.setInt(3, client.months_owed);

					prepstmt.setInt(4, client.clientID);

					prepstmt.executeUpdate();

					clientDetails.add(client);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return clientDetails;

	}
	
	public void showBillingRecords() {
		System.out.println("SHOW BILLING RECORDS HAS BEEN CALLED");
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		clientDetails.clear();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM billing_tracking, client_accounts WHERE billing_tracking.client_number = client_accounts.client_number ORDER BY billing_tracking.name ";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.serviceLevel = rs.getString("service_level");
				client.service_fee = rs.getInt("service_fee");
				client.connection_fees = rs.getInt("connection_fees");
				client.connection_fees_recd = rs.getInt("connection_fees_recd");
				client.connection_fees_balance = rs.getInt("conn_fees_balance");
				client.month = rs.getString("month");
				client.paymentDate = rs.getDate("payment_date");
				client.amount_paid = rs.getInt("amount_paid");
				client.balance = rs.getInt("balance");
				
				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).serviceLevel);
				writer.println(clientDetails.get(i).service_fee);
				writer.println(clientDetails.get(i).connection_fees);
				writer.println(clientDetails.get(i).connection_fees_recd);
				writer.println(clientDetails.get(i).connection_fees_balance);
				writer.println(clientDetails.get(i).month);
				writer.println(clientDetails.get(i).paymentDate);
				writer.println(clientDetails.get(i).amount_paid);
				writer.println(clientDetails.get(i).balance);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void showBillingRecords(String searchData) {
		System.out.println("SHOW BILLING RECORDS HAS BEEN CALLED");
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		clientDetails.clear();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "SELECT * FROM billing_tracking, client_accounts WHERE billing_tracking.name = '"
					+ searchData + "' AND billing_tracking.client_number = client_accounts.client_number ORDER BY name";

			preparedStatement = conn.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs == null) {
				try {
					PrintWriter writer = new PrintWriter(
							clientSocket.getOutputStream());
					writer.println("null");
					writer.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			while (rs.next()) {
				BillingServer client = new BillingServer();

				client.name = rs.getString("name");
				client.client_number = rs.getString("client_number");
				client.phone_number = rs.getString("phone_number");
				client.serviceLevel = rs.getString("service_level");
				client.service_fee = rs.getInt("service_fee");
				client.connection_fees = rs.getInt("connection_fees");
				client.connection_fees_recd = rs.getInt("connection_fees_recd");
				client.connection_fees_balance = rs.getInt("conn_fees_balance");
				client.month = rs.getString("month");
				client.paymentDate = rs.getDate("payment_date");
				client.amount_paid = rs.getInt("amount_paid");
				client.balance = rs.getInt("balance");
				
				clientDetails.add(client);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			for (int i = 0; i < clientDetails.size(); i++) {
				
				writer.println(clientDetails.get(i).name);
				writer.println(clientDetails.get(i).client_number);
				writer.println(clientDetails.get(i).phone_number);
				writer.println(clientDetails.get(i).serviceLevel);
				writer.println(clientDetails.get(i).service_fee);
				writer.println(clientDetails.get(i).connection_fees);
				writer.println(clientDetails.get(i).connection_fees_recd);
				writer.println(clientDetails.get(i).connection_fees_balance);
				writer.println(clientDetails.get(i).month);
				writer.println(clientDetails.get(i).paymentDate);
				writer.println(clientDetails.get(i).amount_paid);
				writer.println(clientDetails.get(i).balance);

			}

			writer.println("null");
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
