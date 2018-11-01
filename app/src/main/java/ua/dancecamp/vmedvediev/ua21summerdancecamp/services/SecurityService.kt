package ua.dancecamp.vmedvediev.ua21summerdancecamp.services

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.support.v7.app.AlertDialog
import ua.dancecamp.vmedvediev.ua21summerdancecamp.BuildConfig
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.openLockScreenSettings

@TargetApi(Build.VERSION_CODES.M)
class SecurityService(context: Context) {

    companion object {
        fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private val keyguardManager: KeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    private var fingerprintManager: FingerprintManager? = null

    init {
        if (hasMarshmallow()) {
            fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
        }
    }

    fun isDeviceSecure(): Boolean = if (hasMarshmallow()) keyguardManager.isDeviceSecure else keyguardManager.isKeyguardSecure

    fun isFingerprintHardwareAvailable() = fingerprintManager?.isHardwareDetected ?: false

    fun hasEnrolledFingerprints() = fingerprintManager?.hasEnrolledFingerprints() ?: false

    fun authenticateFingerprint(cryptoObject: FingerprintManager.CryptoObject,
                                cancellationSignal: CancellationSignal, flags: Int,
                                callback: FingerprintManager.AuthenticationCallback, handler: Handler?) {
        fingerprintManager?.authenticate(cryptoObject, cancellationSignal, flags, callback, handler)
    }
}