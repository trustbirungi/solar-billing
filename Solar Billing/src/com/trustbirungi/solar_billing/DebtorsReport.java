package com.trustbirungi.solar_billing;

import java.awt.BorderLayout;
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
import javax.swing.table.TableCellRenderer;

public class DebtorsReport {

	public JFrame frame;
	public JTable table;

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
					ViewClients window = new ViewClients();
					
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
	public DebtorsReport() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setUpNetworking();
		
		try {
			writer.println("DEBTORS REPORT");
	
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
			// TODO Auto-generated catch block
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
				//frame.setVisible(false);
				frame.dispose();
				Starter home = new Starter();
				home.frame.setVisible(true);
			}
		});

		mnWindow.add(mntmHome);

		JMenuItem mntmEmployeeRegistration = new JMenuItem(
				"Employee Registration");
		mnWindow.add(mntmEmployeeRegistration);

		JMenuItem mntmUpdateClientAccount = new JMenuItem(
				"Update Client Account");
		mnWindow.add(mntmUpdateClientAccount);
		
		JMenu mnPrint = new JMenu("Print");
		menuBar.add(mnPrint);
		
		JMenuItem mntmPrintCurrentDocument = new JMenuItem("Print Current Document");
		
		mntmPrintCurrentDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					table.print();
					
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		String[] columns = {"Name", "Phone Number", "District", "County", "Village", "Installation Date", "Energy Store", "Service Level", "Account Balance", "Months Owed", "Next Payment Date"};
		
		ArrayList<MyClients> clientDetails = new ArrayList<MyClients>();
	   
	    for(int y = 0; y < details.size(); y++) {
			MyClients client = new MyClients();

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
		
//		Check if any data was returned from the database
		if(clientDetails.isEmpty()) {
			JOptionPane.showMessageDialog(frame, errorMessage);
			
			frame.setVisible(false);
			SearchClients search = new SearchClients();
			search.frame.setVisible(true);
		}
		
	    String[][] clients = new String[clientDetails.size()][20];
	   
	    for(int x = 0; x < clientDetails.size(); x++) {
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
                	
                	if(message.equals("null")) {
                		
                		break;
                	}
                	details.add(message);
                	//System.out.println("Details in run() contains: " + details.get(i));
                	
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
