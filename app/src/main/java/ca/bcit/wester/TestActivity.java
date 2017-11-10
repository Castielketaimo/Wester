package ca.bcit.wester;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;

public class TestActivity extends AppCompatActivity
{
    private String TAG = TestActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ServiceController dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dbHandler = new ServiceController(this);

        new JsonHandler().execute();
    }

    private class JsonHandler extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... arg0)
        {
            String[] urls = getResources().getStringArray(R.array.urls);
            HttpHandler http = new HttpHandler();
            for(int index = 0; index < urls.length; index++)
            {
                String jsonStr = http.makeServiceCall(urls[index]);
                //Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr == null)
                {
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

                try
                {
                    JSONArray serviceJsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < serviceJsonArray.length(); i++)
                    {
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
                }
                catch (final JSONException e)
                {
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
            for(Service test : testArray)
            {
                System.out.println(test.getName());
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TestActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
