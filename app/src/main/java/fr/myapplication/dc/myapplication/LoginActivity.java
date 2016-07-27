package fr.myapplication.dc.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final String url = "http://www.chrystduhamel.fr:8080/Vibe/rest/user";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

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
    private AutoCompleteTextView loginView;
    private EditText PasswordView;
    private View mLoginFormView;
    private Button laConnection;
    private Button buttonVersIns;

    private ProgressBar mProgressView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        loginView = (AutoCompleteTextView) findViewById(R.id.editTextCoLogin);
        populateAutoComplete();

        buttonVersIns = (Button)findViewById(R.id.buttonVersIns);

        buttonVersIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, InscriptionActivity.class);
                startActivity(i);
            }
        });

        PasswordView = (EditText) findViewById(R.id.editTextCoPassword);
        PasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editTextCoLogin || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mProgressView.setMax(100);

        laConnection = (Button) findViewById(R.id.email_sign_in_button);
        laConnection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loginView.getText().toString().equals("") || !PasswordView.getText().toString().equals("")) {
                    /*Intent i = new Intent();
                    *//*intentionResultat.putExtra("loginUtilisateur", loginView.getText().toString());*//*
                    String leLoginInLoginActivaty = loginView.getText().toString();
                    i.putExtra("leLogin",leLoginInLoginActivaty);
                    Log.v("EditText", leLoginInLoginActivaty.toString());
                              Log.v("EditText", loginView.getText().toString());*/



                    authUser(loginView.getText().toString(), PasswordView.getText().toString());
                    LoginActivity.this.mProgressView.setProgress(0);
                } else {
                    Toast.makeText(LoginActivity.this, "Veuillez entrer votre login et votre mot de passe !", Toast.LENGTH_LONG).show();
                }
            }
        });



        mLoginFormView = findViewById(R.id.login_form);

    }

    public void authUser(final String login, final String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jo = new JSONObject();
        try {
            jo.put("login", login);
            jo.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity user = null;
        try {
            user = new StringEntity(jo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(LoginActivity.this, url + "/auth", user, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                boolean cancel = false;
                View focusView = null;
                try {
                    if (responseBody != null) {
                        response = new String(responseBody, "UTF-8");

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            // JSON Object
                            JSONObject obj = new JSONObject(response);

                            // When the JSON response has status boolean value assigned with true
                            if (statusCode == 200) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
////////////////////////////////Récupération de mon login dans l'editText//////////////////////////////
                                User user = new User();
                                if (obj.has("avatar")){
                                    user.setAvatar(obj.getString("avatar"));
                                }
                                String leLoginInLoginActivaty = loginView.getText().toString();
                                String leMDPInLoginActivaty = PasswordView.getText().toString();
                                i.putExtra("leLogin",leLoginInLoginActivaty);
                                i.putExtra("leMDP",leMDPInLoginActivaty);
                                if(user.getAvatar() != null){
                                    i.putExtra("avatar", user.getAvatar());
                                }else{
                                    i.putExtra("avatar", "null");
                                }
                                Toast.makeText(getApplicationContext(),user.getAvatar(), Toast.LENGTH_LONG).show();
                                Log.v("Login", leLoginInLoginActivaty.toString());
                                Log.v("MDP", leMDPInLoginActivaty.toString());
///////////////////////////////////////////////////////////////////////////////////////////////////////

                                Toast.makeText(getApplicationContext(), "Vous êtes connecté", Toast.LENGTH_LONG).show();
                                startActivity(i);
                                // Navigate to Home screen
                                if (cancel) {
                                    // There was an error; don't attempt login and focus the first
                                    // form field with an error.
                                    focusView.requestFocus();
                                } else {
                                    // Show a progress spinner, and kick off a background task to
                                    // perform the user login attempt.
                                    showProgress(true);
                                    mAuthTask = new UserLoginTask(login, password);
                                    mAuthTask.execute((Void) null);
                                }

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

                    } else {
                        Toast.makeText(getApplicationContext(), "Mauvais mot de passe, le serveur renvoi user = null", Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
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


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(loginView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        loginView.setError(null);
        PasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = loginView.getText().toString();
        String password = PasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = PasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            loginView.setError(getString(R.string.error_field_required));
            focusView = loginView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            loginView.setError(getString(R.string.error_invalid_email));
            focusView = loginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        loginView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
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
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                PasswordView.setError(getString(R.string.error_incorrect_password));
                PasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

