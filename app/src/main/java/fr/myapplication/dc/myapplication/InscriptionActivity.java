package fr.myapplication.dc.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class InscriptionActivity extends AppCompatActivity {
    private static final String url = "http://www.chrystduhamel.fr:8080/Vibe/rest/user";
    private String token;
    private static final int RESULT_LOAD_IMAGE = 1;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "RegIntentService";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText editTextLoginIns, editTextMdp, editTextMdpConfirm;
    private View mProgressView;
    private View mLoginFormView;
    private Button buttonPourInscription, buttonAvat;
    InstanceID instanceID;
    private String avatar = null;


    public InscriptionActivity() {
        token = null;
    }

    /*String token= null;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        // Set up the login form.
        editTextLoginIns = (EditText) findViewById(R.id.pseudoInscription);
        editTextMdp = (EditText) findViewById(R.id.mdpInscription);
        editTextMdpConfirm = (EditText) findViewById(R.id.mdpInscriptionConfirm);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                   /*final String token = intent.getStringExtra("token");*/
                    InscriptionActivity.this.token = intent.getStringExtra("token");
                    Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            InscriptionActivity.this.startService(itent);
        }

        buttonAvat = (Button)findViewById(R.id.buttonAvatar);
        buttonAvat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });

        Button buttonPourInscription = (Button) findViewById(R.id.buttonInscription);
        assert buttonPourInscription != null;
        buttonPourInscription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = editTextLoginIns.getText().toString();
                String pwd1 = editTextMdp.getText().toString();
                String pwd2 = editTextMdpConfirm.getText().toString();

                if (!login.equals("") && !pwd1.equals("") && !pwd2.equals("")) {
                    if (pwd1.equals(pwd2)) {
                        //bouton en disable lors d'un click
                        v.setEnabled(false);

                        createUser(login, pwd1, token, avatar);
                        Toast.makeText(InscriptionActivity.this, InscriptionActivity.this.url + "/add", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InscriptionActivity.this, "Les mots de passe sont différents !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                }

            }

        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    public void createUser(String login, String password, String token, String avatar) {
        AsyncHttpClient client = new AsyncHttpClient();

        JSONObject jo = new JSONObject();
        try {
            jo.put("login", login);
            jo.put("password", password);
            jo.put("regid", token);
            if(avatar != null){
                jo.put("avatar", avatar);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity user = null;
        try {
            user = new StringEntity(jo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(InscriptionActivity.this, this.url + "/add", user, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), responseBody.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (statusCode == 200) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        Intent i = new Intent(InscriptionActivity.this, MainActivity.class);
 ////////////////////////////////Récupération de mon login dans l'editText//////////////////////////////
                        String leLoginInInscriptionActivaty = editTextLoginIns.getText().toString();
                        i.putExtra("leLogin",leLoginInInscriptionActivaty);

///////////////////////////////////////////////////////////////////////////////////////////////////////
                        startActivity(i);
                    }
                    // Else display error message
                    else {

                        Toast.makeText(getApplicationContext(), "Aucune réponse", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public class ReggistrationIntentService extends IntentService {

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public ReggistrationIntentService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // [END get_token]

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }




        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_LONG).show();

            avatar = Base64.encodeToString(getBytesFromBitmap(BitmapFactory.decodeFile(picturePath)), Base64.NO_WRAP);
        }//if
    }//onActivityResult

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }//getBytesFromBitmap

}

