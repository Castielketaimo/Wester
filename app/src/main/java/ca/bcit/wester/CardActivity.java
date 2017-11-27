package ca.bcit.wester;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import ca.bcit.wester.controllers.ServiceController;
import ca.bcit.wester.models.Service;

import static java.lang.Math.abs;

public class CardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuItem filterList = null;
    private ArrayList<String> filterNameList = new ArrayList<String>();

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_card, menu);

        filterList = menu.findItem(R.id.card_info_filter);
        filterList.getSubMenu().clear();

        MenuItem searchItem = menu.findItem(R.id.card_action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(false);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        final ServiceController serviceController = new ServiceController(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Clear entry on submit
                Toast.makeText(CardActivity.this, "Searching for " + query, Toast.LENGTH_LONG).show();
                searchView.setIconified(true);
                searchView.clearFocus();
                serviceController.readRecordsByDescription(query);
                recreate();
                // collapse the action view after submit
                searchView.setIconified(true);
                (menu.findItem(R.id.card_action_search)).collapseActionView();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.card_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CardAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Populates FilterList
    public void populateFilter() {
        Set<String> sortedNames = new TreeSet<String>();
        ServiceController serviceController = new ServiceController(this);
        ArrayList<Service> testArray = (ArrayList<Service>) serviceController.read();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ServiceController serviceController = new ServiceController(this);
        //      if the id was a 0 or negative number, then the item selected has come from the filter list.
        if (item.getItemId() < 0) {
            serviceController.readRecordsByCategory(filterNameList.get(abs(item.getItemId()) - 1));
            recreate();
            return super.onOptionsItemSelected(item);
        } else if (item.getItemId() == 0) {
            serviceController.readAllIntoView();
            recreate();
            return super.onOptionsItemSelected(item);
        } else {
            switch (item.getItemId()) {
                case R.id.action_return: {
                    setResult(RESULT_OK, null);
                    finish();
                    break;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
