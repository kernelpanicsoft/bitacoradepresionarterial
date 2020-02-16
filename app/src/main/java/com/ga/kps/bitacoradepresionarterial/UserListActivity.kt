package com.ga.kps.bitacoradepresionarterial

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import helpers.REGISTRAR_USUARIO
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

        val adapter = UsersAdapter(this)
        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel::class.java)
        usuarioViewModel.getAllUsuarios().observe(this, Observer {
            adapter.submitList(it)
        })

        RecViewUsuarios.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val usuario = adapter.getUsuarioAt(RecViewUsuarios.getChildAdapterPosition(it))
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            with(sharedPref.edit()){
                putInt("actualUserID",usuario.id)
                apply()
                Log.d("actualUserSelected", it.toString())
            }
            Toast.makeText(this,  getString(R.string.usuario_seleccionado_correctamente), Toast.LENGTH_SHORT).show()
            finish()
        })

        addUserFAB.setOnClickListener {
            val nav = Intent(this, AddUserActivity::class.java)
            startActivityForResult(nav, REGISTRAR_USUARIO)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REGISTRAR_USUARIO && resultCode == Activity.RESULT_OK){
            usuarioViewModel.getLastUserID().observe(this, Observer {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                with(sharedPref.edit()){
                    putInt("actualUserID",it!!.toInt())
                    apply()
                    Log.d("actualUser", it.toString())
                }
                Toast.makeText(this,  getString(R.string.usuario_registrado_correctamente), Toast.LENGTH_SHORT).show()
                finish()
            })

        }
    }
}
