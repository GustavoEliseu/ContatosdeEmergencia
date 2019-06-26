package com.gustavo.contatosdeemergencia

import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_seccond.*
import kotlin.collections.ArrayList
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SeccondActivity : AppCompatActivity(),RegisterContactFragment.OnFragmentInteractionListener, ListFragment.OnListFragmentInteractionListener,ViaCep.OnFragmentInteractionListener{
    override fun onFragmentInteraction(string: String) {

    }

    lateinit var myContacts: ArrayList<User>
    internal val fragmentContact: Fragment = RegisterContactFragment()
    internal val fragmentList: Fragment = ListFragment()
    internal val framentCep: Fragment = ViaCep()
    lateinit var navView: BottomNavigationView

    internal var numeroContatos: Int = 0
    override fun onListFragmentInteraction(item: User?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var sharedPreferences: SharedPreferences
    var myfragment = Fragment();

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_add -> {
                changeFragment(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_call -> {
                changeFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cep-> {
                changeFragment(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    public fun changeFragment(opt:Int){
    var tag:String=""
        when(opt){
            0->{
                myfragment =fragmentContact
            tag = "registrar"
            }
            1->{
                myfragment =fragmentList
                tag = "call"
            }
            2->{
                myfragment=framentCep
                tag= "cep"
            }
        }

        val ft: FragmentTransaction =getSupportFragmentManager().beginTransaction()
        myfragment.setRetainInstance(true)
        ft.replace(R.id.fragment, myfragment, tag)

        ft.commit()

        supportFragmentManager.executePendingTransactions()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seccond)

        loadData()


        //TODO Salvar lista de usuários no onStop e rechamar a mesma no onStart
        var opt:Int = 0;
        navView = myNav_view as BottomNavigationView
        myfragment=fragmentContact
        myfragment.setRetainInstance(true);

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.selectedItemId=navView.getChildAt(0).id
        changeFragment(opt)
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("SelectedItemId", navView.getSelectedItemId())
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItemId = savedInstanceState.getInt("SelectedItemId")
        navView.setSelectedItemId(selectedItemId)
    }



    fun saveData()
    {
         val sharedPreferences:SharedPreferences = getSharedPreferences ("shared preferences", MODE_PRIVATE);
         val editor:SharedPreferences.Editor = sharedPreferences . edit ();
         val gson:Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        var json:String = gson . toJson (myContacts);
        editor.putString("user list", json);
        Log.e("saveData",json)
        editor.apply();
    }

    inline fun <reified T> teste() =  object: TypeToken<T>() {}.type
    fun loadData() {
        try {
        val sharedPreferences:SharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        val gson:Gson = Gson();
        val json:String? = sharedPreferences.getString("user list", "");
        val type:Type = teste<ArrayList<User>>();
        myContacts = gson.fromJson(json, type);

    }
        catch(e: Exception) {
            myContacts = ArrayList<User>();
            Log.e("loadData:","ARRAY VAZIO")
        }
    }


    override fun onStart(){
        super.onStart()

            /*val gson = Gson()
            object:TypeToken<List<User>>(){}.type;
            //TODO PESQUISAR A FORMA CORRETA DE SE IMPLEMENTAR A PRÓXIMA LINHA
            myContacts = gson.fromJson("myUserArray", myContacts.javaClass)*/


    }



    override fun onDestroy(){

            saveData()
        try {
        }
        catch(e:Exception){
            e.printStackTrace()
            Log.e("Exception onDestroy"," falha salvar")
        }
        super.onDestroy()

    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
