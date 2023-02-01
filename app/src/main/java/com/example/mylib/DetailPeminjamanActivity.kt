package com.example.mylib

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mylib.databinding.ActivityDetailPeminjamanBinding
import com.example.mylib.model.Buku
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_peminjaman.*
import java.sql.Date
import java.text.SimpleDateFormat

class DetailPeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPeminjamanBinding
    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null
    private var list = mutableListOf<Buku>()

    var key=""
    var judul =""
    var tanggal_peminjaman =""
    var tanggal_pengembalian =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPeminjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        key = intent.getStringExtra("id").toString()
        judul = intent.getStringExtra("judul").toString()
        tanggal_peminjaman = intent.getStringExtra("tanggal_peminjaman").toString()
        tanggal_pengembalian = intent.getStringExtra("tanggal_pengembalian").toString()
        //Toast.makeText(this,"dapet ga? ${key}", Toast.LENGTH_SHORT).show()


        val sdf = SimpleDateFormat("dd MMM yyyy")
        val resultdate = Date(tanggal_peminjaman.toLong())


        dpjudulbuku.text = judul.toString()
        dptanggalpeminjaman.text = sdf.format(resultdate).toString()
        dptanggalpengembalian.text = tanggal_pengembalian.toString()


        binding.btnajukan.setOnClickListener{
            ajukanData()
        }
    }

    private fun ajukanData() {

        // dia malah membuat row baru pada database
        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("meminjam")
        databaseReference?.child(key)?.child("perpanjangan")?.get()?.addOnSuccessListener {
            if (it.value!!.equals("true")){
                Toast.makeText(applicationContext, "${"tidak bisa update lagi"}", Toast.LENGTH_SHORT).show()
            }else{
                databaseReference?.child(key.toString())?.child("tanggal_pengembalian")?.get()?.addOnSuccessListener {
                    Toast.makeText(applicationContext, "${it.value}", Toast.LENGTH_SHORT).show()
                    // convert dulu ke bentuk long baru kita tambahkan dengan nilai 7hari kedepan
                    val num = java.lang.Long.valueOf(it.value.toString());
                    var tanggalbaru = num+604800000

                    Toast.makeText(applicationContext, "${tanggalbaru}", Toast.LENGTH_SHORT).show()
                    databaseReference?.child(key.toString())?.child("tanggal_pengembalian")?.setValue(tanggalbaru)
                    databaseReference?.child(key.toString())?.child("perpanjangan")?.setValue("true")
                }
            }
        }
    }
}