package com.fastbiz.solution.idm.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName = "HellWordService")
public class HelloWordWS{

    @WebMethod
    public String sayHello(String who){
        return who + " said hello";
    }
}
