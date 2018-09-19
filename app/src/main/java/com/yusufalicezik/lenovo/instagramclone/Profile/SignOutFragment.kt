package com.yusufalicezik.lenovo.instagramclone.Profile


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth

import com.yusufalicezik.lenovo.instagramclone.R


class SignOutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var alert=AlertDialog.Builder(activity!!)
                .setTitle("İnstagramdan çıkış yap")
                .setMessage("Emin misiniz?")
                .setPositiveButton("Çıkış Yap", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        FirebaseAuth.getInstance().signOut()
                        activity!!.finish() //activity kapansın

                    }

                })
                .setNegativeButton("İptal",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        dismiss()

                    }

                })
                .create()


        return alert
    }


}
