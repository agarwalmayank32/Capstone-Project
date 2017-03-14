package info.mayankag.parlorbeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

class UtilsShop {

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    static void NetworkToast(Context context)
    {
        ShortToast(context,"Internet Not Connected. Please connect to internet and try again.");
    }

    static void ShortToast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static void savePreference(String value,Context context)
    {
        String FILENAME = "preference.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String loadPreference(Context context) {

        String FILENAME = "preference.txt";
        String out = "";
        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;
            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    static void saveShopEmail(String mail, Context context)
    {
        String FILENAME = "shopemail.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(mail.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static  String loadShopEmail(Context context) {
        String FILENAME = "shopemail.txt";
        String out = "";

        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;

            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    static void saveShopkeeperName(String name, Context context)
    {
        String FILENAME = "shopkeepername.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(name.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String loadShopkeeperName(Context context) {
        String FILENAME = "shopkeepername.txt";
        String out = "";
        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;
            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    static void saveForgetPassEmailShop(String mail,Context context)
    {
        String FILENAME = "forgetPassShopEmail.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(mail.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String loadForgetPassEmailShop(Context context) {
        String FILENAME = "forgetPassShopEmail.txt";
        String out = "";
        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;
            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    static void onNaviationItemSelectedShop(Activity currentActivity, @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int menuItemId;
        if (currentActivity.getClass() == MainScreenShop.class) {
            menuItemId = R.id.shop_todaySchedule;
        } else if (currentActivity.getClass() == MyBookingsShop.class) {
            menuItemId = R.id.shop_myBooking;
        } else if (currentActivity.getClass() == MyEmployeesShop.class) {
            menuItemId = R.id.shop_myemployee;
        } else if (currentActivity.getClass() == ServicesShop.class) {
            menuItemId = R.id.shop_services;
        } else if (currentActivity.getClass() == ShopDetails.class) {
            menuItemId = R.id.shop_details;
        } else if (currentActivity.getClass() == About.class) {
            menuItemId = R.id.about;
        } else if (currentActivity.getClass() == ContactUs.class) {
            menuItemId = R.id.contact;
        } else {
            menuItemId = -1;
        }

        int id = item.getItemId();
        if (id == menuItemId) {
            DrawerLayout drawer = (DrawerLayout)currentActivity.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return ;
        }


        if (id==R.id.shop_todaySchedule) {
            if(currentActivity.getClass() != MainScreenShop.class) {
                currentActivity.startActivity(new Intent(currentActivity, MainScreenShop.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id.shop_myBooking) {
            if(currentActivity.getClass() != MyBookingsShop.class) {
                currentActivity.startActivity(new Intent(currentActivity, MyBookingsShop.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id. shop_myemployee) {
            if(currentActivity.getClass() != MyEmployeesShop.class) {
                currentActivity.startActivity(new Intent(currentActivity, MyEmployeesShop.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id.shop_services) {
            if(currentActivity.getClass() != ServicesShop.class) {
                currentActivity.startActivity(new Intent(currentActivity, ServicesShop.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id.shop_details) {
            if(currentActivity.getClass() != ShopDetails.class) {
                currentActivity.startActivity(new Intent(currentActivity, ShopDetails.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id.about) {
            if(currentActivity.getClass() != About.class) {
                currentActivity.startActivity(new Intent(currentActivity, About.class));
                currentActivity.finish();
            }
        }
        else if(id==R.id.contact) {
            if(currentActivity.getClass() != ContactUs.class) {
                currentActivity.startActivity(new Intent(currentActivity, ContactUs.class));
                currentActivity.finish();
            }
        }
        DrawerLayout drawer = (DrawerLayout)currentActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
