package org.example;

import java.util.List;

public class Solution {
    public static int flippingMatrix(List<List<Integer>> matrix) {
        int n = matrix.size() / 2; // n là kích thước của góc phần tư trên bên trái
        int maxSum = 0;

        // Duyệt qua góc phần tư trên bên trái
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Lấy giá trị lớn nhất trong 4 giá trị đối xứng
                int topLeft = matrix.get(i).get(j);
                int topRight = matrix.get(i).get(2 * n - 1 - j);
                int bottomLeft = matrix.get(2 * n - 1 - i).get(j);
                int bottomRight = matrix.get(2 * n - 1 - i).get(2 * n - 1 - j);
                maxSum += Math.max(Math.max(topLeft, topRight), Math.max(bottomLeft, bottomRight));
            }
        }

        return maxSum;
    }

    public static void main(String[] args) {
        // Test case
        List<List<Integer>> matrix = List.of(
                List.of(112, 42, 83, 119),
                List.of(56, 125, 56, 49),
                List.of(15, 78, 101, 43),
                List.of(62, 98, 114, 108)
        );

        System.out.println(flippingMatrix(matrix)); // Output: 414
    }
}

