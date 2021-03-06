package ua.dancecamp.vmedvediev.ua21camp.UI.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21camp.model.Credentials
import ua.dancecamp.vmedvediev.ua21camp.repository.Repository

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