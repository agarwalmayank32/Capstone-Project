package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ShopDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profilePic;
    private ImageView frontPic;
    private TextView shopkeeperName;
    private TextView shopkeeperEmail;
    private TextView shopName;
    private TextView shopPhone;
    private TextView shopAddress;
    private TextView shopCity;
    private TextView shopZip;
    private TextView shopOpenTime;
    private TextView shopCloseTime;

    private static final int PICK_IMAGE_ID1 = 0;
    private static final int PICK_IMAGE_ID2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        profilePic = (ImageView)findViewById(R.id.shopkeeperImage);
        frontPic = (ImageView)findViewById(R.id.shopImage);

        shopkeeperName = (TextView) findViewById(R.id.shopKeeperName);
        shopkeeperEmail = (TextView)findViewById(R.id.shopKeeperEmail);
        shopName = (TextView)findViewById(R.id.shopName);
        shopPhone = (TextView)findViewById(R.id.shopPhone);
        shopAddress = (TextView)findViewById(R.id.shopAddress);
        shopCity = (TextView)findViewById(R.id.shopCity);
        shopZip = (TextView)findViewById(R.id.shopZipCode);
        shopOpenTime = (TextView)findViewById(R.id.shopOpenTime);
        shopCloseTime = (TextView)findViewById(R.id.shopCloseTime);

        ShopDetailsSetup();
    }

    private void ShopDetailsSetup()
    {
        if(!UtilsShop.isNetworkAvailable(this))
        {
            UtilsShop.NetworkToast(this);
        }
        else
        {
            String getDetails_url = getResources().getString(R.string.getDetails_url);
            String getPic_url = getResources().getString(R.string.getPic_url);

            JSONObject params = new JSONObject();
            try {
                params.put("email", UtilsShop.loadShopEmail(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getDetails_url,params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try
                    {
                        JSONArray detail=response.getJSONArray("info");
                        if(detail.length()>0)
                        {
                            for(int i=0;i<detail.length();i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                shopkeeperName.setText(singledetail.getString("shopkeepername"));
                                shopkeeperEmail.setText(UtilsShop.loadShopEmail(ShopDetails.this));
                                shopName.setText(singledetail.getString("shopname"));
                                shopPhone.setText(singledetail.getString("phoneno"));
                                shopAddress.setText(singledetail.getString("shopaddress"));
                                shopCity.setText(singledetail.getString("city"));
                                shopZip.setText(singledetail.getString("zipcode"));
                                shopOpenTime.setText(singledetail.getString("starttime"));
                                shopCloseTime.setText(singledetail.getString("endtime"));
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ShopDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ShopDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

            Picasso.with(ShopDetails.this).load(getPic_url+ UtilsShop.loadShopEmail(this)+"/profilepic.png").error(R.drawable.ic_npi).into(profilePic);
            Picasso.with(ShopDetails.this).load(getPic_url+ UtilsShop.loadShopEmail(this)+"/frontpic.png").error(R.drawable.ic_nfi).into(frontPic);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        UtilsShop.onNaviationItemSelectedShop(this,item);
        return true;
    }

    public void onClickEditShopkeeperImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID1);
    }

    public void onClickEditShopImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID1:
                Bitmap bitmap1 = ImagePicker.getImageFromResult(this, resultCode, data);
                if(bitmap1!=null) {
                    profilePic.setImageBitmap(bitmap1);
                    uploadImage(bitmap1,0);
                } else {
                    Toast.makeText(this,"Profile Picture Selection Canceled", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_IMAGE_ID2:
                Bitmap bitmap2 = ImagePicker.getImageFromResult(this, resultCode, data);
                if(bitmap2!=null) {
                    frontPic.setImageBitmap(bitmap2);
                    uploadImage(bitmap2,1);
                } else {
                    Toast.makeText(this,"Shop Front Picture Selection Canceled", Toast.LENGTH_SHORT).show();
                }
                break;
            default: super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void uploadImage(final Bitmap bitmap, int val){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        String UPLOAD_URL;
        if(val==0) {
            UPLOAD_URL = getResources().getString(R.string.profilePicUpload_url);
        }
        else {
            UPLOAD_URL = getResources().getString(R.string.frontPicUpload_url);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resonse) {
                        loading.dismiss();
                        if(resonse.equalsIgnoreCase("success")) {
                            Toast.makeText(ShopDetails.this, "Pic Uploaded" , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
                Toast.makeText(ShopDetails.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                String body;
                if(volleyError.networkResponse.data!=null) {
                    try {
                        body = new String(volleyError.networkResponse.data,"UTF-8");
                        Toast.makeText(ShopDetails.this,body, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(ShopDetails.this,e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                
                Map<String,String> params = new Hashtable<>();
                params.put("image", image);
                params.put("email", UtilsShop.loadShopEmail(ShopDetails.this));
                
                return params;
            }
        };
        
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    
    private String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    
    public void onClickEditShopDetails(View view) {
        Intent intent = new Intent(this,EditShopDetails.class);
        intent.putExtra("ShopkeeperName",shopkeeperName.getText().toString());
        intent.putExtra("ShopkeeperEmail",shopkeeperEmail.getText().toString());
        intent.putExtra("ShopName",shopName.getText().toString());
        intent.putExtra("ShopPhone",shopPhone.getText().toString());
        intent.putExtra("ShopAddress",shopAddress.getText().toString());
        intent.putExtra("ShopCity",shopCity.getText().toString());
        intent.putExtra("ShopZip",shopZip.getText().toString());
        intent.putExtra("ShopOpenTime",shopOpenTime.getText().toString());
        intent.putExtra("ShopCloseTime",shopCloseTime.getText().toString());
        startActivity(intent);
    }
}

