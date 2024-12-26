package com.flab.deepsleep.utils

import timber.log.Timber

class TimberDebugTree: Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        // Link로 연결
        return "${element.fileName}:${element.lineNumber}#${element.methodName}"
    }
}