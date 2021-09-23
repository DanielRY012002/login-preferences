package pe.edu.upeu.preferences
import android.content.ContentValues.TAG

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {
    lateinit var name: String
    private lateinit var buttonLogout: Button
    private lateinit var wText:TextView
    private val db = MainActivity.db

    override fun onCreate(savedInstanceState: Bundle?) {
        val mainActivity = MainActivity.loadPreferences(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        buttonLogout = findViewById(R.id.btn_dashboard_logout)
        wText= findViewById(R.id.tv_dashboard_title)
        buttonLogout.setOnClickListener {
            val edit = mainActivity.edit()
            edit.putBoolean("savedCredentials", false)
            edit.remove("username")
            edit.remove("password")
            edit.apply()
            startActivity(Intent(applicationContext, MainActivity::class.java))

        }
        val spUsername = mainActivity.getString("username", "")
        val spPassword = mainActivity.getString("password", "")
        if (spUsername != "" && spPassword != "") {
            db.collection("users").whereEqualTo("username", spUsername).whereEqualTo("password",spPassword).get().addOnSuccessListener {
                it.forEach { result->
                    wText.text= "${wText.text} ${result.data["firstname"]}"
                    Log.i(TAG,"${result.data["firstname"]} ${wText.text}")
                }
            }
        }
    }
}