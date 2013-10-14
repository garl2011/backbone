package com.socix.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLMessage {
	
	private String encode = "UTF-8";
	private Document doc;
	private Element root;
	
	private XMLMessage() {
	}
	
	public static XMLMessage initXML(byte[] data, int offset, int length) throws JDOMException, IOException {
		XMLMessage msg = new XMLMessage();
		SAXBuilder builder = new SAXBuilder();
		msg.doc = builder.build(new ByteArrayInputStream(data, offset, length));
		msg.root = msg.doc.getRootElement();
		return msg;
	}	
	
	public static XMLMessage initXML(byte[] data) throws JDOMException, IOException {
		XMLMessage msg = new XMLMessage();
		SAXBuilder builder = new SAXBuilder();
		msg.doc = builder.build(new ByteArrayInputStream(data, 0, data.length));
		msg.root = msg.doc.getRootElement();
		return msg;
	}	
	
    public static XMLMessage initXML(String rootname, String encode) {
    	XMLMessage msg = new XMLMessage();
    	msg.encode = encode;
    	msg.doc = new Document();
    	msg.root = new Element(rootname);
    	msg.doc.setRootElement(msg.root);
    	return msg;
    }

    /**
     * create the xml with the given XMLElement
     *
     * @param ele XMLElement
     * @return void
     */
    public static XMLMessage initXML(Element root) {
    	XMLMessage msg = new XMLMessage();
    	msg.doc = new Document();
    	if(root.getParent() != null)
    		root.detach();
    	msg.root = root;
    	msg.doc.setRootElement(msg.root);
        return msg;
    }    
    
    /**
     *Parsing the xml tag from input string,String must be XML Formate
     *
     *@param xmlstring the string follow xml formate
     *@return void
     */
    public static XMLMessage initXML(String xmlstring) throws JDOMException, IOException {
    	XMLMessage msg = new XMLMessage();
		SAXBuilder sb = new SAXBuilder();
		msg.doc = sb.build(new StringReader(xmlstring));
		msg.root = msg.doc.getRootElement();
    	return msg;
    }

    /**
     *Parsing the xml tag from input stream
     *
     *@param        in        inputstream of xml file
     *@return        void
     */
    public static XMLMessage initXML(InputStream in) throws JDOMException, IOException {
    	XMLMessage msg = new XMLMessage();
		SAXBuilder sb = new SAXBuilder();
		msg.doc = sb.build(in);
		msg.root = msg.doc.getRootElement();
    	return msg;
    }

    public static XMLMessage initXML(InputStreamReader isr) throws JDOMException, IOException {
    	XMLMessage msg = new XMLMessage();
		SAXBuilder sb = new SAXBuilder();
		msg.doc = sb.build(isr);
		msg.root = msg.doc.getRootElement();
    	return msg;
    }

    /**
     *Parsing the xml tag from input file
     *
     *@param        file        file object of xml file
     *@return        void
     */
    public static XMLMessage initXML(File file) throws JDOMException, IOException {
    	XMLMessage msg = new XMLMessage();
		SAXBuilder sb = new SAXBuilder();
		msg.doc = sb.build(new FileReader(file));
		msg.root = msg.doc.getRootElement();
    	return msg;
    }
    
    public String getEncode() {
    	return this.encode;
    }
    
    public void setEncode(String encode) {
        this.encode = encode;
    }
    
    public int getNodesCount(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	List<Element> eles = new ArrayList<Element>();
    	for(int i = 0; i < tags.length - 1; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	String eleName = tags[tags.length - 1];
		if(eleName.indexOf('@') > -1) {
			String childName = eleName.substring(0, eleName.indexOf('@'));
			String attName = eleName.substring(eleName.indexOf('@') + 1, eleName.indexOf('='));
			String attValue = eleName.substring(eleName.indexOf('=') + 1);
			List<Element> children = ele.getChildren(childName);
			for(int c = 0; c < children.size(); c++) {
				if(children.get(c).getAttributeValue(attName).equals(attValue)) {
					eles.add(children.get(c));
				}
			}
		} else {
			List<Element> children = ele.getChildren(eleName);
			for(int c = 0; c < children.size(); c++) {
				eles.add(children.get(c));
			}
		}
    	return eles.size();
    }
    
    public List<Element> getNodes(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	List<Element> eles = new ArrayList<Element>();
    	for(int i = 0; i < tags.length - 1; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	String eleName = tags[tags.length - 1];
		if(eleName.indexOf('@') > -1) {
			String childName = eleName.substring(0, eleName.indexOf('@'));
			String attName = eleName.substring(eleName.indexOf('@') + 1, eleName.indexOf('='));
			String attValue = eleName.substring(eleName.indexOf('=') + 1);
			List<Element> children = ele.getChildren(childName);
			for(int c = 0; c < children.size(); c++) {
				if(children.get(c).getAttributeValue(attName).equals(attValue)) {
					eles.add(children.get(c));
				}
			}
		} else {
			List<Element> children = ele.getChildren(eleName);
			for(int c = 0; c < children.size(); c++) {
				eles.add(children.get(c));
			}
		}
    	return eles;
    }
    
    public Element getNode(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	for(int i = 0; i < tags.length; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	return ele;
    } 
    
    public String getNodeText(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	for(int i = 0; i < tags.length; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	return ele.getText();
    } 
    
    public String[] getNodeTextArray(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	List<String> eles = new ArrayList<String>();
    	for(int i = 0; i < tags.length - 1; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	String eleName = tags[tags.length - 1];
		if(eleName.indexOf('@') > -1) {
			String childName = eleName.substring(0, eleName.indexOf('@'));
			String attName = eleName.substring(eleName.indexOf('@') + 1, eleName.indexOf('='));
			String attValue = eleName.substring(eleName.indexOf('=') + 1);
			List<Element> children = ele.getChildren(childName);
			for(int c = 0; c < children.size(); c++) {
				if(children.get(c).getAttributeValue(attName).equals(attValue)) {
					eles.add(children.get(c).getText());
				}
			}
		} else {
			List<Element> children = ele.getChildren(eleName);
			for(int c = 0; c < children.size(); c++) {
				eles.add(children.get(c).getText());
			}
		}
    	return eles.toArray(new String[0]);
    }
    
    public String getNodeAttr(String path, String attr) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	for(int i = 0; i < tags.length; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	return ele.getAttributeValue(attr);
    }
    
    public String[] getNodeAttrArray(String path, String attr) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	List<String> eles = new ArrayList<String>();
    	for(int i = 0; i < tags.length - 1; i++) {
    		if(!tags[i].equalsIgnoreCase("")) {
    			if(tags[i].indexOf('@') > 0) {
    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
    				List<Element> children = ele.getChildren(childName);
    				for(int c = 0; c < children.size(); c++) {
    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
    						ele = children.get(c);
    						break;
    					}
    				}
    			} else {
    				ele = ele.getChild(tags[i]);
    			}
    		}
    	}
    	String eleName = tags[tags.length - 1];
		if(eleName.indexOf('@') > -1) {
			String childName = eleName.substring(0, eleName.indexOf('@'));
			String attName = eleName.substring(eleName.indexOf('@') + 1, eleName.indexOf('='));
			String attValue = eleName.substring(eleName.indexOf('=') + 1);
			List<Element> children = ele.getChildren(childName);
			for(int c = 0; c < children.size(); c++) {
				if(children.get(c).getAttributeValue(attName).equals(attValue)) {
					eles.add(children.get(c).getAttributeValue(attr));
				}
			}
		} else {
			List<Element> children = ele.getChildren(eleName);
			for(int c = 0; c < children.size(); c++) {
				eles.add(children.get(c).getAttributeValue(attr));
			}
		}
		return eles.toArray(new String[0]);
    }
    
    public String getRootAttr(String attr) {
    	return root.getAttributeValue(attr);
    }
    
    public boolean addNode(Element xmle) {
    	try {
	    	if(xmle.getParent() != null)
	    		xmle.detach();
	        root.addContent(xmle);
	        return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean addNode(String path, String data) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length - 1; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				boolean found = false;
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						found = true; 
	    						break;
	    					}
	    				}
	    				if(!found) {
	    					Element child = new Element(childName);
	    					child.setAttribute(new Attribute(attName, attValue));
	    					ele.addContent(child);
	    					ele = child;
	    				}
	    			} else {
	    				if(ele.getChild(tags[i]) == null) {
	    					Element child = new Element(tags[i]);
	    					ele.addContent(child);
	    					ele = child;
	    				} else {
	    					ele = ele.getChild(tags[i]);
	    				}
	    			}
	    		}
	    	}
	    	String eleName = tags[tags.length - 1];
	    	Element newNode = new Element(eleName);
	    	if(data != null)
	    		newNode.setText(data);
	    	ele.addContent(newNode);
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean addNode(String path, Element node) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				boolean found = false;
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						found = true; 
	    						break;
	    					}
	    				}
	    				if(!found) {
	    					Element child = new Element(childName);
	    					child.setAttribute(new Attribute(attName, attValue));
	    					ele.addContent(child);
	    					ele = child;
	    				}
	    			} else {
	    				if(ele.getChild(tags[i]) == null) {
	    					Element child = new Element(tags[i]);
	    					ele.addContent(child);
	    					ele = child;
	    				} else {
	    					ele = ele.getChild(tags[i]);
	    				}
	    			}
	    		}
	    	}
	    	if(node.getParent() != null)
	    		node.detach();
	    	ele.addContent(node);
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean delNode(String path) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						break;
	    					}
	    				}
	    			} else {
	    				ele = ele.getChild(tags[i]);
	    			}
	    		}
	    	}
	    	ele.detach();
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    } 
    
    public boolean delNodeAttr(String path, String attr) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						break;
	    					}
	    				}
	    			} else {
	    				ele = ele.getChild(tags[i]);
	    			}
	    		}
	    	}
	    	ele.removeAttribute(attr);
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean delRootAttr(String attr) {
    	try {
	    	root.removeAttribute("attr");
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean setNodeText(String path, String data) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						break;
	    					}
	    				}
	    			} else {
	    				ele = ele.getChild(tags[i]);
	    			}
	    		}
	    	}
	    	ele.setText(data);
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean setNodeAttr(String path, String attr, String data) {
    	String[] tags = path.split("/");
    	Element ele = root;
    	try {
	    	for(int i = 0; i < tags.length; i++) {
	    		if(!tags[i].equalsIgnoreCase("")) {
	    			if(tags[i].indexOf('@') > 0) {
	    				String childName = tags[i].substring(0, tags[i].indexOf('@'));
	    				String attName = tags[i].substring(tags[i].indexOf('@') + 1, tags[i].indexOf('='));
	    				String attValue = tags[i].substring(tags[i].indexOf('=') + 1);
	    				List<Element> children = ele.getChildren(childName);
	    				for(int c = 0; c < children.size(); c++) {
	    					if(children.get(c).getAttributeValue(attName).equals(attValue)) {
	    						ele = children.get(c);
	    						break;
	    					}
	    				}
	    			} else {
	    				ele = ele.getChild(tags[i]);
	    			}
	    		}
	    	}
	    	ele.setAttribute(new Attribute(attr, data));
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public boolean setRootAttr(String attr, String data) {
    	try {
	    	root.setAttribute(attr, data);
	    	return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public Element getRoot() {
    	return this.root;
    }
    
    public String out2String() throws IOException {
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true).setEncoding(encode));
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		xmlOut.output(doc, out);
		return new String(out.toByteArray(), encode);
    }
    
    public byte[] out2Bytes() throws IOException {
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true).setEncoding(encode));
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		xmlOut.output(doc, out);
		return out.toByteArray();
    }
}