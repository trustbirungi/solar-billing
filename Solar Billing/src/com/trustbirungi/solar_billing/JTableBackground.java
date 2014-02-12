package com.trustbirungi.solar_billing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class JTableBackground {
	 
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("JTable Watermark Example");
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception unused) {
			;
		}
        
        String data[][] = {{"001", "Ivan", "HR", "Manager"},
            {"002", "Jenny", "HR", "Asst. Manager"},
            {"003", "Thomas", "Marketing", "Manager"},
            {"004", "Charles", "Marketing", "Sales Man"},
            {"005", "Tessa", "Research", "Engineer"},
            {"006", "Alan", "Research", "Engineer"},
            {"007", "Maria", "Research", "Engineer"},
            {"008", "Steve", "Research", "Manager"},
            {"009", "Terry", "Admin", "Manager"},
            {"010", "Robert", "Admin", "Executive"},
            {"011", "John", "Marketing", "Executive"},
            {"012", "Mark", "Marketing", "Marketing"},
            {"013", "Anna", "Research", "Consultant"},};
        String col[] = {"ID", "Name", "Department", "Role"};
        final JTable table = new JTable(data, col) {
 
            /**
			 * 
			 */
			private static final long serialVersionUID = -88990263830919846L;

			@Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                final Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false);
                }
                return c;
            }
        };
        table.setRowHeight(25);
        
        table.setOpaque(true);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.WHITE);
     //   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
          ViewPortWithWaterMark viewPortWithWaterMark = new ViewPortWithWaterMark(new File("D:\\Pictures\\Fres_Splash.png").toURL());
            viewPortWithWaterMark.setView(table);
            JScrollPane scroll = new JScrollPane();
            scroll.setViewport(viewPortWithWaterMark);
            frame.getContentPane().add(scroll);
          
        frame.pack();
        //frame.setSize(360, 320);
        frame.setVisible(true);
    }
}
