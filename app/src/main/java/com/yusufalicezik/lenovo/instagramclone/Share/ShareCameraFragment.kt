package com.yusufalicezik.lenovo.instagramclone.Share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_camera.*
import kotlinx.android.synthetic.main.fragment_share_camera.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ShareCameraFragment : Fragment() {

     var cameraView: CameraView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_share_camera, container, false)

        cameraView=view.videoView

        cameraView!!.mapGesture(Gesture.PINCH,GestureAction.ZOOM)
        cameraView!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER); // Tap to focus!





        view.imgFotoCek.setOnClickListener {

            cameraView!!.capturePicture()
        }

        cameraView!!.addCameraListener(object : CameraListener(){

            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)


                var cekilenFotoAdi=System.currentTimeMillis()
                var cekilenFoto= File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Camera/"+cekilenFotoAdi+".jpg")

                var dosyaOlustur=FileOutputStream(cekilenFoto)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()
                //bytearray tipindeki dosyayı belirttiğimzi dizine ekledik dosya olarak.


                activity!!.anaLayout.visibility= View.GONE
                activity!!.fragmentContainerLayout.visibility=View.VISIBLE
                var transaction=activity!!.supportFragmentManager.beginTransaction()


                var secilenResimYolu=cekilenFoto.absolutePath.toString()
                EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(secilenResimYolu,true))


                transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
                transaction.addToBackStack("shareNextFragmentEklendi")
                transaction.commit()


            }

        })

        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cameraView!!.start()
    }

     override fun onPause() {
        super.onPause()
        cameraView!!.stop()
    }

     override fun onDestroy() {
        super.onDestroy()
        if(cameraView!=null)
        {
            cameraView!!.destroy()
        }
    }


}
