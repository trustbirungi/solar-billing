package com.trustbirungi.solar_billing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MonthlyPayment {

	public JFrame frame;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	String name, client_number, phone_number, service_level, month, year, payment_date, amount_paid;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonthlyPayment window = new MonthlyPayment();
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
	public MonthlyPayment() {
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
		
		frame = new JFrame("Solar Billing | Monthly Payment");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 572, 506);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
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
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 7;
		gbc_textField_1.gridy = 3;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		GridBagConstraints gbc_lblPhoneNumber = new GridBagConstraints();
		gbc_lblPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhoneNumber.gridx = 3;
		gbc_lblPhoneNumber.gridy = 4;
		frame.getContentPane().add(lblPhoneNumber, gbc_lblPhoneNumber);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 7;
		gbc_textField_2.gridy = 4;
		frame.getContentPane().add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JLabel lblServiceLevel = new JLabel("Service Level");
		GridBagConstraints gbc_lblServiceLevel = new GridBagConstraints();
		gbc_lblServiceLevel.insets = new Insets(0, 0, 5, 5);
		gbc_lblServiceLevel.gridx = 3;
		gbc_lblServiceLevel.gridy = 5;
		frame.getContentPane().add(lblServiceLevel, gbc_lblServiceLevel);
		
		
		String[] sl_options = {"Select A Service Level", "S1", "S2", "S3", "S4"};
		
		String[] month_options = {"Select A Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		
		
		/*Correct Display*/ JComboBox comboBox = new JComboBox(sl_options);
		
		//JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				service_level = (String)cb.getSelectedItem();
			}
		});
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 5;
		frame.getContentPane().add(comboBox, gbc_comboBox);
		
		JLabel lblMonth = new JLabel("Month");
		GridBagConstraints gbc_lblMonth = new GridBagConstraints();
		gbc_lblMonth.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonth.gridx = 3;
		gbc_lblMonth.gridy = 6;
		frame.getContentPane().add(lblMonth, gbc_lblMonth);
		
		/*Correct Display*/ JComboBox comboBox_1 = new JComboBox(month_options);
		
		//JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				month = (String)cb.getSelectedItem();
			}
		});
		
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 7;
		gbc_comboBox_1.gridy = 6;
		frame.getContentPane().add(comboBox_1, gbc_comboBox_1);
		
		JLabel lblYear = new JLabel("Year");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 3;
		gbc_lblYear.gridy = 7;
		frame.getContentPane().add(lblYear, gbc_lblYear);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 7;
		gbc_textField_3.gridy = 7;
		frame.getContentPane().add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JLabel lblPaymentDate = new JLabel("Payment Date");
		GridBagConstraints gbc_lblPaymentDate = new GridBagConstraints();
		gbc_lblPaymentDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaymentDate.gridx = 3;
		gbc_lblPaymentDate.gridy = 8;
		frame.getContentPane().add(lblPaymentDate, gbc_lblPaymentDate);
		
		textField_4 = new JTextField();
		
		textField_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField_4.setText(new DatePicker(frame).setPickedDate());
			}
		});
		
		
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 7;
		gbc_textField_4.gridy = 8;
		frame.getContentPane().add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JLabel lblAmountPaid = new JLabel("Amount Paid");
		GridBagConstraints gbc_lblAmountPaid = new GridBagConstraints();
		gbc_lblAmountPaid.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmountPaid.gridx = 3;
		gbc_lblAmountPaid.gridy = 9;
		frame.getContentPane().add(lblAmountPaid, gbc_lblAmountPaid);
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 7;
		gbc_textField_5.gridy = 9;
		frame.getContentPane().add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					writer.println("MONTHLY_PAYMENT");
					
					writer.println(textField.getText());
					writer.println(textField_1.getText());
					writer.println(textField_2.getText());
					writer.println(service_level);
					writer.println(month);
					writer.println(textField_3.getText());
					writer.println(textField_4.getText());
					writer.println(textField_5.getText());
				
					
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
			}
		});
		
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 5, 5);
		gbc_btnSubmit.gridx = 6;
		gbc_btnSubmit.gridy = 13;
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
		mnWindow.add(mntmEmployeeRegistration);
		
		JMenuItem mntmUpdateClientAccounts = new JMenuItem("Update Client Accounts");
		mnWindow.add(mntmUpdateClientAccounts);
		
		
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
