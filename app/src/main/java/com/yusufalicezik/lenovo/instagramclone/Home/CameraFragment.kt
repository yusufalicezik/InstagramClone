package com.yusufalicezik.lenovo.instagramclone.Home

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.*
import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileOutputStream

class CameraFragment : Fragment() {

    var kameraIzniVerildiMi=false
    var mCamera: CameraView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater?.inflate(R.layout.fragment_camera,container,false)

        mCamera=view.cameraView


        mCamera!!.mapGesture(Gesture.PINCH,GestureAction.ZOOM)
        mCamera!!.mapGesture(Gesture.TAP,GestureAction.FOCUS_WITH_MARKER)


        mCamera!!.addCameraListener(object : CameraListener()
        {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)

                var cekilenFotoAdi=System.currentTimeMillis()
                var cekilenFoto= File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Camera/"+cekilenFotoAdi+".jpg")

                var dosyaOlustur= FileOutputStream(cekilenFoto)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()
                //bytearray tipindeki dosyayı belirttiğimzi dizine ekledik dosya olarak.


            }
        })

        view.imgCameraSwitch.setOnClickListener {

            if(mCamera!!.facing==Facing.BACK)
            {
                mCamera!!.facing=Facing.FRONT
            }
            else
            {
                mCamera!!.facing=Facing.BACK
            }
        }

        view.imgFotoCek.setOnClickListener {

            if(mCamera!!.facing==Facing.BACK)
            {
                mCamera!!.capturePicture()
            }
            else
            {
                mCamera!!.captureSnapshot() //ön kemara oldugu için metod farklı
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        if(kameraIzniVerildiMi==true)
            mCamera!!.start() //kamera kütüphanesi otomatik izin istiyordu. onu engelleyip kendi iznimizi kullanmak için. start etmeden önce kendi iznimzi soruyoruz. red se eğer kamera ktüphane izni başlamıyor.
    }

    override fun onPause() {
        super.onPause()
        mCamera!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mCamera!=null)
        {
            mCamera!!.destroy()
        }
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
    internal fun onKameraIzinEvent(izinDurumu: EventBusDataEvents.KameraIzinBilgisiGonder) //hangi yayını beklediği parametreden belli olur
    {
        kameraIzniVerildiMi=izinDurumu.kameraIzniVerildiMi!!
    }

}