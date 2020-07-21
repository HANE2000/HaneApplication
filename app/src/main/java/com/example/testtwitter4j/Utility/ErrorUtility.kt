package com.example.testtwitter4j.utility

import android.app.AlertDialog
import android.content.Context

class ErrorUtility {

    companion object {
        // 予期せぬエラーの報告
        fun reportException(context: Context, e: Exception) {
            var stack: String = ""
            for (s in e.stackTrace) {
                stack += s
                stack += "\n"
            }

            AlertDialog.Builder(context)
                .setTitle("予期せぬエラー")
                .setMessage("予期せぬエラーが発生しました。 @HANEKW_ に報告ください。\n内容: ${e}\n詳細: $stack")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}