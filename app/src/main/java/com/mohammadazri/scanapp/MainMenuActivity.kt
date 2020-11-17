package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationMainMenu)
//        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        val fabMenu = findViewById<FloatingActionsMenu>(R.id.floatingActionMenu)
        val fab1 = findViewById<com.getbase.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton1)
        val fab2 = findViewById<com.getbase.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton2)

        val home = HomeFragment()
        val profile = ProfileFragment()

//        addButton.setOnClickListener {
//            startActivity(Intent(this@MainMenuActivity, AddProductActivity::class.java))
//        }

        fab1.setOnClickListener {
            startActivity(Intent(this@MainMenuActivity, AddProductActivity::class.java))
        }

        fab2.setOnClickListener {
            startActivity(Intent(this@MainMenuActivity, CheckProductActivity::class.java))
        }

        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        fragmentChanger.commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.home_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, home)
                    fabMenu.visibility = View.VISIBLE
                    fragmentChanger.commit()
                    true
                }

                R.id.profile_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
//                    addButton.hide()
                    fabMenu.visibility = View.GONE
                    fragmentChanger.replace(R.id.frame_layout, profile)
                    fragmentChanger.commit()

                    true
                }

                else -> false
            }
        }
    }
}