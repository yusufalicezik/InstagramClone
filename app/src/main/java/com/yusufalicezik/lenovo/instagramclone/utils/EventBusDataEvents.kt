package com.yusufalicezik.lenovo.instagramclone.utils

import com.yusufalicezik.lenovo.instagramclone.Model.Users

class EventBusDataEvents{

    internal class KayitBilgileriniGonder(var telNo:String?, var eMail:String?, var verificationID:String?, var fCode:String?, var emailKayit:Boolean)

    internal class KullaniciBilgileriniGonder(var kullanici:Users?)

    internal class PaylasilacakResmiGonder(var dosyaYolu:String?,var dosyaTuruResimMi:Boolean?)

    internal class GaleriSecilenDosyaYolunuGonder(var dosyaYolu: String?)

    internal class KameraIzinBilgisiGonder(var kameraIzniVerildiMi:Boolean?)


}
