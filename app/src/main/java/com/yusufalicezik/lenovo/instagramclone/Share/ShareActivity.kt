package com.yusufalicezik.lenovo.instagramclone.Share

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yusufalicezik.lenovo.instagramclone.Login.LoginActivity
import com.yusufalicezik.lenovo.instagramclone.R

import com.yusufalicezik.lenovo.instagramclone.utils.SharePagerAdapter

import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    private val ACTIVITY_NO=2
    private val TAG="ShareActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthListener()

        storegaVeKameraIzniIste()

       // setupShareViewPager()

    }



    private fun storegaVeKameraIzniIste() {
        Dexter.withActivity(this) //bu aktivitide izin istenecek
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, //toplu izin 3 tane
                                 Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 Manifest.permission.CAMERA,
                                 Manifest.permission.RECORD_AUDIO)
                .withListener(object : MultiplePermissionsListener{ //listener ekledik onay mı red mi edildi izin diye
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if(report!!.areAllPermissionsGranted())
                        {

                            setupShareViewPager() //kullanıcı bana tüm izinleri vermişse eğer devam ederim.
                        }
                        if(report!!.isAnyPermissionPermanentlyDenied)//herhangi bir izni bana bidaha sorma demişse
                        {
                            Toast.makeText(this@ShareActivity,"Lütfen ayarlardan izni manuel olarak verin",Toast.LENGTH_SHORT).show()

                            var builder= AlertDialog.Builder(this@ShareActivity)
                            builder.setTitle("İzin gerekli")
                            builder.setMessage("Ayarlar'dan uygulamaya izin vermeniz gerekiyor. Onaylıyor musunuz?")
                            builder.setPositiveButton("Ayarlara git", object : DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.cancel()
                                    var intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    var uri= Uri.fromParts("package",packageName,null)
                                    intent.setData(uri)
                                    startActivity(intent)
                                    finish()

                                }

                            })
                            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener{
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.cancel()
                                    finish()

                                }

                            })
                            builder.show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        //izinlerden biri reddedilip, ikna etmek için tekrar sorduk
                        //buraya her girdiğimizde sormak için(kabul edilene kadar.)

                        var builder= AlertDialog.Builder(this@ShareActivity)
                        builder.setTitle("İzin gerekli")
                        builder.setMessage("Uygulamanın çalışabilmesi için izin vermeniz gerekiyor. Onaylıyor musunuz?")
                        builder.setPositiveButton("Onay Ver", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.cancel()
                                token!!.continuePermissionRequest()

                            }

                        })
                        builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.cancel()
                                token!!.cancelPermissionRequest()
                                finish()

                            }

                        })
                        builder.show()


                    }

                })

                .withErrorListener(object :PermissionRequestErrorListener{ //olurda bi hata çıkarsa diye
                    override fun onError(error: DexterError?) {

                        Toast.makeText(this@ShareActivity,"İzin alırken hata oluştu",Toast.LENGTH_SHORT).show()

                    }

                })
                .check()

    }

    private fun setupShareViewPager() {

        var tabAdlari:ArrayList<String> = ArrayList()

        tabAdlari.add("GALERİ")
        tabAdlari.add("FOTOĞRAF")
        tabAdlari.add("VİDEO")


        var sharePagerAdapter=SharePagerAdapter(supportFragmentManager,tabAdlari)
        sharePagerAdapter.addFragment(ShareGalleryFragment())
        sharePagerAdapter.addFragment(ShareCameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())


        shareViewPager.adapter=sharePagerAdapter

        shareViewPager.offscreenPageLimit=1 //default olarakta zaten 1. bir önceki ve bir sonraki fragment i açar otomatik.

        sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,1)
        sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,2)


        shareViewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                //ilgili fragmentteysen eğer:
                if(position==0) //eğer galeri açıksa
                {
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,1)
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,2)
                    sharePagerAdapter.secilenFragmentiViewPageraEkle(shareViewPager,0)
                }
                if(position==1) //eğer camera açıksa
                {
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,0)
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,2)
                    sharePagerAdapter.secilenFragmentiViewPageraEkle(shareViewPager,1)
                }
                if(position==2) //eğer video açıksa
                {
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,0)
                    sharePagerAdapter.secilenFragmentiViewPagerdanSil(shareViewPager,1)
                    sharePagerAdapter.secilenFragmentiViewPageraEkle(shareViewPager,2)

                }
            }


        })



        shareTabLayout.setupWithViewPager(shareViewPager) //tablar neye göre değişsin? benim viewpager ıma göre
    }

    override fun onBackPressed() {

        anaLayout.visibility=View.VISIBLE
        fragmentContainerLayout.visibility=View.GONE
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

}
