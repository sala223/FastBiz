package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SHAPasswordEncoder extends PasswordEncoderDelegator{

    public SHAPasswordEncoder() {
        super(new ShaPasswordEncoder());
    }
}
