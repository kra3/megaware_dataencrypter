/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.objects;

import java.util.Date;

/**
 *
 * @author sandeep
 */

public class AlgorithmObject implements java.io.Serializable{
     // serial version id
    private static final long serialVersionUID=41L;
    
    
    private String algname;

    private String description;
    
    private String version;
    
    private Date datemodified;
    
    private String author;
    
    public AlgorithmObject(String algname,String description) {
        this.algname=algname;
        this.description=description;
    }
    
    public AlgorithmObject() {};

    public String getAlgname() {
        return algname;
    }

    public void setAlgname(String algname) {
        this.algname = algname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(Date datemodified) {
        this.datemodified = datemodified;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
}
