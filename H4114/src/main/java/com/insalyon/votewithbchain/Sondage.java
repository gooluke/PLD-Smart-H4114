/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package com.insalyon.votewithbchain;

import com.insalyon.blockchain.Block;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import static com.insalyon.votewithbchain.VoteWithBChain.blockchain;

/**
 *
 * @author avianey
 */
public class Sondage {
    
    private final String question;
    Map<String, Integer> responses;
    PrivateKey privateKey;
    PublicKey publicKey;
    int contestation;
    
    private ArrayList<Block> blockchain; 
    
    public Sondage(String question)
    {
        this.question = question;
        
         try { 
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            this.publicKey = kp.getPublic();
            this.privateKey = kp.getPrivate();
            this.blockchain = new ArrayList<>();
            this.responses = new HashMap<> ();
           
        }
        catch(NoSuchAlgorithmException e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    public void addResponseChoice (String response)
    {
        if (!response.isEmpty())
        {
            this.responses.put(response, 0);
        }
    }
    
    public String addAnswerVote(String data)
    {
        String lastHash = "0";
        if (this.blockchain.size() > 0)
        {
            lastHash = this.blockchain.get(this.blockchain.size()-1).getHash();
        }
        
        Block block = new Block(data, lastHash, this.publicKey);
        this.blockchain.add(block);
        return block.getHash();
    }
    
    public void showResults (PrintStream stream)
    {
        stream.println(this.question+ ":");
        
        for(int i=0; i < this.blockchain.size(); i++) 
        {
            stream.println("Vote " + i + " : ");
            Block block = this.blockchain.get(i);
            block.display(stream);        
        }
    }
    
    public void addContestation()
    {
        this.contestation++;
    }

    @Override
    public String toString() {
        return "Sondage{" + "question=" + question + 
                ", responses=" + responses +
                ", publicKey=" + publicKey + '}';
    }
    
    
    
    
    
    public void showClearResults (PrintStream stream)
    {
        System.out.println(this.question  + ": \n");
        
        for(int i=0; i < this.blockchain.size(); i++) 
        {
           stream.println("Vote " + i + " : ");
            Block block = this.blockchain.get(i);
            String choice =  block.decrypt(this.privateKey);
            
            if (responses.containsKey(choice))
            {
                Integer nb = responses.get(choice) + 1;
                responses.put(choice, nb);
            }
            else
            {
                Integer nb = responses.getOrDefault("", 0) + 1;
                responses.put("", nb);
                
                System.out.println("White vote");
            }
            
            stream.println("Address : " + block.getHash());
            stream.println("Vote : " + choice);
           
        }
        
        stream.println("Bilan :" + responses.toString());
    }
    
    public void getVote(PrintStream stream, String address)
    {
        for (int i = 0; i < this.blockchain.size(); i++)
        {
           if (this.blockchain.get(i).getHash().equals(address))
           {
               stream.println(this.blockchain.get(i).decrypt(privateKey));
           }
        }
    }
    
    public void display(PrintStream stream) {
        for(int i=0; i < this.blockchain.size(); i++) 
        {
            stream.println("Hash for block " + i + " : " + this.blockchain.get(i).getHash());
            stream.println("Data for block " + i + " : " + this.blockchain.get(i).getData());
        }
    }
    
    public Boolean isChainValid() {
        
	Block currentBlock; 
	Block previousBlock;
	
            for(int i=1; i < this.blockchain.size(); i++) 
            {
                currentBlock = this.blockchain.get(i);
                previousBlock = this.blockchain.get(i-1);
               
                if(!currentBlock.getHash().equals(currentBlock.calculateHash()) )
                {
                        System.out.println("Current Hashes not equal");			
                        return false;
                }

                
                if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) 
                {
                        System.out.println("Previous Hashes not equal");
                        return false;
                }
            }
            return true;
    }
    
    
     
    
    
}
