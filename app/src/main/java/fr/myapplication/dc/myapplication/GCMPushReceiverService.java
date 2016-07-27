package fr.myapplication.dc.myapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by chris on 03/05/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String message;
int numNotif = 0;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        data.getString("message");
        if(from.startsWith("/topic/")){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }else{


        }
        sendNotification(message);
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String message) {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(this, LoginActivity.class);
        /*////////////////////////recup du message///////////////////////////
        String leMessageDansNotification = message;
        resultIntent.putExtra("leMessage",leMessageDansNotification);
        /////////////////////////////////////////////////////////////////////*/
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Ajoute la pile de retour
        stackBuilder.addParentStack(LoginActivity.class);
        //Ajoute l'intention de la partie supérieure de la pile
        stackBuilder.addNextIntent(resultIntent);
        //Obtient un PendingIntent contenant l'ensemble du dos stack
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //Build notification
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.wizzwizz)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentTitle("Wizzon")
                .setContentText(message)
                .setAutoCancel(true)
                .setTicker("Nouveau WIZZ")
                .setNumber(++ numNotif)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[] {0, 100, 100, 300, 200, 1000, 100, 200, 150, 600, 30})
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.wizzon, "Retour",resultPendingIntent)
                .setWhen(60000);
        /*int numMessages = 0;
        //Début d'une boucle qui traite les données et avertit l'utilisateur
        noBuilder.setContentText(currentText)
                .setNumber(++numMessages);*/
        noBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //ma notification se met à jour si l'ID est inchangé
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

}
