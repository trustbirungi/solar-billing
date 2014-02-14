package com.trustbirungi.solar_billing;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClientRegistration {

	public JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	
	String name, phone, address, district, county, village, installation_date, energy_store, service_level;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public static boolean isLoggedIn = false;
	public String s_level = "";
	public String e_store = "";
	private JTextField textField_2;
	private JTextField textField_7;
	private JTextField textField_8;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ClientRegistration window = new ClientRegistration();
				window.frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientRegistration() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		if(!isLoggedIn) {
			//frame.setVisible(false);
			//EmployeeLogin empLogin = new EmployeeLogin();
			//empLogin.frame.setVisible(true);
		}
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception unused) {
			;
        }
		
		frame = new JFrame("Solar Billing | Client Registration");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 502, 552);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblClientRegistration = new JLabel("CLIENT REGISTRATION");
		lblClientRegistration.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_lblClientRegistration = new GridBagConstraints();
		gbc_lblClientRegistration.insets = new Insets(0, 0, 5, 0);
		gbc_lblClientRegistration.gridx = 7;
		gbc_lblClientRegistration.gridy = 0;
		frame.getContentPane().add(lblClientRegistration, gbc_lblClientRegistration);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 3;
		gbc_lblName.gridy = 2;
		frame.getContentPane().add(lblName, gbc_lblName);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 7;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblClientNumber = new JLabel("Client Number");
		GridBagConstraints gbc_lblClientNumber = new GridBagConstraints();
		gbc_lblClientNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblClientNumber.gridx = 3;
		gbc_lblClientNumber.gridy = 3;
		frame.getContentPane().add(lblClientNumber, gbc_lblClientNumber);
		
		textField_8 = new JTextField();
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 0);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 7;
		gbc_textField_8.gridy = 3;
		frame.getContentPane().add(textField_8, gbc_textField_8);
		textField_8.setColumns(10);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		GridBagConstraints gbc_lblPhoneNumber = new GridBagConstraints();
		gbc_lblPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhoneNumber.gridx = 3;
		gbc_lblPhoneNumber.gridy = 4;
		frame.getContentPane().add(lblPhoneNumber, gbc_lblPhoneNumber);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 7;
		gbc_textField_1.gridy = 4;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblDistrict = new JLabel("District");
		GridBagConstraints gbc_lblDistrict = new GridBagConstraints();
		gbc_lblDistrict.insets = new Insets(0, 0, 5, 5);
		gbc_lblDistrict.gridx = 3;
		gbc_lblDistrict.gridy = 5;
		frame.getContentPane().add(lblDistrict, gbc_lblDistrict);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 7;
		gbc_textField_3.gridy = 5;
		frame.getContentPane().add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JLabel lblCounty = new JLabel("County");
		GridBagConstraints gbc_lblCounty = new GridBagConstraints();
		gbc_lblCounty.insets = new Insets(0, 0, 5, 5);
		gbc_lblCounty.gridx = 3;
		gbc_lblCounty.gridy = 6;
		frame.getContentPane().add(lblCounty, gbc_lblCounty);
		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 7;
		gbc_textField_4.gridy = 6;
		frame.getContentPane().add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JLabel lblVillage = new JLabel("Village");
		GridBagConstraints gbc_lblVillage = new GridBagConstraints();
		gbc_lblVillage.insets = new Insets(0, 0, 5, 5);
		gbc_lblVillage.gridx = 3;
		gbc_lblVillage.gridy = 7;
		frame.getContentPane().add(lblVillage, gbc_lblVillage);
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 7;
		gbc_textField_5.gridy = 7;
		frame.getContentPane().add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		JLabel lblInstallationDate = new JLabel("Installation Date");
		GridBagConstraints gbc_lblInstallationDate = new GridBagConstraints();
		gbc_lblInstallationDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblInstallationDate.gridx = 3;
		gbc_lblInstallationDate.gridy = 8;
		frame.getContentPane().add(lblInstallationDate, gbc_lblInstallationDate);
		
		textField_6 = new JTextField();
		
		textField_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField_6.setText(new DatePicker(frame).setPickedDate());
			}
		});
		
		
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 0);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 7;
		gbc_textField_6.gridy = 8;
		frame.getContentPane().add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);
		
		JLabel lblEnergyStore = new JLabel("Energy Store");
		GridBagConstraints gbc_lblEnergyStore = new GridBagConstraints();
		gbc_lblEnergyStore.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnergyStore.gridx = 3;
		gbc_lblEnergyStore.gridy = 9;
		frame.getContentPane().add(lblEnergyStore, gbc_lblEnergyStore);
		
		String[] sl_options = {"Select A Service Level", "1", "2", "3", "4"};
		
		String[] es_options = {"Select An Energy Store", "ES 1", "ES 2", "ES 3", "ES 4"};
		
		
		/*Correct Display */ JComboBox<String> comboBox_1 = new JComboBox<String>(es_options);
		//JComboBox<String> comboBox_1 = new JComboBox<String>();
		
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				e_store = (String)cb.getSelectedItem();
			}
		});
		
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 7;
		gbc_comboBox_1.gridy = 9;
		frame.getContentPane().add(comboBox_1, gbc_comboBox_1);
		
		JLabel lblServiceLevel_1 = new JLabel("Service Level");
		GridBagConstraints gbc_lblServiceLevel_1 = new GridBagConstraints();
		gbc_lblServiceLevel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblServiceLevel_1.gridx = 3;
		gbc_lblServiceLevel_1.gridy = 10;
		frame.getContentPane().add(lblServiceLevel_1, gbc_lblServiceLevel_1);
		
		/*Correct Display*/ JComboBox<String> comboBox = new JComboBox<String>(sl_options);
		//JComboBox<String> comboBox = new JComboBox<String>();
		
		comboBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				s_level = (String)cb.getSelectedItem();
			}
		});
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 10;
		frame.getContentPane().add(comboBox, gbc_comboBox);
		
		JButton btnSubmit = new JButton("Submit");
		
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					writer.println("CLIENT_REGISTRATION");
					
					writer.println(textField.getText());
					writer.println(textField_8.getText());
					writer.println(textField_1.getText());
					writer.println(textField_3.getText());
					writer.println(textField_4.getText());
					writer.println(textField_5.getText());
					writer.println(textField_6.getText());
					writer.println(e_store);
					System.out.println("e_store is " + e_store);
					
					writer.println(s_level);
					System.out.println("s_level is " + s_level);
					
					writer.println(textField_2.getText());
					writer.println(textField_7.getText());
					
	                writer.println("END");
	                
	                writer.flush();
	                
	            }
	            catch (Exception ex) {
	                ex.printStackTrace();
	            }
				
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
				textField_5.setText("");
				textField_6.setText("");
				textField_7.setText("");
				textField_8.setText("");
				
			}
		});
		
		JLabel lblConnectionFees = new JLabel("Connection Fees");
		GridBagConstraints gbc_lblConnectionFees = new GridBagConstraints();
		gbc_lblConnectionFees.insets = new Insets(0, 0, 5, 5);
		gbc_lblConnectionFees.gridx = 3;
		gbc_lblConnectionFees.gridy = 11;
		frame.getContentPane().add(lblConnectionFees, gbc_lblConnectionFees);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 7;
		gbc_textField_2.gridy = 11;
		frame.getContentPane().add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JLabel lblConncetionFeesPaid = new JLabel("Connection Fees Paid");
		GridBagConstraints gbc_lblConncetionFeesPaid = new GridBagConstraints();
		gbc_lblConncetionFeesPaid.insets = new Insets(0, 0, 5, 5);
		gbc_lblConncetionFeesPaid.gridx = 3;
		gbc_lblConncetionFeesPaid.gridy = 12;
		frame.getContentPane().add(lblConncetionFeesPaid, gbc_lblConncetionFeesPaid);
		
		textField_7 = new JTextField();
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 0);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 7;
		gbc_textField_7.gridy = 12;
		frame.getContentPane().add(textField_7, gbc_textField_7);
		textField_7.setColumns(10);
		
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmit.gridx = 5;
		gbc_btnSubmit.gridy = 16;
		frame.getContentPane().add(btnSubmit, gbc_btnSubmit);
		
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
		
		mntmEmployeeRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				EmployeeRegistration empReg = new EmployeeRegistration();
				empReg.frame.setVisible(true);
			}
		});
		
		mnWindow.add(mntmEmployeeRegistration);
		
		JMenuItem mntmUpdateClientAccount = new JMenuItem("Update Client Account");
		mnWindow.add(mntmUpdateClientAccount);
		
		
		setUpNetworking();
        
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
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
            try {
                while ((message = reader.readLine()) != null) {
                    //System.out.println("Client read " + message);
                    PrintWriter incoming = null;
					//incoming .append(message + "\n");
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
