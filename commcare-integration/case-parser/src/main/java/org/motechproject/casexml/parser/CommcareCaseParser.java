package org.motechproject.casexml.parser;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.motechproject.casexml.domain.Case;
import org.motechproject.casexml.exception.ParserException;
import org.motechproject.casexml.utils.CaseMapper;
import org.motechproject.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
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
        Case ccCase;
        try {
            parser.parse(inputSource);
            ccCase = parseCase(parser.getDocument());
        } catch (IOException ex) {
            throw new ParserException(ex, "Exception while trying to parse caseXml");
        }
        catch (SAXException ex){
            throw new ParserException(ex, "Exception while trying to parse caseXml");
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

        ccCase.setCase_id(getMandatoryAttribute(item, "case_id"));
        ccCase.setDate_modified(item.getAttribute("date_modified"));
        ccCase.setUser_id(getMandatoryAttribute(item, "user_id"));
        return ccCase;
    }



    private void updateAction(Case ccCase,Element item) {

      if(getMatchingChildNode(item, "create") != null)   {
          setCaseAction(ccCase,"CREATE");
          populateValuesForCreation(ccCase, item);
          populateValuesForUpdation(ccCase, item);

      } else {
          if(getMatchingChildNode(item, "update") != null){
              setCaseAction(ccCase, "UPDATE");
              populateValuesForUpdation(ccCase,item);

          }else {
              if (getMatchingChildNode(item, "close") != null) {
                  setCaseAction(ccCase, "CLOSE");
              }
          }
      }
    }

    private void setCaseAction(Case ccCase,String action) {
        this.caseAction = action;
        ccCase.setAction(action);
    }

    private void populateValuesForCreation(Case ccCase, Element item) {
        ccCase.setCase_type(getMandatoryTextValue(item, "case_type"));
        ccCase.setCase_name(getTextValue(item, "case_name"));
        ccCase.setOwner_id(getMandatoryTextValue(item, "owner_id"));
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
            Node textNode = el.getFirstChild();
            if(textNode != null)
                textVal = textNode.getNodeValue();
        }
        return textVal;
    }

    private String getMandatoryAttribute(Element element, String name){
        String value = element.getAttribute(name);
        if (StringUtil.isNullOrEmpty(value))
            throw new ParserException(String.format("Mandatory field %s is missing",name));
        return value;
    }

    private String getMandatoryTextValue(Element element, String name){
        String value = getTextValue(element, name);
        if (StringUtil.isNullOrEmpty(value))
            throw new ParserException(String.format("Mandatory field %s is missing",name));
        return value;
    }

    private Node getMatchingChildNode(Element ele, String tagName) {
        return getMatchingNode(ele,tagName);
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
