package com.yusufalicezik.lenovo.instagramclone.Profile



import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nostra13.universalimageloader.core.ImageLoader
import com.yusufalicezik.lenovo.instagramclone.Model.Users

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception


class ProfileEditFragment : Fragment() {

    var circleImageView:CircleImageView?=null
    lateinit var gelenKullaniciBilgileri:Users
    val RESIM_SEC=100
    lateinit var mDatabaseRef:DatabaseReference
    var profilePhotoUri:Uri?=null
    lateinit var mStorageRef:StorageReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view=inflater.inflate(R.layout.fragment_profile_edit, container, false)

        mDatabaseRef=FirebaseDatabase.getInstance().reference
        mStorageRef=FirebaseStorage.getInstance().reference

        setupKullaniciBilgileri(view)
        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()

        }


        view.tvFotografiDuzenle.setOnClickListener {

            var intent= Intent()
            intent.setType("image/*") //tüm uzantıları göster
            intent.setAction(Intent.ACTION_PICK) //Seçme işlemi yapılacak
            startActivityForResult(intent,RESIM_SEC)

        }

        view.imgBtnDegisiklikleriKaydet.setOnClickListener {
            if (profilePhotoUri!=null)
            {

                var dialogYukleniyor=YukleniyorFragment()
                dialogYukleniyor.show(activity!!.supportFragmentManager,"yukleniyorFragmenti")

                dialogYukleniyor.isCancelable=false

                var uploadTask=mStorageRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child(profilePhotoUri!!.lastPathSegment).putFile(profilePhotoUri!!)
                        .addOnCompleteListener(object :OnCompleteListener<UploadTask.TaskSnapshot>{
                            override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                                if(p0!!.isSuccessful)
                                {

                                    mDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child("user_detail")
                                            .child("profile_picture").setValue(p0!!.getResult().downloadUrl.toString())

                                    dialogYukleniyor.dismiss()
                                    kullaniciAdiniGuncelle(view,true)
                                }
                            }

                        })
                        .addOnFailureListener(object :OnFailureListener{
                            override fun onFailure(p0: Exception) {
                                dialogYukleniyor.dismiss()
                                kullaniciAdiniGuncelle(view,false)
                            }

                        })

            }else
            {
                kullaniciAdiniGuncelle(view,null)
            }





        }


        return view
    }


    //profilresmiDegisti==true ise resim basarılı bi şekilde storage yüklenmiş ve veritabanına yazılmştır.
    //false ise resim yüklenirken hata oluşmuştur.
    //null ise kullanıcı resmini değiştirmek istememiştir.
    private fun kullaniciAdiniGuncelle(view: View, profilResmiDegisti:Boolean?) {
        if(!gelenKullaniciBilgileri!!.user_name.equals(view.etUsername.text.toString())) //kullanıcı adı için özel şart koştuk aynı olmasın diğerleri ile diye
        {
            mDatabaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var usernameKullanimdaMi=false

                    for(ds in p0.children)
                    {
                        var okunanKullaniciAdi=ds.getValue(Users::class.java)!!.user_name //yalnızca kullanıcı adını aldık.

                        if(okunanKullaniciAdi.equals(view.etUsername.text.toString())) {
                            usernameKullanimdaMi = true
                            profilBilgileriniGuncelle(view,profilResmiDegisti,false)
                            break //döngüden çıkar.
                        }
                    }
                    if(usernameKullanimdaMi==false)
                    {
                        mDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child("user_name").setValue(view.etUsername.text.toString())
                        profilBilgileriniGuncelle(view,profilResmiDegisti,true)
                    }


                }

            })
        }else
        {
            profilBilgileriniGuncelle(view,profilResmiDegisti,null)
        }
    }

    private fun profilBilgileriniGuncelle(view: View, profilResmiDegisti: Boolean?, usernameDegisti: Boolean?) {


        var profilGuncellendiMi:Boolean?=null

        if(!gelenKullaniciBilgileri!!.adi_soyadi.equals(view.etProfileName.text.toString()))
        {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child("adi_soyadi").setValue(view.etProfileName.text.toString())
            profilGuncellendiMi=true
        }
        if(!gelenKullaniciBilgileri!!.user_detail!!.biography.equals(view.etUserBio.text.toString()))
        {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child("user_detail").child("biography").setValue(view.etUserBio.text.toString())
            profilGuncellendiMi=true
        }
        if(!gelenKullaniciBilgileri!!.user_detail!!.website.equals(view.etUserWebsite.text.toString()))
        {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri!!.user_id!!).child("user_detail").child("website").setValue(view.etUserWebsite.text.toString())
            profilGuncellendiMi=true
        }

        if(profilResmiDegisti==null && usernameDegisti==null && profilGuncellendiMi==null)
        {
            Toast.makeText(activity!!,"Değişiklik Yok",Toast.LENGTH_SHORT).show()
        }
        else if(usernameDegisti==false && (profilGuncellendiMi==true || profilResmiDegisti==true))
        {
            Toast.makeText(activity!!,"Bilgiler güncellendi fakat bu kullanıcı adı KULLANIMDA",Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(activity!!,"Kullanıcı güncellendi",Toast.LENGTH_SHORT).show()
            activity!!.onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RESIM_SEC && resultCode==AppCompatActivity.RESULT_OK && data!!.data!=null)
        {
            profilePhotoUri=data!!.data
            circleProfileImage.setImageURI(profilePhotoUri)
        }
    }


    private fun setupKullaniciBilgileri(view: View?) { //fragment old. için view e ihtiyaç duyduk.

        view!!.etProfileName.setText(gelenKullaniciBilgileri!!.adi_soyadi)
        view!!.etUsername.setText(gelenKullaniciBilgileri!!.user_name)
        if(!gelenKullaniciBilgileri!!.user_detail!!.biography.isNullOrEmpty())
        {
            view!!.etUserBio.setText(gelenKullaniciBilgileri!!.user_detail!!.biography)
        }
        if(!gelenKullaniciBilgileri!!.user_detail!!.website.isNullOrEmpty())
        {
            view!!.etUserWebsite.setText(gelenKullaniciBilgileri!!.user_detail!!.website)
        }

        var imgUrl=gelenKullaniciBilgileri!!.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgUrl!!,view.circleProfileImage,view.progressBar,"") //fragment old. için view.circle, progresbar vs yaptık.

    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    //yayını yakalamak için.
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniciBilgileri: EventBusDataEvents.KullaniciBilgileriniGonder) //hangi yayını beklediği parametreden belli olur
    {
        gelenKullaniciBilgileri=kullaniciBilgileri!!.kullanici!!



    }

}
