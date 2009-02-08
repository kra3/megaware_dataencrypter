/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.methods;

import com.megaware.multipleencrypter.algorithms.AlgTwoEncodeText;
import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import com.megaware.multipleencrypter.frames.FolderEncryptionDialog;
import com.megaware.multipleencrypter.objects.CodeObject;
import com.megaware.multipleencrypter.objects.EncryptedFileObject;
import com.megaware.multipleencrypter.objects.FolderEncryptObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

/**
 *
 * @author name
 */
public class FolderEncryptionAlgorithmMethods {

    
    // function to get the folder for encrypt or decrypt
    public java.io.File getEncryptFolder() {
        java.io.File file;
        javax.swing.JFileChooser filechooser=new javax.swing.JFileChooser();
        filechooser.setDialogTitle("Select the folder");
        filechooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        filechooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        int retval=filechooser.showOpenDialog(new javax.swing.JFrame());
        if(retval==javax.swing.JFileChooser.APPROVE_OPTION) {
            file=filechooser.getSelectedFile();
            return file;
        }
        return null;
    }
    
    // function to save the file encrypted
    public boolean saveEncryptedFileObject(CodeObject codeobj, String content,
            File file) {          
                
        EncryptedFileObject fileobject=new EncryptedFileObject();
        String originalextension=new GeneralMethods().getFileExtension(file);
        fileobject.setOriginalFileExtension(originalextension);                   
        fileobject.setCodeobject(codeobj);        
        fileobject.setContent(content);
        boolean filesaved=new GeneralMethods().saveEncryptedFile(
                content, fileobject, file);                
        if(filesaved==true) {
            // change the extension
            File originalfile=file;
            file=new File(new GeneralMethods().setFileExtension(file, "MENCF"));
            originalfile.renameTo(file);            
            return true;
        }
        return false;
    }
    
    
    // function to change the file extensions after decryption
    public void setFileExtensionToOriginal(Vector fileobjects,String path) {
        for (int i = 0; i < fileobjects.size(); i++) {
            String filedata[] =(String[])fileobjects.elementAt(i);
            if(filedata!=null) {
                // get the original file extension
                String originalextension=filedata[0];
                File originalfile=new File(path+"/"+filedata[1]);
                File renamefile=new File(new GeneralMethods().setFileExtension(                        
                        originalfile, originalextension));
                originalfile.renameTo(renamefile);
            }
        }
    }
    
    // function to place the FolderEncryptObject in the encryptedfolder
    protected boolean placeFolderEncryptObject(CodeObject codeobject,Vector encryptedFiles,
            File file) {       
          try {
                // if no files were encrypted then return
                if(encryptedFiles.size()==0) return false;
                FileOutputStream fout=new FileOutputStream(file);
                ObjectOutputStream objout=new ObjectOutputStream(fout);
                FolderEncryptObject object=new FolderEncryptObject();
                // check whether the details should be saved or not by checking the
                // status of detailssaved boolean in the codeobject which is set using the
                // checkbox in codedialog
                // if the details of encryption are not to be saved , then clear codeobj
                if(codeobject.isDetailssaved()==false) {
                    codeobject.destroyObject();
                }
                object.setCodeobj(codeobject);                
                object.setFiles(encryptedFiles);
                object.setEncryptDate(new java.util.Date());                
                objout.writeObject(object);
                objout.close();
                fout.close();
                return true;
          } catch (Exception exception) {
              exception.printStackTrace();
              return false;
          }                      
    }
    
    // function to remove the folderencrypt object from the folder
    public void removeFolderEncryptObject(String path) {
        File file=new File(path+"/ENCRYPTIONSETTINGS.FMENCS");
        file.delete();        
    }
    
    // function to read the FolderEncryptObject
    public FolderEncryptObject readFolderEncryptObject(File file) {
          try {
            FileInputStream filein=new FileInputStream(file);
            ObjectInputStream objin=new ObjectInputStream(filein);
            FolderEncryptObject encobj=new FolderEncryptObject();
            encobj=(FolderEncryptObject)objin.readObject();
            objin.close();
            filein.close();
            return encobj;
          } catch (Exception exception) {
              exception.printStackTrace();
              return null;
          }

      }
    
    
    // function to start the decryption of the selected files from thefiles folder
    public void startDecryption(final String path, final Vector files,
        final FolderEncryptionDialog parentdialog) {
        final CodeObject codeobj=new GeneralMethods().getCodeObject(2,
                parentdialog.getCodeObjectForFolder());
        if(codeobj==null) return;
        
        // process Dialog
        final EncrypterProcessProgress processDialog=
        new EncrypterProcessProgress(new javax.swing.JFrame(),false,1);                        
        Thread trd=new Thread(new Runnable(){
            public void run() {
                try {                                                            
                    processDialog.setProcessType("DECRYPTION");  
                    
                    Vector decryptedfiles=new Vector() // holds the filenams successfully decrypted
                            ,failedfiles=new Vector(); // holds the filenames that was not successfully encrypted                                     
                                        
                    processDialog.setFileProgressData(files.size(), files);                    
                    // start encryption of files
                    for (int i = 0; i < files.size(); i++) {
                        processDialog.incrementFileProcess();
                        String filename = path+"/"+ files.elementAt(i).toString();
                        EncryptedFileObject fileobject=new GeneralMethods().readEncryptedFileObject(
                                filename);
                        String data=fileobject.getContent();
                        if(data==null) {
                            failedfiles=addToFailedFiles(failedfiles,files.elementAt(i).toString(),
                                    "File Read Error");
                            continue;
                        }
                        // decrypt using the selected algorithm
                        for (int j = codeobj.getAlgorithmsused().size()-1; j >=0 ; j--) {
                           String algname = (String) codeobj.getAlgorithmsused().elementAt(j);
                            data=new GeneralAlgorithmMethods().decryptTextWithAlgorithm(data, processDialog,
                                algname,codeobj,(j+1));
                            if(data==null) break; // the encryption has failed or cancelled
                        }    
                        if(data!=null) {
                            // ie on successfull decryption save the file
                            boolean saved=new GeneralMethods().writeContentToFile(data, new File(filename));
                            if(saved==true)  {                              
                                String decryptedobj[]={fileobject.getOriginalFileExtension(),
                                files.elementAt(i).toString()};
                                decryptedfiles.addElement(decryptedobj);
                            }
                            else {
                                failedfiles=addToFailedFiles(failedfiles,files.elementAt(i).toString(),
                                    "Error saving decryptedfiles");
                                continue;
                            }
                        }
                        else {
                            // encyption failed, set the result as encryption failed
                            failedfiles=addToFailedFiles(failedfiles,files.elementAt(i).toString(),
                                    "Error during decryption");
                            continue;
                        }
                    }                    
                    processDialog.setVisible(false);
                    processDialog.dispose();                 
                    setFileExtensionToOriginal(decryptedfiles,path);
                    if(failedfiles.size()==0) {
                        //remove the folder encrypt object files
                        removeFolderEncryptObject(path);
                    }      
                    setProcessStatusForParent(parentdialog,decryptedfiles,failedfiles,
                            path,"DECRYPTION");                                                                                  
                    codeobj.clearCodeValues();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }                     
        });
        trd.start();
        processDialog.setModal(true);
        processDialog.setVisible(true);        
    }
    
    // function to encrypt the files
    public void startEncryption(final String path, final Vector files,
            final FolderEncryptionDialog parentdialog) {        
        final CodeObject codeobj=new GeneralMethods().getCodeObject(1,
                null);
        if(codeobj==null) return;
        
        // process Dialog
        final EncrypterProcessProgress processDialog=
        new EncrypterProcessProgress(new javax.swing.JFrame(),false,1); 
        
        
        Thread trd=new Thread(new Runnable(){
            public void run() {
                try {                                                            
                    processDialog.setProcessType("ENCRYPTION");  
                    
                    Vector encryptedfiles=new Vector() // holds the filenams successfully encrypted
                            ,failedfiles=new Vector(); // holds the filenames that was not successfully encrypted                                                                             
                    processDialog.setFileProgressData(files.size(), files);                    
                    // start encryption of files
                    for (int i = 0; i < files.size(); i++) {
                        processDialog.incrementFileProcess();
                        String filename = path+"/"+ files.elementAt(i).toString();
                        String data=new GeneralMethods().readFileContent(new File(filename));
                        if(data==null) {
                            failedfiles=addToFailedFiles(failedfiles,files.elementAt(i).toString(),
                                    "File Read Error");
                            continue;
                        }
                        for (int j = 0; j < codeobj.getAlgorithmsused().size(); j++) {
                           String algname = (String) codeobj.getAlgorithmsused().elementAt(j);
                            data=new GeneralAlgorithmMethods().encryptTextWithAlgorithm(data, processDialog,
                                algname,codeobj,(j+1));          
                            if(data==null) break; // the encryption has failed or cancelled
                        }          
                        if(data!=null) {
                            // ie on successfull encryption save the file
                             // check whether the details should be saved or not by checking the
                            // status of detailssaved boolean in the codeobject which is set using the
                            // checkbox in codedialog
                            // if the details of encryption are not to be saved , then save a dummy codeobject
                            if(codeobj.isDetailssaved()==false) {
                                CodeObject temp=new CodeObject();
                                
                                // if the hint is used, save it
                                temp.setHint(codeobj.getHint());
                                temp.setDetailssaved(false);
                                saveEncryptedFileObject(temp,data,new File(filename));
                            }
                            else {
                                saveEncryptedFileObject(codeobj,data,new File(filename));
                            }
                            
                            encryptedfiles.addElement(filename);
                        }
                        else {
                            // encyption failed, set the result as encryption failed
                            failedfiles=addToFailedFiles(failedfiles,files.elementAt(i).toString(),
                                    "Error during encryption");
                            continue;
                        }
                    }                    
                    codeobj.clearCodeValues();
                    placeFolderEncryptObject(codeobj,encryptedfiles,
                            new File(path+"/ENCRYPTIONSETTINGS.FMENCS"));
                    processDialog.setVisible(false);
                    processDialog.dispose();                 
                    setProcessStatusForParent(parentdialog,encryptedfiles,failedfiles,
                            path,"ENCRYPTION");                                                      
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }                     
        });
        trd.start();
        processDialog.setModal(true);
        processDialog.setVisible(true);
    }
    
    // function to set the status of the encryption or decryption to the parent 
    // it will refresh the list of files that were encrypted in the list if there is atleast one file
    // in the successfiles vector. Otherwise no files are failed and it shows the report of the files
    // that were failed and sets the encrypted flag to false in the parent and does nothing
    private void setProcessStatusForParent(FolderEncryptionDialog parentdialog, 
            Vector successfiles, Vector failedfiles,String path,String process) {
            if(successfiles.size()!=0)  {
                // Atleast one file may be encrypted so refresh the list to show only
                // the encrypted files
                parentdialog.refreshList(path);                   
                if(process.equals("ENCRYPTION")) {
                    parentdialog.setEncrypted(true);
                }
                else {
                    parentdialog.setEncrypted(false);
                }
                return;
            }
            else {
                // show the failed files and the reason
                String errormsg="The files were not able to process.\n"+
                        "The files failed and the reason are as follows\n\n";
                for(int i=0;i<failedfiles.size();i++) {
                    String errorobj[]=(String[])failedfiles.elementAt(i);
                    errormsg+=errorobj[0]+" ------>  "+errorobj[1]+"\n";
                }                        
                javax.swing.JOptionPane.showMessageDialog(null, errormsg,"ERROR",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
    }
      
    
    // function to add the file as failed file to the passed vector
    // Here the file will content will be filename and the reason
     private Vector addToFailedFiles(Vector failedfiles, String filename,String cause) {
            String fileobject[]={filename,cause};
            failedfiles.addElement(fileobject);
            return failedfiles;
    }     
            
}
