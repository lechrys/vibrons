package fr.myapplication.dc.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class UpdateContact extends AppCompatActivity {
    private static final String url = "http://www.chrystduhamel.fr:8080/Vibe/rest/user";

    private TextView textViewNum,textViewName;
    private EditText nomActivity,numeroActivity;
    private Button buttonAjoutContact;
    private String nom, numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

/////////////////////////////////////update contact///////////////////////////////
        textViewNum = (TextView) findViewById(R.id.textViewNumeroAjout);
        textViewName = (TextView) findViewById(R.id.textViewNomAjout);
        nomActivity = (EditText) findViewById(R.id.editTextNomAjout);
        numeroActivity = (EditText) findViewById(R.id.editTextNumeroAjout);
        buttonAjoutContact = (Button) findViewById(R.id.buttonUpContact);

buttonAjoutContact.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String leNom = textViewName.getText().toString();
        String leNumero = textViewNum.getText().toString();

        if (!textViewNum.getText().toString().equals("") || !textViewName.getText().toString().equals("")) {
            ajoutContact(leNom,leNumero);
        }else{
            Toast.makeText(UpdateContact.this, "Vérifier vos champs !", Toast.LENGTH_LONG).show();
        }
    }
});
    }//onCreate

    public void ajoutContact(String leNom, String leNumero) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jo = new JSONObject();
        try {
            jo.put("nom", leNom);
            jo.put("numero", leNumero);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity user = null;
        try{
            user = new StringEntity(jo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.put(UpdateContact.this, url + "/update", user, "application/json", new AsyncHttpResponseHandler() {

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
                        Intent i = new Intent(UpdateContact.this, MainActivity.class);
                        startActivity(i);
                    }//if
                    else {

                        Toast.makeText(getApplicationContext(), "Aucune réponse", Toast.LENGTH_LONG).show();
                    }
                }catch(JSONException e){
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }//catch
            }//onSucess

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
            }//onFailure
        });//client.put
    }//ajoutContact


}//class
