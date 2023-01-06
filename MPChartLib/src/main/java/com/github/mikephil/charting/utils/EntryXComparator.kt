package com.github.mikephil.charting.utils

import com.github.mikephil.charting.data.Entry

/**
 * Comparator for comparing Entry-objects by their x-value.
 * Created by philipp on 17/06/15.
 */
class EntryXComparator : Comparator<Entry?> {

    override fun compare(o1: Entry?, o2: Entry?): Int {
        val diff = o1!!.getX() - o2!!.getX()

        return if (diff == 0f) 0 else {
            if (diff > 0f) 1 else -1
        }
    }
}