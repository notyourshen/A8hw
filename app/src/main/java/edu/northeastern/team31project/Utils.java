package edu.northeastern.team31project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Utils {

    // update token when login
    public static void updateToken(String userid) {
        // 用于获取设备令牌
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // 表示获取异步任务的结果，并将设备令牌存储在一个名为 token 的字符串变量中
                String token = task.getResult();
                updateToken(userid, token);  // uid和token设备令牌一起更新
            }
        });
    }

    public static void updateToken(String userid, String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tokens");
        databaseReference.child(userid).setValue(new Token(token));
    }

    public static void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid() != null) {
            Utils.updateToken(firebaseAuth.getUid(), "");
            firebaseAuth.signOut();
        }
    }
}
