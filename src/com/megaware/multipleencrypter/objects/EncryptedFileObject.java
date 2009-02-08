/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.objects;

/**
 *
 * @author sandeep
 */
public class EncryptedFileObject implements java.io.Serializable{
    
    // serial version id
    private static final long serialVersionUID=42L;
    
    private CodeObject codeobject;
    
    private String content="";
    
    private String originalFileExtension="";
        

    public CodeObject getCodeobject() {
        return codeobject;
    }

    public void setCodeobject(CodeObject codeobject) {
        this.codeobject = codeobject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginalFileExtension() {
        return originalFileExtension;
    }

    public void setOriginalFileExtension(String originalFileExtension) {
        this.originalFileExtension = originalFileExtension;
    }   

}
