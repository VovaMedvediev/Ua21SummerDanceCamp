package ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.os.Looper
import com.badoo.mobile.util.WeakHandler
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SecurityService

class AuthenticationFingerprintListener(
        private val securityService: SecurityService,
        private val view: AuthenticationFingerprintHelper,
        private val callback: Callback) {

    companion object {
        private const val ERROR_TIMEOUT_MILLIS: Long = 1600
        private const val SUCCESS_DELAY_MILLIS: Long = 1300
    }

    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false
    private var handler: WeakHandler = WeakHandler(Looper.getMainLooper())

    fun isFingerprintAuthAvailable(): Boolean {
        return securityService.isFingerprintHardwareAvailable() && securityService.hasEnrolledFingerprints()
    }

    fun startListening(cryptoObject: FingerprintManager.CryptoObject) {
        if (isFingerprintAuthAvailable()) {
            cancellationSignal = CancellationSignal()
            selfCancelled = false
            securityService.authenticateFingerprint(cryptoObject, cancellationSignal!!, 0, fingerprintCallback, null)
        }
    }

    fun stopListening() {
        cancellationSignal?.let {
            it.cancel()
            selfCancelled = true
            cancellationSignal = null
        }
    }

    private val fingerprintCallback = object : FingerprintManager.AuthenticationCallback() {
        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
            if (!selfCancelled) {
                view.showErrorView(errString.toString())
                handler.postDelayed({ callback.onAuthenticationError() }, ERROR_TIMEOUT_MILLIS)
            }
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
            view.showErrorView(helpString.toString())
            showErrorAndHideItAfterDelay()
        }

        override fun onAuthenticationFailed() {
            view.showErrorView(R.string.authentication_fingerprint_not_recognized)
            showErrorAndHideItAfterDelay()
        }

        @TargetApi(23)
        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
            handler.removeCallbacks(hideErrorRunnable)
            view.showSuccessView()
            handler.postDelayed({ callback.onAuthenticated(result.cryptoObject) }, SUCCESS_DELAY_MILLIS)
        }

        private fun showErrorAndHideItAfterDelay() {
            handler.removeCallbacks(hideErrorRunnable)
            handler.postDelayed(hideErrorRunnable, ERROR_TIMEOUT_MILLIS)
        }

        private val hideErrorRunnable = Runnable { view.hideErrorView() }
    }

    interface Callback {
        fun onAuthenticated(cryptoObject: FingerprintManager.CryptoObject)
        fun onAuthenticationError()
    }
}

