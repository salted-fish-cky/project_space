package com.jesper.seckill;

import java.util.Scanner;
public class Test{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String word = sc.next();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < word.length(); i++) {
            if(i != 0){
                if((word.charAt(i)+"").equals("i")&&(word.charAt(i-1)+"").equals("o")){
                    if(i-4>=0&& !(word.charAt(i-4)+"").equals("o")&& !(word.charAt(i-4)+"").equals("i")){
                        sb.append(word.charAt(i-4));
                    }
                }else if((word.charAt(i)+"").equals("i")&&!(word.charAt(i-1)+"").equals("o")){
                    if(i-2 >= 0&& !(word.charAt(i-2)+"").equals("o")&& !(word.charAt(i-2)+"").equals("i")){
                        sb.append(word.charAt(i-2));
                    }
                }else if((word.charAt(i)+"").equals("o")&&(word.charAt(i-1)+"").equals("i")){
                    if(i-2 >= 0&& !(word.charAt(i-2)+"").equals("o")&& !(word.charAt(i-2)+"").equals("i")){
                        sb.append(word.charAt(i-2));
                    }

                }else if((word.charAt(i)+"").equals("o")&&!(word.charAt(i-1)+"").equals("i")){
                    if(i-2 >= 0 && !(word.charAt(i-2)+"").equals("o")&& !(word.charAt(i-2)+"").equals("i")){
                        sb.append(word.charAt(i-2));
                    }
                }else if(!(word.charAt(i)+"").equals("o")&&!(word.charAt(i-1)+"").equals("i")){
                    sb.append(word.charAt(i));
                }

            }
        }

        System.out.println(sb.toString());
////        while (sc.hasNextInt()) {//注意while处理多个case
//            int num = sc.nextInt();
//        String[] words = new String[num];
//            for(int i=0;i<num;i++){
//                String word = sc.next();
//                words[i] = word;
//
//            }
//        for (int i = 0; i < words.length; i++) {
//                String word = words[i];
//                StringBuffer sb = new StringBuffer();
//            for(int j=0;j<word.length();j++){
//                if(j != 0){
//                    if(Character.isUpperCase(word.charAt(j))&&!Character.isUpperCase(word.charAt(j-1))){
//                        sb.append("_"+(""+word.charAt(j)).toLowerCase());
//                    }else if(Character.isUpperCase(word.charAt(j))&& Character.isUpperCase(word.charAt(j-1))&&Character.isUpperCase(word.charAt(j+1))){
//                        sb.append((""+word.charAt(j)).toLowerCase());
//                    }else if(Character.isUpperCase(word.charAt(j))&& Character.isUpperCase(word.charAt(j-1))&&!Character.isUpperCase(word.charAt(j+1))){
//                        sb.append("_"+(""+word.charAt(j)).toLowerCase());
//                    }
//                    else{
//                        sb.append(word.charAt(j));
//                    }
//                }else{
//                    sb.append((""+word.charAt(j)).toLowerCase());
//                }
//
//
////                System.out.println(word);
//            }
//            System.out.println(sb.toString());
//        }
//
////        }




    }
}