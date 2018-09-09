package com.yusufalicezik.lenovo.instagramclone.News

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class NewsActivity : AppCompatActivity() {

    private val ACTIVITY_NO=3
    private val TAG="NewsActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

      //  setupNavigationView()


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
