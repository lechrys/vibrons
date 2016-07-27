package fr.myapplication.dc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class SplashScreen extends Activity {
    private Vibrator vibrator;
    private Animation shake;
    private ImageView image;
    private AudioManager mAudioManager;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    //vibrate
    long[] pattern = {
            0, 100, 100, 300, 200, 1000, 100, 200, 150, 600, 30
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        image = (ImageView)findViewById(R.id.Logo);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        ////////////////////////////////////////////////////////
        vibrator.vibrate(pattern,-1);
        ///////////////////////////////////////////////////////
        //vibration au demarrage pendant 3secande
        /*vibrator.vibrate(3000);*/
        onShakeImage();

        if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            boolean mobile = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            Log.d("NetworkState", "L'interface de connexion active est du mobile : " + mobile);
        }else {
            //no connection
            Toast toast = Toast.makeText(SplashScreen.this, "Vérifier votre connexion internet", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();

        }

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
    }//onCreate

    public void onShakeImage() {
        image.setAnimation(shake);
    }//onShakeImage

    }//class
