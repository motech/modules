package org.motechproject.commcare.service;

import org.apache.log4j.Logger;
import org.motechproject.commcare.parser.CommcareCaseParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Writer;

public abstract class CaseService<T> {
   private static Logger logger = Logger.getLogger(CaseService.class);
    private Class<T> clazz;

    public CaseService(Class<T> clazz){
        this.clazz = clazz;
   }

    @RequestMapping(value="/process",method= RequestMethod.POST)
    public void ProcessCase(@RequestBody String xmlDocument,Writer writer){
        logger.info(xmlDocument);
        System.out.println("xmldoc "+xmlDocument);

        CommcareCaseParser<T>  caseParser = new CommcareCaseParser<T>(clazz,xmlDocument);
        T  object = caseParser.parseCase();

        if("CREATE".equals(caseParser.getCaseAction()))
            createCase(object);
        else if("UPDATE".equals(caseParser.getCaseAction()))
            updateCase(object);
        else if("CLOSE".equals(caseParser.getCaseAction()))
            closeCase(object) ;
    }

    public  abstract void closeCase(T ccCase);

    public  abstract void updateCase(T ccCase);

    public  abstract void createCase(T ccCase);

}
