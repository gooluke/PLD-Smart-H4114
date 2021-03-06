/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.user;

import com.google.gson.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.DBConnection;

/**
 *
 * @author Arthur
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
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
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        Connection conn = null;
        switch (action) {
            case "connect":
                try {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    JsonObject connection = new JsonObject();
                    JsonObject connect = new JsonObject();
                    DBConnection dbConnection = new DBConnection();
                    conn = dbConnection.getConnection();
                    System.out.println("I will do DBConnection.Connect");
                    boolean flag = DBConnection.Connect(email, password, conn);
                    System.out.println("Well I finished DBConnection.Connect");
                    dbConnection.close();
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        if (flag) {
                            connect.addProperty("connect", "successful");
                            request.getSession().setAttribute("email", email);
                        } else {
                            connect.addProperty("connect", "failed");
                        }
                        connection.add("connect", connect);
                        out.println(gson.toJson(connection));
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "inscription":
                try {
                    String email = request.getParameter("email");
                    String pseudo = request.getParameter("pseudo");
                    String password = request.getParameter("password");
                    JsonObject inscription = new JsonObject();
                    DBConnection dbConnection = new DBConnection();
                    conn = dbConnection.getConnection();
                    System.out.println("I will do DBConnection.Inscription");
                    int resultInsert = DBConnection.Insert(conn, email, pseudo, password);
                    System.out.println("Well I finished DBConnection.Inscription");
                    dbConnection.close();
                    if (resultInsert != -1) {
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonObject inscrit = new JsonObject();
                            inscrit.addProperty("inscrit", "true");
                            inscription.add("inscrit", inscrit);
                            out.println(gson.toJson(inscription));
                        }
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "profil":
                try {
                    String email = (String) request.getSession().getAttribute("email");
                    PrintWriter out = response.getWriter();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    ResultSet rs;
                    DBConnection dbConnection = new DBConnection();
                    conn = dbConnection.getConnection();
                    rs = DBConnection.FindUserWithEmail(email, conn);
                    dbConnection.close();
                    JsonObject jsonCompte = new JsonObject();
                    while (rs.next()) {
                        jsonCompte.addProperty("id_user", rs.getString(1));
                        jsonCompte.addProperty("email", rs.getString(2));
                        jsonCompte.addProperty("pseudo", rs.getString(3));
                    }
                    rs.beforeFirst();
                    JsonObject container = new JsonObject();
                    container.add("profil", jsonCompte);
                    out.println(gson.toJson(container));
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "create rally":
                try {

                    String rally = request.getParameter("rally");
                    String description = request.getParameter("description");
                    String place = request.getParameter("place");
                    String date = request.getParameter("date");
                    String time = request.getParameter("time");
                    String radio = request.getParameter("radio");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");

                    DBConnection dbConnection = new DBConnection();
                    conn = dbConnection.getConnection();
                    JsonObject createRally = new JsonObject();
                    JsonObject rallyCreated = new JsonObject();

                    if (DBConnection.Connect(email, password, conn)) {
                        ResultSet resultSet = DBConnection.FindUserWithEmail(email, conn);
                        resultSet.next();
                        int moderator = Integer.parseInt(resultSet.getString(1));
                        resultSet.beforeFirst();

                        System.out.println("I will do DBConnection.createRally");
                        int resultInsert = DBConnection.createRally(conn, rally, description, place, date, time, radio, moderator);
                        System.out.println("Well I finished DBConnection.createRally");
                        dbConnection.close();
                        if (resultInsert != -1) {
                            try (PrintWriter out = response.getWriter()) {
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                rallyCreated.addProperty("rallyCreated", "true");
                                createRally.add("createRally", rallyCreated);
                                out.println(gson.toJson(createRally));
                            }
                        }
                    } else {
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            rallyCreated.addProperty("rallyCreated", "false");
                            createRally.add("createRally", rallyCreated);
                            out.println(gson.toJson(createRally));
                        }

                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
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
