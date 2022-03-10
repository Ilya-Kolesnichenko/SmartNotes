package com.ilyakoles.smartnotes.presentation.folders

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.FoldersFragmentBinding
import com.ilyakoles.smartnotes.domain.folders.Folder
import com.ilyakoles.smartnotes.presentation.SmartNotesApp
import com.ilyakoles.smartnotes.presentation.ViewModelFactory
import com.ilyakoles.smartnotes.presentation.adapters.FolderAdapter
import com.ilyakoles.smartnotes.presentation.viewmodels.FolderViewModel
import com.ilyakoles.smartnotes.utils.AddItemDialog
import com.ilyakoles.smartnotes.utils.DividerItemDecoration
import com.ilyakoles.smartnotes.utils.SwipeHelper
import com.ilyakoles.smartnotes.utils.SwipeHelper.UnderlayButtonClickListener
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FoldersFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: FolderViewModel
    private lateinit var prefs: SharedPreferences
    private var userId: Int = 0
    private var elemId: Int = 0
    private var level: Int = 0
    private var parentFolder: Folder? = null
    private lateinit var _view: View

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: FolderAdapter

    private val component by lazy {
        (requireActivity().application as SmartNotesApp).componentFolders
    }

    private var _binding: FoldersFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FoldersFragment: binding пустой")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FoldersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().applicationContext.getSharedPreferences(
            "SmartNotes_Settings",
            Context.MODE_PRIVATE
        )

        userId = prefs.getInt("UserID", 0)

        viewModel = ViewModelProvider(this, viewModelFactory)[FolderViewModel::class.java]

        adapter = FolderAdapter(requireContext())
        adapter.onFolderClickListener = object : FolderAdapter.OnFolderClickListener {
            override fun onFolderClick(folder: Folder) {
                level++
                parentFolder = folder
                if (level == 0)
                    binding.fabBackFolder.visibility = View.GONE
                else
                    binding.fabBackFolder.visibility = View.VISIBLE
                elemId = folder.folderId

                lifecycleScope.launch {
                    viewModel.getFolders(userId, elemId, level).let {
                        if (it != null) {
                            it.observe(viewLifecycleOwner) {
                                adapter.submitList(it)
                            }
                        }
                    }
                }
            }
        }

        with(binding) {
            rvFolders.adapter = adapter
            rvFolders.itemAnimator = null
            rvFolders.layoutManager = LinearLayoutManager((requireContext()));
            getContext()?.let {
                ContextCompat.getDrawable(it, R.drawable.items_divide)?.let {
                    DividerItemDecoration(it)
                }?.let {
                    rvFolders.addItemDecoration(it)
                }
            }
            setupSwipeListener(rvFolders, view)
        }

        lifecycleScope.launch {
            if (parentFolder == null)
                binding.fabBackFolder.visibility = View.GONE
            viewModel.getFolders(userId, elemId, level).let {
                if (it != null) {
                    it.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }
            }
        }

        binding.fabAddFolder.setOnClickListener {
            val addDialog = AddItemDialog()
            val manager = requireActivity().supportFragmentManager
            addDialog.setTargetFragment(this, 1);
            fragmentManager?.let { it1 ->
                addDialog.show(it1, "addDialog")
            }
            _view = it

         //   LaunchFragment(view, EditFolderFragment.MODE_ADD)

            /*launchFragment(
                EditFolderFragment.newInstance(
                    EditFolderFragment.MODE_ADD,
                    elemId,
                    userId,
                    level
                ), "add"
            )*/
        }

        binding.fabBackFolder.setOnClickListener {
            level--
            if (level == 0) {
                binding.fabBackFolder.visibility = View.GONE
                parentFolder = null
            } else
                binding.fabBackFolder.visibility = View.VISIBLE
            lifecycleScope.launch {
                if (level != 0)
                    viewModel.getParentFolderByCurFolderId(1, elemId).observe(viewLifecycleOwner) {
                        parentFolder = it
                    }

                if (parentFolder == null)
                    elemId = -1
                else
                    elemId = parentFolder?.folderId ?: 0
                viewModel.getFolders(userId, elemId, level).let {
                    if (it != null) {
                        it.observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                    }
                }
            }
        }
        // observeViewModel()
    }

    /* private fun observeViewModel() {
         viewModel.shouldCloseScreen.observe(viewLifecycleOwner, object: Observer<Any> {
             override fun onChanged(t: Any?) {
                 Log.d("receive", t.toString())
                 if (t == true)
                     lifecycleScope.launch {
                         viewModel.getFolders(userId, elemId, level).let {
                             if (it != null) {
                                 it.observe(viewLifecycleOwner) {
                                     adapter.submitList(it)
                                 }
                             }
                         }
                     }
             }
         })
     }*/

    private fun setupSwipeListener(rv: RecyclerView, view: View) {
        object : SwipeHelper(requireContext(), rv, true) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                // Delete Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "",
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_delete_24
                    ),
                    Color.parseColor("#4BB6E8"), Color.parseColor("#4BB6E8"),
                    UnderlayButtonClickListener { pos: Int ->
                        deleteFolder(adapter.currentList[pos].folderId)
                    }
                ))

                // Edit Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "",
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_edit_24
                    ),
                    Color.parseColor("#4BB6E8"), Color.parseColor("#4BB6E8"),
                    UnderlayButtonClickListener { pos: Int ->
                        var parentId = elemId
                        elemId = adapter.currentList[pos].folderId
                        Log.d("log_data_edit", elemId.toString())
                        LaunchFragment(view, EditFolderFragment.MODE_EDIT)
                        elemId = parentId
                    }
                ))
            }
        }
    }

    private fun deleteFolder(folderId: Int) {
        lifecycleScope.launch {
            var isRes = true
            var message = ""
            with(binding) {
                var answer = viewModel.deleteFolder(folderId)
                isRes = answer?.error ?: false
                message = answer?.message ?: ""
                Log.d("answ", isRes.toString() + " onView")
                Log.d("answ", message)
                if (!isRes) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }

            if (parentFolder == null)
                binding.fabBackFolder.visibility = View.GONE
            viewModel.getFolders(userId, elemId, level).let {
                if (it != null) {
                    it.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }


/*  private fun setupSwipeListener(rv: RecyclerView) {
      val callback = object : ItemTouchHelper.SimpleCallback(
          0,
          ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
      ) {

          override fun onMove(
              recyclerView: RecyclerView,
              viewHolder: RecyclerView.ViewHolder,
              target: RecyclerView.ViewHolder
          ): Boolean {
              return false
          }

          override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              val item = adapter.currentList[viewHolder.adapterPosition]
//                viewModel.deleteShopItem(item)
          }
      }
      val itemTouchHelper = ItemTouchHelper(callback)
      itemTouchHelper.attachToRecyclerView(rv)
  }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun LaunchFragment(view: View, mode: String) {
        val bundle = Bundle()
        bundle.putInt("elemId", elemId)
        bundle.putInt("userId", userId)
        bundle.putInt("level", level)
        bundle.putString("mode", mode)

        val navController = Navigation.findNavController(view)

        if (mode == EditFolderFragment.MODE_ADD) {
            navController.graph.findNode(R.id.editFolderFragment)?.label = "Добавление папки"
        } else {
            navController.graph.findNode(R.id.editFolderFragment)?.label = "Редактирование папки"
        }
        navController.navigate(R.id.action_navigation_folders_to_editFolderFragment, bundle)
    }


/*private fun launchFragment(fragment: Fragment, name: String) {
    requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.nav_host_fragment_activity_main, fragment, "edit_folder")
        .addToBackStack(null)
        .commit()
    //fm.executePendingTransactions()
    /*requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.nav_host_fragment_activity_main, fragment)
        .addToBackStack(null)
        .commit()*/
}*/


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoldersFragment().apply {
                arguments = bundleOf(
                    ARG_PARAM1 to param1,
                    ARG_PARAM2 to param2
                )
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.getIntExtra("value", 0) == 0)
            LaunchFragment(_view, EditFolderFragment.MODE_ADD)
        else {
        }
    }
}
