package com.ilyakoles.smartnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.ilyakoles.smartnotes.domain.Answer
import com.ilyakoles.smartnotes.domain.folders.DeleteFolderUseCase
import com.ilyakoles.smartnotes.domain.folders.GetFoldersUseCase
import com.ilyakoles.smartnotes.domain.folders.SaveFolderUseCase
import javax.inject.Inject

class FolderViewModel @Inject constructor(
    private val getFoldersUseCase: GetFoldersUseCase,
    private val saveFolderUseCase: SaveFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase
) : ViewModel() {

    private val _shouldCloseScreen = MutableLiveData<Any>()
    val shouldCloseScreen: LiveData<Any>
        get() = _shouldCloseScreen

    suspend fun getFolders(userId: Int, parentFolderId: Int, level: Int) =
        getFoldersUseCase(userId, parentFolderId, level)

    suspend fun getParentFolderByCurFolderId(isParent: Int, folderId: Int) =
        getFoldersUseCase(isParent, folderId)

    suspend fun saveFolder(id: Int, parentId: Int, name: String, userId: Int, isShared: Int,
                           description: String, level: Int): Answer? =
        saveFolderUseCase(id, parentId, name, userId, isShared, description, level)

    suspend fun deleteFolder(id: Int): Answer? = deleteFolderUseCase(id)

    /*
    val allMovies: LiveData<List<MovieEntity>>

    init {
        val movieDao = MovieDatabase.getInstance(application).movieDao()
        repository = MovieRepository(movieDao)
        allMovies = repository.allMovies
    }

    fun insert(movie: MovieEntity) = viewModelScope.launch {
        repository.insert(movie)
    }

     */

    fun finishWork(flag: Boolean) {
        _shouldCloseScreen.value = flag
        Log.d("receive", "model: " + flag)
    }
}

