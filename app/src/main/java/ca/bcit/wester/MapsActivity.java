package ca.bcit.wester;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private GoogleMap mMap;
    private String TAG = MapsActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ServiceController dbHandler;
    private MenuItem filterList = null;
    private ArrayList<String> filterNameList = new ArrayList<String>();
    private Service tempService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Add actionbar to activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //populate list
        dbHandler = new ServiceController(this);
        if(dbHandler.checkTableIsEmpty()) {
            dbHandler.readAllIntoView();
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

        if(ServiceController.isInitialLoad()) {
            dbHandler.stopInitialLoad();
            pinAllServices();
        } else {
            //pin all the services needed
            pinCurrentList();
        }

        //set up the listeners for markers
        setUpListener();
    }

    /**
     * Set up the listener for markers
     */
    private void setUpListener(){
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                influteBottomSheet(Integer.parseInt(arg0.getTag().toString()));

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
        dbHandler = new ServiceController(this);
        List<Service> services = dbHandler.readAllIntoView();
        pinServices(services);
    }

    /**
     * Re-pins all items that were shown on the text view.
     *
     * @param requestCode requestCode that was received
     * @param resultCode resultCode that was received
     * @param data data that was received
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
        }
    }

    /**
     * filter the services based on cate
     * pin the services onto map
     * @param category to search for
     */
    private void pinFilterServices(String category) {
        dbHandler = new ServiceController(this);
        List<Service> services = dbHandler.readRecordsByCategory(category);
        pinServices(services);
    }
    
    /**
     * search the user input in our data base for match result
     * search based on desc, title, and cate
     * pin the services onto map
     * @param desc
     */
    private void pinSearchedServices(String desc) {
        dbHandler = new ServiceController(this);
        List<Service> services = dbHandler.readRecordsByDescription(desc);
        pinServices(services);
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
                .position(location)
                );
        //give each marker a unique tag
        marker.setTag(tag);
        //give marker a different icon based on cate
        int drawableLocations = findCateIcon(cate);
        marker.setIcon(bitmapDescriptorFromVector(this, drawableLocations));
    }


    /**
     * Make the drawable icon into bitmap so we can use for the markers
     * @param context
     * @param vectorResId
     * @return
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    /**
     * returns the int of drawable file based on the cate
     * @param cate
     * @return
     */
    private int findCateIcon(String cate){

        switch(cate) {
            case "Drop-In Centre" :
                return R.drawable.ic_dropin_marker;

            case "Education, Language and Literacy" :
                return R.drawable.ic_education_marker;

            case "Emergency, Transitional and Supported Housing" :
                return R.drawable.ic_emergency_marker;

            case "Employment and Job Training" :
                return R.drawable.ic_job_marker;

            case "Family and General Support Programs" :
                return R.drawable.ic_family_marker;

            case "Food Programs and Services" :
                return R.drawable.ic_food_marker;

            case "Government and Justice Services" :
                return R.drawable.ic_judge_marker;

            case "Health, Mental Health & Addictions Services" :
                return R.drawable.ic_health_marker;

            case "Housing Outreach, Advocacy and Referral" :
                return R.drawable.ic_outreach_marker;

            case "Non-Market and Co-op Housing" :
                return R.drawable.ic_house_marker;

            case "Parks, Recreation and Community School Programming":
                return R.drawable.ic_park_marker;

            case "Seniors Services" :
                return R.drawable.ic_elder_marker;

            case "Child Care, Child Development and Early Learning Programs":
                return R.drawable.ic_child_marker;

            case "Settlement Services":
                return R.drawable.ic_settle_marker;

            default :
                return R.drawable.ic_done_white_24dp;
        }
    }

    /**
     * Create the menu options for the app bar
     * @param menu the layout menu
     * @return if the query text for search has changed
     */
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

    /**
     * When an option of the menu bar is selected
     * @param item the selected option
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//      if the id was a 0 or negative number, then the item selected has come from the filter list.
        if (item.getItemId() < 0) {
            pinFilterServices(filterNameList.get(abs(item.getItemId()) - 1));
            return super.onOptionsItemSelected(item);
        } else if (item.getItemId() == 0) {
            pinAllServices();
            return super.onOptionsItemSelected(item);
        } else {
        // Take appropriate action for each action item click
            switch (item.getItemId()) {
                case R.id.action_text_info: {
                    Intent intent = new Intent(MapsActivity.this, CardActivity.class);
                    startActivityForResult(intent, 1);
                    return false;
                }
                case R.id.action_input: {
                    createInputDialog();
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Creates an input dialog for the user to input data
     */
    private void createInputDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        //getting all the view
        final EditText nameInput = (EditText) mView.findViewById(R.id.input_name);
        final EditText descInput = (EditText) mView.findViewById(R.id.input_desc);
        final EditText hoursInput = (EditText) mView.findViewById(R.id.input_hours);
        final EditText addressInput = (EditText) mView.findViewById(R.id.input_address);
        final EditText postInput = (EditText) mView.findViewById(R.id.input_postocode);
        final EditText phoneInput = (EditText) mView.findViewById(R.id.input_phone);
        final EditText emailInput = (EditText) mView.findViewById(R.id.input_email);
        final EditText websiteInput = (EditText) mView.findViewById(R.id.input_website);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        tempService = new Service(0,
                                nameInput.getText().toString(),
                                0,
                                0,
                                null,
                                "",
                                descInput.getText().toString(),
                                hoursInput.getText().toString(),
                                addressInput.getText().toString(),
                                postInput.getText().toString(),
                                phoneInput.getText().toString(),
                                emailInput.getText().toString(),
                                websiteInput.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    /**
     * The class that takes charge of pulling and creating the database
     */
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
        }
    }

    /**
     * Populates the filtered services
     */
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
        for (int i = 1; i <= filterNameList.size(); i++) {
            //give unique id of each item the inverse to prevent conflicts with other items when implementing onclick
            filterList.getSubMenu().add(0, (i*-1), i, filterNameList.get(i - 1));
        }
    }

    /*Pins services currently in the servicelist*/
    public void pinCurrentList() {
        pinServices(ServiceController.getServiceList());
    }

    /**
     * pin the service passed in as param onto the map fragment
     * @param services
     */
    private void pinServices(List<Service> services) {
        mMap.clear();
        for(Service s : services){
            LatLng servicePin = new LatLng(s.getLatitude(), s.getLongitude());
            String Cate = s.getCategory();
            String name = s.getName();
            int tag = s.getID();
            addMarker(servicePin, Cate, name, tag);
        }
    }
}
