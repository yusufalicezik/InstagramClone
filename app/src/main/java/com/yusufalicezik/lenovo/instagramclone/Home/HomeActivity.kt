package com.yusufalicezik.lenovo.instagramclone.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import com.yusufalicezik.lenovo.instagramclone.utils.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO=0
    private val TAG="HomeActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupNavigationView()
        setupHomeViewPager()

    }

    private fun setupHomeViewPager() {
       var homePagerAdapter=HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.addFragment(CameraFragment()) // id=0
        homePagerAdapter.addFragment(HomeFragment())    //id=1
        homePagerAdapter.addFragment(MessagesFragment()) //id=2

        //activitymain de bulunan viewpager a oluşturdugumuz adaptörü atadık.
        homeViewPager.adapter=homePagerAdapter

        //burda da viewpager ın homefragment ile başlamasını sağladık.
        homeViewPager.setCurrentItem(1)

    }


    fun setupNavigationView()
    {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
