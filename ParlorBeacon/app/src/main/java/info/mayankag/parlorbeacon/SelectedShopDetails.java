package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class SelectedShopDetails extends AppCompatActivity {

    private TextView shopkeeperNameText;
    private TextView shopNameText;
    private TextView shopEmailText;
    private TextView shopPhoneText;
    private TextView shopAddressText;
    private TextView shopCityText;
    private TextView shopZipText;
    private TextView shopOpenTimeText;
    private TextView shopCloseTimeText;
    private String shopEmail;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_shop_details);

        shopkeeperNameText = (TextView) findViewById(R.id.shopkeeperNameSelected);
        shopNameText = (TextView) findViewById(R.id.shopNameSelected);
        shopEmailText = (TextView) findViewById(R.id.shopEmailSelected);
        shopPhoneText = (TextView) findViewById(R.id.shopPhoneSelected);
        shopAddressText = (TextView) findViewById(R.id.shopAddressSelected);
        shopOpenTimeText = (TextView) findViewById(R.id.shopOpenTimeSelected);
        shopCloseTimeText = (TextView) findViewById(R.id.shopCloseTimeSelected);
        shopCityText = (TextView) findViewById(R.id.shopCitySelected);
        shopZipText = (TextView) findViewById(R.id.shopZipSelected);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shopEmail = extras.getString("ShopEmail");
            shopName = extras.getString("ShopName");
            setTitle(shopName);
            showSelectedShopDetails();
        }
    }

    public void book(View view) {
        Intent intent = new Intent(this, ServiceSelectCust.class);
        intent.putExtra("ShopEmail", shopEmail);
        startActivity(intent);
        finish();
    }

    private void showSelectedShopDetails() {
        if (!UtilsCust.isNetworkAvailable(this)) {
            UtilsCust.NetworkToast(this);
        } else {

            JSONObject params = new JSONObject();
            try {
                params.put("email", shopEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String selected_shop_details_url = getResources().getString(R.string.selected_shop_details_url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, selected_shop_details_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray detail = response.getJSONArray("shopdata");

                        JSONObject singledetail = detail.getJSONObject(0);

                        shopkeeperNameText.setText(singledetail.getString("shopkeepername"));
                        shopNameText.setText(shopName);
                        shopEmailText.setText(shopEmail);
                        shopPhoneText.setText(singledetail.getString("phoneno"));
                        shopAddressText.setText(singledetail.getString("shopaddress"));
                        shopOpenTimeText.setText(singledetail.getString("starttime"));
                        shopCloseTimeText.setText(singledetail.getString("endtime"));
                        shopZipText.setText(singledetail.getString("zipcode"));
                        shopCityText.setText(singledetail.getString("city"));
                    } catch (JSONException e) {
                        Toast.makeText(SelectedShopDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SelectedShopDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
