package com.flab.photocollect.utils

enum class Page(private val position: Int) {
    HOME(0),
    BOOKMARK(1);

    companion object {
        fun positionOfPage(position: Int): Page {
            return entries.find { it.position == position }
                ?: throw IllegalArgumentException("Unexpected position $position")
        }
    }
}