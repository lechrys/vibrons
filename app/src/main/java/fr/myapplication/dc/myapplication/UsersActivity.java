package fr.myapplication.dc.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by gm-tech on 18/05/2016.
 */
public class UsersActivity extends AppCompatActivity {
    private CheckBox laCheckBox;
    private Button validerAjout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        validerAjout = (Button) findViewById(R.id.ajoutLesContacts);
        laCheckBox = (CheckBox) findViewById(R.id.checkbox);

    }//onCreate

    private void onCheckboxClicked(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()) {
            case R.id.checkbox:
                if (checked) {
                    Toast.makeText(getApplicationContext(),"selectionné",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"partie",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }//onCheckboxClicked
}//class
