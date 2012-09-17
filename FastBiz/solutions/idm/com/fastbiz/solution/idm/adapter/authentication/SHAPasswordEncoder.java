package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("shaPasswordEncoder")
public class SHAPasswordEncoder extends PasswordEncoderDelegator{

    public SHAPasswordEncoder() {
        super(new ShaPasswordEncoder());
    }
}
