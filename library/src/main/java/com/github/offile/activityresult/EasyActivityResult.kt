package com.github.offile.activityresult

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.offile.activityresult.callback.ActivityResultCallback
import com.github.offile.activityresult.callback.PermissionsResultCallback
import com.github.offile.activityresult.util.HandleUtil

/**
 * Call the with method to get an instance
 */
class EasyActivityResult private constructor(private val fragmentManager: FragmentManager) {

    companion object {
        /**
         * The unique tag of the ProxyFragment added to the FragmentManager
         * @see ProxyFragment
         */
        const val TAG = "EasyActivityResult\$ProxyFragment"

        /**
         * Create an instance from FragmentActivity
         * @param fragmentActivity A FragmentActivity instance
         * @return EasyActivityResult
         */
        @JvmStatic
        fun with(fragmentActivity: FragmentActivity): EasyActivityResult {
            return EasyActivityResult(fragmentActivity.supportFragmentManager)
        }

        /**
         * Create an instance from Fragment
         * @param fragment A Fragment instance
         * @return EasyActivityResult
         */
        @JvmStatic
        fun with(fragment: Fragment): EasyActivityResult {
            return EasyActivityResult(fragment.childFragmentManager)
        }
    }

    /**
     * Only one ProxyFragment exists in a FragmentManager
     */
    val fragment: ProxyFragment by lazy {
        synchronized(fragmentManager){
            val proxyFragment = fragmentManager.findFragmentByTag(TAG)
            if (proxyFragment != null) {
                proxyFragment as ProxyFragment
            } else {
                ProxyFragment().also {
                    HandleUtil.syncRunOnUiThread {
                        fragmentManager.beginTransaction()
                            .add(it, TAG)
                            .commitNow()
                    }
                }
            }
        }
    }

    /**
     * use callback to receive results
     * @see Fragment.startActivityForResult
     */
    fun startActivityForResult(intent: Intent, callback: ActivityResultCallback) {
        startActivityForResult(intent, null, callback)
    }

    /**
     * use callback to receive results
     * @see Fragment.startActivityForResult
     */
    fun startActivityForResult(intent: Intent, options: Bundle?, callback: ActivityResultCallback) {
        fragment.startActivityForResult(intent, options, callback)
    }

    /**
     * use callback to receive results
     * @see Fragment.requestPermissions
     */
    fun requestPermissions(vararg permissions: String, callback: PermissionsResultCallback) {
        fragment.requestPermissions(permissions, callback)
    }
}