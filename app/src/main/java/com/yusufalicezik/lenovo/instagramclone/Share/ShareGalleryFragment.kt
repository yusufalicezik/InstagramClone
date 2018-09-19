package com.yusufalicezik.lenovo.instagramclone.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.yusufalicezik.lenovo.instagramclone.R
import com.yusufalicezik.lenovo.instagramclone.utils.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareGalleryFragment : Fragment() {


    var secilenDosyaYolu:String?=null
    var resimMi:Boolean?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view=inflater.inflate(R.layout.fragment_share_gallery, container, false)


        var klasorPath=ArrayList<String>()
        var klasorAdlari=ArrayList<String>()

        var root=Environment.getExternalStorageDirectory().path

        var kameraResimleri=root+"/DCIM/Camera"
        var indirilenResimler=root+"/Download"
        var whatsappResimleri=root+"WhatsApp/Media/WhatsApp Images"

        klasorPath.add(kameraResimleri)
        klasorPath.add(indirilenResimler)
        klasorPath.add(whatsappResimleri)

        klasorAdlari.add("Kamera")
        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("WhatsApp")

        var spinnerArrayAdapter=ArrayAdapter(activity,android.R.layout.simple_spinner_item,klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) //ok için

        view.spnKlasorAdlari.adapter=spinnerArrayAdapter

        //ilk açılma durumunda kategori seçilmesi için.
        view.spnKlasorAdlari.setSelection(0)


        view.spnKlasorAdlari.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                //setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPath.get(position)))

                setupRecyclerView(DosyaIslemleri.klasordekiDosyalariGetir((klasorPath.get(position))))


            }

        }




        view.tvNextButton.setOnClickListener {
            activity!!.anaLayout.visibility= View.GONE
            activity!!.fragmentContainerLayout.visibility=View.VISIBLE
            var transaction=activity!!.supportFragmentManager.beginTransaction()


            EventBus.getDefault().postSticky(EventBusDataEvents.PaylasilacakResmiGonder(secilenDosyaYolu,resimMi))

            videoView.stopPlayback()
            transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
            transaction.addToBackStack("shareNextFragmentEklendi")
            transaction.commit()




        }


        view.imgClose.setOnClickListener {

            activity!!.onBackPressed()
        }

        return view
    }

    private fun setupRecyclerView(klasordekiDosyalar: ArrayList<String>) {

        var recyclerViewAdapter=ShareGalleryRecyclerAdapter(klasordekiDosyalar,this.activity!!)
        recylerViewDosyalar.adapter=recyclerViewAdapter
        var layoutManager=GridLayoutManager(this.activity!!,4) //gridmiş gibi 4 sütunlu kullandık.
        recylerViewDosyalar.layoutManager=layoutManager!!

        //hızlı çalışması için

        recylerViewDosyalar.setHasFixedSize(true)
        recylerViewDosyalar.setItemViewCacheSize(30)
        recylerViewDosyalar.setDrawingCacheEnabled(true)
        recylerViewDosyalar.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW)

        //




        //ilk başlangıçta çalışması için
        if(klasordekiDosyalar.size>0)
        {
             secilenDosyaYolu=klasordekiDosyalar.get(0)
            resimVeyaVideoGoster(secilenDosyaYolu!!)
        }

    }

    /*   fun setupGridView(secilenKlasordekiDosyalar:ArrayList<String>)
    {

        var gridAdapter=ShareActivityGridViewAdapter(activity!!,R.layout.tek_sutun_grid_resim,secilenKlasordekiDosyalar)

        recylerViewDosyalar.adapter=gridAdapter

        //ilk başlangıçta çalışması için
        if(secilenKlasordekiDosyalar.size>0)
        {
            resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))
            secilenDosyaYolu=secilenKlasordekiDosyalar.get(0)
        }


        recylerViewDosyalar.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(p2))
                secilenDosyaYolu=secilenKlasordekiDosyalar.get(p2)
            }

        })

    }
    */

    private fun resimVeyaVideoGoster(dosyaYolu: String) {

        var dosyaTuru=dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if(dosyaTuru!=null)
        {
            if(dosyaTuru.equals(".mp4"))
            {
               resimMi=false
                videoView.visibility=View.VISIBLE
                imgCropView.visibility=View.GONE
                videoView.setVideoURI(Uri.parse("file://"+dosyaYolu))
                videoView.start()
            }
            else
            {
                resimMi=true
                videoView.visibility=View.GONE
                imgCropView.visibility=View.VISIBLE
                UniversalImageLoader.setImage(dosyaYolu,imgCropView,null,"file://")
            }
        }

    }


    //yayını yakalamak için.
    @Subscribe
    internal fun onSecilenDosyaEvent(SecilenDosyaYolu: EventBusDataEvents.GaleriSecilenDosyaYolunuGonder) //hangi yayını beklediği parametreden belli olur
    {
        secilenDosyaYolu=SecilenDosyaYolu!!.dosyaYolu!!

        resimVeyaVideoGoster(secilenDosyaYolu!!)

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}
