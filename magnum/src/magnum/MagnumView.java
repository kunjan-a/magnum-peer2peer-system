/*
 * MagnumView.java
 */
package magnum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import net.jxta.document.Advertisement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.protocol.PeerAdvertisement;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.lang.Object;
import java.net.InetAddress;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
//import net.jxta.impl.id.UUID.ModuleClassID;
import net.jxta.peer.PeerInfoService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.ModuleClassID;
import magnum.SendPeerAdv.*;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.protocol.PipeAdvertisement;

import net.jxta.socket.JxtaSocket;
import net.jxta.util.JxtaBiDiPipe;

/**
 * The application's main frame.
 */
public class MagnumView extends FrameView {

    public MagnumView(SingleFrameApplication app) throws InterruptedException {
        super(app);

        initComponents();

        this.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {

                    File sharedfolder = new File("Shared");
                    if (sharedfolder.exists()) {
                        sharedfolder.mkdir();
                    }
                    File sharedFile = new File(sharedfolder, "SharedList.tbl");
                    if (sharedFile.exists()) {
                        sharedFile.delete();
                    }
                    sharedFile.createNewFile();
                    XMLEncoder encoder = new XMLEncoder(new FileOutputStream(sharedFile));
                    DefaultTableModel model = (DefaultTableModel) SharedFileTable.getModel();
                    encoder.writeObject(model.getDataVector());
                    encoder.close();

                //      XMLDecoder a=new XMLDecoder(new FileInputStream(sharedfolderlist));




                } catch (IOException ex) {
                    Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");

      

       
    
        jDialog1.setBounds(300, 150, 300, 175);
        jDialog1.setVisible(true);
    }

    void PeerSearchnew(String Pattern) {

//        try {
        SearchingAnimation = new Runnable() {

            @SuppressWarnings(value = {"static-access", "static-access"})
            public void run() {
                while (true) {

                }
            }
            };
        t = new Thread(SearchingAnimation);
        t.start();



        RowFilter<Object, Object> regexFilter = RowFilter.regexFilter("^" + Pattern + "$", Dataframe.NickName);

        peerSearchResult.setModel(Dataframe.chattablemodel);
        TableRowSorter sorter = new TableRowSorter(Dataframe.chattablemodel);
        sorter.setRowFilter(RowFilter.regexFilter("^" + Pattern + "$", Dataframe.NickName));
        //peerSearchResult.removeColumn(new TableColumn(8));
        peerSearchResult.setRowSorter(sorter);
        System.out.println("sorter model   " + sorter.getModelRowCount());
        if (peerSearchResult.getRowCount() > 0) {
            t.stop();
        }

        jButton2.setText("Search");

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MagnumApp.getApplication().getMainFrame();
            aboutBox = new MagnumAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MagnumApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem6 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Pattern = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        File_Type = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        getFilelistButton = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        peerFilesearchTab1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        peerSearchResult = new javax.swing.JTable();
        SearchingFor = new javax.swing.JLabel();
        searchType = new javax.swing.JLabel();
        TypeDisp = new javax.swing.JLabel();
        PatternDisp = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablerecvFilelist = new javax.swing.JTable();
        listfromPeer = new javax.swing.JLabel();
        remotepeerid = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        fileAdded = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SharedFileTable = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        jFileChooser1 = new javax.swing.JFileChooser();
        jDialog1 = new javax.swing.JDialog();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(magnum.MagnumApp.class).getContext().getResourceMap(MagnumView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(magnum.MagnumApp.class).getContext().getActionMap(MagnumView.class, this);
        jMenuItem2.setAction(actionMap.get("FileMenuItemLogin")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        fileMenu.add(jMenuItem2);

        jMenuItem1.setAction(actionMap.get("FileMenuItemLogout")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText(resourceMap.getString("jCheckBoxMenuItem1.text")); // NOI18N
        jCheckBoxMenuItem1.setName("jCheckBoxMenuItem1"); // NOI18N
        jMenu2.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setText(resourceMap.getString("jCheckBoxMenuItem2.text")); // NOI18N
        jCheckBoxMenuItem2.setName("jCheckBoxMenuItem2"); // NOI18N
        jMenu2.add(jCheckBoxMenuItem2);

        jCheckBoxMenuItem3.setSelected(true);
        jCheckBoxMenuItem3.setText(resourceMap.getString("jCheckBoxMenuItem3.text")); // NOI18N
        jCheckBoxMenuItem3.setName("jCheckBoxMenuItem3"); // NOI18N
        jMenu2.add(jCheckBoxMenuItem3);

        jMenu1.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        jCheckBoxMenuItem4.setSelected(true);
        jCheckBoxMenuItem4.setText(resourceMap.getString("jCheckBoxMenuItem4.text")); // NOI18N
        jCheckBoxMenuItem4.setName("jCheckBoxMenuItem4"); // NOI18N
        jMenu3.add(jCheckBoxMenuItem4);

        jCheckBoxMenuItem5.setSelected(true);
        jCheckBoxMenuItem5.setText(resourceMap.getString("jCheckBoxMenuItem5.text")); // NOI18N
        jCheckBoxMenuItem5.setName("jCheckBoxMenuItem5"); // NOI18N
        jMenu3.add(jCheckBoxMenuItem5);

        jCheckBoxMenuItem6.setSelected(true);
        jCheckBoxMenuItem6.setText(resourceMap.getString("jCheckBoxMenuItem6.text")); // NOI18N
        jCheckBoxMenuItem6.setName("jCheckBoxMenuItem6"); // NOI18N
        jMenu3.add(jCheckBoxMenuItem6);

        jMenu1.add(jMenu3);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        jMenu4.setText(resourceMap.getString("jMenu4.text")); // NOI18N
        jMenu4.setName("jMenu4"); // NOI18N

        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenu4.add(jMenuItem6);

        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenu4.add(jMenuItem7);

        menuBar.add(jMenu4);

        jMenu5.setText(resourceMap.getString("jMenu5.text")); // NOI18N
        jMenu5.setName("jMenu5"); // NOI18N

        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenu5.add(jMenuItem8);

        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenu5.add(jMenuItem9);

        menuBar.add(jMenu5);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setName("jPanel2"); // NOI18N

        Pattern.setFont(resourceMap.getFont("Pattern.font")); // NOI18N
        Pattern.setText(resourceMap.getString("Pattern.text")); // NOI18N
        Pattern.setName("Pattern"); // NOI18N

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Peer", "Group", "Video", "Documents", "Music", "Image" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        File_Type.setFont(resourceMap.getFont("File_Type.font")); // NOI18N
        File_Type.setText(resourceMap.getString("File_Type.text")); // NOI18N
        File_Type.setName("File_Type"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("Search")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton11.setAction(actionMap.get("btnChatClick")); // NOI18N
        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setName("jButton11"); // NOI18N

        downloadButton.setAction(actionMap.get("downloadFile")); // NOI18N
        downloadButton.setText(resourceMap.getString("downloadButton.text")); // NOI18N
        downloadButton.setName("downloadButton"); // NOI18N

        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N

        getFilelistButton.setAction(actionMap.get("getfilelistfrompeer")); // NOI18N
        getFilelistButton.setText(resourceMap.getString("getFilelistButton.text")); // NOI18N
        getFilelistButton.setName("getFilelistButton"); // NOI18N

        jButton15.setText(resourceMap.getString("jButton15.text")); // NOI18N
        jButton15.setName("jButton15"); // NOI18N

        jButton18.setAction(actionMap.get("check")); // NOI18N
        jButton18.setText(resourceMap.getString("jButton18.text")); // NOI18N
        jButton18.setName("jButton18"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(File_Type)
                            .addComponent(Pattern)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton11))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(downloadButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton15))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(getFilelistButton)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {downloadButton, getFilelistButton, jButton11, jButton13, jButton15});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pattern)
                .addGap(6, 6, 6)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(File_Type)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(26, 26, 26)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(downloadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(getFilelistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton18)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {downloadButton, getFilelistButton, jButton11, jButton13, jButton15});

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        peerFilesearchTab1.setName("peerFilesearchTab1"); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        peerSearchResult.setAutoCreateRowSorter(true);
        peerSearchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Rating", "Quantity", "Name", "Type", "Size(Mb)", "Parent"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        peerSearchResult.setName("peerSearchResult"); // NOI18N
        peerSearchResult.getTableHeader().setReorderingAllowed(false);
        peerSearchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                peerSearchResultMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(peerSearchResult);

        SearchingFor.setFont(resourceMap.getFont("SearchingFor.font")); // NOI18N
        SearchingFor.setText(resourceMap.getString("SearchingFor.text")); // NOI18N
        SearchingFor.setName("SearchingFor"); // NOI18N

        searchType.setFont(resourceMap.getFont("searchType.font")); // NOI18N
        searchType.setText(resourceMap.getString("searchType.text")); // NOI18N
        searchType.setName("searchType"); // NOI18N

        TypeDisp.setFont(resourceMap.getFont("TypeDisp.font")); // NOI18N
        TypeDisp.setText(resourceMap.getString("TypeDisp.text")); // NOI18N
        TypeDisp.setName("TypeDisp"); // NOI18N

        PatternDisp.setFont(resourceMap.getFont("PatternDisp.font")); // NOI18N
        PatternDisp.setText(resourceMap.getString("PatternDisp.text")); // NOI18N
        PatternDisp.setName("PatternDisp"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(126, Short.MAX_VALUE)
                .addComponent(SearchingFor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PatternDisp)
                .addGap(108, 108, 108)
                .addComponent(searchType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TypeDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchType)
                        .addComponent(TypeDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SearchingFor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PatternDisp)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        peerFilesearchTab1.addTab(resourceMap.getString("jPanel10.TabConstraints.tabTitle"), jPanel10); // NOI18N

        jPanel12.setName("jPanel12"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        TablerecvFilelist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Size", "Type", "Absolute Path", "Last Modified"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablerecvFilelist.setName("TablerecvFilelist"); // NOI18N
        TablerecvFilelist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablerecvFilelistMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablerecvFilelist);

        listfromPeer.setText(resourceMap.getString("listfromPeer.text")); // NOI18N
        listfromPeer.setName("listfromPeer"); // NOI18N

        remotepeerid.setText(resourceMap.getString("remotepeerid.text")); // NOI18N
        remotepeerid.setName("remotepeerid"); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(listfromPeer)
                        .addGap(27, 27, 27)
                        .addComponent(remotepeerid)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listfromPeer)
                    .addComponent(remotepeerid))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        peerFilesearchTab1.addTab(resourceMap.getString("jPanel12.TabConstraints.tabTitle"), jPanel12); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(peerFilesearchTab1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(peerFilesearchTab1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jTabbedPane3.setName("jTabbedPane3"); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane11.setName("jScrollPane11"); // NOI18N

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Peer", "Name", "Type", "Speed", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.setName("jTable10"); // NOI18N
        jScrollPane11.setViewportView(jTable10);

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addComponent(jButton4)
                .addGap(33, 33, 33)
                .addComponent(jButton5)
                .addContainerGap(434, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No of Peers", "Name", "Type", "Status", "Speed", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setName("jTable4"); // NOI18N
        jScrollPane5.setViewportView(jTable4);

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(375, 375, 375)
                .addComponent(jButton6)
                .addContainerGap(379, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton6)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No of Peers", "Name", "Type", "Status", "Speed", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.setName("jTable6"); // NOI18N
        jScrollPane7.setViewportView(jTable6);

        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(406, 406, 406)
                .addComponent(jButton7)
                .addContainerGap(348, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No of Peers", "Name", "Type", "Status", "Speed", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.setName("jTable7"); // NOI18N
        jScrollPane8.setViewportView(jTable7);

        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(360, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(394, 394, 394))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jPanel9.setName("jPanel9"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No of Peers", "Name", "Type", "Status", "Speed", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.setName("jTable8"); // NOI18N
        jScrollPane9.setViewportView(jTable8);

        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(354, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addGap(400, 400, 400))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        jTabbedPane1.addTab(resourceMap.getString("jTabbedPane3.TabConstraints.tabTitle"), jTabbedPane3); // NOI18N

        jTabbedPane4.setName("jTabbedPane4"); // NOI18N

        jPanel11.setName("jPanel11"); // NOI18N

        jCheckBox1.setText(resourceMap.getString("jCheckBox1.text")); // NOI18N
        jCheckBox1.setName("jCheckBox1"); // NOI18N

        jCheckBox2.setText(resourceMap.getString("jCheckBox2.text")); // NOI18N
        jCheckBox2.setName("jCheckBox2"); // NOI18N

        jCheckBox3.setText(resourceMap.getString("jCheckBox3.text")); // NOI18N
        jCheckBox3.setName("jCheckBox3"); // NOI18N

        jCheckBox4.setText(resourceMap.getString("jCheckBox4.text")); // NOI18N
        jCheckBox4.setName("jCheckBox4"); // NOI18N

        jCheckBox5.setText(resourceMap.getString("jCheckBox5.text")); // NOI18N
        jCheckBox5.setName("jCheckBox5"); // NOI18N

        jButton3.setAction(actionMap.get("ShowBrowseDlg")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        fileAdded.setText(resourceMap.getString("fileAdded.text")); // NOI18N
        fileAdded.setName("fileAdded"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        SharedFileTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Type", "Size", "Absolute Path", "Last Modified"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SharedFileTable.setName("SharedFileTable"); // NOI18N
        jScrollPane3.setViewportView(SharedFileTable);
        SharedFileTable.getColumnModel().getColumn(0).setResizable(false);
        SharedFileTable.getColumnModel().getColumn(1).setResizable(false);
        SharedFileTable.getColumnModel().getColumn(2).setResizable(false);
        SharedFileTable.getColumnModel().getColumn(3).setResizable(false);
        SharedFileTable.getColumnModel().getColumn(4).setResizable(false);

        jButton12.setAction(actionMap.get("deleteFile")); // NOI18N
        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setName("jButton12"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addGap(28, 28, 28)
                .addComponent(jCheckBox2)
                .addGap(44, 44, 44)
                .addComponent(jCheckBox3)
                .addGap(46, 46, 46)
                .addComponent(jCheckBox4)
                .addGap(35, 35, 35)
                .addComponent(jCheckBox5)
                .addGap(27, 27, 27)
                .addComponent(jButton3)
                .addGap(28, 28, 28)
                .addComponent(fileAdded)
                .addGap(18, 18, 18)
                .addComponent(jButton12)
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileAdded)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        jTabbedPane4.addTab(resourceMap.getString("jPanel11.TabConstraints.tabTitle"), jPanel11); // NOI18N

        jTabbedPane1.addTab(resourceMap.getString("jTabbedPane4.TabConstraints.tabTitle"), jTabbedPane4); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        jFileChooser1.setName("jFileChooser1"); // NOI18N

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle(resourceMap.getString("dlg_Login.title")); // NOI18N
        jDialog1.setAlwaysOnTop(true);
        jDialog1.setBackground(resourceMap.getColor("dlg_Login.background")); // NOI18N
        jDialog1.setMinimumSize(new java.awt.Dimension(300, 175));
        jDialog1.setModal(true);
        jDialog1.setName("dlg_Login"); // NOI18N
        jDialog1.setResizable(false);
        jDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                jDialog1WindowClosed(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                jDialog1WindowDeactivated(evt);
            }
        });

        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton16.setAction(actionMap.get("login_check")); // NOI18N
        jButton16.setText(resourceMap.getString("jButton16.text")); // NOI18N
        jButton16.setName("jButton16"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(31, 31, 31))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    private void jDialog1WindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowClosed
        
    }//GEN-LAST:event_jDialog1WindowClosed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jDialog1.dispose();        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jDialog1WindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowDeactivated
    }//GEN-LAST:event_jDialog1WindowDeactivated

    private void peerSearchResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peerSearchResultMouseClicked

        System.out.println(evt.BUTTON1 + "  " + evt.BUTTON2 + "  " + evt.BUTTON3 + "  " + evt.getButton());
        int rowSelected = peerSearchResult.getSelectedRow(); // Get no of rows in a table Means No peers Found
        if (peerSearchResult.isRowSelected(rowSelected)) //Checking whether row is selected or not
        {

            //Checking Whether selected peer is blocked or not available
            if (peerSearchResult.getValueAt(rowSelected, Dataframe.Blocked).equals(true) || !peerSearchResult.getValueAt(rowSelected, Dataframe.Status).equals("Online")) {
                jButton11.setEnabled(false);//disable chat
            } else {
                //Now Checking whether already chat is going or not with selected peer
                String selectedPeerId = (String) peerSearchResult.getValueAt(rowSelected, Dataframe.PeerId).toString();
                for (int i = 0; i < Dataframe.chattablemodel.getRowCount(); i++) {
                    if (Dataframe.chattablemodel.getValueAt(i, 0).toString().equals(selectedPeerId)) {
                        //Dataframe.chattablemodel.setValueAt(peerSearchResult.getValueAt(rowSelected, Dataframe.Blocked), i, Dataframe.Blocked);

                        if (Dataframe.chattablemodel.getValueAt(i, Dataframe.ChatMsg).toString().equals("")) {

                            jButton11.setEnabled(true);
                            getFilelistButton.setEnabled(true);
                            peerindex = i;
                            break;
                        } else {
                            jButton11.setEnabled(false);
                            
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_peerSearchResultMouseClicked

    private void TablerecvFilelistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablerecvFilelistMouseClicked
        int rowSelected = TablerecvFilelist.getSelectedRow(); // Get no of rows in a table Means No peers Found
        if (TablerecvFilelist.isRowSelected(rowSelected)) //Checking whether row is selected or not
        {

            downloadButton.setEnabled(true);
        }
}//GEN-LAST:event_TablerecvFilelistMouseClicked
    static int rowcount;

    @Action
    public void ShowBrowseDlg() {

        int result = jFileChooser1.showOpenDialog(this.getFrame());
        File fp = jFileChooser1.getSelectedFile();
        switch (result) {
            case JFileChooser.APPROVE_OPTION:

                if (fp.canRead()) {
                    DefaultTableModel model = (DefaultTableModel) SharedFileTable.getModel();
                    model.addRow(new Object[]{fp.getName(), fp.getName().substring(fp.getName().lastIndexOf(".")), fp.length(), fp.getAbsolutePath(), fp.lastModified()});
                    SharedFileTable.setModel(model);

                    fileAdded.setText(fp.getName() + " Added Sucessfully");
                }// Approve (Open or Save) was clicked
                break;
            case JFileChooser.CANCEL_OPTION:
                // Cancel or the close-dialog icon was clicked
                break;
            case JFileChooser.ERROR_OPTION:
                fileAdded.setText(fp.getName() + " Could not be Added.");
                // The selection process 
                break;
        }





    }

    @Action
    public Task Search() {
        if (jButton2.getText().equals("Search")) {
            jButton11.setEnabled(false);
            downloadButton.setEnabled(false);
            jButton13.setEnabled(false);
            getFilelistButton.setEnabled(false);
            jButton2.setText("Stop");


            TypeDisp.setText("Peer");
            String Pattern = jTextField1.getText();

            String Type = (String) jComboBox1.getSelectedItem();
            //  jLabel7.setText(Type);

            if (Type.matches("Peer")) {


                //PeerSearch(Pattern); 

                PeerSearchnew(Pattern);

            }


        } else {
            t.stop();

            jButton2.setText("Search");
            PatternDisp.setText("");
            TypeDisp.setText("");
        }
        return new SearchTask(getApplication());
    }

    

    private class SearchTask extends org.jdesktop.application.Task<Object, Void> {

        SearchTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SearchTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel File_Type;
    private javax.swing.JLabel Pattern;
    private javax.swing.JLabel PatternDisp;
    private javax.swing.JLabel SearchingFor;
    public static javax.swing.JTable SharedFileTable;
    private javax.swing.JTable TablerecvFilelist;
    private javax.swing.JLabel TypeDisp;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel fileAdded;
    private javax.swing.JButton getFilelistButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel listfromPeer;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTabbedPane peerFilesearchTab1;
    public static javax.swing.JTable peerSearchResult;
    private javax.swing.JLabel remotepeerid;
    private javax.swing.JLabel searchType;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    NetworkManager myManager = null;
    NetworkConfigurator TheConfig = null;
    static public PeerGroup myPeergroup = null;
    DiscoveryService myDiscoveryService = null;
    PeerInfoService myPeerinfoservice = null;
    static ModuleClassID myModuleclassId = null;
    static public String username = null;
    private boolean login = false;
    Enumeration<Advertisement> localAdvertisements;
    private String Status = "Offline";
    SendPeerAdv adv;
    SendPeerAdv mySendpeerAdv;
    ChatPipe chatpipe;
    Runnable SearchingAnimation;
    Thread t;
    AdvertisementSearch advSearch;
    public int peerindex = -1;
    static public Object objwait = null;
    static public PipeHandler myHandler;
    static Connection con;

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Action
    public void LoginUser() throws PeerGroupException, SQLException {
        try {



            //       jDialog1.setSize(200, 300);
            //   jTextField1.setText(jDialog1.getModalityType());//setVisible(true));

            Runnable r = new Runnable() {

                @SuppressWarnings("static-access")
                public void run() {
                    try {
                        while (!login) {
                        //   ;
                        }

                        Status = "Online";
                        AdvertisementFactory.registerAdvertisementInstance(SendPeerAdv.getAdvertisementType(), new SendPeerAdv.Instantiator());
                        mySendpeerAdv = new SendPeerAdv();
                        new Dataframe().setVisible(true);
//*********Replaced by kunjan so as to use local peer id saved in system**************
                        //mySendpeerAdv.setID(IDFactory.newPeerID(myPeergroup.getPeerGroupID()));
                        //Storing peer info in table @ dataframe claa at index 0

                        String hostname[] = InetAddress.getLocalHost().toString().split("/", 2);
                        Dataframe.chattablemodel.insertRow(0, new Object[]{myPeergroup.getPeerID(), "Online", false, hostname[0], username, hostname[1], myPeergroup.getPeerGroupName(), "", "", IDFactory.newPipeID(myPeergroup.getPeerGroupID())});
                        PipeHandler GlobalPipe = new PipeHandler();
                        Thread GlobalPipeThread = new Thread(GlobalPipe);
                        GlobalPipeThread.start();
                        //  InputPipe tryPipe=myHandler.getPipe();
                        objwait = new Object();
                        ipcheck.start();
                        myDiscoveryService = myPeergroup.getDiscoveryService();
                        int i = 0;
                        synchronized (objwait) {
                            while (true) {
                                System.out.print(i++);
                                mySendpeerAdv.setGroup((String) Dataframe.chattablemodel.getValueAt(0, Dataframe.GroupName));
                                mySendpeerAdv.setName((String) Dataframe.chattablemodel.getValueAt(0, Dataframe.Name));
                                mySendpeerAdv.setID((ID) Dataframe.chattablemodel.getValueAt(0, Dataframe.PeerId));
                                mySendpeerAdv.setIP((String) Dataframe.chattablemodel.getValueAt(0, Dataframe.IPAdd));
                                mySendpeerAdv.setNickName((String) Dataframe.chattablemodel.getValueAt(0, Dataframe.NickName));
                                mySendpeerAdv.setStatus((String) Dataframe.chattablemodel.getValueAt(0, Dataframe.Status));
                                mySendpeerAdv.setPipeid((PipeID) Dataframe.chattablemodel.getValueAt(0, Dataframe.PipeNo));

                                myDiscoveryService.publish(mySendpeerAdv, 10000, 10000);
                                myDiscoveryService.remotePublish(mySendpeerAdv);
                                // myDiscoveryService.remotePublish(myPeergroup.getPeerAdvertisement());
                                // myDiscoveryService.remotePublish(myPeergroup.getPeerGroupAdvertisement());
                                //myDiscoveryService.publish(myPeergroup.getPeerAdvertisement());
                                // Thread.sleep(20000);
                                System.out.println(mySendpeerAdv.toString());
                                objwait.notifyAll();
                                objwait.wait(10000);

                                System.out.println("FREE AT LAST");
                            }
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);

                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            myManager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, username);

//***********Code by kunjan for using local configuration prestored on the system*************
            // Persisting nw manager to make sure the Peer ID is not re-created each
            // time the Network Manager is instantiated
            myManager.setConfigPersistent(true);

            System.out.println("PeerID: " + myManager.getPeerID().toString());

            // Since we won't be setting our own relay or rendezvous seed peers we
            // will use the default (public network) relay and rendezvous seeding.
            myManager.setUseDefaultSeeds(true);

            // Retrieving the Network Configurator
            System.out.println("Retrieving the Network Configurator");
            try {
                TheConfig = myManager.getConfigurator();
            /*URI myuri=new URI("http://rdv.jxtahosts.net/cgi-bin/rendezvous.cgi?3");
            URI relayuri=new URI("http://rdv.jxtahosts.net/cgi-bin/relays.cgi?3");
            TheConfig.setRdvACLURI(myuri);
            TheConfig.setRelayACLURI(relayuri);*/
            } /*catch (URISyntaxException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
            }*/ catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Network Configurator retrieved");

            // Does a local peer configuration exist?
            if (TheConfig.exists()) {

                System.out.println("Local configuration found");
                // We load it
                File LocalConfig = new File(TheConfig.getHome(), "PlatformConfig");
                try {
                    System.out.println("Loading found configuration");
                    System.out.print("Changing the name in the stored configuration from ");
                    System.out.println(TheConfig.getName() + " to " + username);
                    TheConfig.setName(username);
                    TheConfig.save();
                    TheConfig.load(LocalConfig.toURI());
                    System.out.println("Configuration loaded");
                } catch (IOException exio) {
                    exio.printStackTrace();
                    System.exit(-1);
                } catch (javax.security.cert.CertificateException excert) {
                    // An issue with the existing peer certificate has been encountered
                    excert.printStackTrace();
                    System.exit(-1);
                }
            } else {
                System.out.println("No local configuration found");
                TheConfig.setName(username);

                try {
                    System.out.println("Saving new configuration");
                    TheConfig.save();
                    System.out.println("New configuration saved successfully");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }


            // Rendev 
            myPeergroup = myManager.startNetwork();

            File sharedTable = new File("Shared", "SharedList.tbl");
            if (sharedTable.exists()) {
                XMLDecoder decoder = new XMLDecoder(new FileInputStream(sharedTable));
                Vector<Vector<String>> listrecv = (Vector<Vector<String>>) decoder.readObject();
                DefaultTableModel model = new DefaultTableModel();
                Vector<String> columniden = new Vector();
                columniden.add("File Name");
                columniden.add("Type");
                columniden.add("Size");
                columniden.add("Absolute Path");
                columniden.add("Last Modified");
                model.setDataVector(listrecv, columniden);
                model=validateFile(model);
                SharedFileTable.setModel(model);
                decoder.close();
                
            }



            //  invokeDatabase();

            /*     myren=myPeergroup.getRendezVousService();
            System.out.println("Starting rendezvous");
            myren.startRendezVous();
             */

            System.out.println("Peer id is: " + myPeergroup.getPeerID().toString());
            boolean waitForRendezvous = true;//Boolean.valueOf(System.getProperty("RDVWAIT", "false"));
            if (waitForRendezvous) {
                System.out.println("wainting for rendevo");
                myManager.waitForRendezvousConnection(0);
            }



            Thread x = new Thread(r);
            x.setPriority(10);
            x.start();
            /*
             * Stating a Thread for searching and processing <br>
             * advertisements
             * Class call Advertisementsearch immplementing runnable.
             * A new thread is created to search adv*/
            advSearch = new AdvertisementSearch(myPeergroup);
            Thread searchAdv = new Thread(advSearch, "searchadv");
            searchAdv.start();
            System.out.println("Advertisement Search Started");
        } catch (IOException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Action
    public void login_check() {
        try {

            username = jTextField2.getText();
            login = true;
            if (username.isEmpty()) {
            } else {
                File f = new File(".jxta");
                f.delete();
                LoginUser();

                jMenuItem2.setEnabled(false);
                jDialog1.dispose();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PeerGroupException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Action
    public void FileMenuItemLogin() {
        jDialog1.setVisible(true);
        jMenuItem2.setEnabled(false);
    }

    @Action
    public void FileMenuItemLogout() {
        try {
            Status = "Offline";
            login = false;
            jMenuItem2.setEnabled(true);
            jMenuItem1.setEnabled(false);
            mySendpeerAdv.setStatus(Status);
            myDiscoveryService.publish(mySendpeerAdv);
            myDiscoveryService.remotePublish(mySendpeerAdv);
            myDiscoveryService.publish(mySendpeerAdv);
            myDiscoveryService.remotePublish(mySendpeerAdv);
            myManager.stopNetwork();


            // jDialog1
            jDialog1.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    PipeAdvertisement mypipeadv;

    public AdvertisementSearch ret() {
        return advSearch;
    }

    @Action
    public void btnChatClick() {
        try {
            PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
            pipeadv.setName(MagnumView.username);
            pipeadv.setPipeID((PipeID) Dataframe.chattablemodel.getValueAt(peerindex, Dataframe.PipeNo));
            pipeadv.setType(PipeService.UnicastType);


            final JxtaBiDiPipe trychat = new JxtaBiDiPipe(myPeergroup, pipeadv, 10000, null, true);
            trychat.setMessageListener(new PipeMsgListener() {

                public void pipeMsgEvent(PipeMsgEvent event) {
                    MessageElement received;
                    if ((received = event.getMessage().getMessageElement("Chat_Ack", "Accepted")) != null) {
                        System.out.println("<--------Chat Ack received-------->" + received.toString());
                        chatDlg chatdlg = new chatDlg(new javax.swing.JFrame(), false, trychat, (String) Dataframe.chattablemodel.getValueAt(peerindex, Dataframe.NickName));
                        Thread temp3 = new Thread(chatdlg);
                        temp3.start();
                    }
                }
            });
            Message chatReq = new Message();
            StringMessageElement req = new StringMessageElement("PeerID", myPeergroup.getPeerID().toString(), null);
            chatReq.addMessageElement("Chat_Req", req);
            trychat.sendMessage(chatReq);
        } catch (IOException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action
    public void check() throws IOException {


        PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        pipeadv.setName(MagnumView.username);
        pipeadv.setPipeID((PipeID) Dataframe.chattablemodel.getValueAt(peerindex, Dataframe.PipeNo));
        pipeadv.setType(PipeService.UnicastType);


        final JxtaBiDiPipe trychat = new JxtaBiDiPipe(myPeergroup, pipeadv, 10000, null, true);
        trychat.setMessageListener(new PipeMsgListener() {

            public void pipeMsgEvent(PipeMsgEvent event) {
                MessageElement received;
                if ((received = event.getMessage().getMessageElement("List_Ack", "Accepted")) != null) {
                    try {
                        System.out.println("<--------List Ack received-------->" + received.toString());
                        ByteArrayMessageElement buffer = (ByteArrayMessageElement) event.getMessage().getMessageElement("List_Ack", "List");
                        File fp = new File("tempFile.txt");
                        fp.createNewFile();
                        FileOutputStream fpout = new FileOutputStream(fp);
                        fpout.write(buffer.getBytes());
                        fpout.close();
                        XMLDecoder xmldecoder = new XMLDecoder(new FileInputStream(fp));
                        Vector<Vector<String>> listrecv = (Vector<Vector<String>>) xmldecoder.readObject();
                        fp.delete();
                        DefaultTableModel model = (DefaultTableModel) TablerecvFilelist.getModel();
                        Vector<String> columniden = new Vector();
                        columniden.add("File Name");
                        columniden.add("Type");
                        columniden.add("Size");
                        columniden.add("Absolute Path");
                        columniden.add("Last Modified");
                        model.setDataVector(listrecv, columniden);
                        TablerecvFilelist.setModel(model);
                        listfromPeer.setText("Files At " + trychat.getPipeAdvertisement().getName());



                    } catch (IOException ex) {
                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                    }


                }
            }
        });
        Message ListReq = new Message();
        StringMessageElement req = new StringMessageElement("PeerID", myPeergroup.getPeerID().toString(), null);
        StringMessageElement req_pattern = new StringMessageElement("Pattern", "", null);
        ListReq.addMessageElement("List_Req", req);
        ListReq.addMessageElement("List_Req", req_pattern);
        boolean sendMessage = trychat.sendMessage(ListReq);
        if (true) {
            int i = 0;
            File fp = new File("sdf");
            fp.canRead();

        }


    /*        myDiscoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", null, 1);
    Enumeration en=myDiscoveryService.getLocalAdvertisements(DiscoveryService.ADV, "Name",null);
    if(en!=null)
    {
    while(en.hasMoreElements())
    {
    Advertisement ad=(Advertisement) en.nextElement();
    System.out.println(ad.getAdvType());
    }
    }*/




    }
    Thread ipcheck = new Thread(new Runnable() {

        public void run() {

            synchronized (objwait) {
                while (true) {
                    try {
                        System.out.println("***************CHECKING IP****************");
                        if (!InetAddress.getLocalHost().getHostAddress().equals(Dataframe.ChatTable.getValueAt(0, Dataframe.IPAdd))) {
                            Dataframe.chattablemodel.setValueAt(InetAddress.getLocalHost().getHostAddress(), 0, Dataframe.IPAdd);
                            Dataframe.chattablemodel.setValueAt(IDFactory.newPipeID(myPeergroup.getPeerGroupID()), 0, Dataframe.PipeNo);
                            System.out.println("*******NOTIFYING*********");
                            objwait.notifyAll();

                        //                          objwait.wait(1000);
                        }
                        //Thread.sleep(1000);
                        //objwait.notifyAll();
                        objwait.wait(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    });

    public void invokeDatabase() throws SQLException, SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //   Properties p = System.getProperties();
            //System.out.println(p.getProperty("derby.system.home"));
            //  p.put("derby.system.home", "C:\\databases\\sample");
            //  System.out.println(p.getProperty("derby.system.home"));

            con = DriverManager.getConnection("jdbc:derby:shareDB", "magnum", "magnum");

            //  Statement s = con.createStatement();


            // s.execute("create table sharedfiles(FileID int not null Primary Key,FileName varchar(40),Size bigint,AbsoluteName varchar(80),Type varchar(10),LastModified varchar(50))");
            // System.out.println("Created table omDB");





            rowcount = 0;
            PreparedStatement prepstat;
            ResultSet rs;
            prepstat = con.prepareStatement("SELECT * FROM SharedFiles");
            rs = prepstat.executeQuery();
            while (rs.next()) {
                rowcount++;
                for (int i = 1; i <= 6; i++) {
                    System.out.print(rs.getString(i) + "\t");

                }
                System.out.println();
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
            con.close();
        }

    }

    @Action
    public void getfilelistfrompeer() {
        try {



            PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
            pipeadv.setName(MagnumView.username);
            pipeadv.setPipeID((PipeID) Dataframe.chattablemodel.getValueAt(peerindex, Dataframe.PipeNo));
            pipeadv.setType(PipeService.UnicastType);


            final JxtaBiDiPipe trychat = new JxtaBiDiPipe(myPeergroup, pipeadv, 10000, null, true);
            trychat.setMessageListener(new PipeMsgListener() {

                public void pipeMsgEvent(PipeMsgEvent event) {
                    MessageElement received;
                    if ((received = event.getMessage().getMessageElement("List_Ack", "Accepted")) != null) {
                        try {
                            System.out.println("<--------List Ack received-------->" + received.toString());
                            ByteArrayMessageElement buffer = (ByteArrayMessageElement) event.getMessage().getMessageElement("List_Ack", "List");
                            //  MessageElement recv=event.getMessage().getMessageElement("List_Ack", "PeerID");
                            //  PeerID recvpeerid=PeerID.create(new URI(recv.toString()));
                            //  System.out.println("Received peerid:"+recvpeerid.toString());
                            File fp = new File("tempFile.txt");
                            fp.createNewFile();
                            FileOutputStream fpout = new FileOutputStream(fp);
                            fpout.write(buffer.getBytes());
                            fpout.close();
                            XMLDecoder xmldecoder = new XMLDecoder(new FileInputStream(fp));
                            Vector<Vector<String>> listrecv = (Vector<Vector<String>>) xmldecoder.readObject();
                            fp.delete();
                            DefaultTableModel model = (DefaultTableModel) TablerecvFilelist.getModel();
                            Vector<String> columniden = new Vector();
                            columniden.add("File Name");
                            columniden.add("Type");
                            columniden.add("Size");
                            columniden.add("Absolute Path");
                            columniden.add("Last Modified");
                            model.setDataVector(listrecv, columniden);
                            TablerecvFilelist.setModel(model);
                            //  System.out.println("remote pipe adv:-->"+trychat.getRemotePipeAdvertisement().toString());
                            PeerAdvertisement remotePeerAdvertisement = trychat.getRemotePeerAdvertisement();
                            listfromPeer.setText("Files At " + remotePeerAdvertisement.getName());
                            remotepeerid.setText("PeerID:" + remotePeerAdvertisement.getPeerID());

                            trychat.close();

                        } catch (IOException ex) {
                            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            Message ListReq = new Message();
            StringMessageElement req = new StringMessageElement("PeerID", myPeergroup.getPeerID().toString(), null);
            StringMessageElement req_pattern = new StringMessageElement("Pattern", "", null);
            ListReq.addMessageElement("List_Req", req);
            ListReq.addMessageElement("List_Req", req_pattern);
            boolean sendMessage = trychat.sendMessage(ListReq);
        } catch (IOException ex) {
            Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Action
    public void downloadFile() {



        if (TablerecvFilelist.getSelectedRow() != -1) {
            try {
                String peerID = remotepeerid.getText();
                peerID = peerID.substring(7);
                PipeID remotepipeID = null;
                for (int i = 0; i < Dataframe.chattablemodel.getRowCount(); i++) {
                    if (Dataframe.chattablemodel.getValueAt(i, Dataframe.PeerId).toString().equals(peerID)) {
                        remotepipeID = (PipeID) Dataframe.chattablemodel.getValueAt(i, Dataframe.PipeNo);
                        break;
                    }
                }

                PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
                pipeadv.setName(MagnumView.username);
                pipeadv.setPipeID(remotepipeID);
                pipeadv.setType(PipeService.UnicastType);

                final JxtaBiDiPipe downloadrequest = new JxtaBiDiPipe(myPeergroup, pipeadv, 10000, null, true);
                Message File_Req = new Message();
                final String Filename = TablerecvFilelist.getValueAt(TablerecvFilelist.getSelectedRow(), 0).toString();
                StringMessageElement filenameelement = new StringMessageElement("File_Path", TablerecvFilelist.getValueAt(TablerecvFilelist.getSelectedRow(), 3).toString(), null);
                File_Req.addMessageElement("File_Req", filenameelement);
                downloadrequest.sendMessage(File_Req);
                System.out.println(downloadrequest.getWindowSize());
                downloadrequest.setMessageListener(new PipeMsgListener() {

                    public void pipeMsgEvent(PipeMsgEvent event) {
                        Message recv_ack = event.getMessage();
                        if (recv_ack.getMessageElement("File_Ack", "Accepted") != null) {
                            try {
                                String pipeid = recv_ack.getMessageElement("File_Ack", "PipeID").toString();
                                System.out.println("Pipe of the remote bidipipe snap taken magnum view--->" + downloadrequest.getRemotePipeAdvertisement());


                                PipeAdvertisement pipeadv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
                                pipeadv.setName(MagnumView.username);
                                pipeadv.setPipeID(PipeID.create(new URI(pipeid)));
                                pipeadv.setType(PipeService.UnicastType);
                                System.out.println("Pipe adv ---\n" + pipeadv.toString());
                                JxtaSocket fileSocket = new JxtaSocket(MagnumView.myPeergroup, null, pipeadv, 500000, true);
                                InputStream inputStream = fileSocket.getInputStream();
                                //    byte buffer[]=new byte[fileSocket.getReceiveBufferSize()];
                                byte buffer[] = new byte[65536];
                                int downloaded = 0;
                                Long Totalsize = Long.valueOf(TablerecvFilelist.getValueAt(TablerecvFilelist.getSelectedRow(), 2).toString());
                                File fp = new File(Filename);
                                fp.delete();
                                fp.createNewFile();
                                RandomAccessFile fprm = new RandomAccessFile(fp, "rw");
                                System.out.println("kunj");
                                long fileDownloaded=0;
                                long currentTimeMillis = System.currentTimeMillis();
                                long elapsed;
                                while ((downloaded = inputStream.read(buffer)) != -1 && Totalsize != 0) {
                                    System.out.println("Downloaded:-->" + downloaded + "\t" + " Totalsize--> " + Totalsize);
                                    fprm.write(buffer, 0, downloaded);
                                    long currentTimeMillis1 = System.currentTimeMillis();
                                    fileDownloaded+=downloaded;
                                    
                                    if(( elapsed=currentTimeMillis1-currentTimeMillis)>=1000)
                                    {
                                        System.out.println("Speed in Bps:-->"+fileDownloaded/elapsed);
                                    
                                 
                                    }
                                    else
                                        System.out.println("Speed in Bps:-->"+fileDownloaded/elapsed);
                                           fileDownloaded=0;
                                    Totalsize -= downloaded;
                                 
                            
                                      }
                                fprm.close();
                                inputStream.close();
                             




                                System.out.println("Total download Remaining:--------->" + Totalsize);
                            } catch (URISyntaxException ex) {
                                Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                });



            } catch (IOException ex) {
                Logger.getLogger(MagnumView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    private DefaultTableModel validateFile(DefaultTableModel model) {
        int rowno=model.getRowCount();
      
        for(int i=0;i<model.getRowCount();i++){
            String FilePath=(String) model.getValueAt(i, 3);
            File fp=new File(FilePath);
            if(!fp.exists()){
                model.removeRow(i);
                i--;
            }
        }
       return model;
    }

    @Action
    public void deleteFile() {
        if(SharedFileTable.getSelectedRow()!=-1){
            DefaultTableModel model = (DefaultTableModel) SharedFileTable.getModel();
            model.removeRow(SharedFileTable.getSelectedRow());
        }
        else
        {
            
        }
        
    }
   
    }
    
  
       
   


           
        
        
