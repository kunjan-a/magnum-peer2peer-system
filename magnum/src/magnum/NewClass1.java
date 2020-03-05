/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magnum;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class NewClass1 {
    
    public static void main(String a[]){
        try {
            Vector<Vector<String>> vec = new Vector();
            
            
            Vector<String>[] as=new Vector[12];
          
            File fp = new File("obj");
            XMLEncoder la = new XMLEncoder(new FileOutputStream(fp));
            la.writeObject(vec);
            la.close();
            XMLDecoder dec=new XMLDecoder(new FileInputStream(fp));
            Vector<String> read=(Vector<String>) dec.readObject();
            System.out.println(read.toString()+read.toArray());
            dec.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewClass1.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
    }

}
