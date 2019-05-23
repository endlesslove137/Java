package com.auxwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 最近发现公司部署项目上去的时候仅需要针对个别的class文件或者其他个别的文件进行覆盖就行。每次都觉得手动找路径而且又要找文件很麻烦。所以写了一个copy文件的代码出来。
 * 
 * @author ZMM
 * @version 1.0.3
 * @date 2019.05.10
 **/
public class CopyClass {

	private static File fils;
	// 允许复制的文件类型
	public static String[] filterFile = { ".jks", ".js", ".jsp", ".class", ".java", ".xml", ".properties", ".sql" };
	public static String[] srcDirList = { "src", "src_openapi" };

	private static long total = 0l;
	private static List<String> strPackagePath = new ArrayList<String>();;
	private static List<String> errFileList = new ArrayList<String>();

	// 0 将Java 替换为class
	// 1 位url
	// 2 识别号 为0的时候。是添加classes 下的配置文件
	// 3 对应的路径
	static String[] string = new String[4];
	// 创建指派的路径
	private final static String clPth = "www\\WEB-INF\\classes\\";

	public static void main(String[] args) throws Exception {
		// 读取文本信息
		getFile(getSrcFileNameList(
				"D:\\program\\Company\\Formal\\sinocc.pdm\\PDM-DevOps\\1.系统发布SOP\\PDM-190523\\test.txt"));
	}

	/**
	 * @param srcFileNameList path
	 **/
	private static void getFile(List<String> srcFileNameList) throws IOException, Exception {
		for (String sFileName : srcFileNameList) {
			// System.out.println(string);
			if ("".equals(sFileName) || sFileName == null) {
				errFileList.add(String.format("[Error][文件名称非法]", sFileName));
				continue;
//                throw new IOException("请确认文件是否存在");
			}
			String[] aClassInfo = getFile(sFileName, 0);
			String classPath = "";
			for (int i = strPackagePath.size(); i > 0; i--) {
				classPath += strPackagePath.get(i - 1) + "\\";
			}
			String classp = "";
			// 背拷贝的数据 指定时间数据
			String strDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			File des = null;
			if ("".equals(classPath)) {
				// string [2] 为0的时候。是添加classes 下的配置文件
				if (aClassInfo[2].equals("0")) {
					classp = aClassInfo[1] + clPth;
					des = new File("D:\\java\\" + strDate + "\\" + clPth);

				} else {
					classp = aClassInfo[1];
					if (aClassInfo[3].indexOf("conf") != -1) {
						des = new File("D:\\java\\" + strDate + "\\" + clPth
								+ aClassInfo[3].substring(aClassInfo[3].indexOf("conf") + 4));
					} else {

						des = new File("D:\\java\\" + strDate + "\\" + aClassInfo[3]);
					}
				}

			} else {
				classp = aClassInfo[1] + clPth + classPath;
				String sDes = "D:\\java\\" + strDate + "\\" + clPth + classPath;
				des = new File(sDes);
			}
			/*
			 * System.out.println("clPth:" + clPth); System.out.println("classPath:" +
			 * classPath); System.out.println("classp:" + classp);
			 */
			// 需要拷贝的数据
			File src = new File(classp.replace("\\", "\\\\"));
			new CopyClass().copyFolder(src, des, filterFile, aClassInfo[0]);
			// 重置
			strPackagePath = new ArrayList<String>();
		}
	}

	/**
	 * 读取文本数据
	 **/
	public static List<String> getSrcFileNameList(String filePath) {
		List<String> srcFileNameList = new ArrayList<String>();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			// System.out.println(filePath);
			// 判断文件是否存在
			if (file.isFile() && file.exists()) {
				// 考虑到编码格式
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null && !"".equals(lineTxt)) {
					lineTxt = lineTxt.replace("\"", "");
					lineTxt = lineTxt.replace(" ", "");
					if ("".equals(lineTxt))
						continue;
					lineTxt = "D:\\program\\Company\\Formal" + lineTxt;
					lineTxt = lineTxt.replace("\\", "\\\\");
					srcFileNameList.add(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return srcFileNameList;
	}

	/**
	 * @param path 讀文檔路径
	 * @param flag 标识符
	 **/

	private static String[] getFile(String path, int flag) {
		fils = new File(path);
		// System.out.println("path:"+path);
		if (fils.isFile()) {
			// System.out.println("fils:"+fils.getName());
			string[0] = fils.getName().replace("java", "class");
		} else {
			// 文件夹名字
			if (!"src".equals(fils.getName())) {
				strPackagePath.add(fils.getName());
			}

			if ("".equals(fils.getName())) {
				strPackagePath.add("classes");
			}
		}
//        for (String srcDir : srcDirList) {
//	          if (fils.getParent().lastIndexOf(srcDir) != -1) {
//	        	  path = fils.getParent();
//		          if ((path.lastIndexOf(srcDir) + srcDir.length()) != path.length()) {
//		              return getFile(path, 0);
//		          } else if ((path.lastIndexOf(srcDir)) <= path.length()) {
//		              return getFile(path, 1);
//		          }
//	          }
//        }

		if (fils.getParent().lastIndexOf("src") != -1) {
			path = fils.getParent();
			if ((path.lastIndexOf("src") + 3) != path.length()) {
				return getFile(path, 0);
			} else if ((path.lastIndexOf("src")) <= path.length()) {
				return getFile(path, 1);
			}
		}
		if (flag == 1) {
			// D:\springmvc
			string[1] = path.substring(0, path.lastIndexOf("src")).replace("src", "");
			string[2] = "0";
		} else {

			// System.out.println(path);
			if (path.lastIndexOf("src") > -1) {
				string[1] = path.substring(0, path.lastIndexOf("src"));
			} else {

				// /D:\\springmvc01\\WebRoot\\WEB-INF\\index.jsp
				string[1] = path.replace(string[0], "");
			}
			string[2] = "";

			// 针对spring mvc 配置的conf 文件 需要copy到classes文件下
			if (path.indexOf("WebRoot") == -1) {

				// System.out.println(
				// path.substring(path.indexOf("conf"),path.indexOf(string[0])));
				string[3] = path.substring(path.indexOf("conf"), path.indexOf(string[0]));
			} else {
				string[3] = path.substring(path.indexOf("WebRoot"), path.indexOf(string[0]));
			}

		}

		return string;
		/*
		 * for (int i = 0; i < fils.listFiles().length; i++) { for (File file :
		 * fils.listFiles()) { //如果不是目录，直接添加 if (!file.isDirectory()) {
		 * System.out.println(file.getAbsolutePath()); } else {
		 * System.out.println(fils.getAbsolutePath()); //对于目录文件，递归调用
		 * getFile(file.getAbsolutePath()); } } }
		 */
	}

	/**
	 *
	 * @param folder
	 * @param filterFile
	 * @param fileName
	 * @throws Exception
	 */
	public void copyFolder(File srcFolder, File destFolder, String[] filterFile, String destFilename) throws Exception {
		File[] srcDirFiles = srcFolder.listFiles();
		// System.out.println(destFolder);
		// 先删除旧文件 創建目錄
//        deleteDir(destFolder);
		destFolder.mkdirs();

		for (File srcFile : srcDirFiles) {
			if (srcFile.isFile()) {
				String srcFileName = srcFile.getName()
						.substring(srcFile.getName().lastIndexOf("\\") + 1, srcFile.getName().length())
						.replace(".class", "");
				// System.out.println("vl:"+vl);
//              if (fname !=null && vl.equals(fname.replace(".class", ""))||vl.indexOf(fname.replace(".class", "")+"$")==0) {
				if (srcFileName.equals(destFilename.replace(".class", ""))
						|| srcFileName.indexOf(destFilename.replace(".class", "") + "$") == 0) {
					System.out.println("找到目标文件:" + srcFile.getName());
					String desFilePathName = destFolder + File.separator + srcFile.getName();
					for (String suff : filterFile) {
						if (desFilePathName.endsWith(suff)) {
							File destFile = new File(desFilePathName);
							File destPar = destFile.getParentFile();
							destPar.mkdirs();
							if (!destFile.exists()) {
								destFile.createNewFile();
							}
							// D:\springmvc\WebRoot\WEB-INF\index.jsp
							// D:\java\20160603\WebRoot\index.jsp
							// 为了防止重命名并且不在同一个路径下COPY
							if ((srcFile.getParent()
									.substring(srcFile.getParent().length() - 4, srcFile.getParent().length() - 1)
									.equals(destFile.getParent().substring(destFile.getParent().length() - 4,
											destFile.getParent().length() - 1)))
									|| srcFile.getParent().contains("conf")) {
								if (srcFile.length() == 0) {
									throw new IOException(
											"文件不允许为空" + "，需要处理的文件为：" + srcFile.getParent() + "\\" + srcFile.getName());
								}
								copyFile(srcFile, destFile);
							}

						}
					}
				}
			} else {
				copyFolder(srcFile, destFolder, filterFile, destFilename);
			}
		}
	}

	/***
	 * copy file
	 *
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private void copyFile(File src, File dest) throws Exception {
		BufferedInputStream reader = null;
		BufferedOutputStream writer = null;

		try {
			reader = new BufferedInputStream(new FileInputStream(src));
			writer = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] buff = new byte[reader.available()];
			while ((reader.read(buff)) != -1) {
				writer.write(buff);
			}
			total += 1;
		} catch (Exception e) {
			throw e;
		} finally {
			writer.flush();
			writer.close();
			reader.close();
			// 记录
			String temp = "\ncopy:\n" + src + "\tsize:" + src.length() + "\nto:\n" + dest + "\tsize:" + dest.length()
					+ "\n complate\n totoal:" + total;
			System.out.println(temp);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 *
	 * @param dir 将要删除的文件目录 暂时还没用
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();

			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
