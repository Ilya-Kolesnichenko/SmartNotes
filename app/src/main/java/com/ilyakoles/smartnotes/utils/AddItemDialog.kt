package com.ilyakoles.smartnotes.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.ilyakoles.smartnotes.R


class AddItemDialog : DialogFragment() {
    private val catNames = arrayOf("Папка", "Заметка", "Задача")

    lateinit var listener: InterfaceCommunicator

    interface InterfaceCommunicator {
        fun sendRequest(value: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите элемент добавления")
                .setItems(catNames
                ) { dialog, which ->
                    val intent = Intent()
                    intent.putExtra("value", which)
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

                    /*Toast.makeText(activity, "Выбранный кот: ${catNames[which]}",
                        Toast.LENGTH_SHORT).show()*/
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}
