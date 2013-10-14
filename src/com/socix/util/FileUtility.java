package com.socix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtility {
	
	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * 将srcDir文件夹压缩成desFile。压缩格式为zip。 desFile中不带srcDir的文件夹名。起始路径为srcDir的子目录或子文件。
	 * 
	 * @param srcDir
	 * @param desFile
	 * @return
	 */
	public static boolean zipFile(File srcDir, File desFile) {
		if (srcDir == null || desFile == null || !srcDir.exists() || !srcDir.isDirectory())
			return false;
		if (desFile.exists())
			deleteFile(desFile);
		try {
			desFile.createNewFile();
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					desFile));
			File[] fList = srcDir.listFiles();
			for (File f : fList) {
				if (f.isDirectory()) {
					ZipEntry ze = new ZipEntry(f.getName() + "/");
					ze.setTime(f.lastModified());
					zos.putNextEntry(ze);
					addZip(f, zos, f.getName() + "/");
				} else {
					ZipEntry ze = new ZipEntry(f.getName());
					ze.setSize(f.length());
					ze.setTime(f.lastModified());
					zos.putNextEntry(ze);
					byte[] buf = new byte[BUFFER_SIZE];
					int readlen = -1;
					InputStream is = new FileInputStream(f);
					while ((readlen = is.read(buf)) != -1) {
						zos.write(buf, 0, readlen);
					}
					is.close();
				}
			}
			zos.flush();
			zos.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * 内部方法，支持zipFile针对目录的嵌套压缩。
	 * 
	 * @param srcDir
	 * @param zos
	 * @param base
	 * @return
	 */
	private static boolean addZip(File srcDir, ZipOutputStream zos, String base) {
		try {
			File[] fList = srcDir.listFiles();
			for (File f : fList) {
				if (f.isDirectory()) {
					ZipEntry ze = new ZipEntry(base + f.getName() + "/");
					ze.setTime(f.lastModified());
					zos.putNextEntry(ze);
					addZip(f, zos, base + f.getName() + "/");
				} else {
					ZipEntry ze = new ZipEntry(base + f.getName());
					ze.setSize(f.length());
					ze.setTime(f.lastModified());
					zos.putNextEntry(ze);
					byte[] buf = new byte[BUFFER_SIZE];
					int readlen = -1;
					InputStream is = new FileInputStream(f);
					while ((readlen = is.read(buf)) != -1) {
						zos.write(buf, 0, readlen);
					}
					is.close();
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}	

	/**
	 * 将srcFile文件解压成desDir。压缩格式为zip。 解压后将创建名为desDir的目录，目录下包括zip包中的所有文件和文件夹。
	 * 
	 * @param srcFile
	 * @param desDir
	 * @return
	 */
	public static boolean unzipFile(File srcFile, File desDir) {
		if (srcFile == null || desDir == null || !srcFile.exists())
			return false;
		if(desDir.exists())
			deleteFile(desDir);
		desDir.mkdirs();
		try {
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
			ZipEntry entry;
			while(enumeration.hasMoreElements()) {
				entry = enumeration.nextElement();
				File tmpFile = new File(desDir.getAbsolutePath() + "/" + entry.getName());
				if(entry.isDirectory()) {
					tmpFile.mkdirs();
				} else {
					OutputStream os = new FileOutputStream(tmpFile);
					InputStream is = zipFile.getInputStream(entry);
					int length = -1;
					byte[] b = new byte[BUFFER_SIZE];
					while ((length = is.read(b)) > 0)
	                     os.write(b, 0, length);
					os.flush();
					is.close();
					os.close();
				}
			}
			return true;
		} catch (ZipException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * 删除指定的文件夹或文件。即使文件夹内部不为空也能删除。
	 * 
	 * @param dFile
	 * @return
	 */
	public static boolean deleteFile(File dFile) {
		boolean flag = false;
		if (dFile == null || !dFile.exists())
			return true;
		if (dFile.isDirectory()) {
			File[] fList = dFile.listFiles();
			for (File f : fList) {
				flag = deleteFile(f);
				if (flag) {
					continue;
				} else {
					return flag;
				}
			}
			flag = dFile.delete();
		} else {
			flag = dFile.delete();
		}
		return flag;
	}	
	
	/**
	 * copy指定文件到目标文件，如果目标文件存在，则删除再copy
	 * @param src	源文件
	 * @param dest	目标文件
	 * @return	copy是否成功
	 */
	public static boolean copyFile(File src, File dest) {
		if(src == null || dest == null || !src.exists())
			return false;
		if(dest.exists())
			deleteFile(dest);
		try {
			dest.createNewFile();
			FileInputStream is = new FileInputStream(src);
			FileOutputStream os = new FileOutputStream(dest);
			int length = -1;
			byte[] b = new byte[BUFFER_SIZE];
			while ((length = is.read(b)) > 0)
                 os.write(b, 0, length);
			os.flush();
			is.close();
			os.close();
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	
}
