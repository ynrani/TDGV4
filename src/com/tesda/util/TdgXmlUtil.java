/*
 * Object Name : TdgXmlUtil.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		1:43:34 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author vkrish14
 *
 */
public class TdgXmlUtil extends TDGBaseUtil{
	//public static void doXMLGenerations(){
	public static void main(String[] args) throws SAXException, IOException{
		List<Object[]> lst = new ArrayList<Object[]>();
		Object[] ob1 = {"id","name","city"};
		Object[] ob2 = {"1","venkat","pune"};
		Object[] ob3 = {"2","krish","hyd"};
		lst.add(ob1);
		lst.add(ob2);
		lst.add(ob3);
		doGenerateXMLFiles(lst, "C:\\File.xml", 0, "test");
		
/*		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("company");
			doc.appendChild(rootElement);
			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);
			// shorten way
			// staff.setAttribute("id", "1");
			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);
			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);
			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);
			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\file1.xml"));
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}*/
	}
	public static void doMergeFiles(List<String> lstFileNames){
		//XML
	}
	public static void doGenerateXMLFiles(List<Object[]> lstInputParams,
			String strFileName, int iTotalColumns,String strTableName) throws SAXException, IOException{
		try{
			
			StreamResult result = new StreamResult(new File(strFileName));
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		//Document doc = docBuilder.newDocument();
		Document doc = docBuilder.parse(new File(strFileName));
		Element rootElement = doc.getDocumentElement();
		/*doc.appendChild(rootElement);
		Attr attr1 = doc.createAttribute("name");
		attr1.setValue(strTableName);
		rootElement.setAttributeNode(attr1);*/
		// staff elements
		Object[] objectHeaders = lstInputParams.get(0);
			for (int i = 1; i < lstInputParams.size(); i++) {
				Element element = null;
				for (int j = 0; j < objectHeaders.length; j++) {					
					if (j == 0) {
						element = doc.createElement(strTableName);
						rootElement.appendChild(element);
						Attr attr = doc.createAttribute(objectHeaders[j] + "");
						attr.setValue(lstInputParams.get(i)[j] + "");
						element.setAttributeNode(attr);
					}else{
						Element elementChild = doc.createElement(objectHeaders[j] + "");
						elementChild.appendChild(doc.createTextNode(lstInputParams.get(i)[j]+""));
						element.appendChild(elementChild);
					}
				}
			}
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, result);
	} catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	} catch (TransformerException tfe) {
		tfe.printStackTrace();
	}
	}

	public static void doGenerateXMLFile(List<Object[]> lstInputParams, String strFileName, int size,
			Map<Integer, String> mapPositions, List<String> lstRequiredColumnsSequence,
			boolean isAddedFlag,String strTableName){
		try {
			File file = new File(strFileName);
			StreamResult result = new StreamResult(file);
			Document doc = null;
			Element rootElement = null;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			if (isAddedFlag) {
				doc = docBuilder.parse(file);
				rootElement = doc.getDocumentElement();
			} else {
				doc = docBuilder.newDocument();
				rootElement = doc.createElement("table");
				doc.appendChild(rootElement);
				Attr attr1 = doc.createAttribute("name");
				attr1.setValue(strTableName);
				rootElement.setAttributeNode(attr1);
			}
			
			Object[] objectHeaders = lstInputParams.get(0);
			for (int i = 1; i < lstInputParams.size(); i++) {
				Element element = null;
				for (int j = 0; j < objectHeaders.length; j++) {
					if (j == 0) {
						element = doc.createElement(strTableName);
						rootElement.appendChild(element);
						Attr attr = doc.createAttribute(objectHeaders[j] + "");
						attr.setValue(lstInputParams.get(i)[j] + "");
						element.setAttributeNode(attr);
					} else {
						Element elementChild = doc.createElement(objectHeaders[j] + "");
						elementChild.appendChild(doc.createTextNode(lstInputParams.get(i)[j] + ""));
						element.appendChild(elementChild);
					}
				}
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
