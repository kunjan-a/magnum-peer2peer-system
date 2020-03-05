/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magnum;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.socket.JxtaSocket;
import net.jxta.socket.JxtaServerSocket;

/**
 *
 * @author ADMIN
 */
public class ControlMsgHandler implements PipeMsgListener, Runnable{

    JxtaBiDiPipe bidiPipe;
    
    
    public ControlMsgHandler(JxtaBiDiPipe bp){
        bidiPipe=bp;
        
    }
    
    public void pipeMsgEvent(PipeMsgEvent event) {
        try {
           
            processMessage(event.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ControlMsgHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void run() {
        bidiPipe.setMessageListener(this);
      
        
        
        
    }

    private void processMessage(Message message) throws IOException {        
            
        chatDlg chatdlg;
        //try { 
            Iterator<String> messageNamespaces = message.getMessageNamespaces();
            
            while(messageNamespaces.hasNext()){
                String element=messageNamespaces.next();
               if(element.equals("Chat_Req"))
                   {
                       MessageElement msg = message.getMessageElement("Chat_Req", "PeerID");
                       System.out.println("Message For Chat Request------->"+msg.toString());
                       Message chatAck=new Message();
                       StringMessageElement ack=new StringMessageElement("Accepted", "True", null);
                       chatAck.addMessageElement("Chat_Ack", ack);
                       bidiPipe.sendMessage(chatAck);
                       
                       PipeAdvertisement remotePipeAdvertisement = bidiPipe.getRemotePipeAdvertisement();
                       System.out.println("Remote Advertisement from the invoking pipe is:"+remotePipeAdvertisement);
                       //pipe = new JxtaBiDiPipe(MagnumView.myPeergroup, remotePipeAdvertisement, 100000, null, true);  
                       String sendername=remotePipeAdvertisement.getName();
                       System.out.println("name received is :->"+sendername);
                       chatdlg=new chatDlg(new javax.swing.JFrame(),false,this.bidiPipe,sendername);
                       Thread temp3=new Thread(chatdlg,sendername);       
                       temp3.start();
                       
                    }
               if(element.equals("List_Req"))
                   {
                       MessageElement msg = message.getMessageElement("List_Req", "PeerID");
                       MessageElement msg_pattern = message.getMessageElement("List_Req", "Pattern");
                       String pattern=msg_pattern.toString();
                       System.out.println("Pattern to be searched"+pattern);
                       System.out.println("Message For List Request------->"+msg.toString());
                       Message ListAck=new Message();//REPLY MESSAGE OF FILE REQUEST
                       StringMessageElement ack=new StringMessageElement("Accepted", "True", null);
                       ListAck.addMessageElement("List_Ack", ack);
                       DefaultTableModel model = (DefaultTableModel) MagnumView.SharedFileTable.getModel();
                       Vector dataVector = model.getDataVector();                       
                       Vector<String> subvector=new Vector<String>();                   
                       Vector<Vector> searchvector=new Vector();
                       
                     
                       for(int i=0;i<dataVector.size();i++){
                           subvector=(Vector<String>) dataVector.get(i);
                           if(subvector.firstElement().contains(pattern)){                               
                        boolean add = searchvector.add(subvector);                               
                           }
                       }
                       for(int i=0;i<=searchvector.size();i++)
                           System.out.println(searchvector.toString());
                       File fp=new File("tempSearchResult");
                       fp.createNewFile();
                       XMLEncoder xmlencoder=new XMLEncoder(new FileOutputStream(fp));
                       xmlencoder.writeObject(searchvector);
                       xmlencoder.close();
                       FileInputStream fpin=new FileInputStream(fp);
                       int size=(int) fp.length();
                       byte[] buffer=new byte[size];
                       fpin.read(buffer);
                    //   StringMessageElement peerIDElement=new StringMessageElement("PeerID",Dataframe.chattablemodel.getValueAt(0, Dataframe.PeerId).toString(),null);
                       ByteArrayMessageElement ack_list=new ByteArrayMessageElement("List",MimeMediaType.AOS,buffer,null);                       
                       ListAck.addMessageElement("List_Ack", ack_list);
                      // ListAck.addMessageElement("List_Ack",peerIDElement);
                       bidiPipe.sendMessage(ListAck);     
                       bidiPipe.close();
               
               }
                
             if(element.equals("File_Req"))
                   {
                       MessageElement msg = message.getMessageElement("File_Req", "File_Path");
                       System.out.println("Message For file Request------->"+msg.toString());
                       Message fileAck=new Message();
                       //Check baad mein karana hain
                       PipeID sockserverpipeid=IDFactory.newPipeID(MagnumView.myPeergroup.getPeerGroupID());
                       StringMessageElement newpipeid=new StringMessageElement("PipeID", sockserverpipeid.toString(),null);
                
                       StringMessageElement ack=new StringMessageElement("Accepted", "True", null);
                       fileAck.addMessageElement("File_Ack", ack);
                       fileAck.addMessageElement("File_Ack",newpipeid );
                       bidiPipe.sendMessage(fileAck);
                       System.out.println("Bidipipe Advertisement from ctrl msg Handler:-->"+sockserverpipeid);
                       PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
                       pipeadv.setName(MagnumView.username);
                       pipeadv.setPipeID(sockserverpipeid);
                       pipeadv.setType(PipeService.UnicastType);
                       System.out.println("Pipe adv ---\n"+pipeadv.toString());
                       JxtaServerSocket filesocketserver =new JxtaServerSocket(MagnumView.myPeergroup,pipeadv, 10);
                       filesocketserver.setSoTimeout(0);
                       //Jxta
                               Socket fileSocket= filesocketserver.accept();
                       
                       /*
                       JxtaSocket fileSocket=new JxtaSocket(MagnumView.myPeergroup,bidiPipe.getPipeAdvertisement(),0); 
                       */
                       
                       System.out.println("Connection at Socket accepted by the server");
                       
                       File fp=new File(msg.toString());
                       RandomAccessFile fprm=new RandomAccessFile(fp, "r");                       
                       //fileSocket.setSendBufferSize(fileSocket.getReceiveBufferSize());
                       OutputStream outputStream = fileSocket.getOutputStream();
                       byte buffer[]=new byte[102400];
                       long filesize=fp.length();
                       int readFromFile=0;
                       while((readFromFile=fprm.read(buffer))!=-1&&filesize!=0){
                           System.out.println("Uploaded:-->"+readFromFile+"\t"+" Totalsize--> "+filesize);
                           outputStream.write(buffer, 0, readFromFile);
                           filesize-=readFromFile;
                       }
                       System.out.println("Total upload Remaining:--------->"+filesize);
                    }
                                       
           }                                  
            
         
      
    }

    }

