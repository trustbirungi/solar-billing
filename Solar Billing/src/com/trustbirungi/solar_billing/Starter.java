package com.trustbirungi.solar_billing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class Starter {

	public JFrame frame;
	public JTable table;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public static String test;

	static ArrayList<String> details = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Starter window = new Starter();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Starter() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("Test is " + test);
		
		setUpNetworking();

		

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception unused) {
			;
		}

		frame = new JFrame("Solar Billing | Start");
		
		try {
			writer.println("SHOW DUE PAYMENTS");
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		try {
			readerThread.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 557, 596);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblWelcomeToSolar = new JLabel("WELCOME TO SOLAR BILLING");
		lblWelcomeToSolar.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_lblWelcomeToSolar = new GridBagConstraints();
		gbc_lblWelcomeToSolar.insets = new Insets(0, 0, 5, 0);
		gbc_lblWelcomeToSolar.gridx = 6;
		gbc_lblWelcomeToSolar.gridy = 0;
		frame.getContentPane().add(lblWelcomeToSolar, gbc_lblWelcomeToSolar);

		JButton btnRegisterEmployees = new JButton("REGISTER EMPLOYEES");

		btnRegisterEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				EmployeeRegistration empReg = new EmployeeRegistration();
				empReg.frame.setVisible(true);

			}
		});

		GridBagConstraints gbc_btnRegisterEmployees = new GridBagConstraints();
		gbc_btnRegisterEmployees.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegisterEmployees.gridx = 3;
		gbc_btnRegisterEmployees.gridy = 1;
		frame.getContentPane().add(btnRegisterEmployees,
				gbc_btnRegisterEmployees);

		JButton btnRegisterClients = new JButton("REGISTER CLIENTS");

		btnRegisterClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				ClientRegistration clientReg = new ClientRegistration();
				clientReg.frame.setVisible(true);
			}
		});

		GridBagConstraints gbc_btnRegisterClients = new GridBagConstraints();
		gbc_btnRegisterClients.insets = new Insets(0, 0, 5, 0);
		gbc_btnRegisterClients.gridx = 6;
		gbc_btnRegisterClients.gridy = 1;
		frame.getContentPane().add(btnRegisterClients, gbc_btnRegisterClients);

		JButton btnEmployeeLogin = new JButton("LOG OUT");

		btnEmployeeLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				EmployeeLogin empLogin = new EmployeeLogin();
				empLogin.frame.setVisible(true);
			}
		});

		GridBagConstraints gbc_btnEmployeeLogin = new GridBagConstraints();
		gbc_btnEmployeeLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnEmployeeLogin.gridx = 3;
		gbc_btnEmployeeLogin.gridy = 4;
		frame.getContentPane().add(btnEmployeeLogin, gbc_btnEmployeeLogin);

		JButton btnClientAccounts = new JButton("VIEW CLIENTS");

		btnClientAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// frame.setVisible(false);
				frame.dispose();
				ViewClients vClients = new ViewClients();
				vClients.frame.setVisible(true);
			}
		});

		GridBagConstraints gbc_btnClientAccounts = new GridBagConstraints();
		gbc_btnClientAccounts.insets = new Insets(0, 0, 5, 0);
		gbc_btnClientAccounts.gridx = 6;
		gbc_btnClientAccounts.gridy = 4;
		frame.getContentPane().add(btnClientAccounts, gbc_btnClientAccounts);

		JButton btnSearchClients = new JButton("SEARCH CLIENTS");

		btnSearchClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// frame.setVisible(false);
				frame.dispose();
				SearchClients sClients = new SearchClients();
				sClients.frame.setVisible(true);
			}
		});

		GridBagConstraints gbc_btnSearchClients = new GridBagConstraints();
		gbc_btnSearchClients.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearchClients.gridx = 3;
		gbc_btnSearchClients.gridy = 7;
		frame.getContentPane().add(btnSearchClients, gbc_btnSearchClients);

		JButton btnReceivePayment = new JButton("MONTHLY PAYMENT");

		btnReceivePayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				MonthlyPayment rPay = new MonthlyPayment();
				rPay.frame.setVisible(true);
			}
		});

		GridBagConstraints gbc_btnReceivePayment = new GridBagConstraints();
		gbc_btnReceivePayment.insets = new Insets(0, 0, 5, 0);
		gbc_btnReceivePayment.gridx = 6;
		gbc_btnReceivePayment.gridy = 7;
		frame.getContentPane().add(btnReceivePayment, gbc_btnReceivePayment);
		
		JButton btnDebtorsReport = new JButton("DEBTORS' REPORT");
		
		btnDebtorsReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				DebtorsReport debtors = new DebtorsReport();
				debtors.frame.setVisible(true);
			}
		});
		
		GridBagConstraints gbc_btnDebtorsReport = new GridBagConstraints();
		gbc_btnDebtorsReport.insets = new Insets(0, 0, 5, 5);
		gbc_btnDebtorsReport.gridx = 3;
		gbc_btnDebtorsReport.gridy = 10;
		frame.getContentPane().add(btnDebtorsReport, gbc_btnDebtorsReport);
		
		JButton btnPaidClientsReport = new JButton("PAID CLIENTS' REPORT");
		
		btnPaidClientsReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				PaidClientsReport paidClients = new PaidClientsReport();
				paidClients.frame.setVisible(true);
			}
		});
		
		GridBagConstraints gbc_btnPaidClientsReport = new GridBagConstraints();
		gbc_btnPaidClientsReport.insets = new Insets(0, 0, 5, 0);
		gbc_btnPaidClientsReport.gridx = 6;
		gbc_btnPaidClientsReport.gridy = 10;
		frame.getContentPane().add(btnPaidClientsReport, gbc_btnPaidClientsReport);
		
		JButton btnNewButton = new JButton("MONTHLY PAYMENT RECORDS");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				MonthlyPaymentRecords mpr = new MonthlyPaymentRecords();
				mpr.frame.setVisible(true);
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 13;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);

		JMenu mnFaq = new JMenu("FAQ");
		menuBar.add(mnFaq);

		JMenuItem mntmFaq = new JMenuItem("FAQ");
		mnFaq.add(mntmFaq);

		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);

		JMenuItem mntmHome = new JMenuItem("Home");

		mntmHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				Starter home = new Starter();
				home.frame.setVisible(true);
			}
		});

		mnWindow.add(mntmHome);

		JMenuItem mntmEmployeeRegistration = new JMenuItem(
				"Employee Registration");
		mnWindow.add(mntmEmployeeRegistration);

		JMenuItem mntmUpdateClientAccounts = new JMenuItem(
				"Update Client Accounts");
		mnWindow.add(mntmUpdateClientAccounts);
		
		JMenu mnLogOut = new JMenu("Log Out");
		
		menuBar.add(mnLogOut);
		
		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				EmployeeLogin login = new EmployeeLogin();
				login.frame.setVisible(true);
			}
		});
		
		mnLogOut.add(mntmLogOut);
		
		
		if (!details.isEmpty()) {
			int input = JOptionPane
					.showOptionDialog(
							null,
							"There are clients whose payment date has come.\nWould you like to view them?",
							"Payment Reminder", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);

			if (input == JOptionPane.OK_OPTION) {
				String errorMessage = "No Data Found.";
				displayResults(errorMessage);
			}
			
			
		}
		

	}

	private void displayResults(String errorMessage) {

		JPanel nameSearchPane = new JPanel(new BorderLayout());

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setContentPane(nameSearchPane);
		frame.invalidate();
		frame.validate();

		String[] columns = { "Name", "Phone Number", "District",
				"County", "Village", "Installation Date", "Energy Store",
				"Service Level", "Account Balance", "Months Owed",
				"Next Payment Date" };

		ArrayList<MyClients> clientDetails = new ArrayList<MyClients>();

		for (int y = 0; y < details.size(); y++) {
			MyClients client = new MyClients();
			System.out.println("Name in details inside loop is "
					+ details.get(y));
			// System.out.println("Loop ran at time: " + strDate);
			client.name = details.get(y);
			client.phone_number = details.get(++y);
			//client.address = details.get(++y);
			client.district = details.get(++y);
			client.county = details.get(++y);
			client.village = details.get(++y);
			client.installation_date = details.get(++y);
			client.energy_store = details.get(++y);
			client.service_level = details.get(++y);
			client.next_payment_date = details.get(++y);
			client.account_balance = details.get(++y);
			client.months_owed = details.get(++y);
			clientDetails.add(client);

		}

		details.clear();

		// Check if any data was returned from the database
		if (clientDetails.isEmpty()) {
			JOptionPane.showMessageDialog(frame, errorMessage);

			
		}

		String[][] clients = new String[clientDetails.size()][20];

		for (int x = 0; x < clientDetails.size(); x++) {
			clients[x][0] = clientDetails.get(x).name;
			clients[x][1] = clientDetails.get(x).phone_number;
			//clients[x][2] = clientDetails.get(x).address;
			clients[x][2] = clientDetails.get(x).district;
			clients[x][3] = clientDetails.get(x).county;
			clients[x][4] = clientDetails.get(x).village;
			clients[x][5] = clientDetails.get(x).installation_date.toString();
			clients[x][6] = clientDetails.get(x).energy_store;
			clients[x][7] = clientDetails.get(x).service_level;
			clients[x][8] = clientDetails.get(x).account_balance;
			clients[x][9] = clientDetails.get(x).months_owed;
			clients[x][10] = clientDetails.get(x).next_payment_date.toString();

		}

		table = new JTable(clients, columns) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        final Component c = super.prepareRenderer(renderer, row, column);
		        if (c instanceof JComponent) {
		            ((JComponent) c).setOpaque(false);
		        }
		        return c;
		    }
		};
		table.setOpaque(false);
		//table.setPreferredScrollableViewportSize(null);
		 ViewPortWithWaterMark viewPortWithWaterMark = null;
		 URL url = ViewClients.class.getResource("/fres-watermark.png");
		viewPortWithWaterMark = new ViewPortWithWaterMark(url);
		
		
         viewPortWithWaterMark.setView(table);
		//table.setBackground(Color.CYAN);
		JScrollPane tableContainer = new JScrollPane();
		tableContainer.setViewport(viewPortWithWaterMark);
		nameSearchPane.add(tableContainer, BorderLayout.CENTER);
	}

	private void setUpNetworking() {
		try {
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(
					sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			int i = 0;
			try {
				while (!(message = reader.readLine()).equals("null")) {

					if (message.equals("null")) {

						break;
					}
					details.add(message);

					i++;

				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	class MyClients {
		String name = "";
		String phone_number = "";
		//String address = "";
		String district = "";
		String county = "";
		String village = "";
		String installation_date = "";
		String energy_store = "";
		String service_level = "";
		String next_payment_date = "";
		String account_balance = "";
		String months_owed = "";
		String clientID = "";
	}

}
