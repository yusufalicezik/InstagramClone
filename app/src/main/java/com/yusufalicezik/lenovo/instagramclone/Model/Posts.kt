package com.yusufalicezik.lenovo.instagramclone.Model

class Posts {

    var user_id:String?=null
    var post_id:String?=null
    var yuklenme_tarih:Long?=null
    var aciklama:String?=null
    var photo_url:String?=null


    constructor()
    constructor(user_id: String?, post_id: String?, yuklenme_tarih: Long?, aciklama: String?, photo_url: String?) {
        this.user_id = user_id
        this.post_id = post_id
        this.yuklenme_tarih = yuklenme_tarih
        this.aciklama = aciklama
        this.photo_url = photo_url
    }

    override fun toString(): String {
        return "Posts(user_id=$user_id, post_id=$post_id, yuklenme_tarih=$yuklenme_tarih, aciklama=$aciklama, photo_url=$photo_url)"
    }


}