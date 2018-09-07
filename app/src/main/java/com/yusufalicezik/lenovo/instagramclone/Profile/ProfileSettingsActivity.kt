package com.yusufalicezik.lenovo.instagramclone.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)


        setupNavigationView()

        setupToolbar()

        fragmentNavigations()
    }

    private fun fragmentNavigations() {
        tvProfiliDuzenleHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility=View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer,ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()

        }

        tvCikisYap.setOnClickListener {
            profileSettingsRoot.visibility=View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer,SignOutFragment())
            transaction.addToBackStack("signOutFragmentEklendi")
            transaction.commit()


        }
    }

    private fun setupToolbar() {
        imgBack.setOnClickListener {

            onBackPressed() //bi önceki activity e dön

        }
    }

    fun setupNavigationView()
    {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }


    override fun onBackPressed() {
        profileSettingsRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }
}
