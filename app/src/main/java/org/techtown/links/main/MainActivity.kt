package org.techtown.links.main

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.techtown.links.R
import org.techtown.links.data.LinkData
import org.techtown.links.databinding.ActivityMainBinding
import org.techtown.links.util.FirebaseUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var countLinks = 0
    var db = Firebase.firestore
    val linkList = arrayListOf<LinkData>()
    val adapter = LinkAdapter(linkList)


    override fun onResume() {
        super.onResume()
        getLink()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getLink()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        binding.linkRecycler.layoutManager = manager
        binding.linkRecycler.adapter = adapter
        binding.mainExitBtn.setOnClickListener {
            exitDialog()
        }
        binding.plusBtnMain.setOnClickListener {
            showDialog()
        }

    }

    private fun getLink() {
        db.collection("users")
            .document(FirebaseUtil.getUid())
            .collection("links")
            .get()
            .addOnSuccessListener { result ->
                linkList.clear()
                for (document in result) {
                    val item = LinkData(document["title"] as String, document["link"] as String)
                    linkList.add(item)
                }
                adapter.notifyDataSetChanged() // recycler ??????
            }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "Error!!! getting documents: $exception")
            }
    }

    fun showDialog() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_plus, null)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("?????? ??????")
            .setPositiveButton("??????") { dialog, _ ->

                //  editText -> invitee??? ????????????
                //  findViewById??? ??????
                val userRef = db.collection("users").document(FirebaseUtil.getUid()) //????????????
                val textTitle: TextView = view.findViewById(R.id.plusEditTitle)
                val textLink: TextView = view.findViewById(R.id.plusEditLink)
                var inviteeTitle = textTitle.text.toString()
                var inviteeLink = textLink.text.toString()
                var countStr: String = "?????????"
                var countInt: Int = 100
                userRef.get().addOnSuccessListener { DocumentSnapshot ->
                    countStr = DocumentSnapshot.get("count").toString()
                    countInt = Integer.parseInt(countStr)
                    /*if (countInt == 0){
                        userRef.update("count",1)
                    }else{
                        userRef.get().addOnSuccessListener { DocumentSnapshot ->
                            //countStr = DocumentSnapshot.get("count").toString()
                            userRef.update("count",FieldValue.increment(1))

                            countLinks++
                        }

                        //userRef.update("count",countLinks)
                    }*/
                    userRef.update("count",FieldValue.increment(1)).addOnCompleteListener {
                        if (it.isSuccessful){
                            countInt++
                            val dataLinks = hashMapOf(
                                "link" to inviteeLink,
                                "title" to inviteeTitle

                            )
                            userRef.collection("links").document("$countInt").set(dataLinks)
                                .addOnSuccessListener {
                                    Log.d(TAG, "set(dataLinks) success")
                                    getLink() //??????

                                }
                                .addOnFailureListener {
                                    Log.e(TAG, "onCreate: error : $it")
                                }
                        }
                    }
                    //countInt++




                    /* userRef.update("count", countLinks)
                         .addOnSuccessListener {
                             Log.d(TAG, "update(count) success")
                         }
                         .addOnFailureListener {
                             Log.e(TAG, "onCreate: error : $it")
                         }*/
                    //userRef.collection("links").document("$countLinks").set(dataLinks)



                }

            }
            .setNeutralButton("??????", null)
            .create()

        //  ?????? ????????? ??? ???????????????
        alertDialog.setCancelable(false)

        alertDialog.setView(view)
        alertDialog.show()
    }


    fun exitDialog() {
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle(null)
        dialog.setMessage("????????????")

        var dialog_listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Intent()
                    Firebase.auth.signOut()
                }
            }
        }
        dialog.setPositiveButton("??????", dialog_listener)
        dialog.show()
    }

    private fun Intent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}

