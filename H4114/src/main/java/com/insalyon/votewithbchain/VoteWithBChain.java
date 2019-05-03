/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.votewithbchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.insalyon.blockchain.Block;
import java.io.PrintStream;
import java.util.ArrayList;


/**
 *
 * @author avianey
 */
public class VoteWithBChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
    
    public static void main(String[] args) {
       
        Sondage sondage = new Sondage("Où manger ce midi?");
        sondage.addResponseChoice("RU");
        sondage.addResponseChoice("RI");
        sondage.addResponseChoice("Olivier");
        sondage.addResponseChoice("Prevert");
        sondage.addResponseChoice("Grillon");
        
        sondage.addAnswerVote("RU");
        sondage.addAnswerVote("RU");
        sondage.addAnswerVote("RI");
        sondage.addAnswerVote("RI");
        sondage.addAnswerVote("RI");
        sondage.addAnswerVote("Olivier");
        sondage.addAnswerVote("Olivier");
        sondage.addAnswerVote("Olivier");
        sondage.addAnswerVote("Olivier");
        sondage.addAnswerVote("Prevert");
        sondage.addAnswerVote("Grillon");
        sondage.addAnswerVote("Grillon");
        sondage.addAnswerVote("Grillon");
        sondage.addAnswerVote("Grillon");
        
        sondage.showResults (System.out);
        
        sondage.showClearResults (System.out);
        
        
    
        
    }
    
    public static void sendQuestionAndAnswers(PrintStream stream, Sondage sondage)
    {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        final String json = gson.toJson(sondage);
        
    }
    
    public static void setVote(PrintStream stream, Sondage sondage, String response)
    {
        stream.println(sondage.addAnswerVote(response));
        
    }
    
    public static void recapVote(PrintStream stream, Sondage sondage, String address)
    {
        sondage.getVote(stream, address);
       
    }
    
    public static void sendClearResult(PrintStream stream, Sondage sondage)
    {
         sondage.showClearResults(stream);
    }
    
    public static void addContestation(Sondage sondage)
    {
         sondage.addContestation();
    }
    
    
   
    
   
    
    
    
}
