package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.login

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_login.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.Router
import ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication.EncryptionService
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmCredentialsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SecurityService
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Credentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val CREDENTIALS_ID = "1"
    }

    private val loginViewModel by lazy {
        ViewModelProviders.of(this, LoginViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper(), RealmCredentialsMapper())).LoginViewModelFactory())
                .get(LoginViewModel::class.java)
    }
    private val securityServices by lazy(LazyThreadSafetyMode.NONE) { SecurityService(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (securityServices.isFingerprintHardwareAvailable()) {
            allowFingerprintCheckBox.apply {
                visibility = View.VISIBLE
                setOnCheckedChangeListener { _, checked -> onAllowFingerprint(checked) }
            }
        }

        passwordTextInputEditText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (loginViewModel.isPasswordValid(passwordTextInputEditText.text.toString())) {
                        passwordTextInputLayout.error = null
                    }
                }

            })

            setOnEditorActionListener { _, actionId, _ -> onEditorActionClick(actionId) }
        }

        confirmPasswordTextInputEditText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (loginViewModel.isPasswordsMatch(passwordTextInputEditText.text.toString(),
                                    confirmPasswordTextInputEditText.text.toString())) {
                        confirmPasswordTextInputLayout.error = null
                    }
                }
            })

            setOnEditorActionListener { _, id, _ -> onEditorActionClick(id) }
        }

        nextButton.setOnClickListener {
            when {
                !loginViewModel.isPasswordValid(passwordTextInputEditText.text.toString()) -> {
                    passwordTextInputLayout.error = getString(R.string.label_error_password)
                    passwordTextInputEditText.requestFocus()
                }
                !loginViewModel.isPasswordsMatch(passwordTextInputEditText.text.toString(),
                        confirmPasswordTextInputEditText.text.toString()) -> {
                    confirmPasswordTextInputLayout.error = getString(R.string.label_error_passwords_dont_match)
                    confirmPasswordTextInputEditText.requestFocus()
                }
                else -> signUp()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun onAllowFingerprint(checked: Boolean) {
        if (checked && !securityServices.hasEnrolledFingerprints()) {
            allowFingerprintCheckBox.isChecked = false
            Snackbar.make(loginScrollView, R.string.sign_up_snack_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.sign_up_snack_action) { Router.openSecuritySettings(this) }
                    .show()
        }
    }

    private fun signUp() {
        val passwordString = passwordTextInputEditText.text.toString()
        createKeys(passwordString, allowFingerprintCheckBox.isChecked)
        val encryptedPassword = EncryptionService.encrypt(passwordString, passwordString)

        loginViewModel.saveCredentials(Credentials(CREDENTIALS_ID, encryptedPassword, allowFingerprintCheckBox.isChecked))

        Router.startMainActivity(this)
        finish()
    }

    private fun createKeys(password: String, isFingerprintAllowed: Boolean) {
        EncryptionService.createMasterKey(password)

        if (SecurityService.hasMarshmallow()) {
            if (isFingerprintAllowed && securityServices.hasEnrolledFingerprints()) {
                EncryptionService.createFingerprintKey()
            }
            EncryptionService.createConfirmCredentialsKey()
        }
    }

    private fun onEditorActionClick(id: Int): Boolean = when (id) {
        EditorInfo.IME_ACTION_DONE -> {
            nextButton.performClick()
            true
        }
        EditorInfo.IME_ACTION_NEXT -> {
            loginScrollView.fullScroll(View.FOCUS_DOWN)
            confirmPasswordTextInputEditText.requestFocus()
            true
        }
        else -> false
    }
}