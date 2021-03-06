package chaperfive;

import java.util.Arrays;

public class MyStringUtils {
    /**
     * 题目：判断两个字符串是否为变形词
     */
    /*
     * 方法1：将字符串转换成数组，若是变形词，那么排序后的两个数组元素顺序是一致的 这种做法效率较低，但是比较直观
     */
    public static boolean isDeformation(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            return false;
        }
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        // 或者将数组重新转为字符串就行判断
        // return new String(arr1).equals(new String(arr2));
        return true;
    }

    /*
     * 方法2：假设字符串中字符都是ASCII字符，那么使用一个长度为256的数组来记录每个字符在字符串中出现的长度
     */
    public static boolean isDeformation2(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            return false;
        }
        int[] charCount = new int[256];
        for (int i = 0; i < str1.length(); i++) {
            charCount[str1.charAt(i)]++;
        }
        for (int i = 0; i < str2.length(); i++) {
            if (--charCount[str1.charAt(i)] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 题目：给定一个字符串str,求其中全部数字字串所代表的数字之和
     * 
     * 要求：忽略小数点,如“A1.3”，包含1和3 如果贴紧数字字串的左侧出现-号，
     * 当连续出现的数量为奇数时，则数字为负，否则为正，
     * 不连续出现的-号不作数。 例如：“A-1BC--12”，包含数字为-1和12
     * 
     */
    public static int numSumInStr(String str) {
        if (str == null) {
            return 0;
        }
        boolean sign = true;// 表示num的正负
        int cur = 0;
        int sum = 0;
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            cur = str.charAt(i) - '0';
            if (cur < 0 || cur > 9) {
                sum += num;// 在非数字时进行加和
                num = 0;// num归0
                if (str.charAt(i) == '-') {
                    if (i - 1 > -1 && str.charAt(i - 1) == '-') {// 出现第二个-号
                        sign = !sign;
                    } else {// 只出现了一个-号，负数
                        sign = false;
                    }
                } else {// 没有出现-号，正数
                    sign = true;
                }
            } else {
                num = num * 10 + (sign ? cur : -cur);
            }
        }
        sum += num;// 做最后一次加和（字符串以数字结尾的情况）
        return sum;
    }

    /**
     * 题目：给定一个字符串str和一个整数k,如果str中正好有连续的k个'0'字符出现时，
     * 把k个连续的'0'去掉，返回处理后的字符串。 
     * 比如： str ="A00B"，k = 2，返回"A**B"； str = "A0000B000"，k = 3，返回"A0000B***";
     */
    public static String delKzeros(String str, int k) {
        if (str == null || k < 1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int zeroStartIndex = -1;
        int zeroCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '0') {
                zeroCount++;
                zeroStartIndex = (zeroStartIndex == -1 ? i : zeroStartIndex);
            } else {
                // 攒够了连续的k个0，且下一个字符不为0，满足条件，那么消灭这些连续0
                if (zeroCount == k) {
                    while (zeroCount-- > 0) {
                        chars[zeroStartIndex++] = '*';
                    }
                }
                zeroCount = 0;
                zeroStartIndex = -1;// zeroStartIndex重置
            }
        }
        // 由于在满足条件的连续0的下一个字符处做处理，那么如果末尾有满足条件的连续0，
        // 就不会做处理，因此要判断是否满足这种情况（跟上一个题一样）
        if (zeroCount == k) {
            while (zeroCount-- > 0) {
                chars[zeroStartIndex++] = '*';
            }
        }
        return String.valueOf(chars);
    }

    /*
     * 在上面的基础上改良，将*号抹去 比如： str = "A00B"，k = 2，返回"AB"； str = "A0000B000"，k =
     * 3，返回"A0000B"; str = "A00000B00"，k = 2，返回"A0000B";
     */
    public static String delKzerosPro(String str, int k) {
        if (str == null || k < 1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int zeroStartIndex = -1;
        int zeroCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '0') {
                zeroCount++;
                zeroStartIndex = (zeroStartIndex == -1 ? i : zeroStartIndex);
            } else {
                // 攒够了连续的k个0，且下一个字符不为0，满足条件，那么消灭这些连续0
                if (zeroCount == k) {
                    int j = zeroStartIndex;
                    for (; j + k < chars.length; j++) {
                        chars[j] = chars[j + k];
                        // 原来的位置设置为字符*，为了防止经过前移，恰好形成符合条件的连续0
                        chars[j + k] = '*';
                    }
                    while (j < i) {// 放置后面的不能把前面的完全覆盖
                        chars[j++] = '*';
                    }
                    i = i - k;// 压缩后的索引值
                }
                zeroCount = 0;
                zeroStartIndex = -1;// zeroStartIndex重置
            }
        }
        if (zeroCount == k) {
            while (zeroCount-- > 0) {
                chars[zeroStartIndex++] = '*';
            }
        }
        // 统计有多少个正常字符
        int resultLen = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '*') {
                resultLen = i;
                break;
            }
        }
        return new String(chars, 0, resultLen);
    }

    /**
     * 题目：判断是否为旋转词
     */
    public static boolean isRotation(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        String con = a + a;
        return con.contains(b);// 涉及到字符串匹配问题KMP算法
    }
    
    /**
     * 题目：将字符串str中含有的from字符串全部替换成to字符串，若没有from字符串则输出原字符串，
     * 要求str中出现连续的from时，全部替换成一个to字符串。
     * 举个例子：
     * str="123abab" ,from="ab", to="x",输出为123x
     * 
     */
    public static String replace(String str, String from, String to) {
        if (str == null || str.isEmpty() || from == null || from.isEmpty()) {
            return null;
        }
        StringBuilder strsb = new StringBuilder(str);
        int matchLen = from.length();
        int matchIndex = 0;
        StringBuilder curr = new StringBuilder();// 存储被*号分隔开的各字符串
        StringBuilder rest = new StringBuilder();// 存储结果
        for (int i = 0; i < strsb.length(); i++) {
            if (strsb.charAt(i) == from.charAt(matchIndex)) {
                matchIndex++;
                if (matchIndex == matchLen) {
                    // 将str中包含的from串全变成*号
                    clean(strsb, i, matchLen);
                    matchIndex = 0;
                }
            } else {
                matchIndex = 0;
            }
        }
        
        for (int i = 0; i < strsb.length(); i++) {
            if (strsb.charAt(i) != '*') {
                curr.append(strsb.charAt(i));
            }
            // 在不满足条件时进行操作，循环结束以后要有一个判断添加的操作
            if (strsb.charAt(i) == '*' && (i == 0 || strsb.charAt(i - 1) != '*')) {
                rest.append(curr).append(to);
                curr.delete(0, curr.length());
            }
        }
        if (curr.length() != 0) {
            rest.append(curr);
        }
        return rest.toString();
        
    }

    private static void clean(StringBuilder strsb, int end, int matchLen) {
        for (int i = matchLen; i > 0 ; i--) {
            strsb.setCharAt(end, '*');
            end--;
        }
    }
    
    /**
     * 题目：字符串的统计字符串
     * 给定一个字符串str,返回str的统计字符串。
     * 举个栗子：
     * aaabbadddffc 的统计字符串为 a_3_b_2_a_1_d_3_f_2_c_1
     */
    public static String statisticStr(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int count = 1;
        StringBuilder result = new StringBuilder();
        char[] chas = str.toCharArray();
        result.append(chas[0]).append('_');
        for (int i = 1; i < str.length(); i++) {
            if (chas[i] == chas[i - 1]) {
                count++;
            } else {// 在不符合条件时往结果中添加新的字符，因此循环结束之后要加上最后一个字符的统计个数
                result.append(count).append('_').append(chas[i]).append('_');
                count = 1;
            }
        }
        result.append(count);
        return result.toString();
    }
    
    /**
     * 题目：给定一个字符串的统计字符串cstr（上题），再给定一个整数index，返回cstr所代表的原始字符串上的第index个字符。
     * 举个栗子：
     * “a_1_b_100”，给定index=50，那么原始字符串中第50个字符应该是'b'
     */
    public static char getCharAt(String cstr, int index) {
        if (cstr == null || cstr.isEmpty()) {
            return 0;
        }
        char curr = 0;
        int sum = 0;
        boolean isAlpha = true;
        int num = 0;// 字母重复次数
        char[] chas = cstr.toCharArray();
        
        for (int i = 0; i < chas.length; i++) {
            if (chas[i] == '_') {
                isAlpha = !isAlpha;
            } else if (isAlpha) {// 处理字母
                sum += num;
                if (sum > index) {
                    return curr;
                }
                num = 0;// 次数清零
                curr = chas[i];
            } else { // 处理数字
                num = num * 10 + chas[i] - '0';// 将字符串中字母出现的次数换算成数字
            }
        }
        return sum + num > index ? curr : 0;// 统计最后一次,不满足条件就返回0
    }
    
    
    /**
     * 题目：输入一个英文句子。翻转句子中单词的顺序，但单词内字符的顺序不变。
     * 为简单起见，标点符号和普通字母一样处理。
     * 举个栗子：输入字符串为"I am a student.",
     * 输出为"student. a am I".
     */
    /*
     * 时间复杂度为O(n),空间复杂度为O(1)
     */
    public static String reverseSentense(String sentense) {
        if (sentense == null || sentense.isEmpty()) {
            return sentense;
        }
        char[] sentenseChars = sentense.toCharArray();
        int len = sentenseChars.length;
        reverseHelper(sentenseChars, 0, len - 1);// 先翻转整个句子
        for (int i = 0, j = 0; j <= len; j++) { // 翻转句子中每一个单词
            if (j == len || sentenseChars[j] == ' ') {
                reverseHelper(sentenseChars, i, j - 1);
                i = j + 1;
            }
        }
        return String.valueOf(sentenseChars);
    }

    private static void reverseHelper(char[] sentenseChars, int start, int end) {
        char temp;
        for (int i = start, j = end; i <= j; i++, j--) {
            temp = sentenseChars[i];
            sentenseChars[i] = sentenseChars[j];
            sentenseChars[j] = temp;
            
        }
    }
    
    /**
     * 题目：字符串的左旋操作是指把字符串前面的若干个字符转移到字符串的尾部。
     * 请定义一个方法，实现字符串左旋操作的功能。
     * 举个栗子：输入字符串为"abcdefg",数字为2，
     * 输出为"cdefgab".
     */
    public static String leftRotateString(String str, int n) {
        if (str == null || str.isEmpty() || n <= 0 || n >= str.length()) {
            return str;
        }
        char[] chas = str.toCharArray();
        int len = chas.length;
        reverseHelper(chas, 0, n - 1);// 翻转0~n部分
        reverseHelper(chas, n, len - 1);// 翻转n~len部分
        reverseHelper(chas, 0, len - 1);// 翻转整个字符串
        return String.valueOf(chas);
    }
    
    /**
     * 题目：请实现一个方法，用来找出字符流中第一个只出现一次的字符。
     * 举个栗子：
     * 当从字符流只读出前两个字符"go"时，第一个只出现一次的字符是"g",
     * 当从字符流只读出前6个字符"google"时，第一个只出现一次的字符是"l"
     */
    /*
     * 在O(1)时间内往容器中插入一个字符，以及更新一个字符对应的值。因此使用哈希表。
     * 用字符的ASCII码作为哈希表的键，而把字符对应的位置作为哈希表的值。
     * 
     * occurance[i] = -1,表示没有ASCII码对应的字符
     * occurance[i] = -2,表示这个ASCII码对应的字符出现多于1次
     * occurance[i] >= 0,表示仅仅出现一次
     */
    public static char getFirstAppearingOneceChar(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        char[] chars = str.toCharArray();
        int[] occurance = new int[256];
        // 初始化occurance数组元素为-1
        for (int i = 0, len = occurance.length; i < len; i++) {
            occurance[i] = -1;
        }
        // 遍历chars数组，第一次出现字符时，在occurance中相应位置记录索引i
        // 第二次出现时，在occurance中相应位置设置值-2
        for (int i = 0, len = chars.length; i < len; i++) {
            if (occurance[chars[i]] >= 0) {
                occurance[chars[i]] = -2;
            }  else if (occurance[chars[i]] == -1) {
                occurance[chars[i]] = i;
            }
        }
        int minIndex = chars.length - 1;
        char result = 0;
        for (int i = 0, len = occurance.length; i < len; i++) {
            if (occurance[i] >= 0) {
                minIndex = Math.min(minIndex, occurance[i]);
                result = chars[minIndex];
            }
        }
       return result; 
    }
    
    
    
}
