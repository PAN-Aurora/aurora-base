package com.aurora.base.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 说明：上传文件
 */
public class FileUpload {

	/**
	 * 获取文件内容 支持word 和 pdf
	 * @param filePath
	 * @return
	 */
	public static StringBuilder getTextFromWordOrPdf(String filePath) {
		StringBuilder buffer = new StringBuilder();
		InputStream is  = null;
		PDDocument document = null;
		try {
			if (filePath.endsWith(".doc")) {
				is = new FileInputStream(new File(filePath));
				WordExtractor ex = new WordExtractor(is);
				buffer.append(ex.getText());
				ex.close();
			} else if (filePath.endsWith("docx")) {
				OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
				POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
				buffer.append(extractor.getText());
				extractor.close();
			} else if (filePath.endsWith("pdf")) {
				File file = new File(filePath);
				PDFTextStripper pdfStripper = new PDFTextStripper();
				document = PDDocument.load(file);
				//获取pdf总共多少页
				int pageNum =  document.getNumberOfPages();
				System.out.println("pdf读取一共"+pageNum+"页");
				for(int i=0;i<pageNum;i++) {
					System.out.println("pdf读取：第"+(i+1)+"页开始");
					pdfStripper.setStartPage(i);
					pdfStripper.setEndPage(i+1);
					System.out.println("pdf读取：第"+(i+1)+"页结束");
					//   System.out.println(pdfStripper.getText(document));
					buffer.append(pdfStripper.getText(document).toString());
					//System.out.println(buffer.toString());
				}
				//buffer.append(pdfStripper.getText(document));

			} else {
				System.out.println("上传文件格式仅为pdf,dox.docx结尾！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(document !=null ) {
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return buffer;
	}

	/**上传文件
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static String fileUp(MultipartFile file, String filePath, String fileName){
		String extName = ""; // 扩展名格式：
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			copyFile(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
		} catch (IOException e) {
			System.out.println(e);
		}
		return fileName+extName;
	}
	
	/**
	 * 写文件到当前目录的upload目录中
	 * @param in
	 * @param realName
	 * @throws IOException
	 */
	public static String copyFile(InputStream in, String dir, String realName)
			throws IOException {
		File file = mkdirsmy(dir,realName);
		FileUtils.copyInputStreamToFile(in, file);
		in.close();
		return realName;
	}
	
	
	/**判断路径是否存在，否：创建此路径
	 * @param dir  文件路径
	 * @param realName  文件名
	 * @throws IOException 
	 */
	public static File mkdirsmy(String dir, String realName) throws IOException{
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
	
	
	/**下载网络图片上传到服务器上
	 * @param httpUrl 图片网络地址
	 * @param filePath	图片保存路径
	 * @param myFileName  图片文件名(null时用网络图片原名)
	 * @return	返回图片名称
	 */
	public static String getHtmlPicture(String httpUrl, String filePath , String myFileName) {
		
		URL url;						//定义URL对象url
		BufferedInputStream in;			//定义输入字节缓冲流对象in
		FileOutputStream file;			//定义文件输出流对象file
		try {
			String fileName = null == myFileName?httpUrl.substring(httpUrl.lastIndexOf("/")).replace("/", ""):myFileName; //图片文件名(null时用网络图片原名)
			url = new URL(httpUrl);		//初始化url对象
			in = new BufferedInputStream(url.openStream());									//初始化in对象，也就是获得url字节流
			//file = new FileOutputStream(new File(filePath +"\\"+ fileName));
			file = new FileOutputStream(mkdirsmy(filePath,fileName));
			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			file.close();
			in.close();
			return fileName;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
