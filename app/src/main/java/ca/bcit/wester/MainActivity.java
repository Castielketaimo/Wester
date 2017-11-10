package ca.bcit.wester;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ServiceController dbHandler;
    private MenuItem filterList = null;
    private static ArrayList<String> filterNames = new ArrayList<String>();
    private static Set<String> filterName= new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstancStance) {
        super.onCreate(savedInstancStance);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        dbHandler = new ServiceController(this);
//        new MainActivity.JsonHandler().execute();
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

//    private class JsonHandler extends AsyncTask<Void, Void, Void>
//    {
//
//        @Override
//        protected Void doInBackground(Void... arg0)
//        {
//            String[] urls = getResources().getStringArray(R.array.urls);
//            HttpHandler http = new HttpHandler();
//            for(int index = 0; index < urls.length; index++)
//            {
//                String jsonStr = http.makeServiceCall(urls[index]);
//                //Log.e(TAG, "Response from url: " + jsonStr);
//
//                if (jsonStr == null)
//                {
//                    Log.e(TAG, "Couldn't get json from server.");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Couldn't get json from server. Check LogCat for possible errors!",
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//
//                    return null;
//                }
//
//                try
//                {
//                    JSONArray serviceJsonArray = new JSONArray(jsonStr);
//                    for (int i = 0; i < serviceJsonArray.length(); i++)
//                    {
//                        JSONObject serviceJson = serviceJsonArray.getJSONObject(i);
//                        String name = serviceJson.getString("Name");
//                        String description = serviceJson.getString("Description");
//                        String category = serviceJson.getString("Category");
//                        String hours = serviceJson.getString("Hours");
//                        String location = serviceJson.getString("Location");
//                        String postal = serviceJson.getString("PC");
//                        String phone = serviceJson.getString("Phone");
//                        String email = serviceJson.getString("Email");
//                        String website = serviceJson.getString("Website");
//                        double x = Double.parseDouble(serviceJson.getString("X"));
//                        double y = Double.parseDouble(serviceJson.getString("Y"));
//                        ArrayList<String> tags = new ArrayList<>();
//                        Service service = new Service(0, name, x, y, tags, description, category, hours, location, postal, phone, email, website);
//                        dbHandler.create(service);
//                    }
//                }
//                catch (final JSONException e)
//                {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//                }
//            }
//            ArrayList<Service> testArray = (ArrayList) dbHandler.read();
//            for(Service test : testArray)
//            {
//                filterNames.add(test.getDescription());
//            }
//
//            filterName.addAll(filterNames);
//            filterNames.clear();
//            filterNames.addAll(filterName);
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid)
//        {
//            super.onPostExecute(aVoid);
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//            for(int i = 0; i < filterNames.size(); i++) {
//                filterList.getSubMenu().add(0, i, i, filterNames.get(i));
//            }
//        }
//    }
}