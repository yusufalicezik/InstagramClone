package com.yusufalicezik.lenovo.instagramclone.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.flags.Flag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.yusufalicezik.lenovo.instagramclone.Login.LoginActivity
import com.yusufalicezik.lenovo.instagramclone.Model.Users
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO=4
    private val TAG="ProfileActivity"
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef:DatabaseReference
    lateinit var mUser:FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef=FirebaseDatabase.getInstance().reference
        mUser=mAuth!!.currentUser!!

        setupToolbar()
        kullaniciBilgileriniGetir()
        //setupProfilePhoto()



    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()

    }
    private fun kullaniciBilgileriniGetir() {

        tvProfilDuzenleButon.isEnabled=false
        imgProfilSettings.isEnabled=false

        mRef.child("users").child(mUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                //for a gerek yok tek bir kullanıcı düğümünü geziyorum.
                if (p0.getValue()!=null) {
                    var okunanKullaniciVerileri = p0!!.getValue(Users::class.java) //detaylar vs hepsi gelmiş oldu. detaylarda zaten users ın içinde.

                    EventBus.getDefault().postSticky(EventBusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciVerileri))
                    //yayınımızı yaptık, profile settings iiçin yeniden okumak yerine okunanları gönderdik.
                    tvProfilDuzenleButon.isEnabled=true
                    imgProfilSettings.isEnabled=true

                    tvProfilAdiToolbar.setText(okunanKullaniciVerileri!!.user_name.toString())
                    tvFollowerSayisi.setText(okunanKullaniciVerileri!!.user_detail!!.follower.toString())
                    tvFollowingSayisi.setText(okunanKullaniciVerileri!!.user_detail!!.folowing.toString())
                    tvProfileGercekAd.setText(okunanKullaniciVerileri!!.adi_soyadi.toString())
                    tvPostSayisi.setText(okunanKullaniciVerileri!!.user_detail!!.post.toString())
                    var imgUrl=okunanKullaniciVerileri!!.user_detail!!.profile_picture.toString()
                    UniversalImageLoader.setImage(imgUrl,circleProfileImage,progressBar,"")
                    if(!okunanKullaniciVerileri!!.user_detail!!.biography.isNullOrEmpty())
                    {
                        tvBiyografi.visibility=View.VISIBLE
                        tvBiyografi.setText(okunanKullaniciVerileri!!.user_detail!!.biography.toString())
                    }
                    if(!okunanKullaniciVerileri!!.user_detail!!.website.isNullOrEmpty())
                    {
                        tvWebsitesi.visibility=View.VISIBLE
                        tvWebsitesi.setText(okunanKullaniciVerileri!!.user_detail!!.website.toString())
                    }


                }
            }

        })


    }

 /*   private fun setupProfilePhoto() {
        var imgUri="images.pexels.com/photos/1213447/pexels-photo-1213447.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        UniversalImageLoader.setImage(imgUri,circleProfileImage,progressBar,"https://")
    }
*/
    private fun setupToolbar() {
        imgProfilSettings.setOnClickListener {

            var intent=Intent(this,ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }


        tvProfilDuzenleButon.setOnClickListener {
            profileRoot.visibility= View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer,ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()
        }
    }


    fun setupNavigationView()
    {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        profileRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }

    private fun setupAuthListener() {

        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                //her giriş çıkışta tetiklenir.

                var user=FirebaseAuth.getInstance().currentUser
                if(user==null)
                {
                    var intent= Intent(applicationContext, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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
