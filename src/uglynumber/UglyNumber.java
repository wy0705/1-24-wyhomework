package uglynumber;

import java.util.Random;
import java.util.Scanner;

public class UglyNumber {
    //计数：第几个丑数
    private int n=0;
    //目标个数
    private int target;
    //生成多长的数组
    private int length;
    public UglyNumber(int length,int target){
        this.length=length;
        this.target=target;
    }
    public int ugly() {
        if (target > length) {
            System.out.println("超范围");
            return 0;
        }
        int[] nums = new int[length];
        Random random = new Random();
        for (int i = 0; i < nums.length; i++) {
            //小于２０的随机数
            nums[i] = random.nextInt(20);
        }
        //判断是否为丑数
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0 || nums[i] % 3 == 0 || nums[i] % 5 == 0) {
                n++;
            }
            if (n==target){
                return nums[i];
            }
        }
        System.out.println("超范围");
        return 0;
    }
}
class Test{
    public static void main(String[] args) {
        System.out.println("选择要数出第几个丑数：");
        Scanner scanner=new Scanner(System.in);
        int target=scanner.nextInt();
        UglyNumber uglyNumber=new UglyNumber(20,target);
        int t=uglyNumber.ugly();
        if (t!=0){
            System.out.println("第"+target+"个丑数为："+t);
        }

    }
}
