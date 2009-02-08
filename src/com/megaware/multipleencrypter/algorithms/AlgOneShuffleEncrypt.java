/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.algorithms;

import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import com.megaware.multipleencrypter.methods.GeneralAlgorithmMethods;
import com.megaware.multipleencrypter.methods.ShuffleAlgorithmOneMethods;
import java.math.BigDecimal;

/**
 *
 * @author sandeep
 */
public class AlgOneShuffleEncrypt extends Algorithm{
    
    //code for encryption
    private  BigDecimal code;
    
    // equation code for encryption
    private  BigDecimal equcode;
    
    // text to be encrypted
    private  String encodeText;
    
    // progress dialog for the algorithm
    private EncrypterProcessProgress processDialog;
    
    // Encryption thread
    private Thread trdEncryption;
    
    // Integer array to hold the digits of code
    private int codedigits[];
    
    // Integer array to hold the digits of equcode
    private int equdigits[];
    
    // character array to hold the characters of encodetext
    private char encodeTextChars[];
            
    // function to set the details
    public void setEncryptionDetails(BigDecimal code,BigDecimal equcode,String encodeText
                    ,EncrypterProcessProgress processDialog){
        this.setCode(code);
        this.setEncodeText(encodeText);
        this.processDialog=processDialog;
        this.setEqucode(equcode);
        this.calculateValues();
        this.startEncryption();
    }
    
    // function to calculate the values required for encryption
    protected void calculateValues() {
        codedigits=new ShuffleAlgorithmOneMethods().getBigDecimalDigits(getCode());
        equdigits=new ShuffleAlgorithmOneMethods().getBigDecimalDigits(getEqucode());              
        int textlen=getEncodeText().length();
        encodeTextChars=new char[textlen];        
        getEncodeText().getChars(0, textlen, encodeTextChars, 0);       
        int maxchars=new ShuffleAlgorithmOneMethods().getShuffleCount(textlen, 
                codedigits, equdigits);
        processDialog.setProcessData("ENCRYPTION SHUFFLE 1.0",maxchars, maxchars*2);
        processDialog.setProcessingTypeCaption("shuffles");
    }
    
    // function to start the encryption thread
    public void startEncryption() {
        trdEncryption=new Thread(this);
        getTrdEncryption().start();
    }       
    
    // function to shuffle the encodetext using the digits array
    protected synchronized void shuffleEncodeText(int digits[]) {
        
        try {
            // shuffle as per the code digits                       
            for (int i = 0; i < digits.length && stop == false; i++) {
                int j = digits[i];                
                /*
                 * when the digit is 0, ignore it
                 */
                if(j==0) continue;
                for (int k = 0; k < encodeTextChars.length-(j) && stop == false; k+=j) {                    
                    
                    // pausing the thread
                    synchronized(this) {
                        while(pause==true) {
                            wait();
                        }
                    }                    
                    char temp=encodeTextChars[k];
                    encodeTextChars[k]=encodeTextChars[k+j];
                    encodeTextChars[k+j]=temp;
                    processDialog.incrementProcess();                    
                    //System.out.println("after pass "+(j)+"."+(k)+" "+String.copyValueOf(encodeTextChars));
                    Thread.sleep(2);
                }                                
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    // function to clear the values 
    public void clearAllValues() {
        this.setCode(new BigDecimal("0"));
        this.setEqucode(new BigDecimal("0"));        
        this.setEncodeText(null);
        this.codedigits=null;
        this.equdigits=null;
        this.encodeTextChars=null;
    }
    
    public void run() {
        try {
               shuffleEncodeText(codedigits);
               if(equdigits!=null) {
                   System.out.println("shuffling with equation");
                   shuffleEncodeText(equdigits);
               }
               setEncodeText(String.copyValueOf(encodeTextChars));
               if(stop==true) {
                   clearAllValues();
               }
        } catch (Exception exception) {
            exception.printStackTrace();
            clearAllValues();
        }

    }

    public String getEncodeText() {
        return encodeText;
    }   
    
    public Thread getTrdEncryption() {
        return trdEncryption;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getEqucode() {
        return equcode;
    }

    public void setEqucode(BigDecimal equcode) {
        this.equcode = equcode;
    }

    public void setEncodeText(String encodeText) {
        this.encodeText = encodeText;
    }
}


