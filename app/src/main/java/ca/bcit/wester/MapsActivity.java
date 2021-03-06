package ca.bcit.wester;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ServiceController serviceC;
    private GoogleMap mMap;
    private String TAG = MapsActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ServiceController dbHandler;
    private MenuItem filterList = null;
    private static ArrayList<String> filterNames = new ArrayList<String>();
    private static Set<String> filterName= new TreeSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Add actionbar to activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        dbHandler = new ServiceController(this);
//        new MapsActivity.JsonHandler().execute();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //populate list
        dbHandler = new ServiceController(this);
        new MapsActivity.JsonHandler().execute();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng newWest = new LatLng(49.206654, -122.910429);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newWest, 15));
        pinAllServices();
    }

    private void pinAllServices() {
        serviceC = new ServiceController(this);
        List<Service> services = serviceC.read();
        for(Service s : services){
            LatLng servicePin = new LatLng(s.getLatitude(), s.getLongitude());
            String Cate = s.getCategory();
            String name = s.getName();
            addMarker(servicePin, Cate, name);
        }
    }

    private void addMarker(LatLng location, String Cate, String name){
        mMap.addMarker(new MarkerOptions()
                .title(name)
                .snippet(Cate)
                .position(location));
    }


    public void onClickDone(View v) {
        CharSequence text = "Button was clicked.";
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), text, Snackbar.LENGTH_LONG);
        snackbar.setAction("H" +
                "ide", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(MapsActivity.this, "Done", Toast.LENGTH_LONG);
                t.show();
            }
        });
        snackbar.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        filterList = menu.findItem(R.id.info_filter);
        filterList.getSubMenu().clear();
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.info_filter:
                // search action
                System.out.println("Filter");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class JsonHandler extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            String[] urls = getResources().getStringArray(R.array.urls);
            HttpHandler http = new HttpHandler();
            for (int index = 0; index < urls.length; index++) {
                String jsonStr = http.makeServiceCall(urls[index]);
                //Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr == null) {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                    return null;
                }

                try {
                    JSONArray serviceJsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < serviceJsonArray.length(); i++) {
                        JSONObject serviceJson = serviceJsonArray.getJSONObject(i);
                        String name = serviceJson.getString("Name");
                        String description = serviceJson.getString("Description");
                        String category = serviceJson.getString("Category");
                        String hours = serviceJson.getString("Hours");
                        String location = serviceJson.getString("Location");
                        String postal = serviceJson.getString("PC");
                        String phone = serviceJson.getString("Phone");
                        String email = serviceJson.getString("Email");
                        String website = serviceJson.getString("Website");
                        double x = Double.parseDouble(serviceJson.getString("X"));
                        double y = Double.parseDouble(serviceJson.getString("Y"));
                        ArrayList<String> tags = new ArrayList<>();
                        Service service = new Service(0, name, x, y, tags, description, category, hours, location, postal, phone, email, website);
                        dbHandler.create(service);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
            ArrayList<Service> testArray = (ArrayList) dbHandler.read();
            for (Service test : testArray) {
                filterNames.add(test.getDescription());
            }

            filterName.addAll(filterNames);
            filterNames.clear();
            filterNames.addAll(filterName);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //Add Menu Items
            for (int i = 0; i < filterNames.size(); i++) {
                filterList.getSubMenu().add(0, i, i, filterNames.get(i));
            }
        }
    }
}
