package com.yusufalicezik.lenovo.instagramclone.Profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {

    var circleImageView:CircleImageView?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view=inflater.inflate(R.layout.fragment_profile_edit, container, false)

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()

        }

        circleImageView=view.circleProfileImage


        setupProfilePicture()


        return view
    }




    private fun setupProfilePicture() {

        //https://scontent-mxp1-1.xx.fbcdn.net/v/t1.0-9/40464186_10212868832541803_9002957021053452288_n.jpg?_nc_cat=0&oh=7c2e5033402cd2d2716e12bddb0610d2&oe=5C36FB91

        var imgUri="scontent-mxp1-1.xx.fbcdn.net/v/t1.0-9/40464186_10212868832541803_9002957021053452288_n.jpg?_nc_cat=0&oh=7c2e5033402cd2d2716e12bddb0610d2&oe=5C36FB91"
        UniversalImageLoader.setImage(imgUri,circleImageView!!,null,"https://")


    }


}
