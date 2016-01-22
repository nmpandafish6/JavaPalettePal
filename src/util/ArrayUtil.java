/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Arrays;

/**
 *
 * @author Nicolas
 */
public class ArrayUtil {
    
    public static int getMaxIndex(int[] array){
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;
        for(int i = 0; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    public static int[] looseSort(int[] array){
        int[] looseArray = new int[array.length];
        for(int i = 0; i < array.length; i++){
            int index = array[i];
            int j = i;
            for(; j > 0 && array[looseArray[j-1]] > index; j--){
                looseArray[j] = looseArray[j-1];
            }
            looseArray[j] = i;
        }
        return looseArray;
    }
}
