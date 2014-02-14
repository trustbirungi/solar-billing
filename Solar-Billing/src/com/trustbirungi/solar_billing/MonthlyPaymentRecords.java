package com.trustbirungi.solar_billing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MonthlyPaymentRecords {

	public JFrame frame;
	public JTable table;
	public JFrame myFrame;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	static ArrayList<String> details = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonthlyPaymentRecords window = new MonthlyPaymentRecords();
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
	public MonthlyPaymentRecords() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setUpNetworking();

		try {
			writer.println("MONTHLY_BILLING_RECORDS");
			System.out.println("DATA WRITTEN");
			writer.println("END");

			writer.flush();

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		try {
			readerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String errorMessage = "No clients found.\nRegister new clients.";
		displayResults(errorMessage);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception unused) {
			;
		}

		/*
		 * frame = new JFrame(); frame.setBounds(100, 100, 450, 300);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 */

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
				frame.dispose();
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

		JMenu mnPrint = new JMenu("Print");
		menuBar.add(mnPrint);

		JMenuItem mntmPrintCurrentDocument = new JMenuItem(
				"Print Current Document");
		mntmPrintCurrentDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.print();
				} catch (PrinterException ex) {
					ex.printStackTrace();
				}
			}
		});

		mnPrint.add(mntmPrintCurrentDocument);
	}

	private void displayResults(String errorMessage) {

		frame = new JFrame("Solar Billing | View Clients");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel nameSearchPane = new JPanel(new BorderLayout());

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setContentPane(nameSearchPane);
		frame.invalidate();
		frame.validate();

		String[] columns = { "Name", "Client Number", "Phone Number",
				"Service Level", "Service Fee", "Connection Fees", "Connection Fees Received", "Connection Fees Balance", "Month", "Payment Date",
				"Amount Paid", "Monthly Balance" };

		ArrayList<MyClients> clientDetails = new ArrayList<MyClients>();

		for (int y = 0; y < details.size(); y++) {
			MyClients client = new MyClients();

			client.name = details.get(y);
			client.client_number = details.get(++y);
			client.phone_number = details.get(++y);
			client.service_level = details.get(++y);
			client.service_fee = details.get(++y);
			client.connection_fees = details.get(++y);
			client.connection_fees_recd = details.get(++y);
			client.connection_fees_balance = details.get(++y);
			client.month = details.get(++y);
			client.paymentDate = details.get(++y);
			client.amount_paid = details.get(++y);
			client.balance = details.get(++y);
			
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
			clients[x][1] = clientDetails.get(x).client_number;
			clients[x][2] = clientDetails.get(x).phone_number;
			clients[x][3] = clientDetails.get(x).service_level;
			clients[x][4] = clientDetails.get(x).service_fee;
			clients[x][5] = clientDetails.get(x).connection_fees;
			clients[x][6] = clientDetails.get(x).connection_fees_recd;
			clients[x][7] = clientDetails.get(x).connection_fees_balance;
			clients[x][8] = clientDetails.get(x).month;
			clients[x][9] = clientDetails.get(x).paymentDate.toString();
			clients[x][10] = clientDetails.get(x).amount_paid;
			clients[x][11] = clientDetails.get(x).balance;
		}

		table = new JTable(clients, columns) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				final Component c = super
						.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(true);
				}
				return c;
			}
		};

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 1) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();

					String cellValue = (String) target.getValueAt(row, column);
					System.out.println("**** THE VALUE AT " + row + ","
							+ column + " is " + cellValue + "***");

						search(cellValue, column);
				}
			}
		});

		table.setOpaque(false);
		// table.setPreferredScrollableViewportSize(null);
		ViewPortWithWaterMark viewPortWithWaterMark = null;
		URL url = ViewClients.class.getResource("/fres-watermark.png");

		viewPortWithWaterMark = new ViewPortWithWaterMark(url);

		viewPortWithWaterMark.setView(table);
	
		JScrollPane tableContainer = new JScrollPane();
		tableContainer.setViewport(viewPortWithWaterMark);
		nameSearchPane.add(tableContainer, BorderLayout.CENTER);
	}

	private void showResults(String errorMessage) {
		myFrame = new JFrame("Solar Billing | Show Clients");
		initializeFrame();
		frame.setVisible(false);
		
		myFrame.setVisible(true);
		
		URL url_1 = ViewClients.class.getResource("/icon.png");
		myFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel resultsPane = new JPanel(new BorderLayout());

		myFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		myFrame.setContentPane(resultsPane);
		myFrame.invalidate();
		myFrame.validate();

		String[] columns = { "Name", "Client Number", "Phone Number",
				"Service Level", "Service Fee", "Connection Fees", "Connection Fees Received", "Connection Fees Balance",  "Month", "Payment Date",
				"Amount Paid", "Monthly Balance" };

		ArrayList<MyClients> clientDetails = new ArrayList<MyClients>();

		for (int y = 0; y < details.size(); y++) {
			MyClients client = new MyClients();

			client.name = details.get(y);
			client.client_number = details.get(++y);
			client.phone_number = details.get(++y);
			client.service_level = details.get(++y);
			client.service_fee = details.get(++y);
			client.connection_fees = details.get(++y);
			client.connection_fees_recd = details.get(++y);
			client.connection_fees_balance = details.get(++y);
			client.month = details.get(++y);
			client.paymentDate = details.get(++y);
			client.amount_paid = details.get(++y);
			client.balance = details.get(++y);
			
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
			clients[x][1] = clientDetails.get(x).client_number;
			clients[x][2] = clientDetails.get(x).phone_number;
			clients[x][3] = clientDetails.get(x).service_level;
			clients[x][4] = clientDetails.get(x).service_fee;
			clients[x][5] = clientDetails.get(x).connection_fees;
			clients[x][6] = clientDetails.get(x).connection_fees_recd;
			clients[x][7] = clientDetails.get(x).connection_fees_balance;
			clients[x][8] = clientDetails.get(x).month;
			clients[x][9] = clientDetails.get(x).paymentDate.toString();
			clients[x][10] = clientDetails.get(x).amount_paid;
			clients[x][11] = clientDetails.get(x).balance;
		}

		table = new JTable(clients, columns) {
			
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				final Component c = super
						.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(true);
				}
				return c;
			}
		};


		table.setOpaque(false);
		// table.setPreferredScrollableViewportSize(null);
		ViewPortWithWaterMark viewPortWithWaterMark = null;
		URL url = ViewClients.class.getResource("/fres-watermark.png");

		viewPortWithWaterMark = new ViewPortWithWaterMark(url);

		viewPortWithWaterMark.setView(table);
		
		JScrollPane tableContainer = new JScrollPane();
		tableContainer.setViewport(viewPortWithWaterMark);
		resultsPane.add(tableContainer, BorderLayout.CENTER);
	}

	private void initializeFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(myFrame);
		} catch (Exception unused) {
			;
		}

		
		JMenuBar menuBar = new JMenuBar();
		myFrame.setJMenuBar(menuBar);

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
				myFrame.dispose();
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

		JMenu mnPrint = new JMenu("Print");
		menuBar.add(mnPrint);

		JMenuItem mntmPrintCurrentDocument = new JMenuItem(
				"Print Current Document");
		mntmPrintCurrentDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.print();
				} catch (PrinterException ex) {
					ex.printStackTrace();
				}
			}
		});

		mnPrint.add(mntmPrintCurrentDocument);

	}

	protected void search(String cellValue, int column) {
		setUpNetworking();

		String methodIdentifier = "";
		
		switch(column) {
			case 0:
				methodIdentifier = "NAME_BILL";
				break;
			
			case 3:
				methodIdentifier = "SERVICE_LEVEL_BILL";
				break;
				
			case 5:
				methodIdentifier = "MONTH_BILL";
				break;
				
			case 6:
				methodIdentifier = "PAYMENT_DATE_BILL";
				break;
				
			default:
				break;
		}
		
		System.out.println("Cell Value is " + cellValue);

		try {
			writer.println(methodIdentifier);
			writer.println(cellValue);
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
		String errorMessage = "No clients found.\nRegister new clients";
		showResults(errorMessage);
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
					System.out.println("Details in run() contains: "
							+ details.get(i));

					i++;

				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	class MyClients {
		String name = "";
		String client_number = "";
		String phone_number = "";
		String service_level = "";
		String service_fee = "";
		String month = "";
		String paymentDate = "";
		String amount_paid = "";
		String balance = "";
		String connection_fees = "";
		String connection_fees_recd = "";
		String connection_fees_balance = "";
		

	}

}
