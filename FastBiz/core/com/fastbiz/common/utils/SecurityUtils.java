package com.fastbiz.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import com.fastbiz.common.security.CertificateInfo;
import com.fastbiz.common.security.KeyInfo;
import com.fastbiz.common.security.KeyStoreInfo;
import com.fastbiz.common.security.SecurityException;

;
public abstract class SecurityUtils{

    public static final String CERTIFICATE_TYPE_X509  = "X.509";

    public static final String KEY_STORE_TYPE_PKCS12  = "PKCS12";

    public static final String KEY_STORE_TYPE_PKCS11  = "PKCS11";

    public static final String KEY_STORE_TYPE_JKS     = "jks";

    public static final String SECRET_ALGORITHM_AES   = "AES";

    public static final String KEY_PAIR_ALGORITHM_RSA = "RSA";

    public static final String KEY_PAIR_ALGORITHM_DSA = "DSA";

    public static SecretKey createSecretKey(String algorithm, int bit) throws NoSuchAlgorithmException{
        Assert.notNull(algorithm);
        KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
        kgen.init(bit);
        return kgen.generateKey();
    }

    public static SecretKey createAESSecretKey() throws NoSuchAlgorithmException{
        KeyGenerator kgen = KeyGenerator.getInstance(SECRET_ALGORITHM_AES);
        kgen.init(128);
        return kgen.generateKey();
    }

    public static KeyStore createKeyStore(KeyStoreInfo keyStoreInfo, boolean overwrite){
        Assert.notNull(keyStoreInfo);
        Assert.notNull(keyStoreInfo.getFilePath());
        Assert.notNull(keyStoreInfo.getType());
        Assert.notNull(keyStoreInfo.getFilePath());
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(keyStoreInfo.getType());
            File keyStoreFile = new File(keyStoreInfo.getFilePath());
            if (keyStoreFile.isFile()) {
                if (overwrite) {
                    keyStore.load(null, keyStoreInfo.getPassword());
                } else {
                    keyStore.load(new FileInputStream(keyStoreFile), keyStoreInfo.getPassword());
                }
            } else {
                keyStore.load(null, keyStoreInfo.getPassword());
            }
            keyStore.load(null, keyStoreInfo.getPassword());
            keyStore.store(new FileOutputStream(keyStoreFile), keyStoreInfo.getPassword());
            return keyStore;
        } catch (KeyStoreException e) {
            throw new SecurityException(e, "Provider not support keystore type %s?", keyStoreInfo.getType());
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (CertificateException e) {
            throw new SecurityException(e);
        } catch (IOException e) {
            throw new SecurityException(e);
        }
    }

    public static void genKeyPair(KeyStoreInfo keyStoreInfo, KeyInfo keyInfo, CertificateInfo certificateInfo){
        try {
            Assert.notNull(keyInfo);
            Assert.notNull(keyInfo.getAlgorithm());
            Assert.notNull(keyInfo.getAlias());
            Assert.notNull(keyInfo.getPassword());
            Assert.notNull(certificateInfo);
            Assert.notNull(certificateInfo.getSignatureAlgorithm());
            Assert.notNull(certificateInfo.getIssuer());
            KeyStore keyStore = createKeyStore(keyStoreInfo, false);
            KeyPair keyPair = createKeyPair(keyInfo.getAlgorithm(), null);
            X509Certificate certificate = createX509V3Certificate(keyPair, certificateInfo);
            Certificate[] certs = new Certificate[] { certificate };
            keyStore.setKeyEntry(keyInfo.getAlias(), keyPair.getPrivate(), keyInfo.getPassword(), certs);
            FileOutputStream out = new FileOutputStream(keyStoreInfo.getFilePath());
            keyStore.store(out, keyStoreInfo.getPassword());
        } catch (Throwable e) {
            String fmt = new String("Failed create key pair with keystore=%s, key=%s,certificate=%s");
            throw new SecurityException(e, fmt, keyStoreInfo, keyInfo, certificateInfo);
        }
    }

    public static void exportCertificate(KeyStoreInfo keyStoreInfo, String alias, String exportFilePath){
        try {
            Assert.notNull(keyStoreInfo);
            Assert.notNull(keyStoreInfo.getFilePath());
            Assert.notNull(keyStoreInfo.getType());
            Assert.notNull(keyStoreInfo.getFilePath());
            Assert.notNull(alias);
            KeyStore keyStore = KeyStore.getInstance(keyStoreInfo.getType());
            File keyStoreFile = new File(keyStoreInfo.getFilePath());
            keyStore.load(new FileInputStream(keyStoreFile), keyStoreInfo.getPassword());
            Certificate certificate = keyStore.getCertificate(alias);
            byte[] encoded = certificate.getEncoded();
            FileOutputStream out = new FileOutputStream(exportFilePath);
            out.write(encoded);
            out.close();
        } catch (Throwable e) {
            String fmt = "Failed export certificate to %s with alias %s from %s";
            throw new SecurityException(e, fmt, exportFilePath, alias, keyStoreInfo.getFilePath());
        }
    }

    public static KeyPair createKeyPair(String algorithm, AlgorithmParameterSpec params){
        Assert.notNull(algorithm);
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(algorithm);
            if (params != null) {
                generator.initialize(params);
            }
            return generator.genKeyPair();
        } catch (Throwable ex) {
            throw new SecurityException(ex, "Failed create key pair with algorithm parameters %s", params.toString());
        }
    }

    public static X509Certificate createX509V3Certificate(KeyPair keyPair, CertificateInfo certificate){
        Assert.notNull(keyPair);
        String keyPairAlg = keyPair.getPublic().getAlgorithm();
        if (!KEY_PAIR_ALGORITHM_RSA.equals(keyPairAlg)) {
            String fmt = new String("Only support create a certificate with %s key pair");
            throw new SecurityException(fmt, KEY_PAIR_ALGORITHM_RSA);
        }
        Assert.notNull(certificate);
        Assert.notNull(certificate.getSignatureAlgorithm());
        Assert.notNull(certificate.getIssuer());
        Assert.notNull(certificate.getSubject());
        Assert.notNull(certificate.getValidFrom());
        Assert.notNull(certificate.getValidTo());
        PublicKey publicKey = keyPair.getPublic();
        BigInteger serialNumber = BigInteger.valueOf(certificate.getSerialNumber());
        X500Name subject = X500Name.getInstance(new X509Principal(certificate.getSubject()).getDERObject());
        X500Name issuer = X500Name.getInstance(new X509Principal(certificate.getIssuer()).getDERObject());
        DefaultSignatureAlgorithmIdentifierFinder saiFinder = new DefaultSignatureAlgorithmIdentifierFinder();
        AlgorithmIdentifier sigAlgId = saiFinder.find(certificate.getSignatureAlgorithm());
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        try {
            DERObject keyObj = new ASN1InputStream(new ByteArrayInputStream(publicKey.getEncoded())).readObject();
            SubjectPublicKeyInfo spki = SubjectPublicKeyInfo.getInstance(keyObj);
            Date vf = certificate.getValidFrom();
            Date vt = certificate.getValidTo();
            X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuer, serialNumber, vf, vt, subject, spki);
            AsymmetricKeyParameter akp = PrivateKeyFactory.createKey(keyPair.getPrivate().getEncoded());
            ContentSigner cs = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(akp);
            X509CertificateHolder holder = builder.build(cs);
            X509CertificateStructure eeX509CertificateStructure = holder.toASN1Structure();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
            X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
            is1.close();
            return theCert;
        } catch (Exception ex) {
            throw new SecurityException(ex);
        }
    }

    public static byte[] encrypt(SecretKey key, byte[] content){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(content);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }

    public static byte[] decrypt(String algorithm, byte[] key, byte[] source){
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(source);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    public static char[] getRandomPassword(int length){
        Assert.isTrue(length > 0 && length < 500, "password length is restricted from 1 to 128");
        char[] pwd = new char[length];
        for (int pos = 0; pos < length; pos++) {
            char nextChar = getRandomChar();
            pwd[pos] = nextChar;
        }
        return pwd;
    }

    public static char getRandomChar(){
        Random random = new Random();
        char newChar;
        int next = random.nextInt();
        int type = random.nextInt() % 3;
        int d;
        switch (type) {
        case 0:
            d = next % 10;
            if (d < 0) {
                d = d * (-1);
            }
            newChar = (char) (d + 48);
            break;
        case 1:
            d = next % 26;
            if (d < 0) {
                d = d * (-1);
            }
            newChar = (char) (d + 97);
            break;
        default:
            d = (next % 26);
            if (d < 0) {
                d = d * (-1);
            }
            newChar = (char) (d + 65);
        }
        return newChar;
    }
}
