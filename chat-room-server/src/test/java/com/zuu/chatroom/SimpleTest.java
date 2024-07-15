package com.zuu.chatroom;

import org.junit.jupiter.api.Test;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 23:13
 */
public class SimpleTest {

    @Test
    public void aaa(){
        int[] nums = new int[]{0,1};
    }
    public void moveZeroes(int[] nums) {
        //双指针，一个指向0，一个指向非0
        int p = 0,q = 0,n = nums.length;
        while(p < n && q < n){
            while(p < n && nums[p] != 0) p++;
            while(q < n && nums[q] == 0) q++;
            if(p < n && q < n)
                swap(nums,p,q);
        }
    }

    public void swap(int[] nums,int i,int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
