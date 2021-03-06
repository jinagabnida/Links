package org.techtown.links.main

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.techtown.links.R
import org.techtown.links.data.UserProfile
import org.techtown.links.databinding.ActivityJoinBinding
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    var isExistBlank = false
    var isPWSame = false
    var isExistSpecial = false
    var isRegexChk = false
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding
    var db = Firebase.firestore
    lateinit var userInfo: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinFinishBtn.setOnClickListener {

            val email = binding.joinId.text.toString()
            val pw = binding.joinPassword.text.toString()
            val pw_ck = binding.joinPasswordCheak.text.toString()
            val special_email_chk: Boolean =
                Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$", email)
            val special_pw_chk: Boolean =
                Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$", pw)
            val regex_email: Boolean =
                Pattern.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$", email)
            val regex_pw: Boolean =
                Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$\n", pw)


            if (email.isEmpty() || pw.isEmpty() || pw_ck.isEmpty()) {
                isExistBlank = true
            } else if (!special_email_chk && !special_pw_chk) {
                isExistSpecial = true
            } else if (!regex_email && !regex_pw) {
                isRegexChk = true
            } else {
                if (pw == pw_ck) {
                    isPWSame = true
                }
            }

            if (!isExistBlank && isPWSame && !isExistSpecial && !isRegexChk) {
                Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show()

                CreateUserWith(email, pw)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            } else {
                if (isExistBlank) {
                    dialog("empty")
                } else if (!isPWSame) {
                    dialog("differ")
                } else if (!isRegexChk) {
                    dialog("regex")
                } else if (!isExistSpecial) {
                    dialog("existSqe")
                }
            }
        }
    }

    private fun CreateUserWith(userEmail: String, userPw: String) {

        Log.d(TAG, "CreateUserWith: afasd")

        auth.createUserWithEmailAndPassword(userEmail, userPw).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //setDb()
            } else {
                Log.d(">", task.exception?.message.toString())
            }
        }
    }

    /*fun setDb() {
        val currentUser = auth.currentUser
        userInfo.uid = currentUser!!.uid

        /*db.collection("users").document(userInfo.uid!!).set(userInfo)
            .addOnSuccessListener {

                Log.d("db.userInfo.set", "CreateUserWith: success")
            }*/
    }*/

    private fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)

        //?????? ????????? ??????
        if (type.equals("empty")) {
            dialog.setTitle("???????????? ??????")
            dialog.setMessage("???????????? ?????? ??????????????????")
        }

        //???????????? ??????
        else if (type.equals("differ")) {
            dialog.setTitle("???????????? ??????")
            dialog.setMessage("??????????????? ????????????")
        }

        //?????? ??????
        else if (type.equals("regex")) {
            dialog.setTitle("???????????? ??????")
            dialog.setMessage("?????????, ???????????? ????????? ???????????????")
        }

        //??????, ??????, ??????????????? ????????? ????????? ?????????
        else if (type.equals("existSqe")) {
            dialog.setTitle("???????????? ??????")
            dialog.setMessage("??????, ??????, ??????????????? ???????????? ?????? ????????? ???????????????")
        }

        val dialog_listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                    Log.d("join", "???????????????")
            }
        }
        dialog.setPositiveButton("??????", dialog_listener)
        dialog.show()

    }

}








