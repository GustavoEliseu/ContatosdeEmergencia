package com.gustavo.contatosdeemergencia

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList

class myRecyclerAd(private val context: Context, private val myContacts: ArrayList<User>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<myRecyclerAd.ViewHolder>() {
    private var mClickListener: ItemClickListener? = null
    private val mInflater: LayoutInflater

    init {
        this.mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): myRecyclerAd.ViewHolder {
        val view = mInflater.inflate(R.layout.recycler_view_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mNomeList.text = myContacts[i].nome
        viewHolder.mNumeroList.text = myContacts[i].numero
        val theme: Resources.Theme = context.theme
        viewHolder.imgBtnDel.setImageDrawable(context.resources.getDrawable(android.R.drawable.ic_menu_delete))
    }

    override fun getItemCount(): Int {
        try {
            return myContacts.size
        } catch (e: Exception) {
            return 0
        }

    }

    inner class ViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v), View.OnClickListener {
        // each data item is just a string in this case
        var mNomeList: TextView
        var mNumeroList: TextView
        var mLinear: LinearLayout
        var imgBtnDel: ImageView

        init {
            mLinear = v.findViewById(R.id.myLinear)
            mNomeList = v.findViewById(R.id.list_nome)
            mNumeroList = v.findViewById(R.id.list_numero)
            imgBtnDel = v.findViewById(R.id.list_btn)
            mLinear.setOnClickListener(this)
            v.setOnClickListener(this)
            imgBtnDel.setOnClickListener(this)

        }


        override fun onClick(v: View) {
            if (mClickListener != null)
                if (v.id == imgBtnDel.id) {//click no botão cancelar

                    /*
                 * Se necessário, pode utilizar a boolean controlecor, como algo para saber se o item está habilitado ou não.
                 * com if(controleCor==true).
                 * */
                    mClickListener!!.onItemClick(imgBtnDel, adapterPosition, 1)
                } else {//click no view em si

                    mClickListener!!.onItemClick(v, adapterPosition, 0)
                }
        }
    }

    // convenience method for getting data at click position
    internal fun getItem(id: Int): String {
        return myContacts[id].nome
    }

    // allows clicks events to be caught
    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int, tipoClasse: Int)
    }
}




