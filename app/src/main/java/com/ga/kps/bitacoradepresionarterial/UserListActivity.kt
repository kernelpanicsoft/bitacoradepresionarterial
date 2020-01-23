package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user_list.*
import room.components.viewmodels.UsuarioViewModel


class UserListActivity : AppCompatActivity() {
    lateinit var usuarioViewModel : UsuarioViewModel
    lateinit var RV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.usuarios_registrados)


        RV = findViewById(R.id.RecViewUsuarios)
        RV.setHasFixedSize(true)


        val mLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = UsuariosAdapter(this)
        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel::class.java)
        usuarioViewModel.getAllUsuarios().observe(this, Observer {
            adapter.submitList(it)
        })

        RecViewUsuarios.adapter = adapter

        addUserFAB.setOnClickListener {
            val nav = Intent(this, AddUserActivity::class.java)
            startActivity(nav)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
