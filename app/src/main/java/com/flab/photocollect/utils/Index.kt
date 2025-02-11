package com.flab.photocollect.utils

enum class Index(private val position: Int) {
    HOME(0),
    BOOKMARK(1);

    companion object {
        fun positionOfIndex(position: Int): Index {
            return entries.find { it.position == position }
                ?: throw IllegalArgumentException("Unexpected position $position")
        }
    }
}