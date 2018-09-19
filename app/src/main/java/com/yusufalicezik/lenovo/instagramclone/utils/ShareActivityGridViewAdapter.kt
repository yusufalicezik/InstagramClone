package com.yusufalicezik.lenovo.instagramclone.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.yusufalicezik.lenovo.instagramclone.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*

class ShareActivityGridViewAdapter(context: Context?, resource: Int, var klasordekiDosyalar: ArrayList<String>?) : ArrayAdapter<String>(context, resource, klasordekiDosyalar) {



     var inflater:LayoutInflater
    lateinit var viewHolder:ViewHolder
     var tekSutunResim:View?=null


    init { //ne zman sınıf oluşturursak o zaman çalısır.
        inflater=LayoutInflater.from(context)
    }


    inner class ViewHolder(){
        lateinit var imageView:GridImageView
        lateinit var progressBar:ProgressBar
        lateinit var tvSure:TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        tekSutunResim=convertView

        if(tekSutunResim==null)
        {

            tekSutunResim=inflater.inflate(R.layout.tek_sutun_grid_resim,parent,false)
            viewHolder=ViewHolder()

            viewHolder.imageView=tekSutunResim!!.imgTekSutunImage
            viewHolder.progressBar=tekSutunResim!!.progressBar
            viewHolder.tvSure=tekSutunResim!!.tvSure

            tekSutunResim!!.setTag(viewHolder)

        }else
        {
            viewHolder= tekSutunResim!!.getTag() as ViewHolder
        }

        //o an oluşturulan dosya
        var dosyaYolu=klasordekiDosyalar!!.get(position)
        var dosyaTuru=dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        if(dosyaTuru!=null) {
            if (dosyaTuru.equals(".mp4")) {

                viewHolder.tvSure.visibility=View.VISIBLE
                var retriever=MediaMetadataRetriever() //video süresi için
                retriever.setDataSource(context, Uri.parse("file://"+dosyaYolu))

                var videoSuresi=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) //milisaniye türünden süreyi aldık.
                var videoSuresiLong=videoSuresi.toLong()


                viewHolder.tvSure.setText(convertDuration(videoSuresiLong))
                UniversalImageLoader.setImage(klasordekiDosyalar!!.get(position),viewHolder.imageView,viewHolder.progressBar,"file:/")

            }
            else
            {
                viewHolder.tvSure.visibility=View.GONE
                UniversalImageLoader.setImage(klasordekiDosyalar!!.get(position),viewHolder.imageView,viewHolder.progressBar,"file:/")

            }

        }




        return tekSutunResim!!
    }

    fun convertDuration(duration:Long):String
    {

        val second=duration/1000%60
        val minute=duration/(1000*60)%60
        val hour=duration/(1000*60*60)%24

        var time=""
        if(hour>0)
        {
            time=String.format("%02d:%02d:%02d",hour,minute,second)
        }else
        {
            time= String.format("%02d:%02d",minute,second)
        }

        return time

    }

}