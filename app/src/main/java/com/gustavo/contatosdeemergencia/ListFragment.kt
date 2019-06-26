package com.gustavo.contatosdeemergencia

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_item_list.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFragment.OnListFragmentInteractionListener] interface.
 */
class ListFragment : androidx.fragment.app.Fragment(), myRecyclerAd.ItemClickListener {
    private lateinit var mRecycler: androidx.recyclerview.widget.RecyclerView
    internal lateinit var mRecyclerAd: myRecyclerAd
    val M_CONTACT = 2
    val PICK_IMAGE = 1

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        return view
    }

    override fun onStart(){
        super.onStart()
        mRecycler= list
        // Set the adapter
        mRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        mRecyclerAd= myRecyclerAd((activity as SeccondActivity), (activity as SeccondActivity).myContacts)
        mRecyclerAd.setClickListener(this)
        mRecycler.adapter = mRecyclerAd

        if(view!=null){
        val fab: com.google.android.material.floatingactionbutton.FloatingActionButton = fab
        fab.setOnClickListener(View.OnClickListener {
            if ((activity as SeccondActivity).numeroContatos < 5) {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(intent, M_CONTACT)
            } else {
                Toast.makeText(context, getString(R.string.max_warn), Toast.LENGTH_SHORT).show()
            }
        })
            mRecycler.getAdapter()!!.notifyDataSetChanged()
    }}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        listener = null
        mRecycler= androidx.recyclerview.widget.RecyclerView((activity as SeccondActivity))
        super.onDetach()
    }

     override fun onItemClick(view:View, position:Int, tipoClasse:Int) {
    val mAlert = AlertDialog.Builder((activity as SeccondActivity))
    val dialog: AlertDialog
    when (tipoClasse) {
     1 -> {

        mAlert.setCancelable(true)
        mAlert.setTitle(getString(R.string.delete))
        mAlert.setMessage(getString(R.string.delete_desc))
        mAlert.setPositiveButton(getString(R.string.confirm)
        ) { dialog, which ->
            (activity as SeccondActivity).myContacts.removeAt(position)
            (activity as SeccondActivity).numeroContatos--
            mRecycler.getAdapter()!!.notifyDataSetChanged()
        }
        mAlert.setNegativeButton(android.R.string.cancel, object: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which:Int) {}
    })

    dialog = mAlert.create()
    dialog.show()
    }

    2 -> {
        mAlert.setCancelable(true)
        mAlert.setTitle(getString(R.string.add_photo))
        mAlert.setMessage(getString(R.string.add_photo_desc))
        mAlert.setPositiveButton(getString(R.string.confirm),
        object: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which:Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra("position", position)
        intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)

    }
    })
        mAlert.setNegativeButton(android.R.string.cancel, object: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which:Int) {}
        })

    dialog = mAlert.create()
    dialog.show()
    }
    0 -> {
        val uri = "tel:" + (activity as SeccondActivity).myContacts.get(position).numero
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }
}

}


    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        if (requestCode == M_CONTACT && resultCode == Activity.RESULT_OK)
        {
        val nome:String
        val numero:String

        val contactUri = data!!.data
        val cursor = (activity as SeccondActivity).getContentResolver().query(contactUri!!, null, null, null, null)
        cursor!!.moveToFirst()

        numero = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        for (i in 0 until (activity as SeccondActivity).numeroContatos)
        {
            if (numero == (activity as SeccondActivity).myContacts.get(i).numero)
            {
                Toast.makeText((activity as SeccondActivity).getApplicationContext(), getString(R.string.number_exists), Toast.LENGTH_SHORT).show()
                return
            }
        }
    nome = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
    cursor.close()
    val person = ContentUris.withAppendedId(
    ContactsContract.Contacts.CONTENT_URI, java.lang.Long
    .parseLong(id))
    val teste = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)

            (activity as SeccondActivity).grantUriPermission((activity as SeccondActivity).getPackageCodePath(), teste, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            (activity as SeccondActivity).grantUriPermission((activity as SeccondActivity).getPackageCodePath(), teste, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            (activity as SeccondActivity).grantUriPermission((activity as SeccondActivity).getPackageCodePath(), teste, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

    try
    {
        val inputStream = (activity as SeccondActivity).getContentResolver().openInputStream(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY))
    }
    catch (E:Exception) {
        Log.e("TAG:Erro", E.toString())
    }

    (activity as SeccondActivity).myContacts.add(User(nome, numero))
    (activity as SeccondActivity).numeroContatos++
    }



}


    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
