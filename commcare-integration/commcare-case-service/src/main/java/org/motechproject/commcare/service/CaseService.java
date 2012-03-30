package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.Case;
import org.motechproject.commcare.parser.CommcareCaseParser;
import org.motechproject.commcare.utils.DomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.ServiceMode;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/22/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class CaseService<T> {
    CommcareCaseParser caseParser;
    DomainMapper<T> domainMapper;
    private static Logger logger = LoggerFactory.getLogger(CaseService.class.toString());

   public CaseService(Class<T> clazz){
       this.caseParser = new CommcareCaseParser();
       domainMapper = new DomainMapper<T>(clazz);
   }

    //@POST
    //@Path("/process")
    @RequestMapping(value="/process",method= RequestMethod.POST)
    public void ProcessCase(@RequestBody String xmlDocument){
        logger.info(xmlDocument);
        System.out.println("xmldoc "+xmlDocument);

        Case ccCase = caseParser.parseCase(xmlDocument);
        T object = domainMapper.mapToDomainObject(ccCase);
        
        if(ccCase.getAction().equals("CREATE"))
            createCase(object);
        else if(ccCase.getAction().equals("UPDATE"))
            updateCase(object);
        else if(ccCase.getAction().equals("CLOSE"))
            closeCase(object) ;
    }

    //@GET
    //@Path("/test")
    public String Test(){
        return "Hello World";

    }

    protected  abstract void closeCase(T ccCase);

    protected  abstract void updateCase(T ccCase);

    protected  abstract void createCase(T ccCase);

}
