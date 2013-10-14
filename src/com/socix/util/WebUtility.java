package com.socix.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WebUtility {
	
	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * 返回某个url链接代表的媒体类型：图片、音频、视频
	 * @param urlStr		url链接
	 * @return		url所代表的媒体类型
	 */
	public static String getContentType(String urlStr) {
		String rs = null;
		try {
			String address = urlStr;
			while(true) {
				URL url = new URL(address);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(false);
				con.connect();
				if(con.getResponseCode() == 200) {
					rs = con.getContentType();
					break;
				} else if(con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 303) {
					address = con.getHeaderField("Location");
					if(address == null) {
						rs = "error/redirect";
						break;
					}
				} else {
					rs = "error/service";
					break;
				}
			}
		} catch (MalformedURLException e) {
			return "error/MalformedURL";
		} catch (IOException e) {
			return "error/IO";
		}
		return rs;
	}
	
	/**
	 * 获取网站的favicon
	 * @param	urlStr	想要获取icon的网站网址
	 * @return	对象网站的favicon
	 */
	public static String getFavIcon(String urlStr) {
		String str = urlStr;
		int min = str.startsWith("http://")?6:str.startsWith("https://")?7:Integer.MAX_VALUE;
		String result = null;
		if(str.lastIndexOf("/") == min) {
			try {
				URL url = new URL(str + "/favicon.ico");
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.connect();
				if(con.getResponseCode() == 200) {
					result = str + "/favicon.ico";
				}
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}
		} else {
			while(str.lastIndexOf("/") > min) {
				str = str.substring(0, str.lastIndexOf("/"));
				try {
					URL url = new URL(str + "/favicon.ico");
					HttpURLConnection con = (HttpURLConnection)url.openConnection();
					con.connect();
					if(con.getResponseCode() == 200) {
						result = str + "/favicon.ico";
						break;
					}
				} catch (MalformedURLException e) {
					continue;
				} catch (IOException e) {
					continue;
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取url对应的网页内容，如果有错，则返回null
	 * @param urlStr		网页url
	 * @return		对应的网页内容，或者null如果网页无法访问
	 */
	public static String getWebPage(String urlStr) {
		String rs = null;
		try {
			String address = urlStr;
			while(true) {
				URL url = new URL(address);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(false);
				con.connect();
				if(con.getResponseCode() == 200) {
					InputStream is = con.getInputStream();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int c = 0;
					while((c = is.read()) != -1) {
						bos.write(c);
					}
					byte[] b = bos.toByteArray();
					is.close();
					bos.close();
					if(isUTF8(b)) {
						rs = new String(b, "UTF-8");
					} else {
						rs = new String(b, "GBK");
					}
					break;
				} else if(con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 303) {
					address = con.getHeaderField("Location");
					if(address == null) {
						break;
					}
				} else {
					break;
				}
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return rs;
	}
	
	/**
	 * 获取网站的title和description
	 * @param	urlStr	想要获取信息的网站网址
	 * @return	网站信息数组，0为title，1为description，有问题返回null
	 */
	public static String[] getWebInfo(String urlStr) {
		String[] rs = new String[2];
		String head = null;
		String html = getWebPage(urlStr);
		if(html != null) {
			try {
				int start = html.indexOf("<head");
				int end = html.indexOf("/head>");
				if(start >= 0 && end > 0)
					head = html.substring(start, end + 6);
				if(head != null) {
					SAXReader reader = new SAXReader();
					Document doc = null;
					start = head.indexOf("<title");
					end = head.indexOf("/title>");
					if(start >= 0 && end > 0) {
						String txml = head.substring(start, end + 7);
						doc = reader.read(new InputStreamReader(new ByteArrayInputStream(txml.getBytes("UTF-8")), "UTF-8"));
						Element title = doc.getRootElement();
						rs[0] = StringEscapeUtils.unescapeHtml4(title.getText());
					}
					Pattern p = Pattern.compile("<meta .*name=\"[dD]escription\".*/>");
					Matcher m = p.matcher(head);
					while(m.find()) {
						String dxml = head.substring(m.start(), m.end());
						doc = reader.read(new InputStreamReader(new ByteArrayInputStream(dxml.getBytes("UTF-8")), "UTF-8"));
						Element meta = doc.getRootElement();
						rs[1] = StringEscapeUtils.unescapeHtml4(meta.attributeValue("content"));
						break;
					}
				}
			} catch (IOException e) {
			} catch (DocumentException e) {
			}
		}
		return rs;
	}
	
	public static boolean downloadFile(String urlStr, File file) {
		if(file == null || file.isDirectory())
			return false;
		try {
			String address = urlStr;
			while(true) {
				URL url = new URL(address);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(false);
				con.connect();
				if(con.getResponseCode() == 200) {
					InputStream is = con.getInputStream();
					if(file.exists())
						file.delete();
					file.createNewFile();
					FileOutputStream os = new FileOutputStream(file);
					int length = -1;
					byte[] b = new byte[BUFFER_SIZE];
					while ((length = is.read(b)) > 0)
		                 os.write(b, 0, length);
					os.flush();
					is.close();
					os.close();
					return true;
				} else if(con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 303) {
					address = con.getHeaderField("Location");
					if(address == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * 判断数据流是否为UTF-8编码
	 * @param data		数据流
	 * @return		是否为UTF-8编码
	 */
	public static boolean isUTF8(byte[] data) {
        int n = 0;
        for (int i = 1; i < data.length; i++)
        {
            n += ((data[i] & 0xC0) == 0x80) ? ((((data[i - 1]) & 0xC0) == 0xC0) ? 1 : ((data[i - 1] & 0x80) == 0x00) ? -1 : 0) : ((data[i - 1] & 0xC0) == 0xC0) ? -1 : 0;
        }
        return n > 0;
    }

}
