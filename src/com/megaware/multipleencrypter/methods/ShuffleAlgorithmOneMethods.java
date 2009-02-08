/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.megaware.multipleencrypter.methods;

import java.math.BigDecimal;

/**
 *
 * @author sandeep
 */
public class ShuffleAlgorithmOneMethods {   
    
    
       public ShuffleAlgorithmOneMethods() {};
       
       // function to return the digits of a given bigdecimal as integer array
       // used by shuffle algorithms
       public int[] getBigDecimalDigits(BigDecimal value) {  
           System.out.println("value digits"+value);
           int len=value.toString().length();
           if(len==0||value.compareTo(new BigDecimal("0"))==0) return  null;           
           int digits[]=new int[len];
           // always shuffle once
           //digits[0]=1;
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
       
       // function to calculate the number of iteration or shuffling in case of shuffle
       // algorithms. Here the parameters are textlength and codedigit ans equationdigits
       public int getShuffleCount(int textlength,int codedigits[],int equdigits[]) {
           int count=0;
           for (int i = 0; i < codedigits.length; i++) {
               int j = codedigits[i];
               /* As the shuffling is done with an interval of each digit, the total
                * number of shuffles done by a single digit will be equal to the 
                * textlength divided by that digit
                * for eg: if digit is 2 and the textlength is 100. First replacement is
                * 0 then 0+2, 1+2,.....                
                */               
               if(j>0)
                count+=textlength/j;                                
           }
           
           if(equdigits!=null) {
               for (int i = 0; i < equdigits.length; i++) {
                   // same as for code digits
                   int j = equdigits[i];
                   if(j>0)
                        count+=textlength/j;                   
               }
           }
           return count;
       }                      
}
