/*
 * AlgTwoDecodeText is the class used to decrypt the character encrypted using
 * AlgOneEncodeText
 */
package com.megaware.multipleencrypter.algorithms;

import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import java.math.BigDecimal;

/**
 * AlgTwoDecodeText1.0 - Megaware Encryption Technology Algorithm 1.0 
 * This is the decryption algorithm for the AlgOneEncodeText (META 1.0)
 * You as a user is authorized to use this algorithm as you wish and to modify it as
 * much as your intelligence can.But please place this text on any modified file and 
 * if you respect my toil and hardwork in implmenting this algorithm, please place the
 * following on the modified or distribution file
 * Base Version Developer -------> Sandheep G.R
 * Organization -----------------> Megaware
 * Contact email ----------------> gr.sandeep1@gmail.com 
 * Version ----------------------> 1.0.0
 * @author sandeep
 */
public class AlgTwoDecodeText extends AlgMETAParent {

    // empty constructor
    public AlgTwoDecodeText() {
    }    
    // function to start the encryption details
    public  void setDecryptionDetails( 
        BigDecimal code,String decodeText,
            EncrypterProcessProgress progress) {
            setText(decodeText);
            setCode(code);                     
            setProgressDialog(progress);            
            // calculate the code values using the values got
            calculateValues();
        }
        
        // function to start encrypting the characters
        public void startDecrypting() {
           setThread(new Thread(this));
           getThread().start();
        }                        

        // function to clear the values 
        // function to clear the values 
        public  void clearAllValues() {
            setCode(new BigDecimal("0"));
            setEqucode(new BigDecimal("0"));
            setText(null);
        }

        public void run() {
            try {


                // if and error has occured, return 
                if (getEqucode().compareTo(new BigDecimal("0")) == 0 && getCode().compareTo(
                        new BigDecimal("0")) == 0) {
                    // error 
                    return;
                }
                System.out.println("decrypt");
                int keyslen=getSumDigitValues().size();
                char textchars[]=new char[getText().length()];
                getText().getChars(0, textchars.length, textchars, 0);
                System.out.println("size "+getText().length());
                for (int i = 0,j=0; i < textchars.length&&stop==false; i++,j++) {
                    synchronized(this) {
                        while(pause==true) {
                            wait();
                        }
                    }
                    if(j>=keyslen) {
                        j=0;
                    }
                    int keyvalue=(Integer)getSumDigitValues().elementAt(j); 
                    String operation=(String)getBitOperationSequence().elementAt(
                            findIntDigitSum(keyvalue));
                    textchars[i]=doBitOperation(textchars[i], keyvalue, operation);
                    getProgressDialog().incrementProcess();
                    Thread.sleep(2);
                }
                setText(String.copyValueOf(textchars));
                // if the thread was stopped by used then clear the values
                if (stop == true) {
                    clearAllValues();
                }
            } catch (Exception e) {
                // on error clear the values
                clearCodeValues();
                e.printStackTrace();
            }
        }

        // function to clear the code values
        public void clearCodeValues() {
            setCode(new BigDecimal("0"));
            setEqucode(new BigDecimal("0"));
        }
}

