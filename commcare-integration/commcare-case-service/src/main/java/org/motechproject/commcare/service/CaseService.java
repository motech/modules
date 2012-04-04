package org.motechproject.commcare.service;

import org.motechproject.commcare.parser.CommcareCaseParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/22/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class CaseService<T> {
   // private static Logger logger = LoggerFactory.getLogger(CaseService.class.toString());
    private Class<T> clazz;

    public CaseService(Class<T> clazz){
        this.clazz = clazz;
   }

    @RequestMapping(value="/process",method= RequestMethod.POST)
    public void ProcessCase(@RequestBody String xmlDocument,Writer writer){
     //   logger.info(xmlDocument);
        System.out.println("xmldoc "+xmlDocument);

        CommcareCaseParser<T>  caseParser = new CommcareCaseParser<T>(clazz,xmlDocument);
        T  object = caseParser.parseCase();

        if("CREATE".equals(caseParser.getCaseAction()))
            createCase(object,writer);
        else if("UPDATE".equals(caseParser.getCaseAction()))
            updateCase(object,writer);
        else if("CLOSE".equals(caseParser.getCaseAction()))
            closeCase(object,writer) ;
    }

    public  abstract void closeCase(T ccCase,Writer writer);

    public  abstract void updateCase(T ccCase,Writer writer);

    public  abstract void createCase(T ccCase,Writer writer);

}
