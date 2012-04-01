package org.motechproject.commcare.gateway;

import org.motechproject.commcare.utils.RequestMapper;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/26/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommcareCaseGateway<T> {

    RequestMapper<T> domainMapper;

    public CommcareCaseGateway(Class<T> clazz){
        domainMapper = new RequestMapper<T>(clazz);
    }

    public void submitCase(T object){

        
    }
    
}
