package com.yusufalicezik.lenovo.instagramclone.Share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_share.*

import kotlinx.android.synthetic.main.fragment_share_video.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File


class ShareVideoFragment : Fragment() {


     var videoView: CameraView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_share_video, container, false)

        videoView=view.videoView


        var olusacakVideoDosyaAdi=System.currentTimeMillis()

        var olusacakVideoDosya=File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Camera/"+olusacakVideoDosyaAdi+".mp4")


        videoView!!.addCameraListener(object : CameraListener(){

            override fun onVideoTaken(video: File?) {
                super.onVideoTaken(video)

                activity!!.anaLayout.visibility= View.GONE
                activity!!.fragmentContainerLayout.visibility=View.VISIBLE
                var transaction=activity!!.supportFragmentManager.beginTransaction()


                var secilenResimYolu=video!!.absolutePath.toString()
                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(secilenResimYolu,false))

                transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()


            }

        })


        view.imgVideoCek.setOnTouchListener(object :View.OnTouchListener{  //video uzun tıklama için onClik yerine bunu kullandık.
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if(event!!.action==MotionEvent.ACTION_DOWN) //parmağım basılı oldugu sürece
                {
                    videoView!!.startCapturingVideo(olusacakVideoDosya)
                    Toast.makeText(activity,"Video kaydediliyor",Toast.LENGTH_SHORT).show()

                    return true
                }
                else if(event!!.action==MotionEvent.ACTION_UP) //parmağımı kaldırdıgım anda
                {

                    videoView!!.stopCapturingVideo()
                    Toast.makeText(activity,"Video kaydedildi",Toast.LENGTH_SHORT).show()

                    return true
                }

                return false

            }

        })


        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        videoView!!.start();
    }

    override fun onPause() {
        super.onPause()
        videoView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(videoView!=null)
        {
            videoView!!.destroy()
        }

    }





}
