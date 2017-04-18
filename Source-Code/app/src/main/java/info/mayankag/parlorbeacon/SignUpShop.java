package info.mayankag.parlorbeacon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpShop extends DialogFragment {

    private EditText shopkeeperNameText;
    private EditText emailText;
    private EditText passText;
    private EditText shopNameText;
    private EditText shopPhoneText;
    private EditText shopAddressText;
    private EditText shopCityText;
    private EditText shopZipText;
    private EditText shopOpenTimeText;
    private EditText shopCloseTimeText;
    private CheckBox agree;
    private Button signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_shop, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Signup");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        shopkeeperNameText = (EditText) v.findViewById(R.id.signup_shop_shopkeeperName);
        emailText = (EditText) v.findViewById(R.id.signup_shop_email);
        passText = (EditText) v.findViewById(R.id.signup_shop_pass);

        shopNameText = (EditText) v.findViewById(R.id.signup_shop_name);
        shopPhoneText = (EditText) v.findViewById(R.id.signup_shop_phone);
        shopAddressText = (EditText) v.findViewById(R.id.signup_shop_address);
        shopCityText = (EditText) v.findViewById(R.id.signup_shop_city);
        shopZipText = (EditText) v.findViewById(R.id.signup_shop_zip);
        shopOpenTimeText = (EditText) v.findViewById(R.id.signup_shop_openTime);
        shopCloseTimeText = (EditText) v.findViewById(R.id.signup_shop_closeTime);

        agree = (CheckBox) v.findViewById(R.id.acceptTC);
        signup = (Button) v.findViewById(R.id.signup_shop_button);

        shopOpenTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mopenTime = Calendar.getInstance();
                int hour = mopenTime.get(Calendar.HOUR_OF_DAY);
                int minute = mopenTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String openTime;
                        if (selectedHour > 12) {
                            openTime = String.valueOf(selectedHour - 12) + ":" + String.valueOf(selectedMinute) + "PM";
                            shopOpenTimeText.setText(openTime);
                        } else {
                            openTime = String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute) + "AM";
                            shopOpenTimeText.setText(openTime);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Open Time");
                mTimePicker.show();
            }
        });

        shopCloseTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcloseTime = Calendar.getInstance();
                int hour = mcloseTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcloseTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String closeTime;
                        if (selectedHour > 12) {
                            closeTime = String.valueOf(selectedHour - 12) + ":" + String.valueOf(selectedMinute) + "PM";
                            shopCloseTimeText.setText(closeTime);
                        } else {
                            closeTime = String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute) + "AM";
                            shopCloseTimeText.setText(closeTime);
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Close Time");
                mTimePicker.show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        return v;
    }

    private void signup() {
        if (!UtilsShop.isNetworkAvailable(getActivity())) {
            UtilsShop.NetworkToast(getActivity());
        } else {
            final String shopkeepername = shopkeeperNameText.getText().toString();
            final String shopemail = emailText.getText().toString();
            final String shoppass = passText.getText().toString();

            final String shopname = shopNameText.getText().toString();
            final String shopphone = shopPhoneText.getText().toString();
            final String shopaddr = shopAddressText.getText().toString();
            final String shopcity = shopCityText.getText().toString();
            final String shopzip = shopZipText.getText().toString();
            final String shopopen = shopOpenTimeText.getText().toString();
            final String shopclose = shopCloseTimeText.getText().toString();

            if (shopkeepername.equals("") || shopname.equals("") || shopemail.equals("") || shoppass.equals("") || shopphone.equals("") || shopaddr.equals("") || shopcity.equals("") || shopzip.equals("") || shopopen.equals("") || shopclose.equals("")) {
                UtilsShop.ShortToast(getActivity(), getString(R.string.enter_all_detail_text));
            } else if (!agree.isChecked()) {
                UtilsShop.ShortToast(getActivity(), getString(R.string.terms_and_conditions_text));
            } else {
                if (!shopemail.contains("@") && !shopemail.contains(".com")) {
                    UtilsShop.ShortToast(getActivity(), getString(R.string.valid_email_text));
                } else {
                    if (!(shoppass.length() > 8)) {
                        UtilsShop.ShortToast(getActivity(), getString(R.string.valid_password_text));
                    } else {
                        String signup_url = getResources().getString(R.string.signup_shop_url);
                        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Signing Up", "Please Wait", true, true);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {

                                    if (response.equalsIgnoreCase(String.valueOf(0))) {
                                        UtilsShop.saveShopEmail(shopemail, getActivity());
                                        UtilsShop.saveShopkeeperName(shopkeepername, getActivity());
                                        startActivity(new Intent(getActivity(), MainScreenShop.class));
                                        getActivity().finish();
                                    } else if (response.equalsIgnoreCase(String.valueOf(1))) {
                                        UtilsShop.ShortToast(getActivity(), getString(R.string.already_registered_text));
                                    } else if (response.equalsIgnoreCase(String.valueOf(2))) {
                                        UtilsShop.ShortToast(getActivity(), getString(R.string.error_text));
                                    } else {
                                        UtilsShop.ShortToast(getActivity(), response);
                                    }
                                } catch (Exception e) {
                                    UtilsShop.ShortToast(getActivity(), getString(R.string.error1_text));
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                UtilsShop.ShortToast(getActivity(), error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("shopkeepername", shopkeepername);
                                parameters.put("shopname", shopname);
                                parameters.put("email", shopemail);
                                parameters.put("password", shoppass);
                                parameters.put("shopaddress", shopaddr);
                                parameters.put("phoneno", shopphone);
                                parameters.put("starttime", shopopen);
                                parameters.put("endtime", shopclose);
                                parameters.put("city", shopcity);
                                parameters.put("zipcode", shopzip);
                                return parameters;
                            }
                        };

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(stringRequest);
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_signup_shop, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Shopkeeper.relativeLayout.setVisibility(View.VISIBLE);
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
