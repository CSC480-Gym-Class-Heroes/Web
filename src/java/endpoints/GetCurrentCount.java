package endpoints;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import model.DatabaseUtility;
import model.Gym;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * Sends a client the current number of customers in the 
 * given gym.
 * @baseURL /getcurrentcount
 * @requestParameter gym The name of the gym from which we want to retrieve the 
 * number of customers.
 * @author Chaskin Saroff
 */
@WebServlet(name = "GetCurrentCount", urlPatterns = {"/getcurrentcount", })
public class GetCurrentCount extends HttpServlet {
    
    /**
     * Sends a client the current number of customers in the given gym.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        //System.out.println("Get Current Count");
        //response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/json;charset=UTF-8");
        String gymName = request.getParameter("gym");
        System.out.println("Get count was called \"" + gymName);
        try (PrintWriter out = response.getWriter()) {
            out.println(DatabaseUtility.getCurrentCount(Gym.getGym(gymName)));
        }catch(IOException ioe){
            throw new RuntimeException(ioe);
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
}
