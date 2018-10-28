package ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication

import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.dialog_fingerprint_backup.*
import kotlinx.android.synthetic.main.dialog_fingerprint_container.*
import kotlinx.android.synthetic.main.dialog_fingerprint_content.*
import timber.log.Timber
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SystemServices
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.showKeyboard

class AuthenticationDialog : AppCompatDialogFragment(), AuthenticationFingerprint.Callback {

    var passwordVerificationListener: ((password: String) -> Boolean)? = null
    var authenticationSuccessListener: ((password: String) -> Unit)? = null

    var fingerprintAuthenticationSuccessListener: ((cryptoObject: FingerprintManager.CryptoObject) -> Unit)? = null
    var fingerprintInvalidationListener: ((invalidatedByBiometricEnrollment: Boolean) -> Unit)? = null

    var stage = Stage.FINGERPRINT

    var cryptoObjectToAuthenticateWith: FingerprintManager.CryptoObject? = null

    private var authenticationFingerprint: AuthenticationFingerprint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuthenticationDialog)
    }

    override fun onResume() {
        super.onResume()
        if (stage == Stage.FINGERPRINT) {
            if (authenticationFingerprint?.isFingerprintAuthAvailable() == true) {
                cryptoObjectToAuthenticateWith?.let { authenticationFingerprint?.startListening(it) }
            } else {
                goToBackup()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        authenticationFingerprint?.stopListening()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fingerprint_container, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setTitle(getString(R.string.authentication_title))
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) activity?.finish()
            true
        }
        cancelButtonView.setOnClickListener { activity?.finish() }
        secondButtonView.setOnClickListener { if (stage == Stage.FINGERPRINT) goToBackup() else verifyPassword() }
        passwordView.setOnEditorActionListener { _, actionId, _ -> onEditorAction(actionId) }

        if (SystemServices.hasMarshmallow()) {
            authenticationFingerprint = AuthenticationFingerprint(
                    SystemServices(context!!.applicationContext),
                    AuthenticationFingerprintView(fingerprintIconView, fingerprintStatusView), this)
        }

        updateStage()

        if (authenticationFingerprint?.isFingerprintAuthAvailable() != true) {
            goToBackup()
        }
    }

    override fun onAuthenticated(cryptoObject: FingerprintManager.CryptoObject) {
        fingerprintAuthenticationSuccessListener?.invoke(cryptoObject)
        dismiss()
    }

    override fun onAuthenticationError() {
        goToBackup()
    }

    private fun onEditorAction(actionId: Int): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword()
            true
        } else false
    }

    private fun updateStage() {
        Log.i("updateStage", stage.name)
        when (stage) {
            Stage.FINGERPRINT -> showFingerprintStage()
            Stage.NEW_FINGERPRINT_ENROLLED, Stage.PASSWORD -> showBackupStage()
        }
    }

    private fun showFingerprintStage() {
        cancelButtonView.setText(R.string.authentication_cancel)
        secondButtonView.setText(R.string.authentication_use_password)
        fingerprintContainerView.visibility = View.VISIBLE
        backupContainerView.visibility = View.GONE

    }

    private fun showBackupStage() {
        cancelButtonView.setText(R.string.authentication_cancel)
        secondButtonView.setText(R.string.authentication_ok)
        fingerprintContainerView.visibility = View.GONE
        backupContainerView.visibility = View.VISIBLE
        if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
            passwordDescriptionView.visibility = View.GONE
            fingerprintEnrolledView.visibility = View.VISIBLE
            useFingerprintInFutureView.visibility = View.VISIBLE
        }
    }

    private fun goToBackup() {
        stage = Stage.PASSWORD
        updateStage()

        passwordView.requestFocus()

        passwordView.showKeyboard(delay = 500)

        authenticationFingerprint?.stopListening()
    }

    private fun verifyPassword() {
        val password = passwordView.text.toString()
        if (!checkPassword(password)) {
            passwordView.error = getString(R.string.authentication_error_incorrect_password)
            return
        }

        if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
            fingerprintInvalidationListener?.invoke(useFingerprintInFutureView.isChecked)
        }
        passwordView.setText("")
        authenticationSuccessListener?.invoke(password)
        dismiss()
    }

    private fun checkPassword(password: String): Boolean {
        return passwordVerificationListener?.invoke(password) ?: false
    }

    enum class Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}