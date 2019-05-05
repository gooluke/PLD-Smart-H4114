package com.insalyon.videostream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pacabrera
 */
@WebServlet(urlPatterns = {"/ActionServlet2"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            System.out.println("processing");

            String action = request.getParameter("action");
            if (action.equals("room")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject jsonResponse = new JsonObject();
                HashMap<String, Boolean> rooms = ServerEndPoint.getServerEndPointState();
                JsonArray jsonListe = new JsonArray();
                for (Map.Entry<String, Boolean> entry : rooms.entrySet()) {
                    if(Objects.equals(entry.getValue(), Boolean.FALSE)){
                        JsonObject json = new JsonObject();
                        json.addProperty("num", entry.getKey());
                        jsonListe.add(json);
                    }
                }
                jsonResponse.add("rooms", jsonListe);
                out.println(gson.toJson(jsonResponse));
                out.close();
            } else if(action.equals("create")){
                
            } else if(action.equals("join")){
                 Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject jsonResponse = new JsonObject();
                HashMap<String, Boolean> rooms = ServerEndPoint.getServerEndPointState();
                String number = request.getParameter("number");
                if(Objects.equals(rooms.get(number), Boolean.FALSE)){
                        jsonResponse.addProperty("join", "true");
                }else{
                    jsonResponse.addProperty("join", "false");
                }
                out.println(gson.toJson(jsonResponse));
                out.close();
            }
        }
    }
    
    



    /*public static void printListePersonnes(PrintWriter out, List<Service.Personne> personnes) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonArray jsonListe = new JsonArray();
        for (Service.Personne p : personnes) {
            JsonObject jsonPersonne = new JsonObject();
            jsonPersonne.addProperty("id", p.getId());
            jsonPersonne.addProperty("civilite", p.getCivilite());
            jsonPersonne.addProperty("nom", p.getNom());
            jsonPersonne.addProperty("prenom", p.getPrenom());
            jsonPersonne.addProperty("mail", p.getMail());
            jsonPersonne.addProperty("adresse", p.getAdresse());
            jsonListe.add(jsonPersonne);
        }
        JsonObject container = new JsonObject();
        container.add("personnes", jsonListe);
        out.println(gson.toJson(container));
    }*/
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
        processRequest(request, response);
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
        processRequest(request, response);
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

    /* private void printPersonne(PrintWriter out, Service.Personne p) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonPersonne = new JsonObject();
        jsonPersonne.addProperty("id", p.getId());
        jsonPersonne.addProperty("civilite", p.getCivilite());
        jsonPersonne.addProperty("nom", p.getNom());
        jsonPersonne.addProperty("prenom", p.getPrenom());
        jsonPersonne.addProperty("mail", p.getMail());
        jsonPersonne.addProperty("adresse", p.getAdresse());
        jsonPersonne.addProperty("dateNaissance", p.getDateNaissance().toString());
        out.println(gson.toJson(jsonPersonne));
    }*/
   
}