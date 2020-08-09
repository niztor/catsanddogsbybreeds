package ar.com.nestor.ejercicioretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.com.nestor.ejercicioretrofit.cats.CatsActivity
import ar.com.nestor.ejercicioretrofit.dogs.DogsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_button_perritos.setOnClickListener {
            val intent = Intent(this, DogsActivity::class.java)
            startActivity(intent)
        }

        main_button_gatitos.setOnClickListener {
            val intent = Intent(this, CatsActivity::class.java)
            startActivity(intent)
        }
    }
}