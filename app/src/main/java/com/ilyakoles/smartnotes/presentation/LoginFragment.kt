package com.ilyakoles.smartnotes.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.FragmentLoginBinding
import com.ilyakoles.smartnotes.presentation.viewmodels.UserViewModel
import com.ilyakoles.smartnotes.utils.RSACrypt
import kotlinx.coroutines.launch
import java.security.KeyPairGenerator
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var prefs: SharedPreferences

    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as SmartNotesApp).component
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("LoginFragment: binding пустой")

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle("Авторизация")
        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        binding.tvError.visibility = View.GONE;
        binding.tvLogin.addTextChangedListener(TextFieldValidation(binding.tvLogin))
        binding.tvPassword.addTextChangedListener(TextFieldValidation(binding.tvPassword))

        binding.btEnter.setOnClickListener {
            lifecycleScope.launch {
                loginUser(savedInstanceState)
            }
        }
        binding.tvNewUser.setOnClickListener {
            launchFragment(NewUserFragment.newInstance("", ""))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchFragment(fragment: Fragment) {
        var pfm = parentFragmentManager
        pfm.popBackStack()
        pfm.beginTransaction()
            .replace(R.id.fragment_container, fragment, "new_user")
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        //  pfm.executePendingTransactions()
    }

    private suspend fun loginUser(savedInstanceState: Bundle?) {
        with(binding) {
            var password = RSACrypt.getEncryptStr(tvPassword.text.toString(), RSACrypt.KEY_PRIVATE)

            var answer = viewModel.logined(
                tvLogin.text.toString(),
                password
            )
            val isError = answer?.error ?: false
            val message = answer?.message ?: ""
            val userId = answer?.id ?: 0
            if (!isError) {
                prefs = activity?.getSharedPreferences("SmartNotes_Settings", Context.MODE_PRIVATE)!!

                prefs.edit().putString("Password", password).apply()
                prefs.edit().putInt("UserID", userId).apply()
                binding.tvError.visibility = View.GONE
                val intent = FoldersActivity.newIntent(requireContext(), userId)
                startActivity(intent)
            } else {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = message
                tvLogin.requestFocus()
            }
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            binding.tvError.visibility = View.GONE;
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = bundleOf(
                    ARG_PARAM1 to param1,
                    ARG_PARAM2 to param2
                )
            }
    }
}


