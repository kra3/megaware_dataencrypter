/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.objects;

/**
 *
 * @author name
 */
public class FolderEncryptObject implements java.io.Serializable{        
        
     // serial version id
    private static final long serialVersionUID=44L;
    
    private java.util.Vector files;
            
    private java.util.Date encryptDate;
    
    private CodeObject codeobj;
    
    

    public java.util.Vector getFiles() {
        return files;
    }

    public void setFiles(java.util.Vector files) {
        this.files = files;
    }

    public java.util.Date getEncryptDate() {
        return encryptDate;
    }

    public void setEncryptDate(java.util.Date encryptDate) {
        this.encryptDate = encryptDate;
    }

    public CodeObject getCodeobj() {
        return codeobj;
    }

    public void setCodeobj(CodeObject codeobj) {
        this.codeobj = codeobj;
    }
}
