/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.blockchain;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;

/**
 *
 * @author avianey
 */
public final class Block {
    
    private String hash;
    private String previousHash;
    private byte [] data; 

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public byte [] getData() {
        return data;
    }
    
    public String  getDataString() {
        return Base64.getEncoder().encodeToString(this.data);
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    private final long timeStamp; 
    
    public Block(String data,String previousHash, PublicKey pk) 
    {
            this.data = encrypt(data, pk);
            this.previousHash = previousHash;
            this.timeStamp = new Date().getTime();
            this.hash = calculateHash();
    }
    
    public byte [] encrypt(String data, PublicKey pk)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");  
            cipher.init(Cipher.ENCRYPT_MODE, pk); 
            return cipher.doFinal(data.getBytes());

        }
        catch(Exception e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    public String decrypt(PrivateKey pk) 
    {

        try
        {
            Cipher cipher = Cipher.getInstance("RSA");  
            cipher.init(Cipher.DECRYPT_MODE, pk);
            byte[] decryptedData = cipher.doFinal(this.data);
            return new String(decryptedData);

        }
        catch(Exception e) 
        {
            throw new RuntimeException(e);
        }
    }
    
 
    public void display()
    {
        System.out.println("Address : " + this.getHash());
        System.out.println("Vote : " + this.getData());
    }
    

    public String calculateHash() {
        
        String dataToHash =     previousHash + 
                                Long.toString(timeStamp) +
                                data;

        try 
        {     
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] strHash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(strHash);
        } 
        catch(NoSuchAlgorithmException e) 
        {
            throw new RuntimeException(e);
        }
    }

    public void display(PrintStream stream) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
