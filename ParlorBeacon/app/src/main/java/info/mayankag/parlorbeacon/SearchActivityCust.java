package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.mayankag.parlorbeacon.Adapter.SearchAdapter;
import info.mayankag.parlorbeacon.Models.Search;

public class SearchActivityCust extends AppCompatActivity{

    private ArrayList<Search> mSearches;
    private ArrayAdapter<Search> mSearchArrayAdapter;
    private ListView searchListView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cust);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView)findViewById(R.id.noShopText);
        searchListView = (ListView)findViewById(R.id.searchListView);
        mSearches = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            search(bundle.getString("Key"));
        }
    }

    private void search(String key)
    {
        if(!UtilsCust.isNetworkAvailable(this)) {
            UtilsCust.NetworkToast(this);
        }
        else {

            JSONObject params = new JSONObject();
            try {
                params.put("search", key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String search_cust_url = getResources().getString(R.string.search_cust_url) ;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, search_cust_url,params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    mSearches.clear();
                    textView.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);
                    try {
                        JSONArray detail=response.getJSONArray("data");
                        String name,rating,address,email;

                        if(detail.length()>0) {
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);

                                name = singledetail.getString("shopname");
                                rating = singledetail.getString("shoprating");
                                address = singledetail.getString("shopaddress");
                                email = singledetail.getString("email");
                                mSearches.add(new Search(name,rating,address,email));
                            }
                            mSearchArrayAdapter = new SearchAdapter(SearchActivityCust.this,mSearches);
                            searchListView.setAdapter(mSearchArrayAdapter);
                            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(SearchActivityCust.this,SelectedShopDetails.class);
                                    intent.putExtra("ShopName",mSearches.get(i).getName());
                                    intent.putExtra("ShopEmail",mSearches.get(i).getEmail());
                                    startActivity(intent);
                                }
                            });

                        }
                        else
                        {
                            textView.setText("This service is not available by any shop.");
                            textView.setVisibility(View.VISIBLE);
                            searchListView.setVisibility(View.GONE);
                        }
                    }
                    catch (JSONException e) {
                        Toast.makeText(SearchActivityCust.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SearchActivityCust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }
}
