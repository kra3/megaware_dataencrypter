/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.methods;

import com.megaware.multipleencrypter.frames.CodeDialog;
import com.megaware.multipleencrypter.objects.CodeObject;
import com.megaware.multipleencrypter.objects.EncryptedFileObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author sandeep
 */
public class GeneralMethods {

      public GeneralMethods() {
          
      }
      
      public int showConfirmationDialog(String msg,String caption) {       
        int retval=JOptionPane.showConfirmDialog(null, msg,caption,
                JOptionPane.YES_NO_CANCEL_OPTION);
        return retval;
      }
      
      public void showErrorMessage(String msg) {          
          JOptionPane.showMessageDialog(null,msg,"ERROR",
                  JOptionPane.ERROR_MESSAGE);
      }
      
      public java.io.File getFile(String title,int type,int dialogType) {
          javax.swing.JFileChooser filechooser=new javax.swing.JFileChooser();
          filechooser.setDialogTitle(title);                    
          filechooser.setDialogType(dialogType);
          filechooser.setFileSelectionMode(type);
          int retval=filechooser.showOpenDialog(null);
          if(retval==javax.swing.JFileChooser.APPROVE_OPTION) {
              java.io.File file=filechooser.getSelectedFile();
              return file;
          }
          return null;
                  
      }
      
      public boolean writeContentToFile(String content,java.io.File file) {
        java.io.FileWriter writer = null;
        try {            
            writer = new java.io.FileWriter(file);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(GeneralMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
      }
      
      public String readFileContent(java.io.File file)        {
          try {              
            java.io.BufferedReader reader=new java.io.BufferedReader(
                    new java.io.FileReader(file));
            String content="";            
            char buf[]=new char[(int)file.length()];
            reader.read(buf);
            content=String.copyValueOf(buf);
            return content;
          } catch (Exception exception) {
              exception.printStackTrace();
          }
          return null;          
      }
      
      
      // functiont to write the content of the text as  object if the file
      // is encrypted
      public boolean saveEncryptedFile(String content,EncryptedFileObject fileobj,
              java.io.File file) {
          try {
                               
                FileOutputStream fout=new FileOutputStream(file);
                ObjectOutputStream objout=new ObjectOutputStream(fout);
                EncryptedFileObject object=new EncryptedFileObject();
                object.setContent(content);
                object.setCodeobject(fileobj.getCodeobject());
                object.setOriginalFileExtension(fileobj.getOriginalFileExtension());
                objout.writeObject(object);
                objout.close();                
                fout.close();
                return true;
          } catch (Exception exception) {
              exception.printStackTrace();
              return false;
          }          
      }
      
      // function to read the encrypted file object
    
      public EncryptedFileObject readEncryptedFileObject(String filename) {
          try {
            FileInputStream filein=new FileInputStream(new File(filename));
            ObjectInputStream objin=new ObjectInputStream(filein);
            EncryptedFileObject encobj=new EncryptedFileObject();                        
            encobj=(EncryptedFileObject)objin.readObject();      
            objin.close();
            filein.close();
            return encobj;
          } catch (Exception exception) {
              exception.printStackTrace();
              return null;
          }

      }
      
      
    // function to get the extension of the file
    public String getFileExtension(File file)   {
        String extension="";
        String strfile=file.getName();
        int index=strfile.indexOf(".");
        
        // if no extension used then return .txt as default
        if(index==-1){
            return "txt";
        }
        else {
            return strfile.substring(index+1);
        }
    }
      
    // function to set an extension to a file by replacing the curren extension      
    public String setFileExtension(File file, String extension) {
       String path=file.getPath();       
       int ind=path.lastIndexOf(".");
       if(ind!=-1) {
           path=path.substring(0,ind+1);
           path+=extension;
       }
       else {
           path+="."+extension;
       }           
       return path;
    }
    
    
     // function to show the codedialog and get the code object
       protected CodeObject getCodeObject(int type,CodeObject codeobj) {
           // if type==1 then code for encryption
           // if type==2 then code for decryption           
           CodeDialog dialog=new CodeDialog(new javax.swing.JFrame(),false,type);
           if(codeobj!=null) {
               dialog.setCodeObject(codeobj);
           }
           CodeObject object=dialog.showDialog();
           return object;
                   
       }
     
       // function to return the printable characters in an order for encryption
       // and decryption
       public String[] getEncryptionCharacterOrderSet() {
           // printable 94 characters
           String ch[]=new String[94];
           int index=0;
           
           // first add the digits 
           ch[0]="|)|";
           ch[1]="|!|";
           ch[2]="|@|";
           ch[3]="|#|";
           ch[4]="|~|";
           ch[5]="|%|";
           ch[6]="|^|";
           ch[7]="|&|";
           ch[8]="|*|";
           ch[9]="|(|";
           index=10;
           
           // now add the characters before the digits
           for(int i=33;i<=47;i++) {
               ch[index]=Character.toString((char)i);
               index++;
           }
           
           // add all other characters after digit 9
           for(int i=58;i<=126;i++) {
               ch[index]=Character.toString((char)i);               
               index++;
           }
           return ch;    
                      
       }
      
     // function to get the decryption characters in order
              public String[] getDecryptionCharacterOrderSet() {
           // printable 94 characters
           String ch[]=new String[94];
           int index=0;
           
           // first add the digits 
           for(int i=48;i<=57;i++) {
               ch[index]=Character.toString((char)i);
               index++;
           }
           
           // now add the characters before the digits
           for(int i=33;i<=47;i++) {
               ch[index]=Character.toString((char)i);
               index++;
           }
           
           // add all other characters after digit 9
           for(int i=58;i<=126;i++) {
               ch[index]=Character.toString((char)i);               
               index++;
           }
           return ch;    
                      
       }
              
              
       // function to mask the digits in the text before encryption as they
       // may create infinite recursion
       public String maskEncryptionTextDigits(String text) {
            text=text.replaceAll("0", "|)|");
            text=text.replaceAll("1", "|!|");
            text=text.replaceAll("2", "|@|");
            text=text.replaceAll("3", "|#|");
            text=text.replaceAll("4", "|~|");// if |$| it generates matcher Error
                                             // I think that the character $ is pattern for matcher
            text=text.replaceAll("5", "|%|");
            text=text.replaceAll("6", "|^|");
            text=text.replaceAll("7", "|&|");
            text=text.replaceAll("8", "|*|");
            text=text.replaceAll("9", "|(|");
            return text;
       }
       
       // function to get  the application path
       public String getApplicationPath() {
           
           // The use of this funtion arises from the difference in  file system of different 
           // platforms. In windows new File("settings/somefile") points to the folder settings that
           // resides in the same folder as that of jar file. But in linux it points to home directory.
           // This may cause further errors. In linux the windows format works fine in the ide, but when
           // jar file is created, it fails
           //
           // so here the class path is obtained as url and it is formatted to obtain the path of jar file
           
           String path=new File(this.getClass().getResource("").getPath()).getPath();                                                       
           String currOsName=System.getProperty("os.name").toLowerCase();
                        
           // every jar file path has ! in it. find the index 
           int index=path.indexOf("!");                                 
           if(index!=-1) {               
                // break the path to upto ! which gives the path+"/jar filename"
                path=path.substring(0,index);
                File file=new File(path);
                // the parent of the jar gives the application path
                path=file.getParent()+"/";                
                // remove the file:/ protocol from the url string                
                if(path.startsWith("file:/")||path.startsWith("file:\\")) {                    
                    path=path.substring(6);
                }   
                // replace the %20  with space
                path=path.replaceAll("%20", " ");
                
                // for linux the string should start with a / like /media/sda1...
                // but for windows it is illegal /C:/windows/....                
                if(currOsName.contains("linux")) {                    
                    path="/"+path;
                }
           }
           else {
               path="";
           }               
           return path;           
       }

   
}



