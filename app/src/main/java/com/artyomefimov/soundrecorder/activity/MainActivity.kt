package com.artyomefimov.soundrecorder.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.filesfragment.FilesFragment
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager

    companion object {
        private const val RECORD_FRAGMENT = "record_fragment"
        private const val FILES_FRAGMENT = "files_fragment"
        const val PLAYING = "playing"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        when (intent?.action) {
            PLAYING -> callFragmentByTag(FILES_FRAGMENT)
            else -> callFragmentByTag(RECORD_FRAGMENT)
        }

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_record -> callFragmentByTag(RECORD_FRAGMENT)
                R.id.action_files -> callFragmentByTag(FILES_FRAGMENT)
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun callFragmentByTag(tag: String) {
        var fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null)
            fragment = if (tag == FILES_FRAGMENT) FilesFragment() else RecordFragment()

        performFragmentTransaction(fragment, tag)
    }

    private fun performFragmentTransaction(fragment: Fragment, tag: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        fragmentTransaction.commit()
    }
}
