/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DatabaseUtility;
import model.DayOfWeek;
import model.Gym;

/**
 *
 * @author csaroff
 */
@WebServlet(name = "GetAverageInCount", urlPatterns = {"/getaverageincount"}, 
    initParams = { 
        @WebInitParam(name = "gym", value = ""),
        @WebInitParam(name = "day", value = "")})
public class GetAverageInCount extends HttpServlet {

    /**
     * Returns the average number of in's for a given day and gym.  day is an
     * optional parameter with it's default being DayOfWeek.today().
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String gymName = request.getParameter("gym");
        String dayName = request.getParameter("day");
        DayOfWeek day;
        if(dayName!=null&&!dayName.equals("")){
            day = DayOfWeek.getDayOfWeek(dayName);
        }else{
            day = DayOfWeek.today();
        }
        try (PrintWriter out = response.getWriter()) {
            System.out.println(DatabaseUtility.getAverageInCount(Gym.getGym(gymName), day));
            out.println(DatabaseUtility.getAverageInCount(Gym.getGym(gymName), day));
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
