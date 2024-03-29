package com.example.mylib

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylib.adapter.RiwayatAdapter
import com.example.mylib.databinding.FragmentRiwayatBinding
import com.example.mylib.model.Riwayat
import com.google.firebase.database.*

class RiwayatFragment : Fragment() {
    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null
    private var list = mutableListOf<Riwayat>()

    private var adapter: RiwayatAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("riwayat")
        getData()
    }

    private fun initRecyclerView() {
        adapter = RiwayatAdapter()
        binding.apply {
            recyclerView.layoutManager =  LinearLayoutManager(activity)
            recyclerView.adapter = adapter
        }
    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children){
                    val id = ds.key
                    val keterangan = ds.child("keterangan").value.toString()
                    val kodebuku = ds.child("kode_buku").value.toString()
                    val noinduk = ds.child("no_induk").value.toString()
                    val tanggalpengembalian = ds.child("tanggal_pengembalian").value.toString()

                    val riwayat = Riwayat(id = id, keterangan= keterangan, kode_buku = kodebuku, no_induk = noinduk, tanggal_pengembalian = tanggalpengembalian)
                    list.add(riwayat)
                }
                Log.e("ooooo","size:${list.size} ")

                adapter?.setItems(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("oooo", "onCancelled :${error.toException()}")
            }
        })
    }
}

