package launcher.axay.cmd

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import launcher.axay.cmd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding
            .instaPromo
            .makeClickable(
                text = ":// For updates, follow on Instagram",
                clickableText = "Instagram",
                clickListener = {
                    val uri = Uri.parse("https://www.instagram.com/axay.06")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            )

        binding
            .defaultTextViewButton
            .setOnClickListener {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:launcher.axay.cmd")
                this.startActivity(intent)
            }
    }

    private fun TextView.makeClickable(text: CharSequence, clickableText: String, clickListener: () -> Unit) {
        val spannable = SpannableString(text)
        val clickableStart = text.indexOf(clickableText)
        val clickableEnd = clickableStart + clickableText.length
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickListener.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }, clickableStart, clickableEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        this.text = spannable
        this.movementMethod = LinkMovementMethod.getInstance()
    }
}