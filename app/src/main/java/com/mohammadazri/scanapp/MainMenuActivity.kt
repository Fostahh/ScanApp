package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationMainMenu)
        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        val home = HomeFragment()
        val profile = ProfileFragment()


        addButton.setOnClickListener {
            startActivity(Intent(this@MainMenuActivity, AddProductActivity::class.java))
        }

        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        fragmentChanger.commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.home_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    addButton.show()
                    fragmentChanger.replace(R.id.frame_layout, home)
                    fragmentChanger.commit()
                    true
                }

                R.id.profile_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    addButton.hide()
                    fragmentChanger.replace(R.id.frame_layout, profile)
                    fragmentChanger.commit()

                    true
                }
                else -> false
            }
        }
    }
}