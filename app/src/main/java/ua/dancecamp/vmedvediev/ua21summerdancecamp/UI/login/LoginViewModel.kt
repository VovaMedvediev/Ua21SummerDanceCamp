package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.widget.Toast
import timber.log.Timber
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Credentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }

    fun saveCredentials(credentials: Credentials) { repository.saveCredentials(credentials)}

    fun isPasswordValid(password: String) : Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    fun isPasswordsMatch(password: String, confirmationPassword: String) : Boolean {
        return password == confirmationPassword
    }

    inner class LoginViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(repository) as T
        }
    }
}