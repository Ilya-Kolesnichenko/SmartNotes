package com.ilyakoles.smartnotes.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.CalendarFragmentBinding
import com.ilyakoles.smartnotes.databinding.FoldersFragmentBinding
import com.ilyakoles.smartnotes.presentation.viewmodels.CalendarViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: CalendarViewModel

    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("CalendarFragment: binding пустой")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = bundleOf(
                    ARG_PARAM1 to param1,
                    ARG_PARAM2 to param2
                )
            }
    }

}