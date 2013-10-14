package com.socix.configure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import com.socix.xml.XMLMessage;

/**
 * This class should preserve.
 * @preserve public
 */
public final class GlobalInfo {
	private static String path = "config/config.xml";
    private static Map<String, Map<String, String>> propMap = new HashMap<String, Map<String, String>>();

    /**
     * common way to get a global property
     * the return value is the one in memory, not in file.
     * @param category String
     * @param categoryType String
     * @param attributeName String
     * @return String
     */
    public static String getProperty(String categoryCode, String categoryType, String attributeName) {
        return propMap.get(categoryCode + "," + categoryType).get(attributeName);
    }

    /**
     * common way to set a global property
     * the change will not effect the file unless saveConfig() is called
     * @param category String
     * @param categoryType String
     * @param attributeName String
     * @param attributeValue String
     */
    public static void setProperty(String categoryCode, String categoryType, String attributeName, String attributeValue) {
        propMap.get(categoryCode + "," + categoryType).put(attributeName, attributeValue);
    }

    /**
     * get all attributes name and value under the category
     * @param categoryCode String
     * @param categoryType String
     * @return Map<String, String>
     */
    public static Map<String, String> getPropertyMap(String categoryCode, String categoryType) {
    	Map<String, String> tmp = propMap.get(categoryCode + "," + categoryType);
    	Map<String, String> res = new HashMap<String, String>();
    	for(String key : tmp.keySet()) {
    		res.put(key, tmp.get(key));
    	}
    	return res;
    }
    
    /**
     * set all attributes name and value under the category
     * @param categoryCode String
     * @param categoryType String
     * @param pMap Map<String, String>
     */
    public static void setPropertyMap(String categoryCode, String categoryType, Map<String, String> pMap) {
    	Map<String, String> tmp = propMap.get(categoryCode + "," + categoryType);
    	tmp.clear();
    	for(String key : pMap.keySet()) {
    		tmp.put(key, pMap.get(key));
    	}
    }
    
    /**
     * get category ids by category type
     * return all category ids if category type is null
     * @param categoryType String
     * @return String[]
     */
    public static String[] getCategoryIDS(String categoryType) {
    	List<String> rv = new ArrayList<String>();
    	Set<String> keySet = propMap.keySet();
    	if(categoryType == null) {
    		for(String key : keySet) {
	    		String[] tmp = key.split(",");
    			rv.add(tmp[0]);
	    	}
    	} else {
	    	for(String key : keySet) {
	    		String[] tmp = key.split(",");
	    		if(tmp[1].equals(categoryType))
	    			rv.add(tmp[0]);
	    	}
    	}
    	return rv.toArray(new String[0]);   	
    }
    
    /**
     * load config
     */
    public static void loadConfig() {
    	//get all info from the xml file and put these info into Map
    	propMap.clear();
    	try {
	    	XMLMessage config = XMLMessage.initXML(new File(path));
	    	List<Element> categories = config.getNodes("category");
	    	for(int i = 0; i < categories.size(); i++) {
	    		Element category = categories.get(i);
	    		List<Element> params = category.getChildren("param");
	    		Map<String, String> tmpMap = new HashMap<String, String>();
	    		for(int c = 0; c < params.size(); c++) {
	    			Element param = params.get(c);
	    			tmpMap.put(param.getAttributeValue("name"), param.getAttributeValue("value"));
	    		}
	    		String key = category.getAttributeValue("code") + "," + category.getAttributeValue("type");
	    		propMap.put(key, tmpMap);
	    	}
    	} catch(Exception e) {
    		
    	}
    }
    
    /**
     * load config with the given file
     * @param filePath
     */
    public static void loadConfig(String filePath) {
    	path = filePath;
    	loadConfig();
    }

    /**
     * save config
     */
    public static void saveConfig() throws IOException {
    	//create the config.xml
    	XMLMessage config = XMLMessage.initXML("global", "UTF-8");
    	Set<String> cateKeys = propMap.keySet();
    	for(String cateKey : cateKeys) {
    		Element category = new Element("category");
    		String[] tmp = cateKey.split(",");
    		category.setAttribute("code", tmp[0]);
    		category.setAttribute("type", tmp[1]);
    		Map<String, String> params = propMap.get(cateKey);
    		Set<String> paramKeys = params.keySet();
    		for(String paramKey : paramKeys) {
    			Element param = new Element("param");
    			param.setAttribute("name", paramKey);
    			param.setAttribute("value", params.get(paramKey));
    			category.addContent(param);
    		}
    		config.addNode(category);
    	}
    	//write the xml to file
    	File file = new File(path);
    	if(file.exists())
    		file.delete();
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(config.out2Bytes());
    }
}