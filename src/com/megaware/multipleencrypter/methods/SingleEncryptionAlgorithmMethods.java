/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.methods;

import com.megaware.multipleencrypter.frames.ChildEncrypterFrame;
import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import com.megaware.multipleencrypter.objects.AlgorithmObject;
import com.megaware.multipleencrypter.objects.CodeObject;
import java.io.File;
import java.math.BigDecimal;

/**
 *
 * @author sandeep
 */
public class SingleEncryptionAlgorithmMethods {
    
    
    // function to write the alogrithm object to the file specified by the
    // filename
    public void writeAlogrithmSettings(AlgorithmObject obj,String filename) {
        try {
            java.io.FileOutputStream fstream=new java.io.FileOutputStream(filename);
            java.io.ObjectOutputStream objout=new java.io.ObjectOutputStream(fstream);
            objout.writeObject(obj);
            objout.close();
            fstream.close();
        } catch (Exception exception) {
                exception.printStackTrace();
        }        
    }
    
    
    // function to read the algorithmObject from the specified file
    public AlgorithmObject readAlgorithmObject(String filename) {
         try {
             AlgorithmObject obj=new AlgorithmObject();
             java.io.FileInputStream istream=new java.io.FileInputStream(filename);
             java.io.ObjectInputStream objin=new java.io.ObjectInputStream(istream);
             obj=(AlgorithmObject)objin.readObject();
             objin.close();
             istream.close();
             return obj;                    
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
    
    // function to return the default algorithm
    public AlgorithmObject getDefaultAlgorithm() {              
        File file=new File(new GeneralMethods().getApplicationPath()+"settings/ALGORITHMSETTINGS.SETTINGS");         
        AlgorithmObject obj=this.readAlgorithmObject(file.getPath());
        return obj;
    }
    
      //FUNCTION TO FILL AN ARRAY OF INTEGERS WITH THE NUMBERS STARTING FROM 
      // seed AND INCREMENTED BY incre
      public void fillArrayWithSeedValues(int seed, int incre, int arr[]) {
          arr[0]=seed;
          for (int i = 1; i < arr.length; i++) {
              arr[i]=arr[i-1]+incre;
          }
      }
      
      // FUNCTION TO GET THE SEED VALUE AND INCREMENT VALUE FOR PROCESS FROM
      // THE CODE PASSED
      /* Here the seed value will be the last 3 digits of the code
       * and the incrementvalue will be the last two digits of the code
       * Returns and integer array whose first element is the seed and
       * the second is the incrementvalue;
       */
       public int[] getSeedAndIncrementForCode(java.math.BigDecimal code,java.math.BigDecimal equcode)   {
           int values[]=new int[2];
           int seed;
           int incrementvalue=0;           
           // check if the length of the code is less than 3 if true then the seed value is 
           // the code itself and the increment value also the digit itself
           if(code.toString().length()<3) {
               values[0]=Integer.parseInt(code.toString());
               values[1]=values[0];
               return values;
           }
           String strcode=code.toString();
           if(equcode.compareTo(new BigDecimal("0"))==1) {
               int equlength=equcode.toString().length();
               if(equlength>3) {
                    String strequcode=equcode.toString().substring(equlength-3,equlength);
                    incrementvalue+=Integer.parseInt(strequcode);
               }
           }
           seed=Integer.parseInt(strcode.substring(strcode.length()-3));
           incrementvalue+=Integer.parseInt(strcode.substring(strcode.length()-2));
           values[0]=seed;
           values[1]=incrementvalue;
           return values;
       }
       
                                               
       // function to encrypt data using the algone
       public void encryptText(final ChildEncrypterFrame frame) {
           
           final CodeObject codeobj=new GeneralMethods().getCodeObject(1,
                   null);
                   
            // if code was cancelled return;
            if(codeobj==null ) return;            
            final EncrypterProcessProgress processDialog=
                    new EncrypterProcessProgress(new javax.swing.JFrame(),false,0); 
            processDialog.setProcessType("ENCRYPTION");
            processDialog.setCurrFileName(frame.getTitle());                                                   
            //The printable characters;
                        
            Thread trd=new Thread(new Runnable() {
               public void run() {
                   try {                                                                      
                        String data=frame.getText();        
                        for (int i = 0; i < codeobj.getAlgorithmsused().size(); i++) {
                           String algname = (String) codeobj.getAlgorithmsused().elementAt(i);
                            data=new GeneralAlgorithmMethods().encryptTextWithAlgorithm(data, processDialog,
                                algname,codeobj,(i+1));                                                        
                            if(data==null) break; // the encryption has failed or cancelled
                       }                      
                        processDialog.setVisible(false);
                        processDialog.dispose();
                        if(data!=null) {
                            frame.setText(data);                                                
                            // set the file as encrypted
                            frame.setEncrypted(true);
                            codeobj.clearCodeValues();
                            // check whether the details should be saved or not by checking the
                            // status of detailssaved boolean in the codeobject which is set using the
                            // checkbox in codedialog
                            // if the details of encryption are not to be saved , then clear codeobj
                            if(codeobj.isDetailssaved()==false) {
                                codeobj.destroyObject();
                             }
                            // clear the undo buffer
                            frame.resetUndoManager();                        
                            frame.getEncObject().setCodeobject(codeobj);    
                        }
                        else {
                            frame.setEncrypted(false);
                        }
                        codeobj.clearCodeValues();
                   }catch(Exception exception)   {
                       frame.setEncrypted(false);
                       exception.printStackTrace();
                   }
               }        
            });
            trd.start();
            processDialog.setModal(true);
            processDialog.setVisible(true);
       }      
       
       
       // function to decrypt the text using algone
       // function to encrypt data using the algone
       public void decryptText(final ChildEncrypterFrame frame) {           
           final CodeObject codeobj=new GeneralMethods().getCodeObject(2,
                   frame.getEncObject().getCodeobject());
           
            // if code was cancelled return;
            if(codeobj==null ) return;            
            final EncrypterProcessProgress processDialog=
                    new EncrypterProcessProgress(new javax.swing.JFrame(),false,0);    
            processDialog.setProcessType("DECRYPTION");
            processDialog.setCurrFileName(frame.getTitle());
            
                                                                                         
            //The printable characters;                        
            Thread trd=new Thread(new Runnable() {
               public void run() {
                   try {                            
                       String data=frame.getText(); 
                         // to check with the decrypted code after decryption if the 
                         // text was full decrypted
                        String original=data;                                                
                        // CHECK THE ALGORITHM TO USE
                        for (int i = codeobj.getAlgorithmsused().size()-1; i >=0 ; i--) {
                           String algname = (String) codeobj.getAlgorithmsused().elementAt(i);
                            data=new GeneralAlgorithmMethods().decryptTextWithAlgorithm(data, processDialog,
                                algname,codeobj,(i+1));                            
                            if(data==null) break; // the encryption has failed or cancelled
                        }                                                                                      
                        processDialog.setVisible(false);
                        processDialog.dispose();                        
                        if(data!=null)
                        if(original.equals(data)) {
                            String msg="code mismatchs.The text cannot be decrypted.";
                            javax.swing.JOptionPane.showMessageDialog(null, msg,
                                    "CODE ERROR",javax.swing.JOptionPane.ERROR_MESSAGE);
                           return;         
                        }
                        else {                                                      
                           // set the file as decrypted and not encrypted                           
                           frame.setEncrypted(false);     
                           frame.setText(data);  
                                                   
                        }     
                        else {
                            frame.setEncrypted(true);     
                            frame.setText(original);  
                        }
                        // clear the code and other key details
                        codeobj.destroyObject();                                                
                   }catch(Exception exception)   {                       
                       exception.printStackTrace();
                   }
               }        
            });
            trd.start();            
            processDialog.setModal(true);
            processDialog.setVisible(true);
       }                                                  
} 





