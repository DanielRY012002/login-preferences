package pe.edu.upeu.preferences

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private val db = MainActivity.db


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var firstname: EditText = findViewById(R.id.et_register_first_name)
        var lastname: EditText = findViewById(R.id.et_register_last_name)
        var username: EditText = findViewById(R.id.et_register_username)
        var pass: EditText = findViewById(R.id.et_register_password)
        var confirmPass: EditText = findViewById(R.id.et_register_confirm_password)
        var signButton: Button = findViewById(R.id.btn_register_sign_up)

        signButton.setOnClickListener {
            signButton.isEnabled = false
            var passStr = pass.text.toString()
            if (firstname.text.isBlank() || lastname.text.isBlank() || username.text.isBlank() || pass.text.isBlank() || confirmPass.text.isBlank()) {
                Toast.makeText(applicationContext, "Empty Required Fields", Toast.LENGTH_SHORT)
                    .show()
                signButton.isEnabled = true
                return@setOnClickListener
            }
            if (passStr.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password requires 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                signButton.isEnabled = true
                return@setOnClickListener
            }

            var rodrigo = confirmPass.text.toString() == passStr
            if (!rodrigo) {
                Toast.makeText(applicationContext, "Password must match", Toast.LENGTH_SHORT).show()
                signButton.isEnabled = true
                return@setOnClickListener
            }


            val register = hashMapOf(
                "username" to username.text.toString(),
                "password" to pass.text.toString(),
                "firstname" to firstname.text.toString(),
                "lastname" to lastname.text.toString()
            )

            Log.i(TAG,"$register")
            db.collection("users").add(register).addOnSuccessListener {
                Toast.makeText(applicationContext,"Successfully registered!",Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }


        }


    }
}