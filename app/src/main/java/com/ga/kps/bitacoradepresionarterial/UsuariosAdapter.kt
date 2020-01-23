package com.ga.kps.bitacoradepresionarterial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import model.Usuario

class UsuariosAdapter(val context: Context) : ListAdapter<Usuario, UsuariosAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener : View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Usuario>(){
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.nombre == newItem.nombre &&
                    oldItem.apellidos == newItem.apellidos &&
                    oldItem.fecha_nacimiento == newItem.fecha_nacimiento &&
                    oldItem.imagen_perfil == newItem.imagen_perfil

        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val nombreUsuario = v.findViewById<TextView>(R.id.nombreUsuarioTV)
        val apellidosUsuario = v.findViewById<TextView>(R.id.apellidosUsuarioTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_usuario, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuarioActual = getItem(position)

        holder.nombreUsuario.text = usuarioActual.nombre
        holder.apellidosUsuario.text = usuarioActual.apellidos
    }

    fun getUsuarioAt(position: Int): Usuario{
        return getItem(position)
    }

    fun setOnClickListener(listAdapter: View.OnClickListener){
        this.listener = listAdapter
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }


}