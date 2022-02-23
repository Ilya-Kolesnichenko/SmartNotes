package com.ilyakoles.smartnotes.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.FragmentNewUserBinding
import com.ilyakoles.smartnotes.presentation.viewmodels.UserViewModel
import com.ilyakoles.smartnotes.utils.FieldValidators
import com.ilyakoles.smartnotes.utils.FormValidations.validateConfirmPassword
import com.ilyakoles.smartnotes.utils.FormValidations.validateEmail
import com.ilyakoles.smartnotes.utils.FormValidations.validateNicName
import com.ilyakoles.smartnotes.utils.FormValidations.validatePassword
import com.ilyakoles.smartnotes.utils.FormValidations.validateUserName
import com.ilyakoles.smartnotes.utils.RSACrypt
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.lang.Thread.sleep
import javax.inject.Inject
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as SmartNotesApp).component
    }

    private var _binding: FragmentNewUserBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("NewUserFragment: binding пустой")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Новый пользователь")
        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
        setupListeners()
        binding.btSave.setOnClickListener {
            lifecycleScope.launch {
                saveUser()
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        var pfm = parentFragmentManager
        pfm.popBackStack()
        pfm.beginTransaction()
            .replace(R.id.fragment_container, fragment, "Login")
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        //  pfm.executePendingTransactions()
    }

    private fun setupListeners() {
        with(binding) {
            tvLogin.addTextChangedListener(TextFieldValidation(binding.tvLogin))
            tvPassword.addTextChangedListener(TextFieldValidation(binding.tvPassword))
            tvConfirmPassword.addTextChangedListener(TextFieldValidation(binding.tvConfirmPassword))
            tvNicName.addTextChangedListener(TextFieldValidation(binding.tvNicName))
            tvEmail.addTextChangedListener(TextFieldValidation(binding.tvEmail))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun saveUser() {
        if (isValidate()) {
            var isRes = true
            var message = ""
            with(binding) {
                var password = RSACrypt.getEncryptStr(tvPassword.text.toString(), RSACrypt.KEY_PRIVATE)

                var answer = viewModel.createUser(
                    tvLogin.text.toString(),
                    password, tvNicName.text.toString(),
                    tvLastName.text.toString(), tvFirstName.text.toString(),
                    tvEmail.text.toString()
                )
                isRes = answer?.error ?: false
                message = answer?.message ?: ""
                Log.d("answ", isRes.toString() + " onView")
                Log.d("answ", message)
                if (!isRes) {
                    tilLogin.isErrorEnabled = false
                    launchFragment(LoginFragment.newInstance("", ""))
                } else {
                    tilLogin.error = message
                    tvLogin.requestFocus()
                }
            }
        }
    }

    private fun isValidate(): Boolean =
        validateUserName(binding.tvLogin, binding.tilLogin) &&
                validatePassword(binding.tvPassword, binding.tilPassword) &&
                validateConfirmPassword(
                    binding.tvConfirmPassword,
                    binding.tvPassword,
                    binding.tilConfirmPassword
                ) &&
                validateNicName(binding.tvNicName, binding.tilNicName) &&
                validateEmail(binding.tvEmail, binding.tilEmail)

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                binding.tvLogin.id -> {
                    validateUserName(binding.tvLogin, binding.tilLogin)
                }
                binding.tvPassword.id -> {
                    validatePassword(binding.tvPassword, binding.tilPassword)
                }
                binding.tvConfirmPassword.id -> {
                    validateConfirmPassword(
                        binding.tvConfirmPassword,
                        binding.tvPassword,
                        binding.tilConfirmPassword
                    )
                }
                binding.tvNicName.id -> {
                    validateNicName(binding.tvNicName, binding.tilNicName)
                }
                binding.tvEmail.id -> {
                    validateEmail(binding.tvEmail, binding.tilEmail)
                }
            }
        }
    }

    /*   private fun validateUserName(): Boolean {
           with(binding) {
               if (tvLogin.text.toString().trim().isEmpty()) {
                   tilLogin.error = "Логин не может быть пустым"
                   tvLogin.requestFocus()
                   return false
               } else if (tvLogin.text.toString().length > 20) {
                   tilLogin.error = "Логин не может содержать больше 20 символов"
                   tvLogin.requestFocus()
                   return false
               } else {
                   tilLogin.isErrorEnabled = false
               }
           }
           return true
       }*/

/*    private fun validateNicName(): Boolean {
        if (binding.tvNicName.text.toString().trim().isEmpty()) {
            binding.tilNicName.error = "Ник не может быть пустым"
            binding.tvNicName.requestFocus()
            return false
        } else {
            binding.tilNicName.isErrorEnabled = false
        }
        return true
    }


    private fun validateEmail(): Boolean {
        if (!binding.tvEmail.text.toString().trim().isEmpty()) {
            if (!isValidEmail(binding.tvEmail.text.toString())) {
                binding.tilEmail.error = "Введен некорректный e-mail"
                binding.tvEmail.requestFocus()
                return false
            }
        } else {
            binding.tilEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.tvPassword.text.toString().trim().isEmpty()) {
            binding.tilPassword.error = "Пароль не может быть пусным!"
            binding.tvPassword.requestFocus()
            return false
        } else if (binding.tvPassword.text.toString().length < 6) {
            binding.tilPassword.error = "password can't be less than 6"
            binding.tvPassword.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.tvPassword.text.toString())) {
            binding.tilPassword.error = "Required at least 1 digit"
            binding.tvPassword.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.tvPassword.text.toString())) {
            binding.tilPassword.error =
                "Password must contain upper and lower case letters"
            binding.tvPassword.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.tvPassword.text.toString())) {
            binding.tilPassword.error = "1 special character required"
            binding.tvPassword.requestFocus()
            return false
        } else {
            binding.tilPassword.isErrorEnabled = false
        }
        return true
    }*/

    /*   private fun validateConfirmPassword(): Boolean {
           when {
               binding.confirmPassword.text.toString().trim().isEmpty() -> {
                   binding.confirmPasswordTextInputLayout.error = "Required Field!"
                   binding.confirmPassword.requestFocus()
                   return false
               }
               binding.confirmPassword.text.toString() != binding.password.text.toString() -> {
                   binding.confirmPasswordTextInputLayout.error = "Passwords don't match"
                   binding.confirmPassword.requestFocus()
                   return false
               }
               else -> {
                   binding.confirmPasswordTextInputLayout.isErrorEnabled = false
               }
           }
           return true
       }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewUserFragment().apply {
                arguments = bundleOf(
                    ARG_PARAM1 to param1,
                    ARG_PARAM2 to param2
                )
            }
    }
}