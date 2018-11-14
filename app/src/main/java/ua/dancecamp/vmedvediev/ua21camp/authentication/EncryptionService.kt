package ua.dancecamp.vmedvediev.ua21camp.authentication

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.security.keystore.KeyPermanentlyInvalidatedException
import timber.log.Timber
import ua.dancecamp.vmedvediev.ua21camp.services.SecurityService
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

object EncryptionService {

        private const val MASTER_KEY = "MASTER_KEY"
        private const val FINGERPRINT_KEY = "FINGERPRINT_KEY"
        private const val CONFIRM_CREDENTIALS_KEY = "CONFIRM_CREDENTIALS_KEY"

        private val KEY_VALIDATION_DATA = byteArrayOf(0, 1, 0, 1)
        private const val CONFIRM_CREDENTIALS_VALIDATION_DELAY = 120 // Seconds

    fun createMasterKey(password: String) {
        if (SecurityService.hasMarshmallow()) {
            KeyStoreWrapper.createAndroidKeyStoreSymmetricKey(MASTER_KEY)
        } else {
            KeyStoreWrapper.createDefaultKeyStoreSymmetricKey(MASTER_KEY, password)
        }
    }

    fun encrypt(data: String, keyPassword: String? = null): String {
        return if (SecurityService.hasMarshmallow()) {
            encryptWithAndroidSymmetricKey(data)
        } else {
            encryptWithDefaultSymmetricKey(data, keyPassword ?: "")
        }
    }

    fun decrypt(data: String, keyPassword: String? = null): String {
        return if (SecurityService.hasMarshmallow()) {
            decryptWithAndroidSymmetricKey(data)
        } else {
            decryptWithDefaultSymmetricKey(data, keyPassword ?: " ")
        }
    }

    private fun encryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = KeyStoreWrapper.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherWrapper.encrypt(data, masterKey, true)
    }

    private fun decryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = KeyStoreWrapper.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherWrapper.decrypt(data, masterKey, true)
    }

    private fun encryptWithDefaultSymmetricKey(data: String, keyPassword: String): String {
        val masterKey = KeyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY, keyPassword)
        return CipherWrapper.encrypt(data, masterKey, true)
    }

    private fun decryptWithDefaultSymmetricKey(data: String, keyPassword: String): String {
        val masterKey = KeyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY, keyPassword)
        return masterKey?.let { CipherWrapper.decrypt(data, masterKey, true) } ?: " "
    }

    fun createFingerprintKey() {
        if (SecurityService.hasMarshmallow()) {
            KeyStoreWrapper.createAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY,
                    userAuthenticationRequired = true,
                    invalidatedByBiometricEnrollment = true,
                    userAuthenticationValidWhileOnBody = false)
        }
    }

    fun prepareFingerprintCryptoObject(): FingerprintManager.CryptoObject? {
        return if (SecurityService.hasMarshmallow()) {
            try {
                val symmetricKey = KeyStoreWrapper.getAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY)
                val cipher = CipherWrapper.cipher
                cipher.init(Cipher.ENCRYPT_MODE, symmetricKey)
                FingerprintManager.CryptoObject(cipher)
            } catch (e: Throwable) {
                if (e is KeyPermanentlyInvalidatedException || e is IllegalBlockSizeException) {
                    return null
                } else if (e is InvalidKeyException) {
                    return null
                }
                throw e
            }
        } else null
    }

    @TargetApi(23)
    fun validateFingerprintAuthentication(cryptoObject: FingerprintManager.CryptoObject): Boolean {
        try {
            cryptoObject.cipher.doFinal(KEY_VALIDATION_DATA)
            return true
        } catch (e: Throwable) {
            if (e is KeyPermanentlyInvalidatedException || e is IllegalBlockSizeException) {
                Timber.e("validateFingerprintAuthentication: Error: ${e.message}")
                return false
            }
            throw e
        }
    }

    fun createConfirmCredentialsKey() {
        if (SecurityService.hasMarshmallow()) {
            KeyStoreWrapper.createAndroidKeyStoreSymmetricKey(
                    CONFIRM_CREDENTIALS_KEY,
                    userAuthenticationRequired = true,
                    userAuthenticationValidityDurationSeconds = CONFIRM_CREDENTIALS_VALIDATION_DELAY)
        }
    }
}