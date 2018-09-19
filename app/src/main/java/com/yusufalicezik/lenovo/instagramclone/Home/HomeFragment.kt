package com.yusufalicezik.lenovo.instagramclone.Home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.yusufalicezik.lenovo.instagramclone.Login.LoginActivity
import com.yusufalicezik.lenovo.instagramclone.Model.Posts
import com.yusufalicezik.lenovo.instagramclone.Model.UserDetails
import com.yusufalicezik.lenovo.instagramclone.Model.UserPosts
import com.yusufalicezik.lenovo.instagramclone.Model.Users
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import com.yusufalicezik.lenovo.instagramclone.utils.HomeFragmentRecyclerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    lateinit var fragmentView:View
    private val ACTIVITY_NO=0


    lateinit var tumGonderiler:ArrayList<UserPosts>

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView=inflater?.inflate(R.layout.fragment_home,container,false)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef= FirebaseDatabase.getInstance().reference
        mUser=mAuth!!.currentUser!!

        tumGonderiler=ArrayList<UserPosts>()

        kullaniciPostariniGetir(mUser.uid)


        fragmentView.imgTabCamera.setOnClickListener {

            (activity!! as HomeActivity).homeViewPager.setCurrentItem(0)
        }

        fragmentView.imgTabDirectMessage.setOnClickListener {

            (activity!! as HomeActivity).homeViewPager.setCurrentItem(2)
        }


        return fragmentView
    }

    private fun kullaniciPostariniGetir(kullaniciID: String) {



        mRef.child("users").child(kullaniciID).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                var userID=kullaniciID
                var kullaniciAdi=p0!!.getValue(Users::class.java)!!.user_name
                var kullaniciFotoUrl=p0!!.getValue(Users::class.java)!!.user_detail!!.profile_picture

                //önce kullanıcı bilgilerini çektik. farklı thread de çalıştığı için bu işlem bittikten sonra posr verilerini çekiyoruz.
                mRef.child("posts").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        if(p0!!.hasChildren())
                        {
                            for(ds in p0!!.children)
                            {
                                var eklenecekUserPost=UserPosts()

                                eklenecekUserPost.user_id=userID
                                eklenecekUserPost.userName=kullaniciAdi
                                eklenecekUserPost.user_photo_url=kullaniciFotoUrl

                                eklenecekUserPost.post_id=ds.getValue(Posts::class.java)!!.post_id
                                eklenecekUserPost.post_url=ds.getValue(Posts::class.java)!!.photo_url
                                eklenecekUserPost.post_aciklama=ds.getValue(Posts::class.java)!!.aciklama
                                eklenecekUserPost.post_yuklenme_tarih=ds.getValue(Posts::class.java)!!.yuklenme_tarih

                                tumGonderiler.add(eklenecekUserPost)
                            }
                        }

                        setupRecyclerView()
                    }

                })


            }

        })




    }

    private fun setupRecyclerView() {
        var recyclerView=fragmentView.recyclerView
        var recyclerAdapter=HomeFragmentRecyclerAdapter(this.activity!!,tumGonderiler)

        recyclerView.adapter=recyclerAdapter

        recyclerView.layoutManager=LinearLayoutManager(this.activity,LinearLayoutManager.VERTICAL,false)
    }


    fun setupNavigationView()
    {

        var fragmentBottomNavigationView=fragmentView.bottomNavigationView
        BottomNavigationViewHelper.setupBottomNavigationView(fragmentBottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this.activity!!,fragmentBottomNavigationView)
        var menu=fragmentBottomNavigationView.menu
        var menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun setupAuthListener() {

        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                //her giriş çıkışta tetiklenir.

                var user=FirebaseAuth.getInstance().currentUser
                if(user==null)
                {
                    var intent= Intent(activity!!, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent)
                    activity!!.finish()
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

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }



}