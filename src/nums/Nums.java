package nums;

import java.util.Scanner;

public class Nums {
    private int[] num1;
    private int[] num2;

    public Nums(int[] num1, int[] num2) {
        this.num1 = num1;
        this.num2 = num2;
    }
    public int tonums(int[] nums){
        int sum=0;
        for(int j=nums.length-1;j>=0;j--){
            sum=sum*10+nums[j];
        }
        return sum;
    }
    public int product(){
        return tonums(num1)*tonums(num2);
    }
}
class ToList{
    private int num;
    public ToList(int num) {
        this.num = num;
    }
    public int[] tolist(){

        String str = String.valueOf(num);//num为需要转化的整数
        int[] nums = new int[str.length()];
        for(int i=0;i<str.length();i++){
            nums[i]=Integer.parseInt(String.valueOf(str.charAt(i)));
        }

        return nums;
    }
    public void print(){
        String str = String.valueOf(num);//num为需要转化的整数
        int[] nums = new int[str.length()];
        for(int i=0;i<str.length();i++){
            nums[i]=Integer.parseInt(String.valueOf(str.charAt(i)));
            System.out.println(nums[i]);
        }
    }
}
class Test{
    public static void main(String[] args) {
        System.out.println("输入两个数字");
        System.out.println("num1:");
        Scanner scanner=new Scanner(System.in);
        int num1=scanner.nextInt();
        System.out.println("num2:");
        int num2=scanner.nextInt();

        if (num1<110&&num1>0&&num2<110&&num2>0){
            ToList toList=new ToList(num1);
            int[] _num1=toList.tolist();
            ToList toList1=new ToList(num2);
            int[] _num2=toList1.tolist();
            Nums nums=new Nums(_num1,_num2);
            int n=nums.product();
            ToList toList2=new ToList(n);
            System.out.println("乘积为：");
            toList2.print();
        }else {
            System.out.println("num1应为小于110的正整数");
        }

    }
}