package com.artyomefimov.soundrecorder.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.filesfragment.FilesFragment
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordFragment
import kotlinx.android.synthetic.main.activity_main.*

class RecordActivity : AppCompatActivity() {
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
            PLAYING -> callFilesFragment()
            else -> callRecordFragment()
        }

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_record -> callRecordFragment()
                R.id.action_files -> callFilesFragment()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun callRecordFragment() {
        var fragment = fragmentManager.findFragmentByTag(RECORD_FRAGMENT)
        if (fragment == null)
            fragment = RecordFragment()

        performFragmentTransaction(fragment, RECORD_FRAGMENT)
    }

    private fun callFilesFragment() {
        var fragment = fragmentManager.findFragmentByTag(FILES_FRAGMENT)
        if (fragment == null)
            fragment = FilesFragment()

        performFragmentTransaction(fragment, FILES_FRAGMENT)
    }

    private fun performFragmentTransaction(fragment: Fragment, tag: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        fragmentTransaction.commit()
    }
}
