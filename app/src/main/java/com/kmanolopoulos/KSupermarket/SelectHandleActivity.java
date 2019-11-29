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

public class SelectHandleActivity extends ListActivity
{
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_handle);

        /* Set activity title */
        SetTitle();

        /* Initialize items array */
        items = new ArrayList<String>();

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
        items.clear();

        // Add all products in the list to be displayed
        items.addAll(browser.GetAllLists());

        // Sort them
        Collections.sort(items);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.selected_text_color));

                return view;
            }
        };
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        String itemSelected = (String)getListAdapter().getItem(position);
        Log.d("onListItemClick()", "Item selected: " + itemSelected);
        OnDispatchList(itemSelected);
    }

    // Set activity title
    private void SetTitle()
    {
        String title = getString(R.string.select_list_text);
        setTitle(title);
    }

    // Dispatch list action
    private void OnDispatchList(String ListName)
    {
        Intent intent = new Intent(this, DispatchHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.DispatchListName", ListName);
        startActivity(intent);
    }
}
