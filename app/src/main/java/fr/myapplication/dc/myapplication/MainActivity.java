package fr.myapplication.dc.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.Base64;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewLoginInMenu,textViewInMainActivit;
    private EditText loginView;
    private String leLoginActivity;
    private Button buttonVersUp;
    private ImageView avatarActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide la barre noir en haut
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recuperer le login de l'utilisateur pour le mettre dans le menu
        loginView = (AutoCompleteTextView) findViewById(R.id.editTextCoLogin);
        //champs pour recuperer le pseudo
        textViewLoginInMenu = (TextView) findViewById(R.id.textViewNomUtilisateur);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////
        //Le header permet d'accéder aux composants dans la navbar
        View header = navigationView.getHeaderView(0);
        //mon élément à mon menu
        textViewLoginInMenu = (TextView) findViewById(R.id.textViewNomUtilisateur);
        textViewLoginInMenu = (TextView) header.findViewById(R.id.textViewNomUtilisateur);
        avatarActivity = (ImageView)header.findViewById(R.id.avatarUtilisateur);
        //Récupération des putExtras fait dans le login activity
        String leLogin, avatar = "null";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                leLogin = null;
            } else {
                leLogin = extras.getString("login");
                avatar = extras.getString("avatar");
            }
        } else {
            leLogin = (String) savedInstanceState.getSerializable("leLogin");
            avatar = (String) savedInstanceState.getSerializable("avatar");
        }

        textViewLoginInMenu.setText(leLogin);
        if(avatar != "null"){
            byte[] img = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            avatarActivity.setImageBitmap(bitmap);
            Log.v("avatar", "avatar bon");
        }else {
            avatarActivity.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_gallery));
            Log.v("avatar", "avatar pas bon");
        }
        ///////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////
                    //////////////////////////////////////
                    /////////////////////////////////////
/*
//////////////////Récupération du login pour le mettre dans le menu////////////////////////////////
        //je regarde mon menu en entier (a garder)
       View header = navigationView.getHeaderView(0);
        //mon élément à mon menu
        textViewLoginInMenu = (TextView) findViewById(R.id.textViewNomUtilisateur);
        textViewLoginInMenu = (TextView) header.findViewById(R.id.textViewNomUtilisateur);
        //Code pour recupérer l'élément et le mettre à l'endroid souhaité
        String data = getIntent().getExtras().getString("leLogin");
            textViewLoginInMenu.setText(data);
            Log.v("EditText", textViewLoginInMenu.toString());
//////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////Récupération de l'avatar///////////////////////////////////////////////////
        avatarActivity = (ImageView)header.findViewById(R.id.avatarUtilisateur);
        String avatar = "null";
        avatar = getIntent().getExtras().getString("avatar");
        //Code pour recupérer l'élément et le mettre à l'endroid souhaité
        if(avatar != "null"){
            byte[] img = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            avatarActivity.setImageBitmap(bitmap);
            Log.v("avatar", "avatar bon");
        }else {
            avatarActivity.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_gallery));
            Log.v("avatar", "avatar pas bon");
        }*/
                        ////////////////////////////////////////
                        ///////////////////////////////////////

///////////////////////Je recupère le message//////////////////////////////////////////////////////
        /*textViewInMainActivity = (TextView) findViewById(R.id.textViewMessage);
        String dataMess = getIntent().getExtras().getString("leMessage");
        textViewInMainActivity.setText(dataMess);*/
        /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Vers update contact///////////////////////////////
        buttonVersUp = (Button) findViewById(R.id.buttonVersAjoutContact);
        buttonVersUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UsersActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view) {

            // Handle the camera action
        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_parametres) {
            Intent i = new Intent(this,ChangeLoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_deconnexion) {
            finish();
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        return;
    }



}//CLASS
