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
 * ������ֹ�˾������Ŀ��ȥ��ʱ�����Ҫ��Ը����class�ļ���������������ļ����и��Ǿ��С�ÿ�ζ������ֶ���·��������Ҫ���ļ����鷳������д��һ��copy�ļ��Ĵ��������
 * @author ZMM
 * @version 1.0.3
 * @date 2019.05.10
 * **/
public class CopyClass {
 
    private static File fils;
    // �����Ƶ��ļ�����
    public static String[] filterFile = { ".jks", ".js", ".jsp", ".class",".java", ".xml", ".properties", ".sql" };
    private static long total = 0l;
    private static List<String> l = new ArrayList<String>();;
    // 0 ��Java �滻Ϊclass
    // 1 λurl
    // 2 ʶ��� Ϊ0��ʱ�������classes �µ������ļ�
    // 3 ��Ӧ��·��
    static String[] string = new String[4];
    // ����ָ�ɵ�·��
    private final static String clPth = "www\\WEB-INF\\classes\\";
    public static void main(String[] args) throws Exception {
        // ��ȡ�ı���Ϣ
      getFile(readTxtFile("D:\\program\\Company\\01.PDM\\java\\sinocc.pdm\\PDM-DevOps\\1.ϵͳ����SOP\\PDM-190516\\filelist_all_zmm.txt"));
 
    	
    }
 
    /**
     * @param l_File
     *            path
     **/
    private static void getFile(List<String> l_File) throws IOException, Exception {
        for (String sFileName : l_File) {
            // System.out.println(string);
            if ("".equals(sFileName) || sFileName == null) {
                throw new IOException("��ȷ���ļ��Ƿ����");
            }
            String[] p = getFile(sFileName, 0);
            String classPath = "";
            for (int i = l.size(); i > 0; i--) {
                classPath += l.get(i - 1) + "\\";
            }
            String classp = "";
            // ������������ ָ��ʱ������
            String df = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File des = null;
            if ("".equals(classPath)) {
                // string [2] Ϊ0��ʱ�������classes �µ������ļ�
                if (p[2].equals("0")) {
                    classp = p[1] + clPth;
                    des = new File("D:\\java\\" + df + "\\" + clPth);
                } else {
                    classp = p[1];
                    if (p[3].indexOf("conf") != -1) {
                        des = new File("D:\\java\\" + df + "\\" + clPth
                                + p[3].substring(p[3].indexOf("conf") + 4));
                    } else {
 
                        des = new File("D:\\java\\" + df + "\\" + p[3]);
                    }
                }
 
            } else {
                classp = p[1] + clPth + classPath;
                des = new File("D:\\java\\" + df + "\\" + clPth + classPath);
            }
            /*
             * System.out.println("clPth:" + clPth);
             * System.out.println("classPath:" + classPath);
             * System.out.println("classp:" + classp);
             */
            // ��Ҫ����������
            File src = new File(classp.replace("\\", "\\\\"));
            // filterFile=new String []{p[0]};
            new CopyClass().copyFolder(src, des, filterFile, p[0]);
            // ����
            l = new ArrayList<String>();
        }
    }
 
    /**
     * ��ȡ�ı�����
     * **/
    public static List<String> readTxtFile(String filePath) {
        List<String> l_File = new ArrayList<String>();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            // System.out.println(filePath);
            // �ж��ļ��Ƿ����
            if (file.isFile() && file.exists()) { 
            	// ���ǵ������ʽ
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null && !"".equals(lineTxt)) {
                	lineTxt = "D:\\program\\Company\\01.PDM\\java" + lineTxt;
                	lineTxt = lineTxt.replace("\\", "\\\\");
                	lineTxt = lineTxt.replace("\"", "");
                	if (!"".equals(lineTxt)) l_File.add(lineTxt);
                }
                read.close();
            } else {
                System.out.println("�Ҳ���ָ�����ļ�");
            }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݳ���");
            e.printStackTrace();
        }
        return l_File;
    }
 
    /**
     * @param path
     *            �x�ęn·��
     * @param flag
     *            ��ʶ��
     * **/
 
    private static String[] getFile(String path, int flag) {
        fils = new File(path);
        // System.out.println("path:"+path);
        if (fils.isFile()) {
            // System.out.println("fils:"+fils.getName());
            string[0] = fils.getName().replace("java", "class");
        } else {
            // �ļ�������
            if (!"src".equals(fils.getName())) {
                l.add(fils.getName());
            }
 
            if ("".equals(fils.getName())) {
                l.add("classes");
            }
        }
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
            string[1] = path.substring(0, path.lastIndexOf("src")).replace(
                    "src", "");
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
 
            // ���spring mvc ���õ�conf �ļ� ��Ҫcopy��classes�ļ���
            if (path.indexOf("WebRoot") == -1) {
 
                // System.out.println(
                // path.substring(path.indexOf("conf"),path.indexOf(string[0])));
                string[3] = path.substring(path.indexOf("conf"),
                        path.indexOf(string[0]));
            } else {
 
                string[3] = path.substring(path.indexOf("WebRoot"),
                        path.indexOf(string[0]));
            }
 
        }
 
        return string;
        /*
         * for (int i = 0; i < fils.listFiles().length; i++) { for (File file :
         * fils.listFiles()) { //�������Ŀ¼��ֱ����� if (!file.isDirectory()) {
         * System.out.println(file.getAbsolutePath()); } else {
         * System.out.println(fils.getAbsolutePath()); //����Ŀ¼�ļ����ݹ����
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
    public void copyFolder(File srcFolder, File destFolder,
            String[] filterFile, String fname) throws Exception {
        File[] files = srcFolder.listFiles();
        // System.out.println(destFolder);
        // ��ɾ�����ļ� ����Ŀ�
//        deleteDir(destFolder);
        destFolder.mkdirs();
 
        for (File file : files) {
            if (file.isFile()) {
                String vl=file.getName().substring(file.getName().lastIndexOf("\\")+1,file.getName().length()).replace(".class", "");
    //          System.out.println("vl:"+vl);
//              if (fname !=null && vl.equals(fname.replace(".class", ""))||vl.indexOf(fname.replace(".class", "")+"$")==0) {
              if (vl.equals(fname.replace(".class", ""))||vl.indexOf(fname.replace(".class", "")+"$")==0) {
                     System.out.println("test:"+file.getName());
                    String pathname = destFolder + File.separator
                            + file.getName();
                    for (String suff : filterFile) {
                        if (pathname.endsWith(suff)) {
                            File dest = new File(pathname);
                            File destPar = dest.getParentFile();
                            destPar.mkdirs();
                            if (!dest.exists()) {
                                dest.createNewFile();
                            }
                            // D:\springmvc\WebRoot\WEB-INF\index.jsp
                            // D:\java\20160603\WebRoot\index.jsp
                            // Ϊ�˷�ֹ���������Ҳ���ͬһ��·����COPY
                            if ((file.getParent()
                                    .substring(file.getParent().length() - 4,
                                            file.getParent().length() - 1)
                                    .equals(dest.getParent().substring(
                                            dest.getParent().length() - 4,
                                            dest.getParent().length() - 1)))||file.getParent().contains("conf")) {
                                if (file.length() == 0) {
                                    throw new IOException("�ļ�������Ϊ��"
                                            + "����Ҫ������ļ�Ϊ��" + file.getParent()
                                            + "\\" + file.getName());
                                }
                                copyFile(file, dest);
                            }
 
                        }
                    }
                }
            } else {
                copyFolder(file, destFolder, filterFile, fname);
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
            // ��¼
            String temp = "\ncopy:\n" + src + "\tsize:" + src.length()
                    + "\nto:\n" + dest + "\tsize:" + dest.length()
                    + "\n complate\n totoal:" + total;
            System.out.println(temp);
        }
    }
 
    /**
     * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
     *
     * @param dir
     *            ��Ҫɾ�����ļ�Ŀ¼ ��ʱ��û��
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
        // Ŀ¼��ʱΪ�գ�����ɾ��
        return dir.delete();
    }
}
