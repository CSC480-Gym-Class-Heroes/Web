/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import model.Gym;
import model.DatabaseUtility;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
/**
 *
 * @author csaroff
 */
@WebServlet(name = "UpdateCount", urlPatterns = {"/updatecount"})
public class UpdateCount extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @baseURL /updatecount
     * @requestParameter gym The name of the gym to update the count for.
     * @requestParameter timestamp A long value representing milliseconds since 
     * the epoch when someone last walked in or out of the gym.
     * @requestParameter count The number of people who are currently in the gym.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        String gymName = request.getParameter("gym");
        String strDate = request.getParameter("timestamp");
        String strCount = request.getParameter("count");
        String strIn = request.getParameter("in");
        
        if(gymName==null||strDate==null||strCount==null||strIn==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else{
            try{
                java.util.Date date = new java.util.Date(Long.parseLong(strDate));
                int count = Integer.parseInt(strCount);
                boolean in = Boolean.parseBoolean(strIn);
                System.out.println(in);
                DatabaseUtility.updateCurrentCount(Gym.getGym(gymName), date, count, in);     
                response.setStatus(HttpServletResponse.SC_OK);
            }catch(NumberFormatException | SQLException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
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