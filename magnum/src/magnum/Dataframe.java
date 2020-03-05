/*
 * Dataframe.java
 *
 * Created on April 20, 2008, 2:38 PM
 */

package magnum;

import javax.swing.table.DefaultTableModel;
import net.jxta.document.Advertisement;
/**
 *
 * @author  kb
 */
public class Dataframe extends javax.swing.JFrame {
    
    /** Creates new form Dataframe */
    static final int PeerId=0;
            static final int Status=1; 
            static final int Blocked=2;
            static final int Name=3;
            static final int NickName=4;
            static final int IPAdd=5;
            static final int GroupName=6;
            static final int ChatMsg=7;
            static final int ThreadName=8;
            static final int PipeNo=9;
            static public DefaultTableModel chattablemodel=null;
    public Dataframe() {
        initComponents();
        chattablemodel=(DefaultTableModel) Dataframe.ChatTable.getModel();
        
    }
    
    public static void addAdv(SendPeerAdv tempadv){
    
    
    
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        ChatTable = new javax.swing.JTable();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(magnum.MagnumApp.class).getContext().getResourceMap(Dataframe.class);
        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenu1.add(jMenuItem1);

        jPopupMenu1.add(jMenu1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        ChatTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Peer ID", "Status", "Blocked", "Name", "Nick Name", "IP Address", "Group", "Chat Text", "Thread Name", "PipeID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ChatTable.setColumnSelectionAllowed(true);
        ChatTable.setMinimumSize(new java.awt.Dimension(400, 300));
        ChatTable.setName("ChatData"); // NOI18N
        ChatTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChatTableMouseClicked(evt);
            }
        });
        ChatTable.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                ChatTableInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ChatTable);
        ChatTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ChatTableInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ChatTableInputMethodTextChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ChatTableInputMethodTextChanged

    private void ChatTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChatTableMouseClicked
      //  evt.
    }//GEN-LAST:event_ChatTableMouseClicked
    
    /**
     * @param args the command line arguments
     */
/*    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dataframe().setVisible(true);
            }
        });
    }*/
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static volatile javax.swing.JTable ChatTable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}