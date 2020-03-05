/*
 * MagnumApp.java
 */

package magnum;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MagnumApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     public */
  
    @Override protected void startup() {
        try {
            show(new MagnumView(this));
        } catch (InterruptedException ex) {
            Logger.getLogger(MagnumApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MagnumApp
     */
    public static MagnumApp getApplication() {
        return Application.getInstance(MagnumApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
       
            launch(MagnumApp.class, args);            
        
        
        
          
           
       
    
    }

   
    private class SearchTask extends org.jdesktop.application.Task<Object, Void> {
        SearchTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SearchTask fields, here.
            super(app);
        }
        @Override protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }


   

    

}
