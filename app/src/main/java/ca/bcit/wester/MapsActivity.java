package ca.bcit.wester;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Character.isDigit;
import static java.lang.Math.abs;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ServiceController serviceC;
    private GoogleMap mMap;
    private String TAG = MapsActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ServiceController dbHandler;
    private MenuItem filterList = null;
    private ArrayList<String> filterNameList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        if(dbHandler.checkTableIsEmpty()) {
            new MapsActivity.JsonHandler().execute();
        }
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
        // Add a marker in new west and move the camera
        LatLng newWest = new LatLng(49.206654, -122.910429);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newWest, 15));
        mMap.clear();

        //pin all the services needed
        pinAllServices();

        //set up the listeners for markers
        setUpListener();
    }

    /**
     * Set up the listener for markers
     */
    private void setUpListener(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //inflate the bottom sheet with information needed
                influteBottomSheet(Integer.parseInt(marker.getTag().toString()));
                return false;
            }
        });
    }

    /**
     * Send the service Id to bottomSheetMapFragment class so BottomSheetMapFragment can Inflate the view as needed
     * @param serviceId
     */
    private void influteBottomSheet(int serviceId){
        //create ne bottom sheet fragment
        BottomSheetDialogFragment b = new BottomSheetMapFragment();

        //sent the service to new fragment
        Bundle args = new Bundle();
        args.putInt("serviceId" , serviceId);
        b.setArguments(args);

        //display the bottom sheet
        b.show(getSupportFragmentManager(), b.getTag());
    }
    /**
     * Pin all the services onto the google map
     * with title and cater as snippets
     */
    private void pinAllServices() {
        serviceC = new ServiceController(this);
        List<Service> services = serviceC.read();
        mMap.clear();
        for(Service s : services){
            LatLng servicePin = new LatLng(s.getLatitude(), s.getLongitude());
            String Cate = s.getCategory();
            String name = s.getName();
            int tag = s.getID();
            addMarker(servicePin, Cate, name, tag);
        }
    }

//    filter pins
    private void pinFilterServices(String category) {
        serviceC = new ServiceController(this);
        List<Service> services = serviceC.readRecordsByCategory(category);
        mMap.clear();
        for(Service s : services){
            LatLng servicePin = new LatLng(s.getLatitude(), s.getLongitude());
            String Desc = s.getDescription();
            String name = s.getName();
            int tag = s.getID();
            addMarker(servicePin, Desc, name, tag);
        }
    }

    //    filter pins
    private void pinSearchedServices(String desc) {
        serviceC = new ServiceController(this);
        List<Service> services = serviceC.readRecordsByDescription(desc);
        mMap.clear();
        for(Service s : services){
            LatLng servicePin = new LatLng(s.getLatitude(), s.getLongitude());
            String Desc = s.getDescription();
            String name = s.getName();
            int tag = s.getID();
            addMarker(servicePin, Desc, name, tag);
        }
    }

    /**
     * makes marker to put onto the map
     * @param location
     * @param cate
     * @param name
     */
    private void addMarker(LatLng location, String cate, String name, int tag) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .title(name)
                .snippet(cate)
                .position(location));
        //give each marker a unique tag
        marker.setTag(tag);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        filterList = menu.findItem(R.id.info_filter);
        filterList.getSubMenu().clear();
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Clear entry on submit
                Toast.makeText(MapsActivity.this, "Searching for " + query, Toast.LENGTH_LONG).show();
                pinSearchedServices(query);
                searchView.setIconified(true);
                searchView.clearFocus();

                // collapse the action view after submit
                searchView.setIconified(true);
                (menu.findItem(R.id.action_search)).collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        populateFilter();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//      if the id was a 0 or negative number, then the item selected has come from the filter list.
        if (item.getItemId() < 0) {
            pinFilterServices(filterNameList.get(abs(item.getItemId())));
            return super.onOptionsItemSelected(item);
        } else if (item.getItemId() == 0) {
            pinAllServices();
            return super.onOptionsItemSelected(item);
        } else {
        // Take appropriate action for each action item click
            switch (item.getItemId()) {
                case R.id.action_text_info: {
                    Intent intent = new Intent(MapsActivity.this, CardActivity.class);
                    startActivity(intent);
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
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
                        Service service = new Service(0, name, x, y, tags, category, description, hours, location, postal, phone, email, website);
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
            mMap.clear();
            pinAllServices();
            populateFilter();
            Toast.makeText(MapsActivity.this, "The size of the db is " + dbHandler.read().size(), Toast.LENGTH_SHORT).show();
        }
    }

    //Populates FilterList
    public void populateFilter() {
        Set<String> sortedNames = new TreeSet<String>();
        ServiceController controller = new ServiceController(this);
        ArrayList<Service> testArray = (ArrayList) controller.read();
        for (Service test : testArray) {
            filterNameList.add(test.getCategory());
        }
        filterList.getSubMenu().clear();
        sortedNames.addAll(filterNameList);
        filterNameList.clear();
        filterNameList.addAll(sortedNames);

        //Add Menu Items
        filterList.getSubMenu().add(0, 0, 0, "All");
        for (int i = 1; i < filterNameList.size(); i++) {
            //give unique id of each item the inverse to prevent conflicts with other items when implementing onclick
            filterList.getSubMenu().add(0, (i*-1), i, filterNameList.get(i - 1));
        }
    }
}
