package com.example.contactos_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.contactos_app.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        ContactDatabase::class.java,
        "contact_db"
    ).build()

    private val dao = db.contactDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val contacts = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                dao.getAllContacts()
            } else {
                dao.searchContacts("%$query%")
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun insert(contact: Contact) = viewModelScope.launch {
        dao.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch {
        dao.update(contact)
    }
    fun delete(contact: Contact) = viewModelScope.launch {
        dao.delete(contact)
    }

    suspend fun getById(id: Int) = dao.getContactById(id)

    suspend fun getByEmail(email: String) = dao.getContactByEmail(email)
}
