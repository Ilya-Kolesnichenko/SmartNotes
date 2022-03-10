package com.ilyakoles.smartnotes.presentation.folders

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.FragmentEditFolderBinding
import com.ilyakoles.smartnotes.presentation.SmartNotesApp
import com.ilyakoles.smartnotes.presentation.ViewModelFactory
import com.ilyakoles.smartnotes.presentation.login.LoginFragment
import com.ilyakoles.smartnotes.presentation.viewmodels.FolderViewModel
import com.ilyakoles.smartnotes.utils.FolderValidations.validateFolderName
import com.ilyakoles.smartnotes.utils.FormValidations
import com.ilyakoles.smartnotes.utils.RSACrypt
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditFolderFragment : Fragment() {
    private var param_mode: String? = null
    private var param_folderId: Int = UNDEFINED
    private var param_userId: Int = UNDEFINED
    private var param_level: Int = UNDEFINED

    private lateinit var viewModel: FolderViewModel
    private lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as SmartNotesApp).componentFolders
    }

    private var _binding: FragmentEditFolderBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("EditFolderFragment: binding пустой")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param_mode = getArguments()?.getString("mode") ?: ""
        param_folderId = getArguments()?.getInt("elemId") ?: 0
        param_userId = getArguments()?.getInt("userId") ?: 0
        param_level = getArguments()?.getInt("level") ?: 0
/*        view.findViewById(R.id.textViewAmount);
        tv.setText(getArguments().getString("amount"));

        arguments?.let {
            param_mode = it.getString(MODE)
            param_folderId = it.getInt(FOLDER_ID)
            param_userId = it.getInt(USER_ID)
            param_level = it.getInt(LEVEL)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        viewModel = ViewModelProvider(this, viewModelFactory)[FolderViewModel::class.java]
        setupListeners()

        if (param_mode == MODE_EDIT) {
            lifecycleScope.launch {
                viewModel.getParentFolderByCurFolderId(0, param_folderId)
                    .observe(viewLifecycleOwner) {
                        with(binding) {
                            tvFolderName.setText(it.name)
                            tvFolderDesc.setText(it.description)
                        }
                    }
            }
        }

        binding.ibSave.setOnClickListener {
            lifecycleScope.launch {
                when (param_mode) {
                    MODE_EDIT -> saveFolder(param_folderId)
                    MODE_ADD -> saveFolder(0)
                    else -> throw RuntimeException("Unknown screen mode $param_mode")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        with(binding) {
            tvFolderName.addTextChangedListener(TextFieldValidation(binding.tilFolderName))
        }
    }

    private suspend fun saveFolder(folderId: Int) {
        if (isValidate()) {
            var isRes = true
            var message = ""
            with(binding) {
                val isShared = if (cbIsShared.isChecked)
                    1
                else
                    0

                var answer = viewModel.saveFolder(
                    folderId, param_folderId, tvFolderName.text.toString(), param_userId,
                    isShared, tvFolderDesc.text.toString(), param_level
                )
                isRes = answer?.error ?: false
                message = answer?.message ?: ""
                Log.d("answ", isRes.toString() + " onView")
                Log.d("answ", message)
                if (!isRes) {
                    tilFolderName.isErrorEnabled = false
                    Toast.makeText(requireContext(), "Папка успешно сохранена", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigateUp()
                    /*viewModel.finishWork(true)
                    requireActivity().supportFragmentManager.popBackStack()*/
                } else {
                    Toast.makeText(requireContext(), "Папка не сохранена", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun isValidate(): Boolean =
        validateFolderName(binding.tvFolderName, binding.tilFolderName)

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                binding.tvFolderName.id -> {
                    validateFolderName(binding.tvFolderName, binding.tilFolderName)
                }
            }
        }
    }

    companion object {
        private const val MODE = "mode"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val FOLDER_ID = "folder_id"
        const val USER_ID = "user_id"
        const val LEVEL = "level"
        private const val UNDEFINED = 0

        @JvmStatic
        fun newInstance(
            param_mode: String,
            param_folderId: Int,
            param_userId: Int,
            param_level: Int
        ) =
            EditFolderFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, param_mode)
                    putInt(FOLDER_ID, param_folderId)
                    putInt(USER_ID, param_userId)
                    putInt(LEVEL, param_level)
                }
            }
    }

}