package info.mayankag.parlorbeacon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import info.mayankag.parlorbeacon.Database.DBContentProvider;
import info.mayankag.parlorbeacon.Database.DBContract;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivityCust extends AppCompatActivity {

    private EditText nameText;
    private EditText phoneText;
    private EditText emailText;
    private EditText dateText;
    private String service;
    private String serviceTime;
    private String shopEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_cust);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            service = extras.getString("ServiceName");
            serviceTime = extras.getString("ServiceTime");
            shopEmail = extras.getString("ShopEmail");
        }

        nameText = (EditText)findViewById(R.id.bookingName);
        phoneText = (EditText)findViewById(R.id.bookingPhone);
        emailText = (EditText)findViewById(R.id.bookingEmail);
        dateText = (EditText)findViewById(R.id.bookingDate);

        nameText.setText(UtilsCust.loadCustName(this));
        emailText.setText(UtilsCust.loadCustEmail(this));

    }

    public void bookingDateSelect(View view)
    {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        final int month=cal.get(Calendar.MONTH);
        final int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                dateText.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(year));
            }
        },year,month,day);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() + 100000000);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    public void book(View view)
    {

        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String phone = phoneText.getText().toString();
        final String date = dateText.getText().toString();

        if (!UtilsCust.isNetworkAvailable(this)) {
            UtilsCust.NetworkToast(this);
        } else {

            if(!name.equals("") && !email.equals("") && !phone.equals("") && !date.equals("")) {
                String booking_cust_url = getResources().getString(R.string.booking_cust_url);
                final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Signing in...", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, booking_cust_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            if (response.equals("Success")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBContract.PB_DB_Entry.SHOPEMAIL,response);
                                contentValues.put(DBContract.PB_DB_Entry.CUSTEMAIL,UtilsCust.loadCustEmail(BookingActivityCust.this));
                                contentValues.put(DBContract.PB_DB_Entry.CUSTNAME,UtilsCust.loadCustName(BookingActivityCust.this));
                                contentValues.put(DBContract.PB_DB_Entry.CUSTPHONE,phoneText.getText().toString());
                                contentValues.put(DBContract.PB_DB_Entry.DATE,date);
                                contentValues.put(DBContract.PB_DB_Entry.TIME,serviceTime);
                                contentValues.put(DBContract.PB_DB_Entry.SERVICE,service);
                                contentValues.put(DBContract.PB_DB_Entry.STATUS,"PENDING");
                                getContentResolver().insert(DBContentProvider.CONTENT_URI,contentValues);
                                startActivity(new Intent(BookingActivityCust.this, BookingSuccessCust.class));
                                finish();
                            }
                            else
                                UtilsCust.ShortToast(BookingActivityCust.this,"BookingCust Failed.");
                        } catch (Exception e) {
                            UtilsCust.ShortToast(BookingActivityCust.this, getString(R.string.error_contact_developer_message));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        UtilsCust.ShortToast(BookingActivityCust.this,  getString(R.string.error_contact_developer_message));
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("shopemail", shopEmail);
                        parameters.put("custname", name);
                        parameters.put("custemail", email);
                        parameters.put("custphone", phone);
                        parameters.put("servicename",service);
                        parameters.put("servicedate", date);
                        parameters.put("servicetime", serviceTime);
                        return parameters;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
            else
                Toast.makeText(BookingActivityCust.this,"Please fill all the details",Toast.LENGTH_SHORT).show();
        }
    }

}
