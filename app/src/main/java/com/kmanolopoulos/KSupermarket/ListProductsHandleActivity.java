package com.kmanolopoulos.KSupermarket;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ListProductsHandleActivity extends ListActivity
{
    private String ListName;
    private ArrayList<String> all_items;
    private ArrayList<String> belonging_items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products_handle);

        /* Get list name */
        GetListName();

        /* Set activity title */
        SetTitle();

        /* Initialize items array */
        all_items = new ArrayList<String>();
        belonging_items = new ArrayList<String>();

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
        belonging_items.clear();

        // Add all lists in the list to be displayed
        all_items.addAll(browser.GetAllProducts());
        belonging_items.addAll(browser.GetListProducts(ListName));

        // Sort them
        Collections.sort(all_items);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, android.R.id.text1, all_items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                CheckedTextView tv = view.findViewById(android.R.id.text1);

                if (belonging_items.contains(tv.getText()))
                {
                    tv.setChecked(true);
                }
                else
                {
                    tv.setChecked(false);
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

        if (((CheckedTextView)v).isChecked())
        {
            if (db.DeleteProductFromList(itemSelected, ListName))
            {
                belonging_items.remove(itemSelected);
                ((CheckedTextView)v).setChecked(false);
            }
        }
        else
        {
            if (db.AddProductToList(itemSelected, ListName))
            {
                belonging_items.add(itemSelected);
                ((CheckedTextView)v).setChecked(true);
            }
        }
    }

    // Get operation
    private void GetListName()
    {
        Intent intent = getIntent();
        ListName = intent.getStringExtra("com.kmanolopoulos.KSupermarket.EditListName");
    }

    // Set activity title
    private void SetTitle()
    {
        String title = getString(R.string.edit_list_text) + " " + ListName;
        setTitle(title);
    }
}
