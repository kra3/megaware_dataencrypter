/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.objects;

import java.math.BigDecimal;
import java.util.Vector;

/**
 *
 * @author sandeep
 */
public class CodeObject implements java.io.Serializable{
    
     // serial version id
    private static final long serialVersionUID=43L;
    
    private BigDecimal code;
    
    private BigDecimal equationvalue;
    
    private String hint; 
    
    private Vector algorithmsused;   
    
    private boolean equused=false;
    
    //  boolean to show whether the details of encryption are saved are not
    private boolean detailssaved=true;

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getEquationvalue() {
        return equationvalue;
    }

    public void setEquationvalue(BigDecimal equationvalue) {
        this.equationvalue = equationvalue;        
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }   
    
    // function to clear all the values in the object    
    // called when settings are not to be saved to the file during object writing
    // and also during end of process
    public void destroyObject() {
        System.out.println("calling destrution");
        this.getAlgorithmsused().removeAllElements();
        this.setAlgorithmsused(null);
        this.setCode(new BigDecimal("0"));
        this.setEquationvalue(new BigDecimal("0"));                
    }
    
    // function to clear the code and equcode
    public void clearCodeValues() {
        System.out.println("calling clearing");
        this.setCode(new BigDecimal("0"));
        this.setEquationvalue(new BigDecimal("0"));
    }        

    public boolean isEquused() {
        return equused;
    }

    public void setEquused(boolean equused) {
        this.equused = equused;
    }

    public Vector getAlgorithmsused() {
        return algorithmsused;
    }

    public void setAlgorithmsused(Vector algorithmsused) {
        this.algorithmsused = algorithmsused;
    }

    public boolean isDetailssaved() {
        return detailssaved;
    }

    public void setDetailssaved(boolean detailssaved) {
        this.detailssaved = detailssaved;
    }
}
   
