package com.fastbiz.solution.idm.adapter.authentication;

public interface PasswordEncoder{

    String encodePassword(String rawPassword, Object salt);

    boolean isPasswordValid(String encodedPassword, String rawPassword, Object salt);
}
