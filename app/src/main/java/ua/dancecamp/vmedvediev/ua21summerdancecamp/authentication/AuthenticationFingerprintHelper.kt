package ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import ua.dancecamp.vmedvediev.ua21summerdancecamp.MyApplication
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.getColorCompat

class AuthenticationFingerprintHelper(private val icon: ImageView, private val errorTextView: TextView) {

    private val resources: Resources = MyApplication.instance.resources

    fun showSuccessView() {
        icon.setImageResource(R.drawable.ic_fingerprint_success)
        errorTextView.setTextColor(resources.getColorCompat(R.color.success_color))
        errorTextView.text = resources.getString(R.string.authentication_fingerprint_success)
    }

    fun showErrorView(errorId: Int) = showErrorView(resources.getString(errorId))

    fun showErrorView(error: String) {
        icon.setImageResource(R.drawable.ic_fingerprint_error)
        errorTextView.text = error
        errorTextView.setTextColor(resources.getColorCompat(R.color.warning_color))
    }

    fun hideErrorView() {
        errorTextView.setTextColor(resources.getColorCompat(R.color.hint_color))
        errorTextView.text = resources.getString(R.string.authentication_fingerprint_hint)
        icon.setImageResource(R.drawable.ic_fp_40px)
    }
}