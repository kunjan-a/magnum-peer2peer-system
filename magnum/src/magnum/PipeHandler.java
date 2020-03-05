package magnum;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipeListener;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.JxtaServerPipe;

public class PipeHandler implements PipeMsgListener,Runnable{
InputPipe myPipe=null;
OutputPipe outPipe=null;
PipeAdvertisement myAdvertisement=null;
JxtaServerPipe chatpipe;
PipeID mypipeid=null;
PipeService myPipeService=null;

public PipeHandler()
{
        try {
            mypipeid = (PipeID) Dataframe.chattablemodel.getValueAt(0,Dataframe.PipeNo);
            myAdvertisement = getAdvertisement(mypipeid);
            chatpipe = new JxtaServerPipe(MagnumView.myPeergroup, myAdvertisement);
            chatpipe.setPipeTimeout(0);
        } catch (IOException ex) {
            Logger.getLogger(PipeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
}

public PipeAdvertisement getAdvertisement(PipeID pID){
    myAdvertisement=(PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
    myAdvertisement.setName(MagnumView.username);
    //pipeadv.setDescription("Chat:"+Dataframe.chattablemodel.getValueAt(0, Dataframe.PeerId));    
    myAdvertisement.setPipeID(pID);
    myAdvertisement.setType(PipeService.UnicastType);
    myPipeService=MagnumView.myPeergroup.getPipeService();
    return myAdvertisement;
}


public void accept()
{
        try {
            System.out.println("Waiting for JxtaBidiPipe connections on JxtaServerPipe : " + mypipeid);
            JxtaBiDiPipe bidipipe = chatpipe.accept();
            System.out.println("<-------Found Connection on Global BIDI pipe-------->");
            
            ControlMsgHandler newConn=new ControlMsgHandler(bidipipe);
         //   bidipipe.setMessageListener(newConn);
            Thread connThread=new Thread(newConn);
            connThread.start();
            
        } catch (IOException ex) {
            Logger.getLogger(PipeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

         
    public void pipeMsgEvent(PipeMsgEvent event) {
        try {
            MessageElement received;
            if((received=event.getMessage().getMessageElement("Test", "Initial Message"))!=null){
                System.out.println("********Message receieved on pipe*********" +received.toString());
                //PipeID remote = (PipeID) Dataframe.chattablemodel.getValueAt(1,Dataframe.PeerId);
                PipeID remote=event.getPipeID();
            /*    myPipeService.createOutputPipe(this.getAdvertisement(remote), new OutputPipeListener() {

                    public void outputPipeEvent(OutputPipeEvent event) {
                        try {
                            OutputPipe remotePipe = event.getOutputPipe();

                            Message msg = new Message();
                            MessageElement init = new StringMessageElement("Initial Message", "Are you there??", null);
                            msg.addMessageElement("Test", init);
                            remotePipe.send(msg);
                            throw new UnsupportedOperationException("Not supported yet.");
                        } catch (IOException ex) {
                            Logger.getLogger(PipeHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
    */
                OutputPipe remotePipe=myPipeService.createOutputPipe(this.getAdvertisement(remote),0);
                Message msg = new Message();
                MessageElement init = new StringMessageElement("Final Message", "Yes I am There.", null);
                msg.addMessageElement("Test", init);
                remotePipe.send(msg);
            }
            else
            if((received=event.getMessage().getMessageElement("Test", "Final Message"))!=null){
                System.out.println("********Message receieved on pipe*********" +received.toString());
            }
        
           
        } catch (IOException ex) {
            Logger.getLogger(PipeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(PipeID remote)
    {
        try {
            OutputPipe remotePipe = myPipeService.createOutputPipe(this.getAdvertisement(remote), 0);
            Message msg = new Message();
            MessageElement init = new StringMessageElement("Initial Message", "Are You There??", null);
            msg.addMessageElement("Test", init);
            remotePipe.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(PipeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            /**
     * Closes the output pipe and stops the platform
     */
    public void stop() {
        // Close the input pipe
        myPipe.close();
    }

    public void run() 
    {
    while(true)
        accept();
    }

}
    
    
    
