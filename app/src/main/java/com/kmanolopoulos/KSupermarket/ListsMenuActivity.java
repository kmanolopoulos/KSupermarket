package com.kmanolopoulos.KSupermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ListsMenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_menu);
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

    // Invoke View Lists menu
    public void OnViewEditLists(View v)
    {
        Intent intent = new Intent(this, ListsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ListsOperation", getString(R.string.view_edit_lists_operation));
        startActivity(intent);
    }

    // Invoke Add List menu
    public void OnAddList(View v)
    {
        Intent intent = new Intent(this, ListsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ListsOperation", getString(R.string.add_list_operation));
        startActivity(intent);
    }

    // Invoke Delete List menu
    public void OnDeleteList(View v)
    {
        Intent intent = new Intent(this, ListsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ListsOperation", getString(R.string.delete_list_operation));
        startActivity(intent);
    }

    // Invoke Rename List menu
    public void OnRenameList(View v)
    {
        Intent intent = new Intent(this, ListsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ListsOperation", getString(R.string.rename_list_operation));
        startActivity(intent);
    }
}
