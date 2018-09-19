package com.yusufalicezik.lenovo.instagramclone.Home

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.nostra13.universalimageloader.core.ImageLoader
import com.yusufalicezik.lenovo.instagramclone.Login.LoginActivity
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.BottomNavigationViewHelper
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import com.yusufalicezik.lenovo.instagramclone.utils.HomePagerAdapter
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    private val ACTIVITY_NO=0
    private val TAG="HomeActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()

        setupAuthListener()
        initUnivImageLoader()

        setupHomeViewPager()







    }



    private fun kameraIzniIste() {
        Dexter.withActivity(this) //bu aktivitide izin istenecek
                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, //toplu izin 3 tane
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener { //listener ekledik onay mı red mi edildi izin diye
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if(report!!.areAllPermissionsGranted())
                        {
                            EventBus.getDefault().postSticky(EventBusDataEvents.KameraIzinBilgisiGonder(true)) //kullanıcı bana tüm izinleri vermişse eğer devam ederim.
                        }
                        if(report!!.isAnyPermissionPermanentlyDenied)//herhangi bir izni bana bidaha sorma demişse
                        {
                            Toast.makeText(this@HomeActivity,"Lütfen ayarlardan izni manuel olarak verin", Toast.LENGTH_SHORT).show()



                            var builder= AlertDialog.Builder(this@HomeActivity)
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
                                    homeViewPager.setCurrentItem(1)
                                    finish()


                                }

                            })
                            builder.show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        //izinlerden biri reddedilip, ikna etmek için tekrar sorduk
                        //buraya her girdiğimizde sormak için(kabul edilene kadar.)

                        var builder= AlertDialog.Builder(this@HomeActivity)
                        builder.setTitle("İzin gerekli")
                        builder.setMessage("Uygulamanın çalışabilmesi için izin vermeniz gerekiyor. Onaylıyor musunuz?")
                        builder.setPositiveButton("Onay Ver", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.cancel()
                                token!!.continuePermissionRequest()
                                homeViewPager.setCurrentItem(1)

                            }

                        })
                        builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.cancel()
                                token!!.cancelPermissionRequest()
                                homeViewPager.setCurrentItem(1)


                            }

                        })
                        builder.show()


                    }

                })

                .withErrorListener(object : PermissionRequestErrorListener { //olurda bi hata çıkarsa diye
                    override fun onError(error: DexterError?) {

                        Toast.makeText(this@HomeActivity,"İzin alırken hata oluştu", Toast.LENGTH_SHORT).show()

                    }

                })
                .check()

    }



    private fun setupHomeViewPager() {
       var homePagerAdapter=HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.addFragment(CameraFragment()) // id=0
        homePagerAdapter.addFragment(HomeFragment())    //id=1
        homePagerAdapter.addFragment(MessagesFragment()) //id=2

        //activitymain de bulunan viewpager a oluşturdugumuz adaptörü atadık.
        homeViewPager.adapter=homePagerAdapter

        //burda da viewpager ın homefragment ile başlamasını sağladık.
        homeViewPager.setCurrentItem(1)



        homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
        homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)
        //baslangicta 0 ve 2 yi silip anasayfada baslatsın



        /////////////777statusbar için.. listener ekledik. her sayfa kaydığında position a göre işlemler yaptık
        homeViewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if(position==0) //eğer kamera fragmenti açıksa fullscreen olsun.
                {
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,1)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)
                    kameraIzniIste()
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,0)


                }
                if(position==1) //eğer home fragment açıksa
                {
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,1)
                }
                if(position==2) //eğer mesaj fragmenti açıksa
                {
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,1)
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,2)
                }
            }

        })
        /////////////

    }


    private fun initUnivImageLoader()
    {
        var universalImageLoader= UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config) //Universal class ında yaptıgımız konfigurasyon ayarlarını çektik.
    }


    private fun setupAuthListener() {
        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                //her giriş çıkışta tetiklenir.

                var user=FirebaseAuth.getInstance().currentUser
                if(user==null)
                {
                    var intent= Intent(applicationContext,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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

    override fun onBackPressed() {

        if(homeViewPager.currentItem==1)
        {

            homeViewPager.visibility=View.VISIBLE
            homeFragmentContainer.visibility=View.GONE
            super.onBackPressed()

        }else
        {
            homeViewPager.visibility=View.VISIBLE
            homeFragmentContainer.visibility=View.GONE
            homeViewPager.setCurrentItem(1)
        }



    }
}
