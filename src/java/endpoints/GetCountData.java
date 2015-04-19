/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DatabaseUtility;
import model.DayOfWeek;
import model.Gym;
import model.GymClass;
import model.containers.CountDatapoint;

/**
 *
 * @author csaroff
 */
@WebServlet(name = "GetCountData", urlPatterns = {"/getcountdata"}, initParams = {
    @WebInitParam(name = "gym", value = ""),
    @WebInitParam(name = "day", value = "")})
public class GetCountData extends HttpServlet {

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
        response.setContentType("text/json;charset=UTF-8");
        Gym gym = Gym.getGym(request.getParameter("gym"));
        DayOfWeek day = DayOfWeek.getDayOfWeek(request.getParameter("day"));
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeArray(
                toJsonArray(DatabaseUtility.getCountData(gym, day)));
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    
    /**
     * Generates a Json Array for the given list of GymClass objects
     * @param gymClasses The list of gym classes to jsonify
     * @return a json array representation of the GymClass list.
     */
    private JsonArray toJsonArray(List<CountDatapoint> countDatapoints){
        Map<String, Object> properties = new HashMap<>();
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonBuilderFactory factory = Json.createBuilderFactory(properties);
        JsonArrayBuilder jsonDatapointsBuilder = factory.createArrayBuilder();
        for(CountDatapoint countDatapoint : countDatapoints){
            jsonDatapointsBuilder.add(toJsonObject(countDatapoint));
        }
        return jsonDatapointsBuilder.build();
    }
    
    /**
     * Converts a gymClass object into a JsonObject.
     * @param gymClass the gymClass object to convert.
     * @return a JsonObject representing the given GymClass
     */
    private JsonObject toJsonObject(CountDatapoint countDatapoint){
        Map<String, Object> properties = new HashMap<>();
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonBuilderFactory factory = Json.createBuilderFactory(properties);
        JsonObject jsonDatapoint = factory.createObjectBuilder()
            .add("count", countDatapoint.getCount())
            .add("timestamp", countDatapoint.getTimestamp())
        .build();
        return jsonDatapoint;
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