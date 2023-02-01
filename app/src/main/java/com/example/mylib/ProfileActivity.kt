package com.example.mylib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mylib.databinding.ActivityAkunBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_akun.*

class ProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAkunBinding
    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var noinduk = intent.getStringExtra("no_induk").toString()
        var nama = ""
        var email = ""

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("ap")
        databaseReference?.child(noinduk)?.get()?.addOnSuccessListener {

            nama = it.child("nama_lengkap").value.toString()
            email = it.child("email").value.toString()
            noinduk = it.child("no_induk").value.toString()

            binding.txnama.text = nama
            binding.txemail.text = email
            binding.txnoinduk.text = noinduk

        }

        binding.btnedit.setOnClickListener{
            val intent = Intent(applicationContext, EditProfile::class.java).apply {
                putExtra("no_induk",noinduk)
            }
            startActivity(intent)
            true
        }
    }
}