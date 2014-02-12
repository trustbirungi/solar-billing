package com.trustbirungi.solar_billing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JViewport;

public class ViewPortWithWaterMark extends JViewport {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 3794514953164915295L;
	BufferedImage backgroundImage;
	   TexturePaint texturePaint;
	    
	   public ViewPortWithWaterMark(URL url) {
	       try{
	           backgroundImage = ImageIO.read(url);
	           Rectangle rect = new Rectangle(0,0,
	                   backgroundImage.getWidth(null),backgroundImage.getHeight(null));
	           texturePaint = new TexturePaint(backgroundImage, rect);
	       }catch(Exception es){
	            
	       }
	    }
	    
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	         
	        if(texturePaint != null) {
	                    Graphics2D g2 = (Graphics2D)g;
	                    g2.setPaint(texturePaint);
	                    g.fillRect(0,0,getWidth(),getHeight());
	 
	            }
	      }
	     
	    public void setView(JComponent view) {
	        view.setOpaque(false);
	    super.setView(view);
	 
	    }
	}