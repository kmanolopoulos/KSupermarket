package com.kmanolopoulos.KSupermarket;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class DispatchHandleActivity extends ListActivity
{
    private String ListName;
    private ArrayList<String> all_items;
    private ArrayList<String> selected_items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_handle);

        /* Get list name */
        GetListName();

        /* Set activity title */
        SetTitle();

        /* Initialize items array */
        all_items = new ArrayList<String>();
        selected_items = new ArrayList<String>();

        /* Show items in list */
        RefreshItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RefreshItems()
    {
        DataFileBrowser browser = new DataFileBrowser(this);

        // Initially empty items list
        all_items.clear();
        selected_items.clear();

        // Add all lists in the list to be displayed
        all_items.addAll(browser.GetListProducts(ListName));

        for (String item : all_items)
        {
            if (browser.GetProductOfListDispatchStatus(item, ListName))
            {
                selected_items.add(item);
            }
        }

        // Sort them
        Collections.sort(all_items);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, android.R.id.text1, all_items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);

                if (selected_items.contains(tv.getText()))
                {
                    tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected_background_color));
                    tv.setActivated(true);
                }
                else
                {
                    tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.unselected_background_color));
                    tv.setActivated(false);
                }

                return view;
            }
        };
        setListAdapter(adapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        DataFileBrowser db = new DataFileBrowser(getBaseContext());
        String itemSelected = (String)getListAdapter().getItem(position);
        Log.d("onListItemClick()", "Item selected: " + itemSelected);

        if (v.isActivated())
        {
            if (db.SetProductOfListDispatchStatus(itemSelected, ListName, false))
            {
                selected_items.remove(itemSelected);
                v.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.unselected_background_color));
                v.setActivated(false);
            }
        }
        else
        {
            if (db.SetProductOfListDispatchStatus(itemSelected, ListName, true))
            {
                selected_items.add(itemSelected);
                v.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.selected_background_color));
                v.setActivated(true);
            }
        }
    }

    // Get operation
    private void GetListName()
    {
        Intent intent = getIntent();
        ListName = intent.getStringExtra("com.kmanolopoulos.KSupermarket.DispatchListName");
    }

    // Set activity title
    private void SetTitle()
    {
        String title = getString(R.string.dispatch_list_text) + " " + ListName;
        setTitle(title);
    }
}
