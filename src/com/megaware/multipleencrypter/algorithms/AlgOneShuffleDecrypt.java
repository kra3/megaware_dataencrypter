/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.algorithms;

/**
 *
 * @author sandeep
 */
import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import com.megaware.multipleencrypter.methods.GeneralAlgorithmMethods;
import com.megaware.multipleencrypter.methods.ShuffleAlgorithmOneMethods;
import java.math.BigDecimal;

/**
 *
 * @author sandeep
 */
public class AlgOneShuffleDecrypt extends Algorithm{
    
    //code for encryption
    private  BigDecimal code;
    
    // equation code for encryption
    private  BigDecimal equcode;
    
    // text to be encrypted
    private  String decodeText;
    
    // progress dialog for the algorithm
    private EncrypterProcessProgress processDialog;
    
    // Encryption thread
    private Thread trdDecryption;
    
    // Integer array to hold the digits of code
    private int codedigits[];
    
    // Integer array to hold the digits of equcode
    private int equdigits[];
    
    // character array to hold the characters of decodeText
    private char decodeTextChars[];
            
    // function to set the details
    public void setDecryptionDetails(BigDecimal code,BigDecimal equcode,String decodeText
                    ,EncrypterProcessProgress processDialog){
        this.setCode(code);
        this.setDecodeText(decodeText);
        this.processDialog=processDialog;
        this.setEqucode(equcode);
        this.calculateValues();
        this.startDecryption();
    }
    
    // function to calculate the values required for encryption
    protected void calculateValues() {
        codedigits=new ShuffleAlgorithmOneMethods().getBigDecimalDigits(getCode());
        equdigits=new ShuffleAlgorithmOneMethods().getBigDecimalDigits(getEqucode());          
        int textlen=getdecodeText().length();
        decodeTextChars=new char[textlen];
        getdecodeText().getChars(0, textlen, decodeTextChars, 0);       
        int maxchars=new ShuffleAlgorithmOneMethods().getShuffleCount(textlen, 
                codedigits, equdigits);
        processDialog.setProcessData("DECRYPTION SHUFFLE 1.0",maxchars, maxchars*2);
        processDialog.setProcessingTypeCaption("shuffles");
    }
    
    // function to start the encryption thread
    public void startDecryption() {
        trdDecryption=new Thread(this);
        gettrdDecryption().start();
    }        
           
    // function to shuffle the decodeText using the digits array
    protected synchronized void shuffledecodeText(int digits[]) {
        
        try {
            
            // set len equivalent to the last element of array for the alignment of remainder
            int len=getDecodeText().length()-1;
            
            // shuffle as per the code digits
            for (int i = digits.length-1; i>=0 && stop == false; i--) {
                int j = digits[i];    
                /*
                 * skip if the digit is 0
                 */
                 if(j==0) continue;
                int rem=len%j;                                                                 
                for (int k = len-rem; k-j >=0 && stop == false; k-=j) {                    
                    
                    // pausing the thread
                    synchronized(this) {
                        while(pause==true) {
                            wait();
                        }
                    }
                    //System.out.println("k"+k+" rem "+rem+" digit "+digits[i] );
                    char temp=(char)decodeTextChars[k];
                    decodeTextChars[k]=(char)decodeTextChars[k-j];
                    decodeTextChars[k-j]=(char)temp;
                    processDialog.incrementProcess();      
                    //System.out.println("after pass "+(j)+"."+(k)+" "+String.copyValueOf(decodeTextChars));
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
        this.setDecodeText(null);
        this.codedigits=null;
        this.equdigits=null;
        this.decodeTextChars=null;
    }
    
    public void run() {
        try {               
               // If an equation was used to shuffle the text , then use
               // it first to reshuffle
               if(equdigits!=null) {
                   shuffledecodeText(equdigits);
               }
               shuffledecodeText(codedigits);
               // set the text after discarding the added space during encryption
               //setDecodeText(String.copyValueOf(decodeTextChars,0,decodeTextChars.length-1));
               setDecodeText(String.copyValueOf(decodeTextChars));
               if(stop==true) {
                   clearAllValues();
               }
        } catch (Exception exception) {
            exception.printStackTrace();
            clearAllValues();
        }

    }

    public String getdecodeText() {
        return getDecodeText();
    }   
    
    public Thread gettrdDecryption() {
        return trdDecryption;
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

    public String getDecodeText() {
        return decodeText;
    }

    public void setDecodeText(String decodeText) {
        this.decodeText = decodeText;
    }
}



