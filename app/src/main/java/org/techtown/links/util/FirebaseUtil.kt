package org.techtown.links.util

import com.google.firebase.auth.FirebaseAuth

class FirebaseUtil {
    companion object {
        fun getUid() : String {
            return FirebaseAuth.getInstance().uid!!
        }
    }

}