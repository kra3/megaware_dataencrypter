/*
 * The class to encode the character of a text using the Alogrithm1.0 technology
 * 
 */

package com.megaware.multipleencrypter.algorithms;

import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import java.math.BigDecimal;

/**
 * AlgTwoEncodeText1.0 - Megaware Encryption Technology Algorithm 1.0 
 * This is the base algorithm or the default algorithm of this program. It encrypts
 * data to the utmost security.
 * You as a user is authorized to use this algorithm as you wish and to modify it as
 * much as your intelligence can.But please place this text on any modified file and 
 * if you respect my toil and hardwork in implmenting this algorithm, please place the
 * following on the modified or distribution file
 * Base Version Developer -------> Sandheep G.R
 * Organization -----------------> Megaware
 * Contact email ----------------> gr.sandeep1@gmail.com 
 * Version ----------------------> 2.0.0
 * @author sandeep
 */
public class AlgTwoEncodeText extends AlgMETAParent{            
        
        // empty constructor
        public AlgTwoEncodeText() {};
                               
        // function to start the encryption details
        public void setEncryptionDetails(BigDecimal code,String encodeText,
                EncrypterProcessProgress progress) {
             this.setText(encodeText);
             this.setCode(code);                     
             this.setProgressDialog(progress);
            // calculate the code values using the values got             
            calculateValues();                                                         
        }
        
        // function to start encrypting the characters
        public void startEncrypting() {
           setThread(new Thread(this));
           getThread().start();
        }
        
        // function to clear the values 
        public   void clearAllValues() {
            this.setCode(new BigDecimal("0"));
            this.setEqucode(new BigDecimal("0"));            
            this.setText(null);
        }                       
                    
    	public void run()
	{	
            try
            {	                
                // if and error has occured, return 
                if(getEqucode().compareTo(new BigDecimal("0"))==0&&getCode().compareTo(
                        new BigDecimal("0"))==0) { 
                    // error                     
                    return;
                }
                   
                char textchars[]=new char[getText().length()];
                getText().getChars(0, textchars.length, textchars, 0);
                int keyslen=getSumDigitValues().size();                
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
                    System.out.println("Operation " +operation);
                    textchars[i]=doBitOperation(textchars[i], keyvalue, operation);
                    getProgressDialog().incrementProcess();
                    Thread.sleep(1);
                }
                setText(String.copyValueOf(textchars));                   
                                                 
                // if the thread was stopped by user then clear the values 
                if(stop==true) {
                    this.clearAllValues();
                }
                                                
                //setEncodeText(encodeText.replaceAll(changeChar, replaceText));
	}catch(Exception e)
	{ 
            // on error clear the code values 
            clearCodeValues();
            e.printStackTrace();
        }
    }
        
    // function to clear the code values
    public  void clearCodeValues() {
        setCode(new BigDecimal("0"));
        setEqucode(new BigDecimal("0"));
    }

}

