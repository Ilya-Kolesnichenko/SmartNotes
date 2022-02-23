package com.ilyakoles.smartnotes.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object FormValidations {

    fun validateUserName(login: TextInputEditText, inputLayout: TextInputLayout): Boolean {
            if (login.text.toString().trim().isEmpty()) {
                inputLayout.error = "Логин не может быть пустым"
                login.requestFocus()
                return false
            } else if (login.text.toString().length > 20) {
                inputLayout.error = "Логин не может содержать больше 20 символов"
                login.requestFocus()
                return false
            } else {
                inputLayout.isErrorEnabled = false
            }
        return true
    }

    fun validateNicName(nicName: TextInputEditText, inputLayout: TextInputLayout): Boolean {
        if (nicName.text.toString().trim().isEmpty()) {
            inputLayout.error = "Ник не может быть пустым"
            nicName.requestFocus()
            return false
        } else {
            inputLayout.isErrorEnabled = false
        }
        return true
    }


    fun validateEmail(email: TextInputEditText, inputLayout: TextInputLayout): Boolean {
        if (!email.text.toString().trim().isEmpty()) {
            if (!FieldValidators.isValidEmail(email.text.toString())) {
                inputLayout.error = "Введен некорректный e-mail"
                email.requestFocus()
                return false
            }
        } else {
            inputLayout.isErrorEnabled = false
        }
        return true
    }

    fun validatePassword(password: TextInputEditText, inputLayout: TextInputLayout): Boolean {
        if (password.text.toString().trim().isEmpty()) {
            inputLayout.error = "Пароль не может быть пусным!"
            password.requestFocus()
            return false
        } else if (password.text.toString().length < 6) {
            inputLayout.error = "Пароль не может сожержать меньше 6 символов"
            password.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainNumber(password.text.toString())) {
            inputLayout.error = "Пароль должен содержать хотя бы одну цифру"
            password.requestFocus()
            return false
        } else if (!FieldValidators.isStringLowerAndUpperCase(password.text.toString())) {
            inputLayout.error = "Пароль должен содержать прописные и заглавные буквы"
            password.requestFocus()
            return false
        } /*else if (!FieldValidators.isStringContainSpecialCharacter(password.text.toString())) {
            inputLayout.error = "1 special character required"
            password.requestFocus()
            return false
        }*/ else {
            inputLayout.isErrorEnabled = false
        }
        return true
    }

    fun validateConfirmPassword(confirmPassword: TextInputEditText, password: TextInputEditText, inputLayout: TextInputLayout): Boolean {
        when {
            confirmPassword.text.toString().trim().isEmpty() -> {
                inputLayout.error = "Введите пароль повторно"
                confirmPassword.requestFocus()
                return false
            }
            confirmPassword.text.toString() != password.text.toString() -> {
                inputLayout.error = "Пароли не совпадают"
                confirmPassword.requestFocus()
                return false
            }
            else -> {
                inputLayout.isErrorEnabled = false
            }
        }
        return true
    }
}