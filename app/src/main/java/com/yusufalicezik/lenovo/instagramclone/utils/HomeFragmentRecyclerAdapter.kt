package com.yusufalicezik.lenovo.instagramclone.utils

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusufalicezik.lenovo.instagramclone.Generic.CommentFragment
import com.yusufalicezik.lenovo.instagramclone.Home.HomeActivity
import com.yusufalicezik.lenovo.instagramclone.Model.UserPosts
import com.yusufalicezik.lenovo.instagramclone.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import java.util.*
import kotlin.Comparator

class HomeFragmentRecyclerAdapter(var mContext:Context,var tumGonderiler:ArrayList<UserPosts>):RecyclerView.Adapter<HomeFragmentRecyclerAdapter.MyViewHolder>() {


    init {
        Collections.sort(tumGonderiler, object :Comparator<UserPosts>
        {
            override fun compare(p0: UserPosts?, p1: UserPosts?): Int {

                if(p0!!.post_yuklenme_tarih!!>p1!!.post_yuklenme_tarih!!)
                    return -1
                else
                    return 1
            }


        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var viewHolder=LayoutInflater.from(mContext).inflate(R.layout.tek_post_recycler_item,parent,false)

        return MyViewHolder(viewHolder,mContext)
    }

    override fun getItemCount(): Int {
        return tumGonderiler.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position,tumGonderiler.get(position))

    }




    class MyViewHolder(itemView: View?, mContextMyHomeActiivity: Context) : RecyclerView.ViewHolder(itemView) {


        var tumLayout=itemView as ConstraintLayout
        var profileImage=tumLayout.imgUserProfile
        var userNameTitle=tumLayout.tvKullaniciAdiBaslik
        var gonderi=tumLayout.imgPostResim
        var userName=tumLayout.tvKullaniciAdi
        var gonderiAciklama=tumLayout.tvPostAciklama
        var gonderiKacZamanOnce=tumLayout.tvKacZamanOnce

        var yorumYap=tumLayout.imgYorum


        var mHomeActivity=mContextMyHomeActiivity

        fun setData(position: Int, oAnkiGonderi: UserPosts) {

            userNameTitle.setText(oAnkiGonderi.userName)
            UniversalImageLoader.setImage(oAnkiGonderi.post_url!!,gonderi,null,"")
            userName.setText(oAnkiGonderi.userName!!)
            gonderiAciklama.setText(oAnkiGonderi.post_aciklama!!)
            UniversalImageLoader.setImage(oAnkiGonderi.user_photo_url!!,profileImage,null,"")

            gonderiKacZamanOnce.setText(TimeAgo.getTimeAgo(oAnkiGonderi.post_yuklenme_tarih!!))


            //tıklanılan postun yorumu açılağacağı için setdatanın içinde yaptık.
            yorumYap.setOnClickListener {

                (mHomeActivity as HomeActivity).homeViewPager.visibility=View.INVISIBLE
                (mHomeActivity as HomeActivity).homeFragmentContainer.visibility=View.VISIBLE

                var transaction=(mHomeActivity as HomeActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.homeFragmentContainer,CommentFragment())
                transaction.addToBackStack("commentFragmentEklendi")
                transaction.commit()

            }

        }
    }


}