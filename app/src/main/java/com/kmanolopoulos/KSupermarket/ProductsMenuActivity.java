package com.kmanolopoulos.KSupermarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ProductsMenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_menu);
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

    // Invoke View Products menu
    public void OnViewProducts(View v)
    {
        Intent intent = new Intent(this, ProductsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ProductsOperation", getString(R.string.view_products_operation));
        startActivity(intent);
    }

    // Invoke Add Product menu
    public void OnAddProduct(View v)
    {
        Intent intent = new Intent(this, ProductsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ProductsOperation", getString(R.string.add_product_operation));
        startActivity(intent);
    }

    // Invoke Delete Product menu
    public void OnDeleteProduct(View v)
    {
        Intent intent = new Intent(this, ProductsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ProductsOperation", getString(R.string.delete_product_operation));
        startActivity(intent);
    }

    // Invoke Rename Product menu
    public void OnRenameProduct(View v)
    {
        Intent intent = new Intent(this, ProductsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.ProductsOperation", getString(R.string.rename_product_operation));
        startActivity(intent);
    }
}
