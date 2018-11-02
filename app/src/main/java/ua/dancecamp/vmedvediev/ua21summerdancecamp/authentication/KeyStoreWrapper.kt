package ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import ua.dancecamp.vmedvediev.ua21summerdancecamp.MyApplication
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyStoreWrapper {

    private const val DEFAULT_KEY_STORE_NAME = "default_keystore"

    private val keyStore: KeyStore = createAndroidKeyStore()

    private val defaultKeyStoreFile = File(MyApplication.instance.filesDir, DEFAULT_KEY_STORE_NAME)
    private val defaultKeyStore = createDefaultKeyStore()

    fun getAndroidKeyStoreSymmetricKey(alias: String): SecretKey? = keyStore.getKey(alias, null) as SecretKey?

    fun getDefaultKeyStoreSymmetricKey(alias: String, keyPassword: String): SecretKey? {
        return try {
            defaultKeyStore.getKey(alias, keyPassword.toCharArray()) as SecretKey
        } catch (e: UnrecoverableKeyException) {
            null
        }
    }

    fun createDefaultKeyStoreSymmetricKey(alias: String, password: String) {
        val key = generateDefaultSymmetricKey()
        val keyEntry = KeyStore.SecretKeyEntry(key)

        defaultKeyStore.setEntry(alias, keyEntry, KeyStore.PasswordProtection(password.toCharArray()))
        defaultKeyStore.store(FileOutputStream(defaultKeyStoreFile), password.toCharArray())
    }

    private fun generateDefaultSymmetricKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        return keyGenerator.generateKey()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun createAndroidKeyStoreSymmetricKey(
            alias: String,
            userAuthenticationRequired: Boolean = false,
            invalidatedByBiometricEnrollment: Boolean = true,
            userAuthenticationValidityDurationSeconds: Int = -1,
            userAuthenticationValidWhileOnBody: Boolean = true): SecretKey {

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(userAuthenticationRequired)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationValidityDurationSeconds(userAuthenticationValidityDurationSeconds)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
            builder.setUserAuthenticationValidWhileOnBody(userAuthenticationValidWhileOnBody)
        }
        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }

    private fun createDefaultKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

        if (!defaultKeyStoreFile.exists()) {
            keyStore.load(null)
        } else {
            keyStore.load(FileInputStream(defaultKeyStoreFile), null)
        }
        return keyStore
    }

}