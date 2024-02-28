package launcher.axay.cmd

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import launcher.axay.cmd.adapter.AppListAdapter
import launcher.axay.cmd.databinding.ActivityLauncherBinding
import java.util.Date

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    private lateinit var packageManager: PackageManager
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the current hour of the day
        val calendar = Calendar.getInstance()

        // Set the greeting based on the time of day
        val greeting = when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 6 until 12 -> "> Hello, Good Morning!"
            in 12 until 17 -> "> Good Afternoon, Keep Going!"
            in 17 until 21 -> "> Good Evening, Relax!"
            else -> "> Good Night, Sleep well!"
        }

        binding
            .greetingsTextview
            .text = greeting

        // Get the current date and format it to display only the date and day
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy")
        val currentDate = formatter.format(Date())

        binding
            .dateTimeTextview
            .text = "> $currentDate"

        packageManager = getPackageManager()

        val adapter = AppListAdapter(this, packageManager)
        binding
            .appGrid
            .adapter = adapter

        onBackPressedDispatcher.addCallback(this) {
            Toast
                .makeText(
                    this@LauncherActivity,
                    "Very funny!",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}