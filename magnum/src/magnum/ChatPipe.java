/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magnum;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.JxtaServerPipe;
/**
 * Creates an advertisement class that stores following information:
 *<br>
 * ID: id associated with a peer/group
 * <br>
 * Name: name associated with a peer/group
 * @author fireball
 */
public class ChatPipe implements PipeMsgListener{

   PeerGroup mypeergroup;  
   chatDlg chatdlg;
   
   
   private final static PipeID BIDI_TUTORIAL_PIPEID = PipeID.create(URI.create("urn:jxta:uuid-59616261646162614E504720503250338944BCED387C4A2BBD8E9411B78C284104"));
    public ChatPipe(PeerGroup myGroup) {
        this.mypeergroup=myGroup;


        
    }
    public PipeAdvertisement getPipeAdvertisement(){
        
               
    PipeAdvertisement pipeadv=(PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
    pipeadv.setName(MagnumView.username);
    pipeadv.setDescription("Chat:"+Dataframe.chattablemodel.getValueAt(0, Dataframe.PeerId));    
    pipeadv.setPipeID((PipeID)Dataframe.chattablemodel.getValueAt(0, Dataframe.PipeNo));
    pipeadv.setType(PipeService.UnicastType);
   
    
    return pipeadv;
}
    
   
    // temp=MagnumView.ret();
    public static int chatarray[]=new int[10];//peers with whom we r chating
   int cnt=0;
   
   public static boolean chk=true;
   public static boolean ret()
   {
       return chk;
   }
   
   
   
   void chatprocess(PipeAdvertisement mypipeadv,String receivername){
        try {
            JxtaServerPipe chatpipe = new JxtaServerPipe(mypeergroup, mypipeadv);
            chatpipe.setPipeTimeout(0);
            System.out.println("Waiting for JxtaBidiPipe connections on JxtaServerPipe : " + mypipeadv.getPipeID());
               
            final JxtaBiDiPipe bidipe = chatpipe.accept();    
            
                
                    
             chatdlg=new chatDlg(new javax.swing.JFrame(),false,bidipe,receivername);             
          //   chatdlg.setVisible(true);
           Thread temp2=new Thread(chatdlg,receivername);       
                      temp2.start();
     
       
           
         /*   Message newmsg=new Message();
            DataInputStream in=new DataInputStream(System.in);
            while(true)     
            if(chatdlg.flag==true){
            String data=chatdlg.senddata();
            MessageElement respElement = new StringMessageElement("TREX", data, null);
            newmsg.addMessageElement(respElement);     
            System.out.println(bidipe.sendMessage(newmsg));       
            bidipe.close();
           
            }*/
            


        } catch (IOException ex) {
            Logger.getLogger(ChatPipe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   void chatprocessnew(PipeAdvertisement mypipeadv,String receivername){
        try {
            JxtaServerPipe chatpipe = new JxtaServerPipe(mypeergroup, mypipeadv);
            chatpipe.setPipeTimeout(0);
            System.out.println("Waiting for JxtaBidiPipe connections on JxtaServerPipe : " + mypipeadv.getPipeID());
               
            final JxtaBiDiPipe bidipe = chatpipe.accept();    
            
                
                    
             chatdlg=new chatDlg(new javax.swing.JFrame(),false,bidipe,receivername);             
          //   chatdlg.setVisible(true);
           Thread temp2=new Thread(chatdlg,receivername);       
                      temp2.start();
     
       
           
         /*   Message newmsg=new Message();
            DataInputStream in=new DataInputStream(System.in);
            while(true)     
            if(chatdlg.flag==true){
            String data=chatdlg.senddata();
            MessageElement respElement = new StringMessageElement("TREX", data, null);
            newmsg.addMessageElement(respElement);     
            System.out.println(bidipe.sendMessage(newmsg));       
            bidipe.close();
           
            }*/
            


        } catch (IOException ex) {
            Logger.getLogger(ChatPipe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    public void pipeMsgEvent(PipeMsgEvent event) {
        Message mymsg=event.getMessage();
        System.out.println("I am in sender listener");
        if(mymsg!=null){
            String temp=mymsg.getMessageElement("TREX").toString();
             System.out.println("found some msg from  reciever --->> "+temp);
             
            // chatdlg.receivedata(temp);
        }
        else
            System.out.println("not found");
        
    }

   

   

        
    }