/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Assembly;
import model.Participant;
import model.User;
import service.DBConnection;

/**
 *
 * @author Arthur
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action=request.getParameter("action"); 
        Connection conn=null;
        String email,password;
        User user;
        Participant participant;
        switch(action){
            case "connect":
                try {
                    email=request.getParameter("email");
                    password=request.getParameter("password");
                    JsonObject connection=new JsonObject();
                    JsonObject connect=new JsonObject();
                    conn = DBConnection.Connection();
                    user = User.Connect(conn, email, password);
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        if(user != null){
                            connect.addProperty("connect", "successful");
                            request.getSession().setAttribute("user", user);
                        }
                        else{
                            connect.addProperty("connect", "failed");
                        }
                        connection.add("connect", connect);
                        out.println(gson.toJson(connection));
                    }
                }
                catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                
            case "inscription":
                email=request.getParameter("email");
                String pseudo=request.getParameter("pseudo");
                password=request.getParameter("password");
                JsonObject inscription=new JsonObject();
                try {
                    conn = DBConnection.Connection();
                    boolean exist=User.UserExist(conn, email,pseudo);
                    System.out.println(exist);
                    if(!exist){
                        user = new User(email,password, pseudo);
                        if(User.Insert(conn, user)){
                         
                            try (PrintWriter out = response.getWriter()) {
                                Gson gson=new GsonBuilder().setPrettyPrinting().create();
                                JsonObject inscrit=new JsonObject();
                                inscrit.addProperty("inscrit", "true");
                                inscription.add("inscrit", inscrit);
                                out.println(gson.toJson(inscription));
                            }
                        }
                        else{
                            try (PrintWriter out = response.getWriter()) {
                                Gson gson=new GsonBuilder().setPrettyPrinting().create();
                                JsonObject inscrit=new JsonObject();
                                inscrit.addProperty("inscrit", "false");
                                inscription.add("inscrit", inscrit);
                                out.println(gson.toJson(inscription));
                            }
                        }
                    }
                    else{
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson=new GsonBuilder().setPrettyPrinting().create();
                            JsonObject inscrit=new JsonObject();
                            inscrit.addProperty("inscrit", "false");
                            inscription.add("inscrit", inscrit);
                            out.println(gson.toJson(inscription));
                        }
                    }
                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                
            case "profil":
                try {
                    email=(String)request.getSession().getAttribute("email");
                    PrintWriter out = response.getWriter();
                    Gson gson=new GsonBuilder().setPrettyPrinting().create();
                    ResultSet rs;
                    conn = DBConnection.Connection();
                    rs = User.FindUserWithEmail(conn, email);
                    JsonObject jsonCompte=new JsonObject();
                    while (rs.next()){
                        jsonCompte.addProperty("id_user", rs.getString(1));
                        jsonCompte.addProperty("email", rs.getString(2));
                        jsonCompte.addProperty("pseudo", rs.getString(3));
                    }
                    rs.beforeFirst();
                    JsonObject container=new JsonObject();
                    container.add("profil", jsonCompte);
                    out.println(gson.toJson(container));
                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                
            case "joinAssembly":
                
                try {
                    JsonObject joinAssembly =new JsonObject();
                    conn = DBConnection.Connection();
                    
                    user = (User) request.getSession().getAttribute("user");
                    participant = (Participant) request.getSession().getAttribute("participant");
                    String idAssembly = request.getParameter("id_assembly");   
                    Assembly assembly;
                    boolean isAlreadyPartAssembly;
                  
                    isAlreadyPartAssembly = User.IsPartAssembly(conn,user);
                    assembly = Assembly.getAssembly(conn, idAssembly);
                    if (isAlreadyPartAssembly || participant != null)
                    {
                        break;
                    }
  
                    String latitude = request.getParameter("latitiude");
                    String longitiude = request.getParameter("longitude");
                    String status = request.getParameter("status");
                
                    if (user == null)
                    {
                        break;
                    }
                                   
                    participant = new Participant(  user,
                            assembly,
                            latitude,
                            longitiude,
                            status);

                if(Participant.Insert(conn, participant)){
                    
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject participate=new JsonObject();
                        participate.addProperty("participate", "true");
                        joinAssembly.add("participate", participate);
                        out.println(gson.toJson(joinAssembly));
                        
                        request.getSession().setAttribute("participant", participant);
                    }
                    
                    
                }
                else{
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject participate=new JsonObject();
                        participate.addProperty("participate", "false");
                        joinAssembly.add("participate", participate);
                        out.println(gson.toJson(joinAssembly));
                    }
                }
            } catch (SQLException | ParseException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            
            case "getPseudo":
                JsonObject getPseudo =new JsonObject();
                
                user = (User) request.getSession().getAttribute("participant");
                if (user == null)
                {
                    break;
                }
                
                String pseudoU = user.getPseudo();
                
                participant = (Participant) request.getSession().getAttribute("participant");
                if (participant != null)
                {
                    
                    String statusU = participant.getStatus();
                    
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject parti =new JsonObject();
                        
                        parti.addProperty("psuedo", pseudoU);
                        parti.addProperty("status", statusU.contains("1"));
                        getPseudo.add("participate", parti);
                        
                        out.println(gson.toJson(getPseudo));
                    }
                }
                else
                {
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject parti =new JsonObject();
                        
                        parti.addProperty("psuedo", pseudoU);
                        parti.addProperty("status", false);
                        getPseudo.add("participate", parti);
                        
                        out.println(gson.toJson(getPseudo));
                    }
                    
                }
                
            break;
            
            case "createAssembly":
                
                try {
                    JsonObject createAssembly =new JsonObject();
                    conn = DBConnection.Connection();
                    
                    user = (User) request.getSession().getAttribute("user");
                    participant = (Participant) request.getSession().getAttribute("participant");
                    if (participant != null)
                    {
                        break;
                    }
                    
                    String title = request.getParameter("title");  
                    String description = request.getParameter("description");  
                    String colour = request.getParameter("colour"); 
                    String date = request.getParameter("date");
                    String place = request.getParameter("place");
                    String radio = request.getParameter("radio");
                    
                    Assembly assembly = new Assembly(0, title, description, place, date, radio, colour);
                    
  
                    String latitude = request.getParameter("latitiude");
                    String longitiude = request.getParameter("longitude");
                    
                
                    if (user == null)
                    {
                        break;
                    }
                                   
                    participant = new Participant(  user,
                            assembly,
                            latitude,
                            longitiude,
                            "2");

                    if(Assembly.Insert(conn, assembly)){

                        try (PrintWriter out = response.getWriter()) {
                            request.getSession().setAttribute("assembly", assembly);
                        }

                    }
                    else{
                        break;
                    }
               

                if(Participant.Insert(conn, participant)){
                    
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject participate=new JsonObject();
                        participate.addProperty("created", "true");
                        createAssembly.add("createAssembly", participate);
                        out.println(gson.toJson(createAssembly));
                        
                        request.getSession().setAttribute("participant", participant);
                    }
                    
                    
                }
                else{
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson=new GsonBuilder().setPrettyPrinting().create();
                        JsonObject participate=new JsonObject();
                        participate.addProperty("created", "false");
                        createAssembly.add("createAssembly", participate);
                        out.println(gson.toJson(createAssembly));
                    }
                }
            } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            default:
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
