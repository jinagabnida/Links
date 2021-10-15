package org.techtown.links.main

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.techtown.links.util.FirebaseUtil
import org.techtown.links.R
import org.techtown.links.data.UserProfile
import org.techtown.links.databinding.ActivityLoginBinding
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    var db = Firebase.firestore
    lateinit var userInfo: UserProfile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = Firebase.auth
        if (auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }//자동로

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginBtn.setOnClickListener {

            var id = binding.loginEmailEditText.text.toString()
            var pw = binding.loginPasswordEditText.text.toString()

            SignInWith(id, pw)

        }
        binding.loginToJoinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }

    private fun SignInWith(userEmail: String, userPw: String) {

        auth.signInWithEmailAndPassword(userEmail, userPw).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //var countStr = ""
                var countRef = 100
                val userRef = db.collection("users").document(FirebaseUtil.getUid())
                userRef.get().addOnSuccessListener { DocumentSnapshot ->
                    //countStr =
                    if (DocumentSnapshot.get("count").toString().equals("null")) {
                        countRef = 0
                    } else{
                        countRef = Integer.parseInt(DocumentSnapshot.get("count").toString())
                    }
                  //  countRef = if(countStr == null) 0 else Integer.parseInt(countStr)
                    Log.d(TAG, "cccccccccccccccountRef $countRef")
                    dialog("success")
                    setDb(countRef)

                }


            } else {
                dialog("fail")
            }
        }
    }


    fun setDb(countRef : Int) {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        var loginEmail = binding.loginEmailEditText.text.toString()
        var loginPw = binding.loginPasswordEditText.text.toString()

        Log.d(TAG, "countReffffffffffffffffff $countRef")
        userInfo = UserProfile(loginEmail,loginPw,countRef,FirebaseUtil.getUid())

        db.collection("users").document(FirebaseUtil.getUid()).set(userInfo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
            }

    }


    fun dialog(type: String) {
        var dialog = AlertDialog.Builder(this)
        var cheak = false

        if (type.equals("success")) {
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공")
            cheak = true
        } else if (type.equals("fail")) {
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                    if (cheak == true) {
                        Intent()
                    }
            }
        }
        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()

    }

    fun Intent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}




