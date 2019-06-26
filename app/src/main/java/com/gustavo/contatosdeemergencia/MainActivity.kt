package com.gustavo.contatosdeemergencia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.codelabs.mdc.kotlin.shrine.NavigationHost

class MainActivity : AppCompatActivity(), NavigationHost {


    override fun navigateTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shr_main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, LoginFragment() as androidx.fragment.app.Fragment)
                    .commit()
        }
    }


}
