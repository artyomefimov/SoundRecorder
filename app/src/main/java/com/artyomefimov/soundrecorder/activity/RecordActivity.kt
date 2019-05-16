package com.artyomefimov.soundrecorder.activity

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.filesfragment.FilesFragment
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordFragment
import kotlinx.android.synthetic.main.activity_main.*

class RecordActivity : FragmentActivity() {
    private val fragmentManager = supportFragmentManager

    companion object {
        private const val RECORD_FRAGMENT = "record_fragment"
        private const val FILES_FRAGMENT = "files_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.show()
        callRecordFragment()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_record -> callRecordFragment()
                R.id.action_files -> callFilesFragment()
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun callRecordFragment() {
        var fragment = fragmentManager.findFragmentByTag(RECORD_FRAGMENT)

        if (fragment == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragment = RecordFragment()
            fragmentTransaction.replace(R.id.fragment_container, fragment, RECORD_FRAGMENT)
            fragmentTransaction.commit()
        }
    }

    private fun callFilesFragment() {
        var fragment = fragmentManager.findFragmentByTag(FILES_FRAGMENT)

        if (fragment == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragment = FilesFragment()
            fragmentTransaction.replace(R.id.fragment_container, fragment, FILES_FRAGMENT)
            fragmentTransaction.commit()
        }
    }
}
