package com.yusufalicezik.lenovo.instagramclone.Share


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.yusufalicezik.lenovo.instagramclone.Home.HomeActivity
import com.yusufalicezik.lenovo.instagramclone.Model.Posts
import com.yusufalicezik.lenovo.instagramclone.Profile.YukleniyorFragment

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.DosyaIslemleri
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import com.yusufalicezik.lenovo.instagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import kotlinx.android.synthetic.main.fragment_yukleniyor.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception


class ShareNextFragment : Fragment() {

    var secilenDosyaYolu:String?=null
    lateinit var photoURI:Uri

    var dosyaTuruResimMi:Boolean?=null

    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser
    lateinit var mStorageReference: StorageReference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_share_next, container, false)

        UniversalImageLoader.setImage(secilenDosyaYolu!!,view.imgSecilenResim,null,"file://")

       // photoURI=Uri.parse("file://"+secilenDosyaYolu!!)


        mAuth=FirebaseAuth.getInstance()
        mUser=mAuth.currentUser!!
        mRef=FirebaseDatabase.getInstance().reference
        mStorageReference=FirebaseStorage.getInstance().reference


        view.tvNextButton.setOnClickListener {



            //resim dosyasını sıkıştır
            if(dosyaTuruResimMi==true)
            {

                DosyaIslemleri.compressResimDosya(this,secilenDosyaYolu)

            }
            //video dosyasını sıkıştır
            else if(dosyaTuruResimMi==false)
            {

                DosyaIslemleri.compressVideoDosya(this,secilenDosyaYolu)


            }




        }

        view.imgClose.setOnClickListener {

            this.activity!!.onBackPressed()

        }



        return view
    }

    private fun veriTabaninaBilgileriYaz(yuklenenFotoURL: String) {

        var postID=mRef.child("posts").child(mUser.uid).push().key


        var yuklenenPost=Posts(mUser.uid,postID,0,etPostAciklama.text.toString(),yuklenenFotoURL)


        mRef.child("posts").child(mUser.uid).child(postID).setValue(yuklenenPost)
        mRef.child("posts").child(mUser.uid).child(postID).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP)


        //gönderi açıklamasını yorum düğümüne ekleme
        if(!etPostAciklama.text.toString().isNullOrEmpty())
        {

            mRef.child("comments").child(postID).child(postID).child("user_id").setValue(mUser.uid)
            mRef.child("comments").child(postID).child(postID).child("yorum_tarih").setValue(ServerValue.TIMESTAMP)
            mRef.child("comments").child(postID).child(postID).child("yorum").setValue(etPostAciklama.text.toString())
            mRef.child("comments").child(postID).child(postID).child("yorum_begeni").setValue(0)

        }

        var intent= Intent(activity!!,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        activity!!.finish()

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
    internal fun onSecilenDosyaEvent(SecilenResim: EventBusDataEvents.PaylasilacakResmiGonder) //hangi yayını beklediği parametreden belli olur
    {
       secilenDosyaYolu=SecilenResim!!.dosyaYolu!!
        dosyaTuruResimMi=SecilenResim!!.dosyaTuruResimMi!!
    }

    fun uploadStorage(filePath: String?) {


        var fileUri=Uri.parse("file://"+filePath)

        var dialogYukleniyor=YukleniyorFragment()
        dialogYukleniyor.show(activity!!.supportFragmentManager,"yukleniyorFragmenti")
        dialogYukleniyor.isCancelable=false

        //önce storega yükleyelim.
        var uploadTask=mStorageReference.child("users").child(mUser.uid).child(fileUri.lastPathSegment).putFile(fileUri)
                .addOnCompleteListener(object :OnCompleteListener<UploadTask.TaskSnapshot>{
                    override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                        if(p0.isSuccessful)
                        {

                            dialogYukleniyor.dismiss()
                            veriTabaninaBilgileriYaz(p0.getResult().downloadUrl.toString())
                        }
                    }

                })
                .addOnFailureListener(object :OnFailureListener{
                    override fun onFailure(p0: Exception) {
                        dialogYukleniyor.dismiss()
                        Toast.makeText(activity,"Hata oluştu",+Toast.LENGTH_SHORT).show()
                    }

                })
                .addOnProgressListener(object :OnProgressListener<UploadTask.TaskSnapshot>{
                    override fun onProgress(p0: UploadTask.TaskSnapshot?) {

                        var progress= 100.0 *p0!!.bytesTransferred/p0!!.totalByteCount
                        dialogYukleniyor.tvBilgi.text="%"+progress.toInt().toString()+" tamamlandı"
                    }

                })




    }
}
