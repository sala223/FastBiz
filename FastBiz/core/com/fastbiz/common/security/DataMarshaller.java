package com.fastbiz.common.security;

public interface DataMarshaller{

    public byte[] seal(byte[] data);

    public byte[] disclose(byte[] securedData);
}
