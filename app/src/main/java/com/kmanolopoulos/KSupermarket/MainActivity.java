package com.kmanolopoulos.KSupermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Invoke Products menu
    public void OnProducts(View v)
    {
        Intent intent = new Intent(this, ProductsMenuActivity.class);
        startActivity(intent);
    }

    // Invoke Lists menu
    public void OnLists(View v)
    {
        Intent intent = new Intent(this, ListsMenuActivity.class);
        startActivity(intent);
    }

    // Invoke Dispatch menu
    public void OnDispatch(View v)
    {
        Intent intent = new Intent(this, DispatchMenuActivity.class);
        startActivity(intent);
    }

}
