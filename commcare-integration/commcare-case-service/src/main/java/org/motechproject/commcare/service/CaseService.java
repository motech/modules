package org.motechproject.commcare.service;

import org.motechproject.commcare.parser.CommcareCaseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/22/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class CaseService<T> {
    CommcareCaseParser<T> caseParser;
    private static Logger logger = LoggerFactory.getLogger(CaseService.class.toString());
    private Class<T> clazz;

    public CaseService(Class<T> clazz){
        this.clazz = clazz;
   }

    @RequestMapping(value="/process",method= RequestMethod.POST)
    public void ProcessCase(@RequestBody String xmlDocument){
        logger.info(xmlDocument);
        System.out.println("xmldoc "+xmlDocument);

        caseParser = new CommcareCaseParser<T>(clazz,xmlDocument);
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
