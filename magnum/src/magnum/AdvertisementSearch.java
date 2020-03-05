/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magnum;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PipeAdvertisement;
import javax.swing.JFrame;
import net.jxta.pipe.PipeID;
import net.jxta.util.JxtaBiDiPipe;

/**
 *
 * @author fireball
 */

public class AdvertisementSearch implements Runnable,DiscoveryListener,PipeMsgListener{
chatDlg chatdlg; 
static PeerGroup myPeerGroup=null;
DiscoveryService myDiscoveryService=null;
final int MAXPEER=100;
int countpeeradv=0,countpipeadv=0;
PipeAdvertisement temppipeadv;
JxtaBiDiPipe  pipe=null;
SendPeerAdv recvpeerAdv[]=new SendPeerAdv[MAXPEER];
PipeAdvertisement recvpipeAdv[]=new PipeAdvertisement[MAXPEER];

public   AdvertisementSearch(PeerGroup myPeerGroup)
    {       AdvertisementSearch.myPeerGroup=myPeerGroup;
            this.myDiscoveryService=AdvertisementSearch.myPeerGroup.getDiscoveryService();                  
            this.myDiscoveryService.addDiscoveryListener(this);
        }
public void searchAdv()//Searching remote advertisements
{
    myDiscoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10);
}

public void advCategorySearch() throws IOException
{
 
            Advertisement tempAdv;
            Boolean flush=true;
            Enumeration<Advertisement> localAdvertisements = myDiscoveryService.getLocalAdvertisements(DiscoveryService.ADV,null,null);
            if(localAdvertisements!=null){
                while(localAdvertisements.hasMoreElements()){
                    tempAdv=localAdvertisements.nextElement();
                    System.out.println("Latest Found :-->"+tempAdv.getAdvType());
                    if(tempAdv.getAdvType()=="jxta:PeerAdvertisement")
                    {
                        System.out.println("Found PeerAdvertisement");
                        if(((SendPeerAdv)tempAdv).getID().equals(Dataframe.chattablemodel.getValueAt(0,Dataframe.PeerId)))
                            flush=false;
                        processPeerAdvertisement(tempAdv);
                        
                    }
                       if(tempAdv.getAdvType()=="jxta:PipeAdvertisement"){
                            System.out.println("Found PipeAdvertisement");
                            processPipeAdvertisement(tempAdv);
                           
                        }                   
                    if(flush){
                        try{
                        myDiscoveryService.flushAdvertisement(tempAdv);
                        } catch (IOException ex) {
                        Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        
        }

    private void processChatRequest(PipeAdvertisement temppipeadv) {    
        try {
            pipe = new JxtaBiDiPipe(myPeerGroup, temppipeadv, 100000, this, true);  
            String sendername=temppipeadv.getName();
            System.out.println("name rev is :->"+sendername);
            chatdlg=new chatDlg(new javax.swing.JFrame(),false,pipe,sendername);
            Thread temp3=new Thread(chatdlg,sendername);       
            temp3.start();
            } catch (Exception ex) {
            try {
                System.out.println("pipe nahin mili.");
                myDiscoveryService.flushAdvertisement(temppipeadv);
                Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
     
               
    }

    private void processPeerAdvertisement(Advertisement tempAdv) {
    
            boolean flag = false;
        
            int rowCount = Dataframe.chattablemodel.getRowCount();
            rowCount--;

            SendPeerAdv receivePeerAdv = (SendPeerAdv) tempAdv;
            for (int rowno = 0; rowno <= rowCount; rowno++) {
                if (receivePeerAdv.getID().equals(Dataframe.chattablemodel.getValueAt(rowno, 0))) {
                    Dataframe.chattablemodel.setValueAt(receivePeerAdv.getIP(), rowno, Dataframe.IPAdd);
                    Dataframe.chattablemodel.setValueAt(receivePeerAdv.getStatus(), rowno, Dataframe.Status);
                    Dataframe.chattablemodel.setValueAt(receivePeerAdv.getNickName(), rowno, Dataframe.NickName);
                    Dataframe.chattablemodel.setValueAt(receivePeerAdv.getPipeId(), rowno, Dataframe.PipeNo);
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                //Dataframe.chattablemodel.insertRow(rowCount, new Object[]{receivePeerAdv.getID(), receivePeerAdv.getStatus(), false, receivePeerAdv.getName(), receivePeerAdv.getNickName(), receivePeerAdv.getIP(), receivePeerAdv.getGroup(), "", ""});
                Dataframe.chattablemodel.addRow(new Object[]{receivePeerAdv.getID(), receivePeerAdv.getStatus(), false, receivePeerAdv.getName(), receivePeerAdv.getNickName(), receivePeerAdv.getIP(), receivePeerAdv.getGroup(), "", "",receivePeerAdv.getPipeId()});
            }
       
                   System.out.println("No of peer adv::---> " + Dataframe.chattablemodel.getRowCount());
        
       
    }

    private void processPipeAdvertisement(Advertisement tempAdv) {
      temppipeadv=(PipeAdvertisement) tempAdv; 
      int rowno=Dataframe.chattablemodel.getRowCount();
      PipeID revcpipeid=(PipeID) temppipeadv.getID();
      
      if(temppipeadv.getDescription().contains("Chat"))
      {
             for (int row = 0; rowno < rowno; row++) {
                if (temppipeadv.getID().equals(Dataframe.chattablemodel.getValueAt(row, Dataframe.PipeNo))&&(Dataframe.chattablemodel.getValueAt(row, Dataframe.PipeNo)).equals(true)) {
                    {
                       System.out.println("Blocked");
                       break; 
                    }
                }
            }
             processChatRequest(temppipeadv);}
      else
          System.out.println("pipe add recv but not for chat  "+temppipeadv.toString());
      
    }

   

  
    public void run() {
        while(true){
            try {
                
                
                searchAdv();
                advCategorySearch();
                Thread.sleep(10000);
            } catch (IOException ex) {
                Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public void discoveryEvent(DiscoveryEvent ev) {
        Advertisement tempAdv;
        Boolean flush=true;
        DiscoveryResponseMsg res=ev.getResponse();
        Enumeration en=res.getAdvertisements();
        if(en!=null){
                while(en.hasMoreElements()){
                    tempAdv = (Advertisement) en.nextElement();
                    
                    if (tempAdv.getAdvType() == "jxta:PeerAdvertisement") {
                        System.out.println("Found PeerAdvertisement");
                        if(((SendPeerAdv)tempAdv).getID().equals(Dataframe.chattablemodel.getValueAt(0,Dataframe.PeerId)))
                            flush=false;
                        processPeerAdvertisement(tempAdv);
                    }
                    if (tempAdv.getAdvType() == "jxta:PipeAdvertisement") {
                        System.out.println("Found PipeAdvertisement");
                        processPipeAdvertisement(tempAdv);
                        
                    }
                    if(flush){
                        try{
                        myDiscoveryService.flushAdvertisement(tempAdv);
                        } catch (IOException ex) {
                        Logger.getLogger(AdvertisementSearch.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                                        
                }
            }     
        
    }

    public void pipeMsgEvent(PipeMsgEvent event) {
        
        Message mymsg=event.getMessage();
        System.out.println("I am in receiver listener");
        if(mymsg!=null){
            
            System.out.println("found some msg from sender  -->>"+ mymsg.getMessageElement("TREX"));
        }
    }
    

}
