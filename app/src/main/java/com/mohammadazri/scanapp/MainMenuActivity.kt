package com.mohammadazri.scanapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationMainMenu)
        val fabMenu = findViewById<FloatingActionsMenu>(R.id.floatingActionMenu)
        val fab1 = findViewById<com.getbase.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton1)
        val fab2 = findViewById<com.getbase.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton2)

        val home = HomeFragment()
        val profile = ProfileFragment()

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
                    supportActionBar?.hide()
                    true
                }

                R.id.profile_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fabMenu.visibility = View.GONE
                    fragmentChanger.replace(R.id.frame_layout, profile)
                    fragmentChanger.commit()
                    supportActionBar?.hide()
                    true
                }

                else -> false
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}