package com.yusufalicezik.lenovo.instagramclone.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.yusufalicezik.lenovo.instagramclone.R
import java.net.URL

class UniversalImageLoader(val mContext:Context) {


    val config:ImageLoaderConfiguration
        get() {

            val defaultOptions=DisplayImageOptions.Builder()
                    .showImageOnLoading(defaultImage)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(FadeInBitmapDisplayer(400))
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build()


            return ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(WeakMemoryCache())
                    .diskCacheSize(100*1024*1024).build()
        }


    companion object {
        private val defaultImage= R.drawable.ic_profile

        fun setImage(imgUrl:String, imageView:ImageView,mProgressbar:ProgressBar?, ilkKisim:String)
        {
            val imageLoader=ImageLoader.getInstance()
            imageLoader.displayImage(ilkKisim+imgUrl,imageView,object :ImageLoadingListener{
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {

                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }

                }

                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.VISIBLE

                    }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }
                }

                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }
                }
            })
        }
    }

}