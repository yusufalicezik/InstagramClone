package com.yusufalicezik.lenovo.instagramclone.Model

class Users {

    var email: String? = null
    var password: String? = null
    var user_name: String? = null
    var adi_soyadi: String? = null
    var phone_number: String? = null
    var email_phone_number: String? = null
    var user_id:String?=null
    var user_detail:UserDetails?=null

    constructor() {}

    constructor(email: String?, password: String?, user_name: String?, adi_soyadi: String?, phone_number: String?, email_phone_number: String?, user_id: String?, user_detail: UserDetails?) {
        this.email = email
        this.password = password
        this.user_name = user_name
        this.adi_soyadi = adi_soyadi
        this.phone_number = phone_number
        this.email_phone_number = email_phone_number
        this.user_id = user_id
        this.user_detail = user_detail
    }

    override fun toString(): String {
        return "Users(email=$email, password=$password, user_name=$user_name, adi_soyadi=$adi_soyadi, phone_number=$phone_number, email_phone_number=$email_phone_number, user_id=$user_id, user_detail=$user_detail)"
    }


}
