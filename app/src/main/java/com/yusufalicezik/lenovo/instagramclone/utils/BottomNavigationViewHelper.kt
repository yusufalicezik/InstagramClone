package com.yusufalicezik.lenovo.instagramclone.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.yusufalicezik.lenovo.instagramclone.Home.HomeActivity
import com.yusufalicezik.lenovo.instagramclone.News.NewsActivity
import com.yusufalicezik.lenovo.instagramclone.Profile.ProfileActivity
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.Search.SearchActivity
import com.yusufalicezik.lenovo.instagramclone.Share.ShareActivity
import java.security.AccessControlContext

class BottomNavigationViewHelper {

    companion object {

        fun setupBottomNavigationView(bottomnavigationViewEx: BottomNavigationViewEx){

            bottomnavigationViewEx.enableAnimation(false)
            bottomnavigationViewEx.setTextVisibility(false)
            bottomnavigationViewEx.enableShiftingMode(false)
            bottomnavigationViewEx.enableItemShiftingMode(false)

        }


        fun setupNavigation(context: Context, bottomnavigationViewEx: BottomNavigationViewEx )
        {

            bottomnavigationViewEx.onNavigationItemSelectedListener=object :BottomNavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    when(item.itemId)
                    {
                        R.id.ic_home-> {

                            val intent=Intent(context,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }
                        R.id.ic_search-> {
                            val intent=Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }
                        R.id.ic_share-> {

                            val intent=Intent(context, ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.ic_news-> {

                            val intent=Intent(context, NewsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.ic_profile-> {
                            val intent=Intent(context, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }


                    }
                    return false

                }


            }
        }

    }

}