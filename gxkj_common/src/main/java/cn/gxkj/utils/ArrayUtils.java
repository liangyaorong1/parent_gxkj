package cn.gxkj.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    /**
     * 数组去重
     */
    public static String [] deduplicate(String [] source) {
        List<String> list = new ArrayList();
        for(int i=0;i<source.length;i++){
            if(source [i] == null || source [i] == ""){
                continue;
            }
            if(!list.contains(source[i])){
                list.add(source[i]);
            }
        }
        String [] targer = new String[list.size()];
        return list.toArray(targer);
    }
}
