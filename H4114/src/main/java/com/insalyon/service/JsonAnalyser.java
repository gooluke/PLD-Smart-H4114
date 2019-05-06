/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Arthur
 */
public class JsonAnalyser {
    public static String CreateVote(){
        JSONObject vote = new JSONObject();
        
        JSONObject question1 = new JSONObject();
        question1.put("number", "Question 1");
        question1.put("content", "a");
        question1.put("type", "radio");
        question1.put("choice 1", "oui");
        question1.put("choice 2", "non");
        
        JSONObject question2 = new JSONObject();
        question2.put("number", "Question 2");
        question2.put("content", "b");
        question2.put("type", "radio");
        question2.put("choice 1", "oui");
        question2.put("choice 2", "non");
        
        JSONArray listQuestions = new JSONArray();
        listQuestions.add(question1);
        listQuestions.add(question2);
        
        vote.put("title", "Vote 1");
        vote.put("Questions", listQuestions);
        String voteString=vote.toJSONString();
        return voteString;
    } 
    public static void ReadVote(String voteString) throws ParseException{
        JSONParser parser=new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(voteString);
        String title = (String) jsonObject.get("title");
        System.out.println(title);
        JSONArray listQuestions =(JSONArray) jsonObject.get("Questions");
        for(Object question :listQuestions){
            JSONObject questionObject=(JSONObject)question;
            Object[] listObject=questionObject.keySet().toArray();
            String type=getTypeQuestion(questionObject);
            System.out.println("type="+type);
            for (Object o: listObject){
                String key=(String)o;
                String value=(String)questionObject.get(key);
                System.out.println("key="+key+";value="+value);
            }
        }
        /*JSONObject question=(JSONObject)listQuestions.get(0);
        String number = (String) question.get("Question number");
        System.out.println(number);*/
    } 
    public static String VoteToHTML(String voteString) throws ParseException{
        String codeHTML="<table>";
        JSONParser parser=new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(voteString);
        String title = (String) jsonObject.get("title");
        codeHTML+="<tr><th>"+title+"</th></tr>";
        JSONArray listQuestions =(JSONArray) jsonObject.get("Questions");
        for(Object question :listQuestions){
            JSONObject questionObject=(JSONObject)question;
            Object[] listObject=questionObject.keySet().toArray();
            String type=getTypeQuestion(questionObject);
            System.out.println("type="+type);
            for (Object o: listObject){
                String key=(String)o;
                String value=(String)questionObject.get(key);
                codeHTML+="<tr><th>"+key+"</th><td>"+value+"</td></tr>";
                //System.out.println("key="+key+";value="+value);
            }
        }
        codeHTML+="</table>";
        return codeHTML;
        /*JSONObject question=(JSONObject)listQuestions.get(0);
        String number = (String) question.get("Question number");
        System.out.println(number);*/
    } 
    public static String getTypeQuestion(JSONObject questionObject){
        String type=(String)questionObject.get("type");
        return type;
    }
    /*public static void main (String[]args) throws ParseException{
        String voteString=CreateVote();
        System.out.println(voteString);
        String codeHTML=VoteToHTML(voteString);
        System.out.println(codeHTML);
    }*/
}
//<table class="table" style="border: none"><tr><th>Vote 1</th></tr><tr><th>number</th><td>Question 1</td></tr><tr><th>type</th><td>radio</td></tr><tr><th>content</th><td>a</td></tr><tr><th>choice 1</th><td>oui</td></tr><tr><th>choice 2</th><td>non</td></tr><tr><th>number</th><td>Question 2</td></tr><tr><th>type</th><td>radio</td></tr><tr><th>content</th><td>b</td></tr><tr><th>choice 1</th><td>oui</td></tr><tr><th>choice 2</th><td>non</td></tr></table>
