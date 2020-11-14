package com.mohammadazri.scanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationMainMenu)
        val home = HomeFragment()
        val profile = ProfileFragment()

        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        fragmentChanger.commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.home_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, home)
                    fragmentChanger.commit()

                    true
                }

                R.id.profile_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    fragmentChanger.replace(R.id.frame_layout, profile)
                    fragmentChanger.commit()

                    true
                }
                else -> false
            }
        }
    }
}