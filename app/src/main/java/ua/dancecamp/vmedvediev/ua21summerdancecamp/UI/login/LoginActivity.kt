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
import ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication.EncryptionServices
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmCredentialsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SystemServices
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Credentials
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.openSecuritySettings

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val CREDENTIALS_ID = "1"
    }

    private val loginViewModel by lazy {
        ViewModelProviders.of(this, LoginViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper(), RealmCredentialsMapper())).LoginViewModelFactory())
                .get(LoginViewModel::class.java)
    }
    private val systemServices by lazy(LazyThreadSafetyMode.NONE) { SystemServices(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (systemServices.isFingerprintHardwareAvailable()) {
            allowFingerprintCheckBox.visibility = View.VISIBLE
        }
        allowFingerprintCheckBox.setOnCheckedChangeListener { _, checked -> onAllowFingerprint(checked) }

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
    }

    private fun onAllowFingerprint(checked: Boolean) {
        if (checked && !systemServices.hasEnrolledFingerprints()) {
            allowFingerprintCheckBox.isChecked = false
            Snackbar.make(loginScrollView, R.string.sign_up_snack_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.sign_up_snack_action) { openSecuritySettings() }
                    .show()
        }
    }

    private fun signUp() {
        val passwordString = passwordTextInputEditText.text.toString()
        createKeys(passwordString, allowFingerprintCheckBox.isChecked)
        val encryptedPassword = EncryptionServices(applicationContext).encrypt(passwordString, passwordString)

        loginViewModel.saveCredentials(Credentials(CREDENTIALS_ID, encryptedPassword, allowFingerprintCheckBox.isChecked))

        startActivity(Router.prepareMainActivityIntent(this))
        finish()
    }

    private fun createKeys(password: String, isFingerprintAllowed: Boolean) {
        val encryptionService = EncryptionServices(applicationContext)
        encryptionService.createMasterKey(password)

        if (SystemServices.hasMarshmallow()) {
            if (isFingerprintAllowed && systemServices.hasEnrolledFingerprints()) {
                encryptionService.createFingerprintKey()
            }
            encryptionService.createConfirmCredentialsKey()
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