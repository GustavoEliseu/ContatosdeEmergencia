package com.gustavo.contatosdeemergencia

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.gustavo.contatosdeemergencia.data.Result
import kotlinx.android.synthetic.main.fragment_via_cep.*
import kotlinx.android.synthetic.main.fragment_via_cep.view.*
import kotlinx.android.synthetic.main.fragment_via_cep.view.cepText
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.os.AsyncTask as AsyncTask1


class ViaCep : Fragment() {


    private var listener: OnFragmentInteractionListener? = null
    lateinit var txtResult: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_via_cep, container, false)
        txtResult = view.result
        Toast.makeText(context,"teste",Toast.LENGTH_SHORT).show()
        view.cep_button.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if (cepText.text.toString().isNullOrEmpty().not()) {
                    MeuAsync(txtResult).execute(cepText.text.toString())
                }}

        })

        // Inflate the layout for this fragment
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(string: String) {
        listener?.onFragmentInteraction(string)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(string: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViaCep.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViaCep().apply {
                arguments = Bundle().apply {
                }
            }
    }


    inner class MeuAsync(val myTxt:TextView): AsyncTask1<String,String,String>() {

        override fun doInBackground(vararg params: String): String {
            try {
                val url = URL("http://viacep.com.br/ws/"+params[0]+"/json/")
                val conn = url.openConnection() as HttpURLConnection
                conn.setRequestMethod("GET")
                conn.setDoInput(true)
                conn.connect()
                val `is` = conn.getInputStream()
                val reader: BufferedReader
                reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
                var data: String? = null
                var content = ""
                while ((reader.readLine().also { data = it }) != null) {
                    content += data!! + "\n"
                }
                val jsonObject = JSONObject(content)
                val cep = jsonObject.getString("cep")
                Log.v("json", cep)

                return jsonObject.toString()
            } catch ( e:Exception) {
                Log.e("Exception: aaa",  e.message)
                return "erro";
            }
        }

        override fun onProgressUpdate(vararg values: String?) {

        }
        override fun  onPostExecute(result:String?) {
            var jsonObject:JSONObject?
            var resultToPrint:String
            try{
                jsonObject = JSONObject(result)
                if(jsonObject.has("erro").not()){
                    resultToPrint = "Rua: " + jsonObject.getString("logradouro") +"\n" +
                            "Bairro: " + jsonObject.getString("bairro") + "\n" +
                            "CEP: " + jsonObject.getString("cep") + "\n" +
                            "Cidade" + jsonObject.getString("localidade") + ", " + jsonObject.getString("uf")
                    Log.v("json",resultToPrint)
                }else{
                    resultToPrint="Endereço não encontrado"
                }
                Log.v("jsonObject viacep: ", resultToPrint)
                myTxt.setText(resultToPrint)
            }catch(e:Exception){
                resultToPrint="Endereço não encontrado"
                myTxt.setText(resultToPrint)
                Log.e("Except onPostExecute:",e.toString())
                e.printStackTrace()
            }
        }
    }

}

