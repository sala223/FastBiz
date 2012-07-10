package com.fastbiz.common.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import com.fastbiz.common.utils.Assert;
import com.fastbiz.common.utils.SecurityUtils;

public class EncryptionDataMarshaller implements DataMarshaller{

    private String           algorithm;

    private static final int ENCRYPTION_ALGORITHM_LENGTH = 128;

    public EncryptionDataMarshaller(String algorithm) {
        this.algorithm = algorithm;
    }

    public EncryptionDataMarshaller() {
        this.algorithm = SecurityUtils.SECRET_ALGORITHM_AES;
    }

    public byte[] seal(byte[] data){
        try {
            SecretKey key = SecurityUtils.createSecretKey(algorithm, ENCRYPTION_ALGORITHM_LENGTH);
            byte[] keyBytes = key.getEncoded();
            byte[] securedData = SecurityUtils.encrypt(key, data);
            SecuredDataObject sdo = new SecuredDataObject();
            sdo.setAlgorithm(key.getAlgorithm());
            sdo.setData(securedData);
            sdo.setKey(keyBytes);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            sdo.write(dos);
            return bos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }
    }

    public byte[] disclose(byte[] securedData){
        SecuredDataObject sdo = new SecuredDataObject();
        ByteArrayInputStream bis = new ByteArrayInputStream(securedData);
        DataInputStream dis = new DataInputStream(bis);
        sdo.read(dis);
        return SecurityUtils.decrypt(sdo.getAlgorithm(), sdo.getKey(), sdo.getData());
    }

    private static final class SecuredDataObject{

        private String algorithm;

        private byte[] key;

        private byte[] data;

        public void write(DataOutputStream out){
            Assert.notNull(algorithm);
            Assert.notNull(key);
            Assert.notNull(data);
            try {
                byte[] ab = algorithm.getBytes();
                out.writeInt(ab.length);
                out.write(ab);
                out.writeInt(key.length);
                out.write(key);
                out.writeInt(data.length);
                out.write(data);
            } catch (IOException e) {
                throw new SecurityException(e, "Output secured data error.");
            }
        }

        public void read(DataInputStream in){
            try {
                int length = in.readInt();
                byte[] aBytes = new byte[length];
                in.readFully(aBytes);
                length = in.readInt();
                byte[] keyBytes = new byte[length];
                in.readFully(keyBytes);
                length = in.readInt();
                byte[] edBytes = new byte[length];
                in.readFully(edBytes);
                if (in.read() != -1) {
                    throw new SecurityException("Expect EOF for the inputstream");
                }
                this.algorithm = new String(aBytes);
                this.data = edBytes;
                this.key = keyBytes;
            } catch (IOException e) {
                throw new SecurityException(e);
            }
        }

        String getAlgorithm(){
            return algorithm;
        }

        void setAlgorithm(String algorithm){
            this.algorithm = algorithm;
        }

        byte[] getKey(){
            return key;
        }

        void setKey(byte[] key){
            this.key = key;
        }

        byte[] getData(){
            return data;
        }

        void setData(byte[] data){
            this.data = data;
        }
    }
}
