package com.example.testtwitter4j.context

import android.app.Activity
import android.content.Intent
import android.util.Log
import java.util.*

class AppContext {
    // シングルトン
    companion object {
        private var instance = AppContext()
        fun getInstance(): AppContext {
            return instance
        }
    }
    // メンバ変数
    // 画面スタック
    private var activityStack: Stack<Class<*>> = Stack<Class<*>>()

    // 起動時のActivityを設定する
    fun setTopActivity(clazz: Class<*>) {
        activityStack.push(clazz)
    }

    // 指定した画面へ遷移する
    fun startActivity(activity: Activity, clazz: Class<*> ) {
        activity.startActivity(Intent(activity, clazz))
        activityStack.push(clazz)
        Log.d("SessionContext.startActivity", "pushed.")
        Log.d("SessionContext.startActivity", "activityStack=$activityStack")
        activity.overridePendingTransition(0, 0)
        activity.finish()
    }

    // 前の画面に戻る
    fun backActivity(currentActivity: Activity) {
        if (activityStack.size == 0) { // 戻り先がないとき（トップのとき）
            return
        }
        activityStack.pop() // 今いる画面を捨てる
        var clazz = activityStack.peek() // back先
        if (clazz == null) {
            return
        }
        currentActivity.startActivity(Intent(currentActivity, clazz))
        currentActivity.overridePendingTransition(0, 0)
        currentActivity.finish()
        Log.d("SessionContext.backActivity", "popped.")
        Log.d("SessionContext.backActivity", "activityStack=$activityStack")
        Log.d("SessionContext.backActivity", "activityStack.peek()=${activityStack.peek()}")
    }

    // 遷移先の画面を、すでに積んであるスタックから探索し、それ以降の要素を破棄する
    // （手続きが完了して、メニューに戻るときなどのため）
    fun goToActivity(activity: Activity, iclazz: Class<*>) {
        while (activityStack.size > 1) { // スタックがあるかぎり
            activityStack.pop()
            var clazz = activityStack.peek()
            if (clazz == null) {
                return
            }
            if (iclazz == clazz) {
                activity.startActivity(Intent(activity, clazz))
                activity.overridePendingTransition(0, 0)
                activity.finish()
                Log.d("SessionContext.goToActivity", "go to 「${activityStack.peek()}」")
                Log.d("SessionContext.backActivity", "activityStack=$activityStack")
                Log.d("SessionContext.goToActivity", "activityStack.peek()=${activityStack.peek()}")
                return
            }
        }
        throw Exception("goToActivity()で遷移先画面が見つかりませんでした。")
    }
}