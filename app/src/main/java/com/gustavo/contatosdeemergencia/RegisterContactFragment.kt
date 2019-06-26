package com.gustavo.contatosdeemergencia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_register_contact.*



// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegisterContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RegisterContactFragment : androidx.fragment.app.Fragment(), View.OnClickListener {

    val M_CONTACT = 2
    val PICK_IMAGE = 1


    override fun onClick(v: View?) {
        if(v!=null && v.id==btnADD.id){
            if(numTxt.text.isEmpty()){
                Toast.makeText(context,"Numero não valido",Toast.LENGTH_SHORT).show()
            }else if(nomeTxt.text.isEmpty()){
                Toast.makeText(context,"Digite um Nome",Toast.LENGTH_SHORT).show()
            }else{
                val nome: String = nomeTxt.text.toString()
                val num: String = numTxt.text.toString()
                val intent: Intent = Intent(ContactsContract.Intents.Insert.ACTION)
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
                intent.apply {
                    putExtra(ContactsContract.Intents.Insert.NAME, nome)
                    putExtra(ContactsContract.Intents.Insert.PHONE,num)
                    putExtra(
                        ContactsContract.Intents.Insert.EMAIL_TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_HOME
                    )
                }
                startActivity(intent)
                (activity as SeccondActivity).myContacts.add(User(nome,num))
            }

        }
    }


    private var listener: OnFragmentInteractionListener? = null
    private lateinit var nomeTxt: TextView
    private lateinit var numTxt: TextView
    private lateinit var btnADD: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }
    override fun onStart(){
        super.onStart()
        if(nome_txt!=null&&num_txt!=null&&btn_add!=null){
            nomeTxt=nome_txt
            numTxt=num_txt
            btnADD=btn_add}
        btnADD.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val v:View =inflater.inflate(R.layout.fragment_register_contact, container, false)

        return v
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */




    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterContactFragment.
         */

        @JvmStatic
        fun newInstance() =
            RegisterContactFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
