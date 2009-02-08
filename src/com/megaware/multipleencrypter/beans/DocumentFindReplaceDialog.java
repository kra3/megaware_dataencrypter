/*
 * DocumentFindReplaceDialog.java
 *
 * Created on April 21, 2008, 10:31 AM
 */

package com.megaware.multipleencrypter.beans;

import com.megaware.multipleencrypter.frames.ChildEncrypterFrame;
import com.megaware.multipleencrypter.frames.MultipleEncrypterFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author  name
 */
public class DocumentFindReplaceDialog extends javax.swing.JDialog {
    
    /** Creates new form DocumentFindReplaceDialog */
    public DocumentFindReplaceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public DocumentFindReplaceDialog(MultipleEncrypterFrame frameParent,boolean modal) {
        super(new javax.swing.JFrame(),modal);
        this.frameParent=frameParent;
        initComponents();
    }
    
    // function to find text and to return the index
    public boolean findText(String text,String textToFind) {
        boolean found=false;
        int index=text.indexOf(textToFind);        
        if(index!=-1) {
            found=true;        
            highlightText(index,textToFind.length());
            findNextIndex=index+textToFind.length();
        }
        else {
            JOptionPane.showMessageDialog(null, "Search item not found");
        }
        return found;        
    }
    
    // function to highlight the text
    protected void highlightText(int startIndex,int endIndex) {        
        parentTextField.requestFocus();
        parentTextField.setSelectionStart(startIndex);
        parentTextField.setSelectionEnd(startIndex+endIndex);
    }
    
    // function to replace the text 
    protected boolean replaceText(String text,String textToReplace,String textToReplaceWith) {
        int index=text.indexOf(textToReplace);
        boolean replaced=false;
        if(index!=-1) {            
            // could have using the regular expression searching but the '$' and the
            // black slash causes error in the searching 
            StringBuffer strbuf=new StringBuffer(text);
            strbuf.replace(index, index+textToReplace.length(), textToReplaceWith);
            text=strbuf.substring(0);
            parentTextField.setText(text);
            replaced=true;
        }        
        if(replaced==false) {
            JOptionPane.showMessageDialog(null, "Search item not found");
        }
        return replaced;
    }
    
    // function to find successively the text
    protected void findNextMatch(String text,String textToFind) {         
         if(findNextIndex+textToFind.length()>=text.length()) {
             JOptionPane.showMessageDialog(null,"Reached end of document");
             findNextIndex=0;
             return;
         }
        String findtext=text.substring(findNextIndex,text.length());
        int index=findtext.indexOf(textToFind);
        if(index==-1) {
            return;
        }
        else {
            highlightText(index+findNextIndex,index+textToFind.length()-1);
            findNextIndex+=index+textToFind.length();               
        }
        
    }            
    
    // function to replaceall the existence of a string with another string
    protected boolean replaceAllText(String text,String textToReplace,String textToReplaceWith) {
        
        // logic used: Take the text, pass it to string buffer, get index of item,
        // replace the text , put text as strbuf's upto index+length of text to replace,
        // set strbuf= text.substring(0,index+length)
        boolean replaced=false;            
        StringBuffer strbuf=new StringBuffer(text);
        int len=textToReplace.length();
        text="";
        int replen=textToReplaceWith.length();
        int index=strbuf.indexOf(textToReplace);
        while(index!=-1) {            
            strbuf.replace(index, index+len, textToReplaceWith);
            text+=strbuf.substring(0);                        
            strbuf=new StringBuffer(text.substring(index+replen)); 
            text=text.substring(0,index+replen);                        
            replaced=true;            
            index=strbuf.indexOf(textToReplace);
        }
        text+=strbuf.substring(0);
        if(replaced==true) {
            parentTextField.setText(text);
        }
        else {
            JOptionPane.showMessageDialog(null, "Search item not found");
        }
        return replaced;
    }
    
    // function to get the text from the document of parentTextField
    protected String getTextFromParent() {
        try {
            this.currActiveFrame=frameParent.getSelectedFrame();
            if(currActiveFrame==null) {
                JOptionPane.showMessageDialog(null, "No window selected");
                return null;
            }
            this.parentTextField=currActiveFrame.getTextPane();
            String text = "";
            Document doc = parentTextField.getDocument();
            text = doc.getText(0, doc.getLength());
            return text;
        } catch (BadLocationException ex) {
            Logger.getLogger(DocumentFindReplaceDialog.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnFind = new javax.swing.JButton();
        btnFindNext = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        btnReplaceAll = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtReplace = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FIND AND REPLACE");

        jPanel1.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setMinimumSize(new java.awt.Dimension(112, 120));
        jPanel3.setLayout(null);

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        jPanel3.add(btnFind);
        btnFind.setBounds(10, 10, 150, 29);

        btnFindNext.setText("Find Next");
        btnFindNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindNextActionPerformed(evt);
            }
        });
        jPanel3.add(btnFindNext);
        btnFindNext.setBounds(10, 40, 150, 29);

        btnReplace.setText("Replace");
        btnReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplaceActionPerformed(evt);
            }
        });
        jPanel3.add(btnReplace);
        btnReplace.setBounds(10, 90, 150, 29);

        btnReplaceAll.setText("Replace All");
        btnReplaceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplaceAllActionPerformed(evt);
            }
        });
        jPanel3.add(btnReplaceAll);
        btnReplaceAll.setBounds(10, 120, 150, 29);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel3.add(btnClose);
        btnClose.setBounds(10, 170, 150, 29);

        jPanel1.add(jPanel3);
        jPanel3.setBounds(430, 0, 170, 216);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Find Text")));
        jPanel4.setLayout(null);

        jLabel1.setText("Text to find:");
        jPanel4.add(jLabel1);
        jLabel1.setBounds(10, 20, 160, 17);
        jPanel4.add(txtFind);
        txtFind.setBounds(10, 40, 370, 27);

        jPanel2.add(jPanel4);
        jPanel4.setBounds(10, 10, 390, 80);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Replace Text"));
        jPanel5.setLayout(null);

        jLabel2.setText("Enter the text to place");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(10, 20, 260, 17);
        jPanel5.add(txtReplace);
        txtReplace.setBounds(10, 40, 370, 27);

        jPanel2.add(jPanel5);
        jPanel5.setBounds(13, 90, 390, 80);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(10, 2, 410, 210);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-616)/2, (screenSize.height-250)/2, 616, 250);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        this.findText(this.getTextFromParent(),txtFind.getText());
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceActionPerformed
        this.replaceText(this.getTextFromParent(),txtFind.getText(), txtReplace.getText());
    }//GEN-LAST:event_btnReplaceActionPerformed

    private void btnReplaceAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceAllActionPerformed
        this.replaceAllText(this.getTextFromParent(),txtFind.getText(), txtReplace.getText());
    }//GEN-LAST:event_btnReplaceAllActionPerformed

    private void btnFindNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindNextActionPerformed
        this.findNextMatch(this.getTextFromParent(), txtFind.getText());
    }//GEN-LAST:event_btnFindNextActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DocumentFindReplaceDialog dialog = new DocumentFindReplaceDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    public int getFindNextIndex() {
        return findNextIndex;
    }
   
    public void setFindNextIndex(int findNextIndex) {
        this.findNextIndex = findNextIndex;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnFindNext;
    private javax.swing.JButton btnReplace;
    private javax.swing.JButton btnReplaceAll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextField txtReplace;
    // End of variables declaration//GEN-END:variables
    
    // the parent textpane whose content is being searched
    JTextPane parentTextField;
    
    // variable to hold the findnext index
    private int findNextIndex=0;
    
    // variable to hold the current active window in the MultipleEncrypterframe deskop
    ChildEncrypterFrame currActiveFrame;
    
    // Multiple Encrypter frame parent
    MultipleEncrypterFrame frameParent;


    
    
}
