package com.yusufalicezik.lenovo.instagramclone.Login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import com.yusufalicezik.lenovo.instagramclone.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        init()
    }

    private fun init() {

        etSifre.addTextChangedListener(watcher)
        etEmailTelOrUsername.addTextChangedListener(watcher)

    }

    var watcher:TextWatcher=object:TextWatcher{
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if(etEmailTelOrUsername.text.toString().length>=6 && etSifre.text.toString().length>=6)
            {
                btnGirisYap.isEnabled=true
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)
                btnGirisYap.setTextColor(ContextCompat.getColor(applicationContext,R.color.beyaz))
            }
            else
            {
                btnGirisYap.isEnabled=false
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
                btnGirisYap.setTextColor(ContextCompat.getColor(applicationContext,R.color.Sonukmavi))
            }

        }


    }
}
