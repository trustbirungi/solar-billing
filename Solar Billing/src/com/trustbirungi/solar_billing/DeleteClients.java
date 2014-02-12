package com.trustbirungi.solar_billing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

class ColorTableModel extends AbstractTableModel {

	  Object rowData[][] = { { "1", Boolean.TRUE }, { "2", Boolean.TRUE }, { "3", Boolean.FALSE },
	     { "4", Boolean.TRUE }, { "5", Boolean.FALSE }, };
	

	  String columnNames[] = { "English", "Boolean" };

	  public int getColumnCount() {
	    return columnNames.length;
	  }

	  public String getColumnName(int column) {
	    return columnNames[column];
	  }

	  public int getRowCount() {
	    return rowData.length;
	  }

	  public Object getValueAt(int row, int column) {
	    return rowData[row][column];
	  }

	  public Class getColumnClass(int column) {
	    return (getValueAt(0, column).getClass());
	  }

	  public void setValueAt(Object value, int row, int column) {
	    rowData[row][column] = value;
	  }

	  public boolean isCellEditable(int row, int column) {
	    return (column != 0);
	  }
	}

public class DeleteClients {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteClients window = new DeleteClients();
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
	public DeleteClients() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception unused) {
			;
        }
		
		frame = new JFrame("Solar Billing | Delete Clients");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		//frame.setBounds(100, 100, 450, 300);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
String[] columns = {"Name", "Phone Number", "Address", "District", "County", "Village", "Installation Date", "Energy Store", "Service Level", "Account Balance", "Months Owed", "Next Payment Date", "Delete"};
		
		ArrayList<BillingServer> clientDetails = new ArrayList<BillingServer>();
		BillingServer ourClients = new BillingServer();
		//clientDetails = ourClients.showClients();
		
	    Object [][] clients = new Object[100][100];
	   
	    for(int x = 0; x < clientDetails.size(); x++) {
	    	clients[x][0] = clientDetails.get(x).name;
	    	clients[x][1] = clientDetails.get(x).phone_number;
	    	//clients[x][2] = clientDetails.get(x).address;
	    	clients[x][3] = clientDetails.get(x).district;
	    	clients[x][4] = clientDetails.get(x).county;
	    	clients[x][5] = clientDetails.get(x).village;
	    	clients[x][6] = clientDetails.get(x).installation_date.toString();
	    	clients[x][7] = clientDetails.get(x).energy_store;
	    	clients[x][8] = Integer.toString(clientDetails.get(x).service_level);
	    	clients[x][9] = Integer.toString(clientDetails.get(x).account_balance);
	    	clients[x][10] = Integer.toString(clientDetails.get(x).months_owed);
	    	clients[x][11] = clientDetails.get(x).next_payment_date.toString();
	    	clients[x][12] = new JCheckBox();
	    	
	    	
	    }
	    TableModel model = new ColorTableModel();
		//table = new JTable(clients, columns);
	    table = new JTable(model);
		JScrollPane tableContainer = new JScrollPane(table);
		frame.getContentPane().add(tableContainer, BorderLayout.CENTER);
		
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
		mnWindow.add(mntmHome);
		
		JMenuItem mntmEmployeeRegistration = new JMenuItem("Employee Registration");
		mnWindow.add(mntmEmployeeRegistration);
		
		JMenuItem mntmUpdateClientAccounts = new JMenuItem("Update Client Accounts");
		mnWindow.add(mntmUpdateClientAccounts);
	}

}
