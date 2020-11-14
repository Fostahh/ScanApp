package com.mohammadazri.scanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainMenuActivity : AppCompatActivity() {

    val addButton:FloatingActionButton = findViewById(R.id.addButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationMainMenu)
        val home = HomeFragment()
        val profile = ProfileFragment()

        addButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainMenuActivity, AddProductActivity::class.java))
        })

        val fragmentChanger = supportFragmentManager.beginTransaction()
        fragmentChanger.replace(R.id.frame_layout, home)
        fragmentChanger.commit()

        showFloatingActionButton()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.home_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    showFloatingActionButton()
                    fragmentChanger.replace(R.id.frame_layout, home)
                    fragmentChanger.commit()
                    true
                }

                R.id.profile_menu -> {
                    val fragmentChanger = supportFragmentManager.beginTransaction()
                    hideFloatingActionButton()
                    fragmentChanger.replace(R.id.frame_layout, profile)
                    fragmentChanger.commit()

                    true
                }
                else -> false
            }
        }
    }

    private fun showFloatingActionButton() {
        addButton.show()
    }

    private fun hideFloatingActionButton() {
        addButton.hide()
    }

}