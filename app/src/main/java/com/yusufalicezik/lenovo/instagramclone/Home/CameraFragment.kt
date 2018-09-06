package com.yusufalicezik.lenovo.instagramclone.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusufalicezik.lenovo.instagramclone.R

class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater?.inflate(R.layout.fragment_camera,container,false)
        return view
    }
}