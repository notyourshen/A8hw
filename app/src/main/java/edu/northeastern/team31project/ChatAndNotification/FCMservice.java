package edu.northeastern.team31project.ChatAndNotification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import edu.northeastern.team31project.Contact.Contact;
import edu.northeastern.team31project.R;

public class FCMservice extends FirebaseMessagingService {
    private static final String NameOfChannel = "A8_stick";
    private static final String idOfChannel = "edu.northeastern.team31project.A8_stick";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        updateDeviceToken(this, token);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String image = remoteMessage.getData().get("img");
        String senderUid = remoteMessage.getData().get("sender");
        String senderName = remoteMessage.getData().get("senderName");
        String receiverUid = remoteMessage.getData().get("receiver");
        String receiverName = remoteMessage.getData().get("receiverName");

        Intent intent = new Intent(this, Contact.class);

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(idOfChannel, NameOfChannel, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chat App notifications");
            notificationManager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(this, idOfChannel);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ChatAdapter.getStickerId(image));
        notificationBuilder
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_account)
                .setLargeIcon(bitmap)
                .setAutoCancel(true);

        notificationManager.notify(999, notificationBuilder.build());
    }


    public static void updateDeviceToken(final Context context, String token) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference1 = databaseReference.child("tokens").child(firebaseUser.getUid());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("tokens", token);

            databaseReference1.setValue(hashMap).addOnCompleteListener(task -> {
               if (!task.isSuccessful()) {
                   Toast.makeText(context, "update failed", Toast.LENGTH_SHORT).show();
               }
            });
        }
    }
}
