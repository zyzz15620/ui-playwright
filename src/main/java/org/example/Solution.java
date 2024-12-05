package org.example;

import java.util.List;

public class Solution {
    public static int wordCount() {
        String input = "The first line contains an ^#$";
        int count = 0;

        for(String word : input.split(" ")){
            if(word.length() >= 3 & word.matches("[a-zA-Z0-9]+") &&
                    word.matches(".*[aeiouAEIOU].*") &&
                    word.matches(".*[^aeiouAEIOU0-9].*")){
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(wordCount());
    }
}

