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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class EmployeeLogin {

	public JFrame frame;
	private JTextField textField;
	private JPasswordField textField_1;
	
	String username, password;
	
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
					EmployeeLogin window = new EmployeeLogin();
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
	public EmployeeLogin() {
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
		
		frame = new JFrame("Solar Billing | Employee Login");
		URL url_1 = ViewClients.class.getResource("/icon.png");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url_1));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblEmployeeLogin = new JLabel("EMPLOYEE LOGIN");
		lblEmployeeLogin.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_lblEmployeeLogin = new GridBagConstraints();
		gbc_lblEmployeeLogin.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmployeeLogin.gridx = 7;
		gbc_lblEmployeeLogin.gridy = 0;
		frame.getContentPane().add(lblEmployeeLogin, gbc_lblEmployeeLogin);
		
		JLabel lblUsername = new JLabel("Username");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 3;
		gbc_lblUsername.gridy = 2;
		frame.getContentPane().add(lblUsername, gbc_lblUsername);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 7;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 3;
		gbc_lblPassword.gridy = 3;
		frame.getContentPane().add(lblPassword, gbc_lblPassword);
		
		textField_1 = new JPasswordField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 7;
		gbc_textField_1.gridy = 3;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					writer.println("EMPLOYEE_LOGIN");
	                writer.println(textField.getText());
	                writer.println(new String(textField_1.getPassword()));
	                
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
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				String errorMessage = "Wrong Username or Password.\nPlease try again.";
				displayResults(errorMessage);
				
			}
		});
		
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.gridx = 5;
		gbc_btnLogin.gridy = 5;
		frame.getContentPane().add(btnLogin, gbc_btnLogin);
		
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
	}
	
	private void displayResults(String errorMessage) {
		
//		Check if any data was returned from the database
		if(details.get(0).equals("Failed Login")) {
			JOptionPane.showMessageDialog(frame, errorMessage);
			textField.setText("");
			textField_1.setText("");
		}else {
			frame.dispose();
			Starter home = new Starter();
			home.frame.setVisible(true);
		}
		
		details.clear();
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
                while (!(message = reader.readLine()).equals("null")) {
                	if(message.equals("null")) {
                		
                		break;
                	}
                	details.add(message);
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }


}
