/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;

/**
 * Creates an advertisement class that stores following information:
 *<br>
 * ID: id associated with a peer/group
 * <br>
 * Name: name associated with a peer/group
 * @author fireball
 */
public class SendPeerAdv extends Advertisement implements Comparable, Cloneable, Serializable{
    private ID id = ID.nullID;
    private String ip;
    private String peer_name;
    private String peer_nickname;
    private String peer_group;
    private String status;
    private PipeID  pipeid=null;
    private final static Logger LOG = Logger.getLogger(SendPeerAdv.class.getName());
    static String idTag = "ID";
    private final static String ipTag = "Ip";
    private final static String nameTag = "Name";
    private final static String statusTag="Status";
    private final static String groupTag="Group";
    private final static String nicknameTag = "NickName";
    private final static String pipeTag="PipeID";
    private final static String[] fields = {idTag, ipTag,nameTag,statusTag,nicknameTag,groupTag,pipeTag};
    public SendPeerAdv() {
    }
     
    public SendPeerAdv(Element root) {
        TextElement doc = (TextElement) root;

        if (!getAdvertisementType().equals(doc.getName())) {
            throw new IllegalArgumentException(
                    "Could not construct : " + getClass().getName() + "from doc containing a " + doc.getName());
        }
        initialize(doc);
    }
    
     public SendPeerAdv(InputStream stream) throws IOException {
        StructuredTextDocument doc = (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, stream);
        initialize(doc);
      
        
    }

    public void setStatus(String stat){
        this.status=stat;
    }
    
     public void setID(ID id) {
        this.id = (id == null ? null : id);
    }
     
     public void setIP(String ip) {
        this.ip = ip;
    }
     
      public void setName(String name) {
        this.peer_name = name;
    }
   public void setNickName(String nickname) {
        this.peer_nickname = nickname;
    }
   public void setGroup(String groupname) {
        this.peer_group = groupname;
    }
public void setPipeid(PipeID pipeid){
    this.pipeid=pipeid==null?null:pipeid;
    
}

    @Override
    public Document getDocument(MimeMediaType asMimeType) {
          StructuredDocument adv = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
          if (adv instanceof Attributable) {
            ((Attributable) adv).addAttribute("xmlns:jxta", "http://jxta.org");
            
        }
          Element e;
           e = (Element) adv.createElement(idTag, getID().toString());
           adv.appendChild(e);
            e = (Element) adv.createElement(nameTag, getName().trim());
           adv.appendChild(e);
            e = (Element) adv.createElement(ipTag, getIP().trim());
        adv.appendChild(e);
        e = (Element) adv.createElement(statusTag, getStatus().trim());
        adv.appendChild(e);   
        e = (Element) adv.createElement(nicknameTag, getNickName().trim());
        adv.appendChild(e);
        e = (Element) adv.createElement(groupTag, getGroup().trim());
        adv.appendChild(e);
e = (Element) adv.createElement(pipeTag, getPipeId().toString());
        adv.appendChild(e);
        
        return adv;
    }


    @Override
        public ID getID() {
            return (id == null ? null : id);
        }
        public String getStatus(){
            return status;
        }
         public String getIP() {
            return ip;
        }
         public String getName() {
            return peer_name;
        }
         public String getNickName() {
            return peer_nickname;
        }
         public String getGroup() {
            return peer_group;
        }
        public ID getPipeId() {
            return pipeid==null?null:pipeid;
        }


protected boolean handleElement(TextElement elem) {
        if (elem.getName().equals(idTag)) {
            try {
                URI id = new URI(elem.getTextValue());

                setID(IDFactory.fromURI(id));
            } catch (URISyntaxException badID) {
                throw new IllegalArgumentException("unknown ID format in advertisement: " + elem.getTextValue());
            } catch (ClassCastException badID) {
                throw new IllegalArgumentException("Id is not a known id type: " + elem.getTextValue());
            }
            return true;
        }
        if (elem.getName().equals(nameTag)) {
            setName(elem.getTextValue());
            return true;
        }
           
   
        if (elem.getName().equals(ipTag)) {
            setIP(elem.getTextValue());
            return true;
        }       
        if(elem.getName().equals(statusTag )){
            setStatus(elem.getTextValue());
            return true;
        }
        if(elem.getName().equals(groupTag )){
            setGroup(elem.getTextValue());
            return true;
        }
        if(elem.getName().equals(nicknameTag )){
            setNickName(elem.getTextValue());
            return true;
        }
        
        if(elem.getName().equals(pipeTag)){
            try {
                URI pipeuri = new URI(elem.getTextValue());

                setPipeid(PipeID.create(pipeuri));
                return true;
            } catch (URISyntaxException ex) {
                Logger.getLogger(SendPeerAdv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // element was not handled
        return false;
  
    }

 protected void initialize(Element root) {
        if (!TextElement.class.isInstance(root)) {
            throw new IllegalArgumentException(getClass().getName() + " only supports TextElement");
        }
        TextElement doc = (TextElement) root;

        if (!doc.getName().equals(getAdvertisementType())) {
            throw new IllegalArgumentException(
                    "Could not construct : " + getClass().getName() + "from doc containing a " + doc.getName());
        }
        Enumeration elements = doc.getChildren();

        while (elements.hasMoreElements()) {
            TextElement elem = (TextElement) elements.nextElement();

            if (!handleElement(elem)) {
                LOG.warning("Unhandleded element \'" + elem.getName() + "\' in " + doc.getName());
            }
        }
    }

    @Override
    public String[] getIndexFields() {
        return fields;
        
       }
     @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SendPeerAdv) {
            SendPeerAdv adv = (SendPeerAdv) obj;

            return getID().equals(adv.getID());
        }

        return false;
    }
     


    public int compareTo(Object other) {
         return getID().toString().compareTo(other.toString());
       
    }
     public static String getAdvertisementType() {
        return "jxta:PeerAdvertisement";
    }
     
    

 public static class Instantiator implements AdvertisementFactory.Instantiator {

       
        public String getAdvertisementType() {
            return SendPeerAdv.getAdvertisementType();
        }

        public Advertisement newInstance() {
            return new SendPeerAdv();
        }

     
        public Advertisement newInstance(Element root) {
           return new SendPeerAdv(root);
        }
  
    }

 
    

}
