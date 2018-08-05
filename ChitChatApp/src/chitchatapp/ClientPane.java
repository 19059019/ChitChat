package chitchatapp;

import java.io.BufferedInputStream;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ClientPane extends javax.swing.JFrame implements Runnable {

    private static Socket client = null;
    private static DataInputStream serverMessage = null;
    private static DataInputStream clientMessage = null;
    private static PrintStream output = null;
    private static boolean status = true;
    public static String user = "Default";
    public static ArrayList<String> userNames;

    public void ClientPaneInit() {
        initComponents();
        setVisible(true);
        setTitle("ChitChat - " + user);
    }

    public static void main(String[] args) {
        // connect to server socket and open input stream
        
        String host = "";
        
        while (host.equals("")) {
            host = JOptionPane.showInputDialog("Please enter the host");
           
            if (host == null) {
                System.exit(0);
            }
        }
        
        try {
            client = new Socket(host, 8000);
            serverMessage = new DataInputStream(client.getInputStream());
            clientMessage = new DataInputStream(new BufferedInputStream(System.in));
            output = new PrintStream(client.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

        if (client != null && serverMessage != null && output != null) {
            try {
                new Thread(new ClientPane()).start();

                while (status) {
                    String message = clientMessage.readLine().trim();

                    if (message.startsWith("EXIT")) {
                        break;
                    } else {
                        output.println(message);
                    }
                    output.println(message);
                }
                System.out.println("Cheerio!");
                output.close();
                clientMessage.close();
                serverMessage.close();
                client.close();
                System.exit(0);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void run() {
        ClientPaneInit();
        messageListener();
    }

    public void messageListener() {
        String message;

        try {
            while ((message = serverMessage.readLine()) != null) {
                if (message.startsWith("*userNames*")) {
                    userNames = new ArrayList<>(Arrays.asList(message.split("##")));
                    message = userNames.get(1);
                    userNames.remove(1);
                    userNames.remove(0);
                    if (user.equals("Default")) {
                        user = userNames.get(userNames.size() - 1);
                    }
                }
                System.out.println(message);
                taChatArea.append("\n" + message);
            }

            status = false;
        } catch (IOException e) {
            System.out.println("Disconnected!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taChatArea = new javax.swing.JTextArea();
        tfMessageInput = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstOnlineUsers = new javax.swing.JList<>();
        btnWhisper = new javax.swing.JButton();
        btnGroup = new javax.swing.JButton();
        lblNumUsers = new javax.swing.JLabel();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taChatArea.setEditable(false);
        taChatArea.setColumns(20);
        taChatArea.setLineWrap(true);
        taChatArea.setRows(5);
        jScrollPane1.setViewportView(taChatArea);

        tfMessageInput.setText("Type message here...");
        tfMessageInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMessageInputFocusGained(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N

        jLabel1.setText("Online Users:");

        lstOnlineUsers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstOnlineUsersValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstOnlineUsers);

        btnWhisper.setText("Whisper");
        btnWhisper.setEnabled(false);
        btnWhisper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWhisperActionPerformed(evt);
            }
        });

        btnGroup.setText("Return to Group Chat");
        btnGroup.setEnabled(false);
        btnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupActionPerformed(evt);
            }
        });

        lblNumUsers.setText("#online");

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSend)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(tfMessageInput))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnWhisper)
                            .addComponent(btnGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNumUsers))
                            .addComponent(jScrollPane2)
                            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblNumUsers))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnWhisper)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGroup)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfMessageInput, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSend)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jScrollPane1.getAccessibleContext().setAccessibleName("");
        ImageIcon logo = new ImageIcon("/home/20058837/RW354/ChitChat/ChitChatApp/chitchat.png");

        lblTitle.setIcon(logo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        if (!tfMessageInput.getText().equals("") && !tfMessageInput.getText().equals("Type message here...")) {
            String msg = tfMessageInput.getText();
            output.println(msg);            
            if (msg.startsWith("EXIT")) {
                tfMessageInput.setText("Cheerio!");
                try {
                    output.close();
                    clientMessage.close();
                    serverMessage.close();
                    client.close();
                    System.exit(0);
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            //message = msg;
            tfMessageInput.setText("Type message here...");
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnWhisperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWhisperActionPerformed
        btnGroup.setEnabled(true);
    }//GEN-LAST:event_btnWhisperActionPerformed

    private void lstOnlineUsersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstOnlineUsersValueChanged
        btnWhisper.setEnabled(true);
    }//GEN-LAST:event_lstOnlineUsersValueChanged

    private void btnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupActionPerformed
        //code, then...
        btnGroup.setEnabled(false);
    }//GEN-LAST:event_btnGroupActionPerformed

    private void tfMessageInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfMessageInputFocusGained
        tfMessageInput.setText("");
    }//GEN-LAST:event_tfMessageInputFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGroup;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnWhisper;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNumUsers;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList<String> lstOnlineUsers;
    private javax.swing.JTextArea taChatArea;
    private javax.swing.JTextField tfMessageInput;
    // End of variables declaration//GEN-END:variables

}
