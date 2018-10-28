package ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication

import android.annotation.TargetApi
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.security.keystore.KeyPermanentlyInvalidatedException
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SystemServices
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

class EncryptionServices(context: Context) {

    companion object {
        const val DEFAULT_KEY_STORE_NAME = "default_keystore"
        const val MASTER_KEY = "MASTER_KEY"
        const val FINGERPRINT_KEY = "FINGERPRINT_KEY"
        const val CONFIRM_CREDENTIALS_KEY = "CONFIRM_CREDENTIALS_KEY"

        val KEY_VALIDATION_DATA = byteArrayOf(0, 1, 0, 1)
        const val CONFIRM_CREDENTIALS_VALIDATION_DELAY = 120 // Seconds
    }

    private val keyStoreWrapper = KeyStoreWrapper(context, DEFAULT_KEY_STORE_NAME)

    fun createMasterKey(password: String? = null) {
        if (SystemServices.hasMarshmallow()) {
            createAndroidSymmetricKey()
        } else {
            createDefaultSymmetricKey(password ?: "")
        }
    }

    fun encrypt(data: String, keyPassword: String? = null): String {
        return if (SystemServices.hasMarshmallow()) {
            encryptWithAndroidSymmetricKey(data)
        } else {
            encryptWithDefaultSymmetricKey(data, keyPassword ?: "")
        }
    }

    fun decrypt(data: String, keyPassword: String? = null): String {
        return if (SystemServices.hasMarshmallow()) {
            decryptWithAndroidSymmetricKey(data)
        } else {
            decryptWithDefaultSymmetricKey(data, keyPassword ?: " ")
        }
    }

    private fun createAndroidSymmetricKey() {
        keyStoreWrapper.createAndroidKeyStoreSymmetricKey(MASTER_KEY)
    }

    private fun encryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true)
    }

    private fun decryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey, true)
    }

    private fun createDefaultSymmetricKey(password: String) {
        keyStoreWrapper.createDefaultKeyStoreSymmetricKey(MASTER_KEY, password)
    }

    private fun encryptWithDefaultSymmetricKey(data: String, keyPassword: String): String {
        val masterKey = keyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY, keyPassword)
        return CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true)
    }

    private fun decryptWithDefaultSymmetricKey(data: String, keyPassword: String): String {
        val masterKey = keyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY, keyPassword)
        return masterKey?.let { CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey, true) } ?: " "
    }

    fun createFingerprintKey() {
        if (SystemServices.hasMarshmallow()) {
            keyStoreWrapper.createAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY,
                    userAuthenticationRequired = true,
                    invalidatedByBiometricEnrollment = true,
                    userAuthenticationValidWhileOnBody = false)
        }
    }

    fun prepareFingerprintCryptoObject(): FingerprintManager.CryptoObject? {
        return if (SystemServices.hasMarshmallow()) {
            try {
                val symmetricKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY)
                val cipher = CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).cipher
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
                return false
            }
            throw e
        }
    }

    fun createConfirmCredentialsKey() {
        if (SystemServices.hasMarshmallow()) {
            keyStoreWrapper.createAndroidKeyStoreSymmetricKey(
                    CONFIRM_CREDENTIALS_KEY,
                    userAuthenticationRequired = true,
                    userAuthenticationValidityDurationSeconds = CONFIRM_CREDENTIALS_VALIDATION_DELAY)
        }
    }
}