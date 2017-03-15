package info.mayankag.parlorbeacon;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditShopDetails extends AppCompatActivity {

    private EditText shopkeeperNameEdit;
    private EditText shopkeeperEmailEdit;
    private EditText shopNameEdit;
    private EditText shopPhoneEdit;
    private EditText shopAddressEdit;
    private EditText shopCityEdit;
    private EditText shopZipEdit;
    private EditText shopOpenTimeEdit;
    private EditText shopCloseTimeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_details);

        shopkeeperNameEdit = (EditText) findViewById(R.id.edit_shop_shopkeeperName);
        shopkeeperEmailEdit = (EditText) findViewById(R.id.edit_shop_email);
        shopNameEdit = (EditText) findViewById(R.id.edit_shop_name);
        shopPhoneEdit = (EditText) findViewById(R.id.edit_shop_phone);
        shopAddressEdit = (EditText) findViewById(R.id.edit_shop_address);
        shopCityEdit = (EditText) findViewById(R.id.edit_shop_city);
        shopZipEdit = (EditText) findViewById(R.id.edit_shop_zip);
        shopOpenTimeEdit = (EditText) findViewById(R.id.edit_shop_openTime);
        shopCloseTimeEdit = (EditText) findViewById(R.id.edit_shop_closeTime);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            shopkeeperNameEdit.setText(extra.getString("ShopkeeperName"));
            shopkeeperEmailEdit.setText(extra.getString("ShopkeeperEmail"));
            shopNameEdit.setText(extra.getString("ShopName"));
            shopPhoneEdit.setText(extra.getString("ShopPhone"));
            shopAddressEdit.setText(extra.getString("ShopAddress"));
            shopCityEdit.setText(extra.getString("ShopCity"));
            shopZipEdit.setText(extra.getString("ShopZip"));
            shopOpenTimeEdit.setText(extra.getString("ShopOpenTime"));
            shopCloseTimeEdit.setText(extra.getString("ShopCloseTime"));
        }

    }

    public void onClickOpenTimeEdit() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditShopDetails.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String openTime;
                if (selectedHour > 12) {
                    openTime = String.valueOf(selectedHour - 12) + ":" + String.valueOf(selectedMinute) + "PM";
                    shopOpenTimeEdit.setText(openTime);
                } else {
                    openTime = String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute) + "AM";
                    shopOpenTimeEdit.setText(openTime);
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Open Time");
        mTimePicker.show();
    }

    public void onClickCloseTimeEdit() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditShopDetails.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String closeTime;
                if (selectedHour > 12) {
                    closeTime = String.valueOf(selectedHour - 12) + ":" + String.valueOf(selectedMinute) + "PM";
                    shopCloseTimeEdit.setText(closeTime);
                } else {
                    closeTime = String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute) + "AM";
                    shopCloseTimeEdit.setText(closeTime);
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Close Time");
        mTimePicker.show();
    }

    public void onClickUpdateDetail() {
        if (!UtilsShop.isNetworkAvailable(this)) {
            UtilsShop.NetworkToast(this);
        } else {
            String updateDetails_url = getResources().getString(R.string.updateDetails_url);
            final ProgressDialog progressDialog = ProgressDialog.show(EditShopDetails.this, "Updating", "Please Wait", true, true);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, updateDetails_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        if (response.equalsIgnoreCase("success")) {
                            Toast.makeText(EditShopDetails.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditShopDetails.this, ShopDetails.class));
                            finish();
                        } else if (response.equalsIgnoreCase("failed")) {
                            Toast.makeText(EditShopDetails.this, "Sorry Details cannot be updated ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditShopDetails.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditShopDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(EditShopDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("shopkeepername", shopkeeperNameEdit.getText().toString());
                    parameters.put("shopname", shopNameEdit.getText().toString());
                    parameters.put("email", shopkeeperEmailEdit.getText().toString());
                    parameters.put("phoneno", shopPhoneEdit.getText().toString());
                    parameters.put("shopaddress", shopAddressEdit.getText().toString());
                    parameters.put("city", shopCityEdit.getText().toString());
                    parameters.put("zipcode", shopZipEdit.getText().toString());
                    parameters.put("starttime", shopOpenTimeEdit.getText().toString());
                    parameters.put("endtime", shopCloseTimeEdit.getText().toString());
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}

