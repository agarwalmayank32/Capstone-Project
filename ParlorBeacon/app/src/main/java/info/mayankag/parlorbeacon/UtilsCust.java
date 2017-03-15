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

class UtilsCust {

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    static void NetworkToast(Context context) {
        ShortToast(context, context.getString(R.string.no_internet_text));
    }

    static void ShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static void saveCustEmail(String mail, Context context) {
        String FILENAME = "custemail.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(mail.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String loadCustEmail(Context context) {
        String FILENAME = "custemail.txt";
        String out = "";

        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;

            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    static void saveCustName(String name, Context context) {
        String FILENAME = "custname.txt";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(name.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String loadCustName(Context context) {
        String FILENAME = "customername.txt";
        String out = "";
        try {
            FileInputStream fis1 = context.openFileInput(FILENAME);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            String sLine1;
            while (((sLine1 = br1.readLine()) != null)) {
                out += sLine1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    static void onNaviationItemSelected(Activity currentActivity, @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int menuItemId;
        if (currentActivity.getClass() == MainScreenCust.class) {
            menuItemId = R.id.search_servicecust;
        } else if (currentActivity.getClass() == MyBookingsCust.class) {
            menuItemId = R.id.mybookingcust;
        } else if (currentActivity.getClass() == MyProfileCust.class) {
            menuItemId = R.id.myprofilecust;
        } else if (currentActivity.getClass() == UpcomingFeatures.class) {
            menuItemId = R.id.uf;
        } else {
            menuItemId = -1;
        }

        int id = item.getItemId();
        if (id == menuItemId) {
            // Already this menu item selected.
            DrawerLayout drawer = (DrawerLayout) currentActivity.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return;
        }


        if (id == R.id.search_servicecust) {
            if (currentActivity.getClass() != MainScreenCust.class) {
                currentActivity.startActivity(new Intent(currentActivity, MainScreenCust.class));
                currentActivity.finish();
            }
        } else if (id == R.id.mybookingcust) {
            if (currentActivity.getClass() != MyBookingsCust.class) {
                currentActivity.startActivity(new Intent(currentActivity, MyBookingsCust.class));
                if (currentActivity.getClass() != MainScreenCust.class) {
                    currentActivity.finish();
                }
            }
        } else if (id == R.id.myprofilecust) {
            if (currentActivity.getClass() != MyProfileCust.class) {
                currentActivity.startActivity(new Intent(currentActivity, MyProfileCust.class));
                if (currentActivity.getClass() != MainScreenCust.class) {
                    currentActivity.finish();
                }
            }
        } else if (id == R.id.uf) {
            if (currentActivity.getClass() != UpcomingFeatures.class) {
                currentActivity.startActivity(new Intent(currentActivity, UpcomingFeatures.class));
                if (currentActivity.getClass() != MainScreenCust.class) {
                    currentActivity.finish();
                }
            }
        } else if (id == R.id.logout) {
            saveCustEmail("", currentActivity);
            currentActivity.startActivity(new Intent(currentActivity, Customer.class));
            currentActivity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) currentActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
