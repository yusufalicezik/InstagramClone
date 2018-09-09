package com.yusufalicezik.lenovo.instagramclone.Login


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit


class TelefonKoduGirFragment : Fragment(){


    var gelenTelNo=""
    lateinit var mCallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationID=""
    var gelenKod=""
    lateinit var progressBar:ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_telefon_kodu_gir, container, false)

        view.tvKullaniciTelNo.setText(gelenTelNo)

        progressBar=view.progressBarRegister

        ///////kod gönderimi için
         setupCallBack()



        view.btnTelKodIler.setOnClickListener {




            if(gelenKod.equals(view.etOnayKodu.text.toString())&&!view.etOnayKodu.text.toString().isEmpty())
            {
              //  Toast.makeText(activity,"ilerleme ok.",Toast.LENGTH_SHORT).show()

                EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(gelenTelNo,null,verificationID,gelenKod,false))

                var transaction=activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer,KayitFragment())
                transaction.addToBackStack("kayitFragmentEklendi")
                transaction.commit()
            }
            else {
                Toast.makeText(activity, "kod hatalı.", Toast.LENGTH_SHORT).show()

            }

        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                gelenTelNo,
                60, //60 saniye
                TimeUnit.SECONDS,
                this.activity!!,
                mCallbacks)







        return view
    }

    private fun setupCallBack() {
       mCallbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks()
       {
           override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
            if(!p0!!.smsCode.isNullOrEmpty())
                progressBar.visibility=View.INVISIBLE
            gelenKod= p0!!.smsCode!!

           }

           override fun onVerificationFailed(p0: FirebaseException?) {
               progressBar.visibility=View.INVISIBLE
           }

           override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
               verificationID= p0!!
               progressBar.visibility=View.VISIBLE

           }

       }
    }


    //yayını yakalamak için.
    @Subscribe (sticky = true)
    internal fun onTelefonNoEvent(kayitBilgileri:EventBusDataEvents.KayitBilgileriniGonder)
    {
        gelenTelNo= kayitBilgileri.telNo!!

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }




}
