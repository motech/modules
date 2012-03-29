package org.motechproject.commcare.parser;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.motechproject.commcare.domain.Case;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/22/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommcareCaseParser {
    
    public Case parseCase(String xmlDoc){
        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDoc));
        Case ccCase = null;
        try {
            parser.parse(inputSource);
            ccCase = parseCase(parser.getDocument());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ccCase;
    }

    public Case parseCase(Document document){
        Element item = (Element)document.getElementsByTagName("case").item(0);
        Case ccCase = createCase(item);
        updateAction(ccCase,item);
        return ccCase;
    }

    private Case createCase(Element item) {
        Case ccCase = new Case();
        ccCase.setCaseId(getTextValue(item, "case_id"));
        ccCase.setDateModified(getTextValue(item, "date_modified"));
        return ccCase;
    }

    private void updateAction(Case ccCase,Element item) {
        if(getMatchingChildNode(item, "create") != null)   {
          ccCase.setAction("CREATE");
          populateValuesForCreation(ccCase, item);
          populateValuesForUpdation(ccCase, item);

      } else if(getMatchingChildNode(item, "update") != null){
          ccCase.setAction("UPDATE");
          populateValuesForUpdation(ccCase,item);
      }
    }
    private void populateValuesForCreation(Case ccCase, Element item) {
        ccCase.setCaseTypeId(getTextValue(item, "case_type_id"));
        ccCase.setCaseName(getTextValue(item, "case_name"));
    }

    private void populateValuesForUpdation(Case ccCase, Element item) {

        Node updateitem = getMatchingNode(item, "update");
        NodeList childNodes = updateitem.getChildNodes();

        for(int i = 0;i<childNodes.getLength();i++){
            Node childNode = childNodes.item(i);
            ccCase.AddFieldvalue(childNode.getNodeName(),childNode.getFirstChild().getNodeValue());
        }

    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    private Node getMatchingChildNode(Element ele, String tagName) {
        Node element = getMatchingNode(ele,tagName);
        return element.getFirstChild();
    }

    private Node getMatchingNode(Element ele, String tagName) {
        Node element = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            element = nl.item(0);
        }
        return element;
    }


}
