package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class MD5PasswordEncoder extends PasswordEncoderDelegator{

    public MD5PasswordEncoder() {
        super(new Md5PasswordEncoder());
    }
}
