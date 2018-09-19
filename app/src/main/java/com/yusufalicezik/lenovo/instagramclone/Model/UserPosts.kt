package com.yusufalicezik.lenovo.instagramclone.Model

class UserPosts {


    var post_id:String?=null
    var post_aciklama:String?=null
    var post_url:String?=null
    var user_id:String?=null
    var userName:String?=null
    var user_photo_url:String?=null
    var post_yuklenme_tarih:Long?=null

    constructor(post_id: String?, post_aciklama: String?, post_url: String?, user_id: String?, userName: String?, user_photo_url: String?, post_yuklenme_tarih: Long?) {
        this.post_id = post_id
        this.post_aciklama = post_aciklama
        this.post_url = post_url
        this.user_id = user_id
        this.userName = userName
        this.user_photo_url = user_photo_url
        this.post_yuklenme_tarih = post_yuklenme_tarih
    }

    constructor()
    {

    }

    override fun toString(): String {
        return "UserPosts(post_id=$post_id, post_aciklama=$post_aciklama, post_url=$post_url, user_id=$user_id, userName=$userName, user_photo_url=$user_photo_url, post_yuklenme_tarih=$post_yuklenme_tarih)"
    }


}