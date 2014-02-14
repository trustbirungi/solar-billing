package com.trustbirungi.solar_billing;

import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class EmployeeRegistration {

	public JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JPasswordField textField_4;

	String name, phone, email, username, password;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeRegistration window = new EmployeeRegistration();
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
	public EmployeeRegistration() {
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

		frame = new JFrame("Solar Billing | Employee Registration");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblEmployeeRegistration = new JLabel("EMPLOYEE REGISTRATION");
		lblEmployeeRegistration
				.setHorizontalTextPosition(SwingConstants.CENTER);
		lblEmployeeRegistration.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployeeRegistration.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_lblEmployeeRegistration = new GridBagConstraints();
		gbc_lblEmployeeRegistration.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmployeeRegistration.gridx = 6;
		gbc_lblEmployeeRegistration.gridy = 0;
		frame.getContentPane().add(lblEmployeeRegistration,
				gbc_lblEmployeeRegistration);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		
		gbc_lblName.gridx = 3;
		gbc_lblName.gridy = 2;
		frame.getContentPane().add(lblName, gbc_lblName);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 6;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);

		JLabel lblPhoneNumber = new JLabel("Phone Number");
		lblPhoneNumber.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPhoneNumber = new GridBagConstraints();
		gbc_lblPhoneNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhoneNumber.gridx = 3;
		gbc_lblPhoneNumber.gridy = 3;
		frame.getContentPane().add(lblPhoneNumber, gbc_lblPhoneNumber);

		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 6;
		gbc_textField_1.gridy = 3;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JLabel lblEmailAddress = new JLabel("E-mail Address");
		GridBagConstraints gbc_lblEmailAddress = new GridBagConstraints();
		gbc_lblEmailAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmailAddress.gridx = 3;
		gbc_lblEmailAddress.gridy = 4;
		frame.getContentPane().add(lblEmailAddress, gbc_lblEmailAddress);

		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 6;
		gbc_textField_2.gridy = 4;
		frame.getContentPane().add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 3;
		gbc_lblUsername.gridy = 5;
		frame.getContentPane().add(lblUsername, gbc_lblUsername);

		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 6;
		gbc_textField_3.gridy = 5;
		frame.getContentPane().add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 3;
		gbc_lblPassword.gridy = 6;
		frame.getContentPane().add(lblPassword, gbc_lblPassword);

		textField_4 = new JPasswordField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 6;
		gbc_textField_4.gridy = 6;
		frame.getContentPane().add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);

		JButton btnSubmit = new JButton("Submit");

		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmployeeRegistration employee = new EmployeeRegistration();

				employee.name = textField.getText();
				employee.phone = textField_1.getText();
				employee.email = textField_2.getText();
				employee.username = textField_3.getText();
				employee.password = new String(textField_4.getPassword());
				
				try {
					writer.println("EMPLOYEE_REGISTRATION");
	                writer.println(employee.name);
	                writer.println(employee.phone);
	                writer.println(employee.email);
	                writer.println(employee.username);
	                writer.println(employee.password);
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
				

			}
		});

		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmit.gridx = 4;
		gbc_btnSubmit.gridy = 8;
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
		
		JMenuItem mntmClientRegistration = new JMenuItem("Client Registration");
		
		mntmClientRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				ClientRegistration clientReg = new ClientRegistration();
				clientReg.frame.setVisible(true);
			}
		});
		
		mnWindow.add(mntmClientRegistration);
		
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
