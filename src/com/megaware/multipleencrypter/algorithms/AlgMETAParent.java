
package com.megaware.multipleencrypter.algorithms;

import com.megaware.multipleencrypter.methods.GeneralAlgorithmMethods;
import com.megaware.multipleencrypter.methods.SingleEncryptionAlgorithmMethods;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * This is the parent class for the AlgMeta Algorithms here AlgTwoEncodeText,
 * AlgTwoDecodeText;
 * It holds all the fields that are common to both the algorithms of its category
 * @author sandeep
 */
public class AlgMETAParent extends Algorithm{                                                                               
        
        // vector holding the sum of digit values for each character
        private static Vector sumDigitValues;
        
        // array holding possible bitwise operations                          
        private String possibleBitOperations[]={"^"// XOR
                                               ,"~^"//XNOR
                                               };
        
        // The vector that will hold the bit operations 256 times in 
        // shuffled order
        private Vector bitOperationSequence;
        
        // The seed value for the process : THE GRANPA CODE
        private static  BigDecimal seedvaluecode;
        
        // This is the function that keys for each character 
        // The key calculations are same for encryption and decryption
        
        // This is a additional value to be added to the seed value code. If such a 
        // value is not used, the even multiples of successive meta 2.0 will produce
        // negative effect to reversing the operation.
        private int encryptionLevel;
        
        
        
        public  void calculateValues() {                                   
           // set the initial conditions;
            // Integer array holding the increment values
            int incrementValues[]=new int[256]; // increment values for 256 characters
            setBitOperationSequence(new Vector());
            setSumDigitValues(new Vector());

            // set the seedvalue as the code itself
            setSeedvaluecode(new BigDecimal(getCode().toString()));
            
            // add the encryption level to prevent the reverse processing in
            // multiple occurences of meta 2.0.
            System.out.println("Encryption level is "+getEncryptionLevel());
            setSeedvaluecode(getSeedvaluecode().add(new BigDecimal(getEncryptionLevel())));
            /* get the sum of digits of code 
             * It will used as the seed value for incremented values of addition
             */
            int codedigits[]=null,equdigits[]=null;
            codedigits=getBigDecimalDigits(getCode());            
            int codedigitssum = new GeneralAlgorithmMethods().getSumOfDigits(null, getCode());            
            if (getEqucode().compareTo(new BigDecimal("0")) != 0) {                
                setSeedvaluecode(getSeedvaluecode().add(getEqucode()));
                codedigitssum += new GeneralAlgorithmMethods().getSumOfDigits(null, getEqucode());                
                equdigits=getBigDecimalDigits(getEqucode());
            }

            // the seed and increment values;           
            // set the base/seed value for incrementing as the sum of digits of
            // code and equation code(if used)


            //get the incrementValues;
            // the value for incrementing is the value of sum of digits of
            // seedvaluecode
            int seedvalueincredigitssum = new GeneralAlgorithmMethods().getSumOfDigits(null, getSeedvaluecode());

            // if increment value is 0 set it to 1;
            seedvalueincredigitssum = (seedvalueincredigitssum <= 0) ? 1 : seedvalueincredigitssum;
            new SingleEncryptionAlgorithmMethods().fillArrayWithSeedValues(
                    codedigitssum, seedvalueincredigitssum,incrementValues);    
            
            // get sum of digits for 256 chars           
            getKeyDigitsSumVector(incrementValues);                                    
            
            // get the sequence vector of bit operations
            createBitOperationSequence();

            // now shuffle the order of the sumDigitValues Vector according to 
            // the key and equation            
            shuffleVectorValues(codedigits);
            if(equdigits!=null) {
                for (int i = 0; i < equdigits.length; i++) {
                    int j = equdigits[i];                    
                }
                shuffleVectorValues(equdigits);
            }                              
        }           
        
        /* since 1.2 */
        // This function fills the sumDigitValues Vector with incrementvalues adding to the 
        // seedvaluecode 
        private void getKeyDigitsSumVector(int increValues[]) {
            for (int i = 0; i < increValues.length; i++) {
                int j = increValues[i];
                BigDecimal currKey=seedvaluecode;
                currKey=currKey.add(new BigDecimal(j));
                int sumofdigits=new GeneralAlgorithmMethods().getSumOfDigits(getSumDigitValues(),currKey);
                getSumDigitValues().addElement(sumofdigits);            
            }
        }
        
        /* since 1.2*/
        // This function will place the available bit operations in a repeated 
        // sequence on the vector 256 times
        private void createBitOperationSequence() {
            int bitopertorslen=getPossibleBitOperations().length;
            int i=0,j=0;
            while(i<256) {
                getBitOperationSequence().addElement(getPossibleBitOperations()[j]);
                j++;
                i++;
                if(j==bitopertorslen) {j=0;}
            }
        }
        
       // function to return the digits of a given bigdecimal as integer array
       // used by shuffle algorithms
       public int[] getBigDecimalDigits(BigDecimal value) {             
           int len=value.toString().length();
           if(len==0||value.compareTo(new BigDecimal("0"))==0) return  null;           
           int digits[]=new int[len];
           int index=0;
           while(value.compareTo(new BigDecimal("0"))==1) {
                BigDecimal values[]=value.divideAndRemainder(new BigDecimal("10"));
                int r=Integer.parseInt(values[1].toString());            
                digits[index]=r;
                index++;
                value=new BigDecimal(values[0].toString());                         
           }             
           return digits;
       }    
       
       
       
       // function to shuffle as per the algorithm 
       protected void shuffleVectorValues(int digits[]) {
           int len=256;            
            for (int i = 0; i < digits.length ; i++) {
                int j = digits[i];                
                
                for (int k = 0; k <len-(j) ; k+=j) {     
                    // shuffling incremented values vector helloaccording to digits
                    int temp=(Integer)getSumDigitValues().elementAt(k);
                    getSumDigitValues().setElementAt( 
                            getSumDigitValues().elementAt(k+j),k);                            
                    getSumDigitValues().setElementAt(temp,k+j);               
                    
                    // shuffling bitoperationsequence vector according to digits
                    String str=(String)getBitOperationSequence().elementAt(k);
                    getBitOperationSequence().setElementAt( 
                            getBitOperationSequence().elementAt(k+j),k);                            
                    getBitOperationSequence().setElementAt(str,k+j);      
                    
                    /* if not used J++ then there is problem when the digit is 0
                     * The loop never ends
                     */ 
                    j++;
                }                                
            }
       }
       
       // function to find the sum of digits of int parameters
       protected int findIntDigitSum(int value) {
           int sum=0,r;
           while(value!=0) {
               r=value%10;
               sum=sum+r;
               value=value/10;
           }
           return sum;
       }
       // function to perform the bit operation checking the type of operator
       // and return the modified character value;
       protected char doBitOperation(char character,int keyvalue,String operation) {
           if(operation.equals(getPossibleBitOperations()[0])) { //XOR
               character=(char)((keyvalue^(int)character));
           }
           else if(operation.equals(getPossibleBitOperations()[1]))  {// XNOR
               character=(char)(~(keyvalue^(int)character));
           }
           return character;
       }
       

        // accessor methods              
        public static Vector getSumDigitValues() {
            return sumDigitValues;
        }

        public static void setSumDigitValues(Vector aSumDigitValues) {
            sumDigitValues = aSumDigitValues;
        }
     
        public static BigDecimal getSeedvaluecode() {
            return seedvaluecode;
        }

        public static void setSeedvaluecode(BigDecimal aSeedvaluecode) {
            seedvaluecode = aSeedvaluecode;
        }

    public String[] getPossibleBitOperations() {
        return possibleBitOperations;
    }

    public void setPossibleBitOperations(String[] possibleBitOperations) {
        this.possibleBitOperations = possibleBitOperations;
    }

    public Vector getBitOperationSequence() {
        return bitOperationSequence;
    }

    public void setBitOperationSequence(Vector bitOperationSequence) {
        this.bitOperationSequence = bitOperationSequence;
    }

    public int getEncryptionLevel() {
        return encryptionLevel;
    }

    public void setEncryptionLevel(int encryptionLevel) {
        System.out.println(" setting Encryption level is "+encryptionLevel);
        this.encryptionLevel = encryptionLevel;
    }

   
}
