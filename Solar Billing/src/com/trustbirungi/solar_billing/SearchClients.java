package com.trustbirungi.solar_billing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class SearchClients {

	JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

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
					SearchClients window = new SearchClients();
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
	public SearchClients() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setUpNetworking();
		
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception unused) {
			;
        }
		
		frame = new JFrame("Solar Billing | Search Clients");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnSearchByName = new JButton("Search By Name");
		
		btnSearchByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchByName();
			}
		});
		
		GridBagConstraints gbc_btnSearchByName = new GridBagConstraints();
		gbc_btnSearchByName.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchByName.gridx = 1;
		gbc_btnSearchByName.gridy = 1;
		frame.getContentPane().add(btnSearchByName, gbc_btnSearchByName);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 3;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnSearchByService = new JButton("Search By Service Level");
		
		btnSearchByService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchByServiceLevel();
			}
		});
		
		GridBagConstraints gbc_btnSearchByService = new GridBagConstraints();
		gbc_btnSearchByService.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchByService.gridx = 1;
		gbc_btnSearchByService.gridy = 4;
		frame.getContentPane().add(btnSearchByService, gbc_btnSearchByService);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 6;
		frame.getContentPane().add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JButton btnSearchByEnergy = new JButton("Search By Energy Store");
		
		btnSearchByEnergy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchByEnergyStore();
			}
		});
		
		GridBagConstraints gbc_btnSearchByEnergy = new GridBagConstraints();
		gbc_btnSearchByEnergy.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchByEnergy.gridx = 1;
		gbc_btnSearchByEnergy.gridy = 7;
		frame.getContentPane().add(btnSearchByEnergy, gbc_btnSearchByEnergy);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 9;
		frame.getContentPane().add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JButton btnSearchByDistrict = new JButton("Search By District");
		btnSearchByDistrict.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				searchByDistrict();
			}
		});
		
		GridBagConstraints gbc_btnSearchByDistrict = new GridBagConstraints();
		gbc_btnSearchByDistrict.gridx = 1;
		gbc_btnSearchByDistrict.gridy = 10;
		frame.getContentPane().add(btnSearchByDistrict, gbc_btnSearchByDistrict);
		
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
		
		JMenuItem mntmEmployeeRegistration = new JMenuItem("Employee Registration");
		mnWindow.add(mntmEmployeeRegistration);
		
		JMenuItem mntmUpdateClientAccounts = new JMenuItem("Update Client Accounts");
		mnWindow.add(mntmUpdateClientAccounts);
		
		JMenuItem mntmSearchClient = new JMenuItem("Search Clients");
		
		mntmSearchClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				SearchClients newClient = new SearchClients();
				newClient.frame.setVisible(true);
			}
		});
		
		mnWindow.add(mntmSearchClient);
		
		JMenu mnNewMenu = new JMenu("Print");
		menuBar.add(mnNewMenu);
		
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
		
		mnNewMenu.add(mntmPrintCurrentDocument);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar.add(menuBar_1);
	}
	
	public void searchByServiceLevel() {
		try {
			writer.println("SEARCH BY SERVICE LEVEL");
			writer.println(textField_1.getText());
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
		String errorMessage = "A client with that service level was not found.\n Try service level: 1, 2, 3 or 4.";
		displayResults(errorMessage);
		
		
		
	}

	private void displayResults(String errorMessage) {
		JPanel nameSearchPane = new JPanel(new BorderLayout());
		
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setContentPane(nameSearchPane);
		frame.invalidate();
		frame.validate();
		
		String[] columns = {"Name", "Client Number", "Phone Number", "District", "County", "Village", "Installation Date", "Energy Store", "Service Level"};
		
		ArrayList<MyClients> clientDetails = new ArrayList<MyClients>();
	   
	    for(int y = 0; y < details.size(); y++) {
			MyClients client = new MyClients();
			System.out.println("Name in details inside loop is " + details.get(y));
			//System.out.println("Loop ran at time: " + strDate);
			
			client.name = details.get(y);
			client.client_number = details.get(++y);
			client.phone_number = details.get(++y);
			client.district = details.get(++y);
			client.county = details.get(++y);
			client.village = details.get(++y);
			client.installation_date = details.get(++y);
			client.energy_store = details.get(++y);
			client.service_level = details.get(++y);
			
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
	    	clients[x][1] = clientDetails.get(x).client_number;
	    	clients[x][2] = clientDetails.get(x).phone_number;
	    	clients[x][3] = clientDetails.get(x).district;
	    	clients[x][4] = clientDetails.get(x).county;
	    	clients[x][5] = clientDetails.get(x).village;
	    	clients[x][6] = clientDetails.get(x).installation_date.toString();
	    	clients[x][7] = clientDetails.get(x).energy_store;
	    	clients[x][8] = clientDetails.get(x).service_level;
	    	    	
	    }
		
		table = new JTable(clients, columns){
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
	
	
	public void searchByName() {
		try {
			writer.println("SEARCH BY NAME");
			writer.println(textField.getText());
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
		
		String errorMessage = "A client with that name was not found. \nEnter another name and try again.";
		displayResults(errorMessage);
	}
	
	
	public void searchByEnergyStore() {
		try {
			writer.println("SEARCH BY ENERGY STORE");
			writer.println(textField_2.getText());
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
		
		String errorMessage = "No client with that energy store was found. \nEnter another energy store and try again.";
		displayResults(errorMessage);
	}
	
	public void searchByDistrict() {
		try {
			writer.println("SEARCH BY DISTRICT");
			writer.println(textField_3.getText());
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
		
		String errorMessage = "No client with that district was found. \nEnter another district and try again.";
		displayResults(errorMessage);
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
		String client_number = "";
		String phone_number = "";
		String district = "";
		String county = "";
		String village = "";
		String installation_date = "";
		String energy_store = "";
		String service_level = "";
	}


}
