package com.yusufalicezik.lenovo.instagramclone.utils

import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.Fragment
import com.iceteck.silicompressorr.SiliCompressor
import com.yusufalicezik.lenovo.instagramclone.Profile.YukleniyorFragment
import com.yusufalicezik.lenovo.instagramclone.Share.ShareNextFragment
import java.io.File
import java.util.*
import kotlin.Comparator

class DosyaIslemleri {

    companion object {

        fun klasordekiDosyalariGetir(klasorAdi:String):ArrayList<String>
        {

            var tumDosyalar=ArrayList<String>()


            var file=File(klasorAdi)

            //parametre olarak gönderdiğimiz klasördeki tüm dosyalar alınır
            var klasordekiTumDosyalar=file.listFiles()

            //parametre olarak gönderdiğimiz klasör yolunda eleman olup olmadıgı kontrol edildi.
            if(klasordekiTumDosyalar!=null)
            {

                //galeriden getirilen resimlerin tarihe göre güncelden eskiye göre listelenmesi
                if(klasordekiTumDosyalar.size>1)
                {
                    Arrays.sort(klasordekiTumDosyalar, object :Comparator<File>{
                        override fun compare(p0: File?, p1: File?): Int {

                            if(p0!!.lastModified()>p1!!.lastModified())
                            {
                                return -1
                            }
                            else
                            {
                                return 1
                            }

                        }


                    })
                }


             for (i in 0..klasordekiTumDosyalar.size-1)
             {
                 //sadece dosyalara bakılır
                 if(klasordekiTumDosyalar[i].isFile)
                 {
                     //okudugumuz dosyanın  telefondaki yeri ve adını içerir
                     var okunanDosyaYolu=klasordekiTumDosyalar[i].absolutePath

                     var dosyaTuru=okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf("."))
                     if(dosyaTuru.equals(".jpg") || dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".png") || dosyaTuru.equals(".mp4"))
                     {
                         tumDosyalar.add(okunanDosyaYolu)
                     }
                 }
             }
            }



            return tumDosyalar

        }

        fun compressResimDosya(fragment: Fragment, secilenResimYolu: String?) {


            ResimCompressAsyncTask(fragment).execute(secilenResimYolu)


        }

        fun compressVideoDosya(fragment: Fragment, secilenDosyaYolu: String?) {

            VideoCompressAsyncTask(fragment).execute(secilenDosyaYolu)
        }

    }

    internal class ResimCompressAsyncTask(fragment: Fragment):AsyncTask<String,String,String>()
    {


        var mFragment=fragment
        var compressFragment=YukleniyorFragment()
        override fun onPreExecute() {


            compressFragment.show(mFragment.activity!!.supportFragmentManager,"compressDialogBasladi")
            compressFragment.isCancelable=false
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {


            var yeniOlusanDosyaninKlasor=File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/compressed/")     //storage/0/gibi yol verir.
            var yeniDosyaYolu=SiliCompressor.with(mFragment.context).compress(params[0],yeniOlusanDosyaninKlasor)



            //dosyayı aldık sıkıştırıp klasöre koyduk. yeni yolu döndürürüz.
            //sıkıştırılmış olan dosya yolunu yani.
            return yeniDosyaYolu

        }

        override fun onPostExecute(filePath: String?) {


            compressFragment.dismiss()
            //aslında bu bir share fragmenttir.
            (mFragment as ShareNextFragment).uploadStorage(filePath)
            super.onPostExecute(filePath)

        }


    }

    internal class VideoCompressAsyncTask(fragment: Fragment):AsyncTask<String,String,String>()
    {
        var mFragment=fragment
        var compressDialog=YukleniyorFragment()



        override fun onPreExecute() {

            compressDialog.show(mFragment.activity!!.supportFragmentManager,"compressDialogBasladi")
            compressDialog.isCancelable=false
            super.onPreExecute()
        }
        override fun doInBackground(vararg p0: String?): String? {

            var yeniOlusanDosyaninKlasor=File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/compressedVideo/")


            //klasör var mı bakıcak(2 sinden birisi true ise:mkdirs demek önce DCIM bakacak sonra compressedVideo ya bakıcak her hangi birinde varsa true döner)
            if(yeniOlusanDosyaninKlasor.isDirectory || yeniOlusanDosyaninKlasor.mkdirs())
            {
                var yeniDosyaninPathi=SiliCompressor.with(mFragment.context).compressVideo(p0[0],yeniOlusanDosyaninKlasor.path)
                //eğer bu klasör zaten varsa direk sıkıştır oraya at..

                return yeniDosyaninPathi
            }

            return null



        }
        override fun onPostExecute(yeniDosyaninPathi: String?) {

            if(!yeniDosyaninPathi.isNullOrEmpty())
            {
                (mFragment as ShareNextFragment).uploadStorage(yeniDosyaninPathi)
                compressDialog.dismiss()
            }
            super.onPostExecute(yeniDosyaninPathi)
        }


    }

}