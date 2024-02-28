package launcher.axay.cmd.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.Locale

class AppListAdapter(private val context: Context, private val packageManager: PackageManager) : BaseAdapter() {
    private val appsList: List<PackageInfo> by lazy {
        packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.GET_ACTIVITIES
        ).map { it.activityInfo.applicationInfo.packageName }
            .distinct()
            .map { packageManager.getPackageInfo(it, PackageManager.GET_ACTIVITIES) }
            .sortedBy { it.applicationInfo.loadLabel(packageManager).toString()
                .lowercase(Locale.ROOT) } // Sort by app name in a case-insensitive manner
    }

    override fun getCount(): Int = appsList.size

    override fun getItem(position: Int): Any = appsList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val packageInfo = appsList[position]
        val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
        // Concatenate "://" to the beginning of the app name
        holder.appNameTextView.text = ":// $appName"

        // Set the TextView to single line mode and enable text ellipsis
        holder.appNameTextView.isSingleLine = true
        holder.appNameTextView.ellipsize = TextUtils.TruncateAt.END

        view?.setOnClickListener {
            val packageName = packageInfo.packageName
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            } else {
                Log.e("AppGridAdapter", "Unable to find launch intent for package $packageName")
            }
        }

        view?.setOnLongClickListener {
            val packageName = packageInfo.packageName
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            context.startActivity(intent)
            true
        }

        return view!!
    }

    private class ViewHolder(view: View) {
        val appNameTextView: TextView = view.findViewById(android.R.id.text1)
    }
}