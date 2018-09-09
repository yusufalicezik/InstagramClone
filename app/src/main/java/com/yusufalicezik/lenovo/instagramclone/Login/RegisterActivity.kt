package com.yusufalicezik.lenovo.instagramclone.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import com.yusufalicezik.lenovo.instagramclone.Model.Users

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener  {


    lateinit var manager:FragmentManager

    lateinit var mRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mRef=FirebaseDatabase.getInstance().reference

        //hata düzeltme
        manager=supportFragmentManager
        manager.addOnBackStackChangedListener(this)
        //

        init()
    }

    private fun init() {

        tvGirisYap.setOnClickListener {
            var intent= Intent(applicationContext,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }



        tvEposta.setOnClickListener {

            viewTelefon.visibility=View.INVISIBLE
            viewEposta.visibility=View.VISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType=InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            etGirisYontemi.setHint("E-posta")
            btnRegister.setBackgroundResource(R.drawable.register_button)
            btnRegister.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.Sonukmavi))
            btnRegister.isEnabled=false
        }

        tvTelefon.setOnClickListener {

            viewTelefon.visibility=View.VISIBLE
            viewEposta.visibility=View.INVISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.setHint("Telefon")
            etGirisYontemi.inputType=InputType.TYPE_CLASS_NUMBER
            btnRegister.setBackgroundResource(R.drawable.register_button)
            btnRegister.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.Sonukmavi))
            btnRegister.isEnabled=false


        }


        etGirisYontemi.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              if(p1+p2+p3>=10)
              {
                  btnRegister.isEnabled=true
                  btnRegister.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
                  btnRegister.setBackgroundResource(R.drawable.register_button_aktif)

              }
                else
              {
                  btnRegister.isEnabled=false
                  btnRegister.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.Sonukmavi))
                  btnRegister.setBackgroundResource(R.drawable.register_button)

              }

            }

        })


        btnRegister.setOnClickListener {

            if(etGirisYontemi.hint.toString().equals("Telefon"))
            {


                if(isValidTelefon(etGirisYontemi.text.toString())) {


                    var ceptelefonuKullanimdaMi=false

                    mRef.child("users").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if(p0.getValue()!=null)
                            {
                                for(user in p0!!.children)
                                {
                                    var okunacakKullanici=user.getValue(Users::class.java)
                                    if(okunacakKullanici!!.phone_number.equals(etGirisYontemi.text.toString()))
                                    {
                                        ceptelefonuKullanimdaMi=true
                                        Toast.makeText(applicationContext,"Bu telefon numarası zaten kullanımda",Toast.LENGTH_SHORT).show()
                                        break
                                    }
                                }

                                //breakle fordan çıkar kontrol yaparız.
                                if(ceptelefonuKullanimdaMi==false)
                                {
                                    loginRoot.visibility=View.GONE
                                    loginContainer.visibility=View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())
                                    transaction.addToBackStack("telefonKoduGirFragmentEklendi.")
                                    transaction.commit()

                                    //tıklayınca yayın yap.
                                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(etGirisYontemi.text.toString(), null, null, null, false))
                                }

                            }

                        }

                    })


                }
                else
                {
                    Toast.makeText(this,"Lütfen geçerli bir telefon numarası girin",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                if(isValidEmail(etGirisYontemi.text.toString()))
                {
                    var eMailKullanımdaMı=false

                    mRef.child("users").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if(p0.getValue()!=null) //yanii p0 ın altında veriler varsa
                            {
                                //tüm userları gezmek için
                                for(user in p0!!.children) //children dediğim her bir child bir user
                                {
                                    var okunanKullanici=user.getValue(Users::class.java)//okunan kullanıcıda mail, adsoyad sifre vs var.
                                    if(okunanKullanici!!.email.equals(etGirisYontemi.text.toString()))
                                    {
                                        Toast.makeText(applicationContext,"Bu e mail zaten kullanımda",Toast.LENGTH_SHORT).show()
                                        eMailKullanımdaMı=true
                                        break
                                    }
                                }

                                if(eMailKullanımdaMı==false)
                                {
                                    loginRoot.visibility=View.GONE
                                    loginContainer.visibility=View.VISIBLE
                                    var transaction=supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer,KayitFragment())
                                    transaction.addToBackStack("emailileGirFragmentEklendi.")
                                    transaction.commit()

                                    //tıklayınca yayın yap.
                                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(null,etGirisYontemi.text.toString(),null,null,true))
                                }
                            }

                        }

                    })


                }
                else
                {
                    Toast.makeText(this,"Lütfen geçerli bir email girin",Toast.LENGTH_SHORT).show()

                }

            }

        }

    }



    override fun onBackStackChanged() {

        //birden fazla fragmentte geri tusundaki olayı halletmek için.
        if(manager.backStackEntryCount==0)
        {
            loginRoot.visibility=View.VISIBLE

        }





    }


    fun isValidEmail(kontrolEdilecekMail:String):Boolean
    {
        if(kontrolEdilecekMail==null)
            return false

        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()
    }
    fun isValidTelefon(kontrolEdilecekTelefon:String):Boolean
    {
        if(kontrolEdilecekTelefon==null||(kontrolEdilecekTelefon.length>=14))
            return false

        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()
    }
}
