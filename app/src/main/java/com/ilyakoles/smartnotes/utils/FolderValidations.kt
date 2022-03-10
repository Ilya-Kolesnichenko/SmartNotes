package com.ilyakoles.smartnotes.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object FolderValidations {

    fun validateFolderName(foldername: TextInputEditText, inputLayout: TextInputLayout): Boolean {
        if (foldername.text.toString().trim().isEmpty()) {
            inputLayout.error = "Наименование папки не может быть пустым"
            foldername.requestFocus()
            return false
        } else {
            inputLayout.isErrorEnabled = false
        }
        return true
    }

}


