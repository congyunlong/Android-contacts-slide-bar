package com.cong.cly.adapter;

import java.util.Comparator;

/**
 * Created by cong on 16/1/21.
 */
public class PinyinComparator implements Comparator<SortModel> {
    @Override
    public int compare(SortModel lhs, SortModel rhs) {
        if (rhs.az.equals("#")) {
            return -1;
        } else if (lhs.equals("#")) {
            return 1;
        }
        return lhs.az.compareTo(rhs.az);
    }
}
