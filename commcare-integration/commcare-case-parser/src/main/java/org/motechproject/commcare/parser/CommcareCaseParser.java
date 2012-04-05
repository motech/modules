package org.motechproject.commcare.parser;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.motechproject.commcare.domain.Case;
import org.motechproject.commcare.utils.CaseMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class CommcareCaseParser<T> {

    CaseMapper<T> domainMapper;
    private String xmlDoc;
    private String caseAction;


    public CommcareCaseParser(Class<T> clazz,String xmlDocument) {
        domainMapper = new CaseMapper<T>(clazz);
         this.xmlDoc = xmlDocument;
    }
    public T parseCase(){
        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDoc));
        Case ccCase = null;
        try {
            parser.parse(inputSource);
            ccCase = parseCase(parser.getDocument());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return domainMapper.mapToDomainObject(ccCase);
    }

    public Case parseCase(Document document){
        Element item = (Element)document.getElementsByTagName("case").item(0);
        Case ccCase = createCase(item);
        updateAction(ccCase,item);
        return ccCase;
    }

    private Case createCase(Element item) {
        Case ccCase = new Case();

        ccCase.setCase_id(item.getAttribute("case_id"));
        ccCase.setDate_modified(item.getAttribute("date_modified"));
        ccCase.setUser_id(item.getAttribute("user_id"));
        return ccCase;
    }

    private void updateAction(Case ccCase,Element item) {

      if(getMatchingChildNode(item, "create") != null)   {
          setCaseAction(ccCase,"CREATE");
          populateValuesForCreation(ccCase, item);
          populateValuesForUpdation(ccCase, item);

      } else if(getMatchingChildNode(item, "update") != null){
          setCaseAction(ccCase, "UPDATE");
          populateValuesForUpdation(ccCase,item);

      }else if(getMatchingChildNode(item, "close") != null){
          setCaseAction(ccCase, "CLOSE");
          populateValuesForUpdation(ccCase,item);
      }

    }

    private void setCaseAction(Case ccCase,String action) {
        this.caseAction = action;
        ccCase.setAction(action);
    }

    private void populateValuesForCreation(Case ccCase, Element item) {
        ccCase.setCase_type(getTextValue(item, "case_type"));
        ccCase.setCase_name(getTextValue(item, "case_name"));
    }

    private void populateValuesForUpdation(Case ccCase, Element item) {
        Node updateitem = getMatchingNode(item, "update");
        NodeList childNodes = updateitem.getChildNodes();

        for(int i = 0;i<childNodes.getLength();i++){
            Node childNode = childNodes.item(i);
            if(!childNode.getNodeName().contains("text"))
                ccCase.AddFieldvalue(childNode.getNodeName(),childNode.getTextContent());

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


    public String getCaseAction() {
        return caseAction;
    }
}
