package com.ttianjun.common.kit.image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream; 
public class ImageKit {
	 /* 
     * 根据尺寸图片居中裁剪 
     */  
     public static void cutCenterImage(String src,String dest,int w,int h) throws IOException{   
         Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int x = Math.abs((reader.getWidth(imageIndex)-w)/2);
         int y = Math.abs((reader.getHeight(imageIndex)-h)/2);
         Rectangle rect = new Rectangle(x, y, w, h);    
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));             
    
     }  
    /* 
     * 图片裁剪二分之一 
     */  
     public static void cutHalfImage(String src,String dest) throws IOException{   
         Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int width = reader.getWidth(imageIndex)/2;   
         int height = reader.getHeight(imageIndex)/2;   
         Rectangle rect = new Rectangle(width/2, height/2, width, height);   
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));     
     }  
    /* 
     * 图片裁剪通用接口 
     */  
  
    public static void cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{   
           Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");   
           ImageReader reader = iterator.next();   
           InputStream in=new FileInputStream(src);  
           ImageInputStream iis = ImageIO.createImageInputStream(in);   
           reader.setInput(iis, true);   
           ImageReadParam param = reader.getDefaultReadParam();   
           Rectangle rect = new Rectangle(x, y, w,h);    
           param.setSourceRegion(rect);   
           BufferedImage bi = reader.read(0,param);     
           ImageIO.write(bi, "jpg", new File(dest));             
  
    }   
    /* 
     * 图片缩放 
     */  
    public static void zoomImage(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    /* 
     * 图片等比缩放  以最小边为准
     */  
    public static void zoomImageDb(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        double scale = wr>hr?hr:wr;
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }
    /* 
     * 图片等比缩放  以最大边为准
     */  
    public static void zoomImageDb2(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        double scale = wr>hr?wr:hr;
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), null);  
        itemp = ato.filter(bufImg, null);  
        
        try {  
            ImageIO.write((BufferedImage) itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }
    public static void watermark(String sourceFileName,File extendFile) throws IOException{
    	File sourceFile = new File(sourceFileName);
    	if(!isPic(sourceFile))return;
    	Image img = ImageIO.read(sourceFile);
		BufferedImage doPic = ImageIO.read(extendFile);
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		int extWidth = doPic.getWidth(null);
		int extHeight = doPic.getHeight(null);
		
		//新的image流
		BufferedImage image = new BufferedImage(width, height+extHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();// draw source image
		g.setColor(Color.WHITE);//设置笔刷白色
		g.fillRect(0,0,width,height+extHeight);//填充整个屏幕
		
		g.drawImage(img, 0, 0, width, height, null);
		g.drawImage(doPic, 0, height, extWidth, extHeight, null);
		g.dispose();
		
		FileOutputStream os = new FileOutputStream(sourceFile);
		ImageIO.write(image, "jpg", os);
    }
  
    public static boolean isPic(File file) {
		String imgeArray[] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico" };
		String fileName = file.getName();
		fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
		for (String suffix : imgeArray) {
			if (fileName.equals(suffix)) {
				return true;
			}
		}
		return false;
	}
    public static void main(String[] args) throws Exception {
    	final int width=200;
    	final int height = 150;
    	final int logoHeight = 50;
    	String src = "C:\\Users\\jun\\Pictures\\test\\1.jpg";
    	String dest = "C:\\Users\\jun\\Pictures\\test\\2.jpg";
    	String logo = "C:\\Users\\jun\\Pictures\\test\\logo.jpg";
    	File logoFile = new File(logo);
    	ImageKit.zoomImage(logoFile.getCanonicalPath(), logoFile.getCanonicalPath(), width, logoHeight);
    	
    	ImageKit.zoomImageDb2(src, dest,width,height);
    	ImageKit.cutCenterImage(dest, dest,width,height);
    	ImageKit.watermark(dest,logoFile);
		
	}
}
