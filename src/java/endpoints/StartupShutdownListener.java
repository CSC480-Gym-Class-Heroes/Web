package endpoints;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.servlet.*;
import model.DatabaseUtility;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.annotation.WebListener;
/**
 *
 * @author csaroff
 */
@WebListener
public class StartupShutdownListener implements ServletContextListener{
    private static ServletContext servletContext;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        try{
            DatabaseUtility.init();
        }catch(IOException ioe){
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static ServletContext getContext(){
        System.out.println(servletContext);
        return servletContext;
    }
}