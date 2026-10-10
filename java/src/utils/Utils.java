package utils;

public class Utils {
    // מחפשת פריט במערך. מחזירה את המקום שלו, או 1- אם הוא לא שם
    public static int findIndex(Object[] arr, Object item) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(item))
                return i;
        }
        return -1;
    }

    // מחברת את כל המספרים במערך
    public static int sumArray(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }

    public static long now() {
        return System.currentTimeMillis();
    }
}