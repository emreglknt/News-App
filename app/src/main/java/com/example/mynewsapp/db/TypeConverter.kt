package com.example.mynewsapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import javax.xml.transform.Source

class TypeConverter {



    // databasedeki source objesini
    // room un tanıyabileceği bir formata convert eder.


    //source sınıfında iki adet değişken var : id ve name .
    // id ile işimiz yok
    // bu yüzden sadece name değişkenini alıp string türüne çevrdik.
    @TypeConverter
    fun fromSource(source: com.example.mynewsapp.model.Source):String{
        return source.name!!
    }



    // bu da tekrar source objesini oluşturur.
    //ancak bu sefer id yerine iki kere name yazarak. Dönüştürdüğümüz objeyi veriririz.
    @TypeConverter
    fun toSource(name:String):com.example.mynewsapp.model.Source{
        return com.example.mynewsapp.model.Source(name,name)
    }

}