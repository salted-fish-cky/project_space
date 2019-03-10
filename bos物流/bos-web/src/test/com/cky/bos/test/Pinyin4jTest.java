package com.cky.bos.test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

public class Pinyin4jTest {

    @Test
    public void test() throws BadHanyuPinyinOutputFormatCombination {

        String province = "河北省";
        String city = "石家庄市";
        String district = "桥西区";

        province = province.substring(0,province.length()-1);
        city = city.substring(0,city.length()-1);
        district = district.substring(0,district.length()-1);

        String info = province+city+district;
        System.out.println(info);
        //简码
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //获取首字母简拼
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        String header = "";
        char[] c = info.toCharArray();
        for (int i = 0; i <c.length ; i++) {
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(c[i],format);
            header+=strings[0].charAt(0);

        }
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        String s = PinyinHelper.toHanyuPinyinString(info, format, "");
        System.out.println(s);


        //城市编码
    }
}
