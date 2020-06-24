package com.ga.kps.bitacoradepresionarterial

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import helpers.REGISTRAR_PRIMER_USUARIO
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
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usuarioID = sharedPref.getInt("actualUserID", -1)
        if(usuarioID != -1) {
            ab!!.setDisplayHomeAsUpEnabled(true)
        }

        val firstRunMessage = sharedPref.getBoolean("FirstRun",true)

        if(firstRunMessage){
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.bienvenido))
            builder.setMessage(getString(R.string.primer_usuario_mensaje))
            builder.setPositiveButton(getString(R.string.registrar_usuario)){ _, _ ->
                val nav = Intent(this, AddUserActivity::class.java)
                startActivityForResult(nav, REGISTRAR_PRIMER_USUARIO)
            }
            val dialog = builder.create()
            dialog.show()
        }
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
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        usuarioViewModel.getAllUsuarios().observe(this, Observer {
            adapter.submitList(it)
        })

        RecViewUsuarios.adapter = adapter

        adapter.setOnClickListener(View.OnClickListener {
            val usuario = adapter.getUsuarioAt(RecViewUsuarios.getChildAdapterPosition(it))
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            with(sharedPreferences.edit()){
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
        if(requestCode == REGISTRAR_PRIMER_USUARIO && resultCode == Activity.RESULT_OK){
            usuarioViewModel.getLastUserID().observe(this, Observer {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                with(sharedPref.edit()){
                    putInt("actualUserID",it!!.toInt())
                    putBoolean("FirstRun",false)
                    apply()
                    Log.d("actualUser", it.toString())
                }
                Toast.makeText(this,  getString(R.string.usuario_registrado_correctamente), Toast.LENGTH_SHORT).show()
                finish()
            })
        }
    }


}
