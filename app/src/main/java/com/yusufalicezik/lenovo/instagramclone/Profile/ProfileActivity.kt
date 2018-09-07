package com.yusufalicezik.lenovo.instagramclone.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*



class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

       setupNavigationView()

        setupToolbar()
        setupProfilePhoto()



    }

    private fun setupProfilePhoto() {
        var imgUri="images.pexels.com/photos/1213447/pexels-photo-1213447.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        UniversalImageLoader.setImage(imgUri,circleProfileImage,progressBar,"https://")
    }

    private fun setupToolbar() {
        imgProfilSettings.setOnClickListener {

            var intent=Intent(this,ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }


        tvProfilDuzenleButon.setOnClickListener {
            profileRoot.visibility= View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer,ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()
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
        profileRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }


}
