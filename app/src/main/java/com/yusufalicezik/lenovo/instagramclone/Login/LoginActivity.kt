package com.yusufalicezik.lenovo.instagramclone.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yusufalicezik.lenovo.instagramclone.Home.HomeActivity
import com.yusufalicezik.lenovo.instagramclone.Model.Users
import com.yusufalicezik.lenovo.instagramclone.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    lateinit var mRef: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupAuthListener()

        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference

        init()
    }


    private fun init() {

        etSifre.addTextChangedListener(watcher)
        etEmailTelOrUsername.addTextChangedListener(watcher)

        btnGirisYap.setOnClickListener {


            oturumAcacakKullaniciyiDenetle(etEmailTelOrUsername.text.toString(), etSifre.text.toString())


        }

        tvGirisYap.setOnClickListener {

            var intent=Intent(applicationContext,RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()

        }

    }

    private fun oturumAcacakKullaniciyiDenetle(eMailPhonenumberOrUsername: String, sifre: String) {

        var kullaniciBulundu=false



        //orderbychild verileri sıraladı. önce e mail boş olan, sonra e mail a ile başlayandan itibaren. diğeri ile aynı aslında sıralanışı farklı sadece
        mRef.child("users").orderByChild("email").addListenerForSingleValueEvent(object : ValueEventListener { //emailleri listele
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                for (ds in p0!!.children) {
                    var okunanKullanici = ds.getValue(Users::class.java)

                    if (okunanKullanici!!.email.toString().equals(eMailPhonenumberOrUsername)) {
                        oturumAc(okunanKullanici, sifre, false)
                        kullaniciBulundu=true
                        break
                    } else if (okunanKullanici!!.user_name.toString().equals(eMailPhonenumberOrUsername)) {
                        oturumAc(okunanKullanici, sifre, false)
                        kullaniciBulundu=true
                        break
                    } else if (okunanKullanici!!.phone_number.toString().equals(eMailPhonenumberOrUsername)) {
                        oturumAc(okunanKullanici, sifre, true)
                        kullaniciBulundu=true
                        break
                    }
                }

                if(kullaniciBulundu==false)
                    Toast.makeText(applicationContext,"E-posta,telefon numarası veya şifre yanlış",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun oturumAc(okunanKullanici: Users, sifre: String, telefonIleGiris: Boolean) {

        var girisYapacakEmail = ""

        if (telefonIleGiris == true) {
            girisYapacakEmail = okunanKullanici.email_phone_number.toString()
        } else {
            girisYapacakEmail = okunanKullanici.email.toString()
        }

        mAuth.signInWithEmailAndPassword(girisYapacakEmail, sifre)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0!!.isSuccessful) {
                            Toast.makeText(applicationContext, "Oturum açıldı.." + mAuth.currentUser!!.uid.toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Şifre hatalı", Toast.LENGTH_SHORT).show()

                        }

                    }

                })


    }

    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (etEmailTelOrUsername.text.toString().length >= 6 && etSifre.text.toString().length >= 6) {
                btnGirisYap.isEnabled = true
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)
                btnGirisYap.setTextColor(ContextCompat.getColor(applicationContext, R.color.beyaz))
            } else {
                btnGirisYap.isEnabled = false
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
                btnGirisYap.setTextColor(ContextCompat.getColor(applicationContext, R.color.Sonukmavi))
            }

        }


    }




    private fun setupAuthListener() {
        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                //her giriş çıkışta tetiklenir.

                var user=FirebaseAuth.getInstance().currentUser
                if(user!=null)
                {
                    var intent=Intent(applicationContext,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent)
                    finish()
                }else
                {

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener (mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener)
        }

    }
}
