/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.algorithms;

import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import java.math.BigDecimal;

/**
 *
 * @author name
 */
public class Algorithm implements java.lang.Runnable{
    
    public  boolean stop=false;
    
    public  boolean pause=false;
    
    // The user defined key
    private  BigDecimal code;

    // The user defined value from equation dialog 
    private  BigDecimal equcode;

    // The text upon which the operations are to be done
    private  String text;      
    
    // Encryption or decryption Thread
    private Thread thread;    
    
    // The processProgress Dialog for the Decryption
    private  EncrypterProcessProgress progressDialog;
    
    public  void pause() {
        pause=true;
    }
    
    public synchronized void myresume() {
        pause=false;
        notify();
    }
    
    public void setStop(boolean stop) {
        this.stop=stop;
    }
    
    // function to clear the values of the code and other critical values
    public void clearAllValues() {
        setCode(new BigDecimal("0"));
        setEqucode(new BigDecimal("0"));
        setText("");        
    }
   
    public void run() {
        
    }
    
    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }   
    
    public  BigDecimal getCode() {
        return code;
    }

    public  void setCode(BigDecimal aCode) {
        code = aCode;
    }

    public  BigDecimal getEqucode() {
        return equcode;
    }

    public  void setEqucode(BigDecimal aEqucode) {
        equcode = aEqucode;
    }

    public String getText() {
        return text;
    }

    public  void setText(String aText) {
        text = aText;
    }
    
    public  EncrypterProcessProgress getProgressDialog() {
        return progressDialog;
    }

    public  void setProgressDialog(EncrypterProcessProgress aProgressDialog) {
        progressDialog = aProgressDialog;
    }

}
