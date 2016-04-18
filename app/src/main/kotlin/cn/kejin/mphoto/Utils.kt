package cn.kejin.mphoto

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/18
 */
internal fun snack(view: View, id: Int,
        length: Int = Snackbar.LENGTH_SHORT,
        actionId: Int = 0, listener: View.OnClickListener? = null) {

    val snackBar = Snackbar.make(view, id, length)
    val barLayout = snackBar.view as Snackbar.SnackbarLayout

    barLayout.alpha = 0.8f
    val textView = barLayout.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(MainApp.color(R.color.textColorPrimary))

    if (actionId != 0) {
        snackBar.setAction(actionId, listener)
    }
    snackBar.show()
}

internal fun snack(view: View, msg: String,
        length: Int = Snackbar.LENGTH_SHORT,
        actionMsg: String?= null, listener: View.OnClickListener? = null) {

    val snackBar = Snackbar.make(view, msg, length)
    val barLayout = snackBar.view as Snackbar.SnackbarLayout

    barLayout.alpha = 0.8f
    val textView = barLayout.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(MainApp.color(R.color.textColorPrimary))

    if (actionMsg != null) {
        snackBar.setAction(actionMsg, listener)
    }
    snackBar.show()
}