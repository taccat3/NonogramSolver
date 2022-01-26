import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    
    public int[][] parse(String str) {
        String[] sections = getSections(str.trim());

        ArrayList<int[]> rc_al = new ArrayList<int[]>();
        int max = 0;

        for(int i = 0; i < sections.length; i++) {
            int[] section  = getNumbers(sections[i]);
            rc_al.add(section);

            if(section.length > max) {
                max = section.length;
            }
        }

        int[][] rc = new int[rc_al.size()][max];
        for(int i = 0; i < sections.length; i++) {
            rc[i] = rc_al.get(i);
        }

        return rc;
    }

    private String[] getSections(String str) {
        return str.split("\n");
    }

    private String removeExtraSpaces(String str) {
        // TODO: make this not stupidly repeatative

        int oldLength = str.length();
        str = str.replaceAll("  ", " ");
        while(oldLength != str.length()) {
            oldLength = str.length();
            str = str.replaceAll("  ", " ");
        }

        return str;
    }

    private int[] getNumbers(String str){

        String strNums[] = (removeExtraSpaces(str)).split(" ");

        int[] nums = new int[strNums.length];

        for(int i = 0; i < strNums.length; i++) {
            nums[i] = Integer.parseInt(strNums[i]);
        }

        return nums;
    }

}
