package fr.myapplication.dc.myapplication;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chris on 09/05/2016.
 */
public class UserListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String url = "http://www.chrystduhamel.fr:8080/Vibe/rest/user";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllUserJSON();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    private void getAllUserJSON() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), url, null, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    if (responseBody != null) {
                        response = new String(responseBody, "UTF-8");

                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        try {
                            // JSON Array
                            JSONArray jsonArray = new JSONArray(response);
                            // When the JSON response has status boolean value assigned with true
                            if (statusCode == 200) {
                                ArrayList<User> userList = JsonToArrayList(jsonArray);
                                UserArrayAdapter uaa = new UserArrayAdapter(getActivity(),userList);
                                setListAdapter(uaa);
                                Toast.makeText(getActivity(), "JSON récup", Toast.LENGTH_LONG).show();
                            }
                            // Else display error message
                            else {
                                Toast.makeText(getActivity(), "Aucune réponse", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getActivity(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Mauvais mot de passe, le serveur renvoi user = null", Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private ArrayList<User> JsonToArrayList(JSONArray jsonArray) {
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setId(obj.getInt("id"));

                user.setLogin(obj.getString("login"));

                userList.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }
}

class UserArrayAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final ArrayList<User> values;

    public UserArrayAdapter(Context context, ArrayList<User> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_item_list, parent, false);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
        /*/////////////////////if i use a listView//////////////////////////////
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        /////////////////////////////////////////////////////////////////////*/
        checkBox.setText(values.get(position).getLogin());
        return rowView;
    }
}
