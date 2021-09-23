package pe.edu.upeu.preferences

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var checkBox: CheckBox
    lateinit var showPass: CheckBox
    lateinit var logButton: Button
    lateinit var usernameText: EditText
    lateinit var passwordText: EditText
    lateinit var signButton: Button

    var firstUser: UserModel = UserModel("juanchovich", "123456","Juan","Trolo")


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logButton = findViewById(R.id.btn_main_auth)
        checkBox = findViewById(R.id.cb_main_credentials)
        usernameText = findViewById(R.id.et_main_username)
        passwordText = findViewById(R.id.et_main_password)
        showPass = findViewById(R.id.cb_main_show_password)
        signButton = findViewById(R.id.efab_main_sign_up)
        val loadedPreferences = loadPreferences(applicationContext)
        if (loadedPreferences.getBoolean("savedCredentials", false)) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
        }
        signButton.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }


        db.collection("users").whereEqualTo("username", firstUser.username).get()
            .addOnSuccessListener {

                if (it.documents.isEmpty()) {
                    db.collection("users").add(
                        mapOf(
                            "username" to firstUser.username,
                            "password" to firstUser.password,
                            "lastname" to firstUser.lastName,
                            "firstname" to firstUser.firstName

                        )
                    ).addOnSuccessListener {
                        Log.i(TAG, "Successfully written document!")
                    }.addOnFailureListener {
                        Log.i(TAG, "Error: ", it)
                    }

                }

            }

        /*

                    it.forEach { result ->
                        Log.i(TAG, "${result.data}")
                    }
        * */

        db.collection("users").get().addOnSuccessListener { result ->
            result.forEach {
                Log.i(TAG, "${it.data}")
            }
        }.addOnFailureListener {
            Log.i(TAG, "Error", it)
        }





        logButton.setOnClickListener {
            saveCredentials(
                usernameText.text.toString(),
                passwordText.text.toString(),
                checkBox.isChecked,
                this
            )
        }
        showPass.setOnClickListener {
            if (showPass.isChecked) {
                passwordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordText.setSelection(passwordText.text.length)
            } else {
                Log.i(TAG, "keloke")
                passwordText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordText.setSelection(passwordText.text.length)
            }
        }

    }

    private fun saveCredentials(
        username: String,
        password: String,
        save: Boolean,
        context: Context
    ) {
        logButton.isEnabled = false

        if (username == "" || password == "") {
            Toast.makeText(applicationContext, "Empty Required Fields", Toast.LENGTH_SHORT)
                .show()
            logButton.isEnabled = true
            return
        }
        db.collection("users").whereEqualTo("username", username).whereEqualTo("password", password)
            .get().addOnSuccessListener {
            if (it.documents.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Invalid Username Or Password",
                    Toast.LENGTH_SHORT
                ).show()
                logButton.isEnabled = true

            } else {
                if (save) {
                    val edit = loadPreferences(context).edit()
                    edit.putString("username", username)
                    edit.putString("password", password)
                    edit.putBoolean("savedCredentials", save)
                    edit.apply()
                    Toast.makeText(
                        applicationContext,
                        "Credentials successfully saved!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Logged without save credentials service",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))


                }
            }


        }
    }


    companion object {
        val db = Firebase.firestore

        fun loadPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        }
    }


}