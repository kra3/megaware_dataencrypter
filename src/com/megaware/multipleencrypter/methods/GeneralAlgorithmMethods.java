/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.methods;

import com.megaware.multipleencrypter.algorithms.AlgOneShuffleDecrypt;
import com.megaware.multipleencrypter.algorithms.AlgOneShuffleEncrypt;
import com.megaware.multipleencrypter.algorithms.AlgTwoDecodeText;
import com.megaware.multipleencrypter.algorithms.AlgTwoEncodeText;
import com.megaware.multipleencrypter.algorithms.Algorithm;
import com.megaware.multipleencrypter.frames.EncrypterProcessProgress;
import com.megaware.multipleencrypter.objects.CodeObject;
import java.math.BigDecimal;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author name
 */
public class GeneralAlgorithmMethods {
           
        
        // function to apply Encryption Algone to the passed text data and return the encrypted text
    public String applyEncryptionAlgTwo(String data,EncrypterProcessProgress processDialog,
               CodeObject codeobj,int encLevel) {
       // Encryption characters passed because in folder encrypt in case of many files the
       // for each file it may need to be calculated 
       try {                                                                   
            BigDecimal code = codeobj.getCode();                                                                                  
            int maxchars=data.length();
            int maxtime=maxchars*2;
            processDialog.setProcessData("ENCRYPTION ALG-META-2.O"+" [ "+encLevel+" ] ", maxchars, maxtime);            
            AlgTwoEncodeText algtwo = new  AlgTwoEncodeText();
            processDialog.setAlgorithm((Algorithm)algtwo);
            algtwo.setEqucode(codeobj.getEquationvalue());
            algtwo.setEncryptionLevel(encLevel);  
            algtwo.setEncryptionDetails(code, data,processDialog);                                  
            algtwo.startEncrypting();
            algtwo.getThread().join();            
            String encodedtext= algtwo.getText();  
            System.out.println("The text noew is "+encodedtext);
            algtwo.clearAllValues();
            return encodedtext;
           }catch(Exception exception)   {
                exception.printStackTrace();
                return null;
           }           
       }
    
       // function to pass the decryption algorithm of to the data and return the decrypted text
        public String applyDecryptionAlgTwo(String data,EncrypterProcessProgress processDialog,
               CodeObject codeobj,int encLevel) {
           try {                              
                BigDecimal code = codeobj.getCode();                                                    
                int maxchars=data.length();
                int maxtime=maxchars*2;
                processDialog.setProcessData("DECRYPTION ALG-META-2.0"+" [ "+encLevel+" ] ", maxchars, maxtime);                                                      
                AlgTwoDecodeText algtwo=new  AlgTwoDecodeText();
                algtwo.setEqucode(codeobj.getEquationvalue());
                algtwo.setEncryptionLevel(encLevel);
                algtwo.setDecryptionDetails(code, data,processDialog);                            
                processDialog.setAlgorithm((Algorithm)algtwo);
                algtwo.startDecrypting();
                algtwo.getThread().join();                                       
                String decodedtext= algtwo.getText();
                 algtwo.clearAllValues();
                return decodedtext;
           } catch (Exception exception) {
               exception.printStackTrace();
               return null;
           }
       }
        
       // function to encrypt data with AlgOneShuffle algorithm
       public String applyAlgOneShuffleEncrypt(String data,EncrypterProcessProgress processDialog,
               CodeObject codeobj,int encLevel) {
            try {
               AlgOneShuffleEncrypt algoneshuffle = new AlgOneShuffleEncrypt();
               processDialog.setAlgorithm((Algorithm)algoneshuffle);
               algoneshuffle.setEncryptionDetails(codeobj.getCode(), codeobj.getEquationvalue(), data, processDialog);                              
               processDialog.setProcessType("ENCRYPTION SHUFFLE 1.0"+" [ "+encLevel+" ] ");
               algoneshuffle.getTrdEncryption().join();
               data = algoneshuffle.getEncodeText();
               return data;
           } catch (InterruptedException ex) {
               Logger.getLogger(GeneralAlgorithmMethods.class.getName()).log(Level.SEVERE, null, ex);
           }
           return null;
       }
       
       // function to decrypt data with AlgOneShuffle algorithm
       public String applyAlgOneShuffleDecrypt(String data,EncrypterProcessProgress processDialog,
               CodeObject codeobj,int encLevel) {
            try {
               AlgOneShuffleDecrypt algoneshuffle = new AlgOneShuffleDecrypt();
               processDialog.setAlgorithm((Algorithm)algoneshuffle);
               algoneshuffle.setDecryptionDetails(codeobj.getCode(), codeobj.getEquationvalue(), data, processDialog);                              
               processDialog.setProcessType("DECRYPTION SHUFFLE 1.0"+" [ "+encLevel+" ] ");
               algoneshuffle.gettrdDecryption().join();
               data = algoneshuffle.getdecodeText();
               return data;
           } catch (InterruptedException ex) {
               Logger.getLogger(GeneralAlgorithmMethods.class.getName()).log(Level.SEVERE, null, ex);
           }
           return null;
       }
       

       
        // function encrypt the text according to the algorithm selected by using the
        // appropriate algorithm
        // here the last argument encLevel is the level of the current algorithm in the
        // hierarchy
        public String encryptTextWithAlgorithm(String data,
                                EncrypterProcessProgress processDialog,
                                String algname,CodeObject codeobj,int encLevel) {            
            if(algname.equals("ALGORITHM META 2-0")) {
                data=applyEncryptionAlgTwo(data,
                processDialog,codeobj,encLevel);
            }            
            else if(algname.equals("ALGORITHM SHUFFLE 1-0")) {
                data=applyAlgOneShuffleEncrypt(data, processDialog, codeobj,encLevel);
            }       
            return data;
        }

        // function decrypt the text according to the algorithm selected by using the
        // appropriate algorithm
        // here the last argument encLevel is the level of the current algorithm in the
        // hierarchy
        public String decryptTextWithAlgorithm(String data,
                                EncrypterProcessProgress processDialog,
                                String algname,CodeObject codeobj,int encLevel) {            
            if(algname.equals("ALGORITHM META 2-0")) {
                data=new GeneralAlgorithmMethods().applyDecryptionAlgTwo(
                data, processDialog, codeobj,encLevel);
            }            
            else if(algname.equals("ALGORITHM SHUFFLE 1-0")) {
                data=applyAlgOneShuffleDecrypt(data, processDialog, codeobj,encLevel);
            }
            return data;
        }
    
    
    

       // function to get the sum of digits of the code
       // Check the sum of digits and if it in the vector add +1 and then again check
    public int getSumOfDigits(Vector sumDigitValues, BigDecimal code) {
        int sum=0;
        while(code.compareTo(new BigDecimal("0"))==1) {
            BigDecimal values[]=code.divideAndRemainder(new BigDecimal("10"));
            int r=Integer.parseInt(values[1].toString());            
            sum+=r;
            code=new BigDecimal(values[0].toString());                         
        }        
        
        // if the sumDigitValues is null then no checking for the existance of 
        // duplicate value just return the sum of digits
        if(sumDigitValues==null) {
            return sum;
        }
        
        // if the value is already in the list then increment the sum by 1
        while(this.checkInValues(sumDigitValues, sum)) {
            sum=sum+50;
        }        
        return sum;
    }        
    
    // function to check whether the value is in the vector
    public boolean checkInValues(Vector values,int value) {
        for (int i = 0; i < values.size(); i++) {
            int objvalue = Integer.parseInt(values.elementAt(i).toString());
            if(objvalue==value) {
                return true;
            }
        }
        return false;
    }
       
       // function to return the character corresponding to the last digit in the code
       // for applymetacbe
       protected char getLetterForLastCodeDigit(BigDecimal code) {
           String strcode=code.toString();           
           String lastdigit=strcode.substring(strcode.length()-1);
           char ch=((char)(65+Integer.parseInt(lastdigit)));
           return ch;
       }
       
       // function to calculate the number of characters to replace from the text 
       // by calculating the number of double spaces in them
       // used by meta1.0 algorithms
       public int calculateDecryptCharacters(String text) {
           java.util.StringTokenizer strtok=new java.util.StringTokenizer(text,"  ");
           return strtok.countTokens();           
       }
              
       
}
