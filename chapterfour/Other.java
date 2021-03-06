package chapterfour;


public class Other {
    
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * 题目：实现函数doublePower(double base, int exponent)，求base的exponent次方，
     * 不得使用库函数，同时不需要考虑大数问题。
     */
    public static double doublePower(double base, int exponent) {
        double result = 1.0;
        if (doubleEquals(base, 0.0)) {
            if (exponent <= 0) {
                throw new RuntimeException("Invalid param!");
            } else {
                result = 0.0;
            }
        } else {
            int absExponent = exponent < 0 ? -exponent : exponent;
            result = powerHelper2(base, absExponent);
            if (exponent < 0) {
                result = 1.0 / result;
            }
        }
        return result;
    }
    
    private static boolean doubleEquals(double num1, double num2) {
        if (num1 - num2 > -0.0000001 && num1 - num2 < 0.0000001) {
            return true;
        } else {
            return false;
        }
    }
    
    private static double powerHelper(double base, int absExponent) {
        double result = 1.0;
        for (int i = 0; i < absExponent; i++) {
            result *= base;
        }
        return result;
    }
    
    private static double powerHelper2(double base, int absExponent) {
        if (absExponent == 0) {
            return 1;
        } else if (absExponent == 1){
            return base;
        } else {
            double result = powerHelper2(base, absExponent >>> 1);
            result *= result;
            if ((absExponent & 0x1) == 1) {// 奇数
                result *= base;
            }
            return result;
        }
    }
    
    /**
     * 题目：打印1到最大的n位数。
     * 输入数字n，按顺序打印出从1最大的n位十进制。比如输入3，
     * 则打印出1,2,3...直到最大的3位数，即999
     */
    /*
     * n并没有限制，最大的n位数可能超过long类型(8 byte)，9223372036854775807，导致溢出，
     * 所以本题是个大数问题。
     * 最常用的也是最容易的方法是使用字符串或者数组表示大数
     * 
     * 解决方法：在字符串解决大数问题
     * 方法1：
     */
    public static void print1ToMaxOfDigits(int n) {
        if (n < 1) {
            return;
        }
        char[] numArr = new char[n];
        for (int i = 0; i < numArr.length; i++) {
            numArr[i] = '0';
        }
        while (!increament(numArr)) {
            printNumer(numArr);
        }
        
    }
    // 打印数组中的字符组成的数字(去掉前面填位的0)
    private static void printNumer(char[] numArr) {
        int numArrLen = numArr.length;
        boolean isBegin = true;
        for (int i = 0; i < numArrLen; i++) {
            if (isBegin && numArr[i] != '0') {
               isBegin = false; 
            }
            if (!isBegin) {
                System.out.print((char)numArr[i]);
            }
        }
        if (!isBegin) {// 如果这个数不为0，那么打印完成后进行换行
            System.out.println();
        }
    }
    // 做数字的递增
    private static boolean increament(char[] numArr) {
        boolean isOverFlow = false;
        int carry = 0;
        int arrLen = numArr.length;
        int digitSum = 0;
        for (int i = arrLen - 1; i >= 0 ; i--) {
            digitSum = numArr[i] - '0' + carry;
            if (i == arrLen - 1) {// 每一次新的递增都是从最后一位数加1开始的
                digitSum++;
            }
            if (digitSum >= 10) {
                if (i == 0) {
                    isOverFlow = true;
                } else {
                    carry = 1;
                    digitSum -= 10;
                    numArr[i] = (char)('0' + (char)digitSum);
                }
            } else {
                numArr[i] = (char)('0' + (char)digitSum);
            }
        }
        return isOverFlow;
    }
    /* 
     * 方法2：将问题转换成全排列。
     * n位数，每一位上的数字都是0~9中的一个，
     * 那么这个问题实际上就是n个从0到9的全排列。
     */
    public static void print1ToMaxOfDigits2(int n) {
        if (n < 1) {
            return;
        }
        char[] numArr = new char[n];
        print1ToMaxOfDigits2Recurse(numArr, 0);
    }

    private static void print1ToMaxOfDigits2Recurse(char[] numArr,  int index) {
        if (index == numArr.length) {// 递归出口
            printNumer(numArr);
        } else {
            for (int j = 0; j < 10; j++) {
                numArr[index] = (char) ((char)j + '0');// 设置后一位
                print1ToMaxOfDigits2Recurse(numArr, index + 1);
            }
        }
      
    }
    
    /*
     * 思考：一个char类型变量占2个字符，也就是16bit，可以表示0~65535，
     * 但现在只需要0-9这10个数字，这显然造成了空间的浪费。
     */
    // TODO optimize the memory
    public static void print1ToMaxOfDigits3(int n) {

    }
    
    
    /**
     * 题目：顺时针打印矩阵，即回字打印矩阵
     */
    /*
     * 思路：分圈处理（注意边界点）
     */
    public static void printMatrixClockwisely(int[][] matrix) {
        // 定义4个指针，左上角/右下角元素各需要2个指针来定位
        int upCol = 0;
        int upRow = 0;
        int downCol = matrix[0].length - 1;
        int downRow = matrix.length - 1;
        while (upCol < downCol) {
            // 打印完一条边之后，左上角和右下角的指针分别沿着对角线方向移动一个单位
            circlePrintMatrixEdge(matrix, upCol++, upRow++, downCol--, downRow--);
        }
    }

    // 打印矩阵的一条边
    private static void circlePrintMatrixEdge(int[][] matrix, int upCol, int upRow, int downCol, int downRow) {
        if (matrix == null) {
            return;
        }
        /*顺时针*/
        // 上
        for (int i = upCol; i < downCol ; i++) {
            System.out.print(matrix[upRow][i] + " ");
        }
        // 右
        for (int i = upRow; i < downRow; i++) {
            System.out.print(matrix[i][downCol] + " ");
        }
        // 下
        for (int i = downCol; i > upCol; i--) {
            System.out.print(matrix[downRow][i] + " ");
        }
        // 左
        for (int i = downRow; i > upRow; i--) {
            System.out.print(matrix[i][upCol] + " ");
        }
        
        /*逆时针 antiClockwisely*/
        /*
        // 左
        for (int i = upRow; i < downRow; i++) {
           System.out.print(matrix[i][upCol] + " "); 
        }
        // 下
        for (int i = upCol; i < downCol; i++) {
            System.out.print(matrix[downRow][i] + " ");
        }
        // 右
        for (int i = downRow; i > upRow; i--) {
            System.out.print(matrix[i][downCol] + " ");
        }
        // 上
        for (int i = downCol; i > upCol; i--) {
            System.out.print(matrix[upRow][i] + " ");
        }
        */
    }
    /**
     * 题目：顺时针旋转矩阵
     */
    /*
     * 分圈的思路，跟上面一样
     */
    public static void rotateMatrixClockwisely(int[][] matrix) {
        int upCol = 0;
        int upRow = 0;
        int downCol = matrix[0].length - 1;
        int downRow = matrix.length - 1;
        while (upCol < downCol) {
            rotatePrintMatrixEdge(matrix, upCol++, upRow++, downCol--, downRow--);
        }
    }

    private static void rotatePrintMatrixEdge(int[][] matrix, int upCol, int upRow, int downCol, int downRow) {
        int rotateTimes = downCol - upCol;
        int temp;
        for (int i = 0; i < rotateTimes; i++) {
            temp = matrix[upRow][upCol + i];
            // 左→上
            matrix[upRow][upCol + i] = matrix[downRow - i][upCol];
            // 上→右
            matrix[downRow - i][upCol] = matrix[downRow][downCol - i];
            // 右→下
            matrix[downRow][downCol - i] = matrix[upRow + i][downCol];
            // 下→左
            matrix[upRow + i][downCol] =  temp;
        }
    }
    

    
}
