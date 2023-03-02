package com.neusoft.qingyi.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {
    /**
     * 取交集（取两个集合中都存在的元素）
     * @return
     */
    public static Set<String> intersectionSet(Set<String> setA, Set<String> setB){
        Set<String> resSet = new HashSet<>();
        resSet.addAll(setA);
        resSet.retainAll(setB);
        return resSet;
    }

}
