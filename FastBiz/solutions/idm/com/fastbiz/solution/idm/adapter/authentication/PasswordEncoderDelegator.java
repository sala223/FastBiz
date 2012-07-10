package com.fastbiz.solution.idm.adapter.authentication;

public class PasswordEncoderDelegator implements PasswordEncoder,org.springframework.security.authentication.encoding.PasswordEncoder {

    private org.springframework.security.authentication.encoding.PasswordEncoder springEncoder;

    private static final String                                                  SPRING_SALT_LEFT_SEPERATOR  = "{";

    private static final String                                                  SPRING_SALT_RIGHT_SEPERATOR = "}";

    public PasswordEncoderDelegator(org.springframework.security.authentication.encoding.PasswordEncoder springEncoder) {
        this.springEncoder = springEncoder;
    }

    @Override
    public String encodePassword(String rawPassword, Object salt){
        return springEncoder.encodePassword(rawPassword, processSalt(salt));
    }

    @Override
    public boolean isPasswordValid(String encodedPassword, String rawPassword, Object salt){
        return springEncoder.isPasswordValid(encodedPassword, rawPassword, processSalt(salt));
    }

    protected String processSalt(Object salt){
        if (salt == null) {
            return "";
        }
        return salt.toString().replace(SPRING_SALT_LEFT_SEPERATOR, "").replace(SPRING_SALT_RIGHT_SEPERATOR, "");
    }
}
