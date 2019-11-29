package com.kmanolopoulos.KSupermarket;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ProductsHandleActivity extends ListActivity
{
    private enum ProductsOperationType
    {
        OPERATION_PRODUCTS_INVALID, OPERATION_PRODUCTS_VIEW, OPERATION_PRODUCT_ADD,
        OPERATION_PRODUCT_DELETE, OPERATION_PRODUCT_RENAME
    }

    private ProductsOperationType ProductsOperation;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_handle);

        /* Get operation */
        GetOperation();

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
        items.addAll(browser.GetAllProducts());

        // Sort them
        Collections.sort(items);

        // Add "Add new product" choice if applicable
        if (ProductsOperation == ProductsOperationType.OPERATION_PRODUCT_ADD)
        {
            items.add(0, getString(R.string.add_product_text));
        }

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                if (((ProductsOperation == ProductsOperationType.OPERATION_PRODUCT_ADD) && (position == 0)) ||
                     (ProductsOperation == ProductsOperationType.OPERATION_PRODUCT_RENAME) ||
                     (ProductsOperation == ProductsOperationType.OPERATION_PRODUCT_DELETE))
                {
                    TextView tv = view.findViewById(android.R.id.text1);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.selected_text_color));
                }
                else
                {
                    TextView tv = view.findViewById(android.R.id.text1);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.unselected_text_color));
                }

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

        switch (ProductsOperation)
        {
            case OPERATION_PRODUCTS_VIEW:
                Log.d("onListItemClick()", "OPERATION_PRODUCTS_VIEW");
                break;
            case OPERATION_PRODUCT_ADD:
                if (position == 0)
                {
                    Log.d("onListItemClick()", "OPERATION_PRODUCT_ADD");
                    OnAddProduct();
                }
                break;
            case OPERATION_PRODUCT_DELETE:
                Log.d("onListItemClick()", "OPERATION_PRODUCT_DELETE");
                OnDeleteProduct(itemSelected);
                break;
            case OPERATION_PRODUCT_RENAME:
                Log.d("onListItemClick()", "OPERATION_PRODUCT_RENAME");
                OnRenameProduct(itemSelected);
                break;
            default:
                Log.d("onListItemClick()", "OPERATION_PRODUCTS_INVALID");
                break;
        }
    }

    // Get operation
    private void GetOperation()
    {
        String operation;

        Intent intent = getIntent();
        operation = intent.getStringExtra("com.kmanolopoulos.KSupermarket.ProductsOperation");

        if (operation.equals(getString(R.string.view_products_operation)))
        {
            ProductsOperation = ProductsOperationType.OPERATION_PRODUCTS_VIEW;
        }
        else if (operation.equals(getString(R.string.add_product_operation)))
        {
            ProductsOperation = ProductsOperationType.OPERATION_PRODUCT_ADD;
        }
        else if (operation.equals(getString(R.string.delete_product_operation)))
        {
            ProductsOperation = ProductsOperationType.OPERATION_PRODUCT_DELETE;
        }
        else if (operation.equals(getString(R.string.rename_product_operation)))
        {
            ProductsOperation = ProductsOperationType.OPERATION_PRODUCT_RENAME;
        }
        else
        {
            ProductsOperation = ProductsOperationType.OPERATION_PRODUCTS_INVALID;
        }
    }

    // Set activity title
    private void SetTitle()
    {
        String title;

        switch (ProductsOperation)
        {
            case OPERATION_PRODUCTS_VIEW:
                title = getString(R.string.view_products_text);
                break;
            case OPERATION_PRODUCT_ADD:
                title = getString(R.string.add_product_text);
                break;
            case OPERATION_PRODUCT_DELETE:
                title = getString(R.string.delete_product_text);
                break;
            case OPERATION_PRODUCT_RENAME:
                title = getString(R.string.rename_product_text);
                break;
            default:
                title = "";
                break;
        }

        setTitle(title);
    }

    // Add product action
    private void OnAddProduct()
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_add_product_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.add_product_text));
        alertDialog.setCancelable(false);

        final EditText name_entry = view.findViewById(R.id.name_entry);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.AddProduct(name_entry.getText().toString()))
                {
                    resId = R.string.product_added;
                }
                else
                {
                    resId = R.string.product_not_added;
                }

                Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                RefreshItems();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    // Delete product action
    private void OnDeleteProduct(String ProductName)
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_delete_product_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete_product_text));
        alertDialog.setCancelable(false);

        final TextView message = view.findViewById(R.id.delete_message);
        final String product = ProductName;

        message.setText("Delete " + product + "?");

        // Set up the buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.DeleteProduct(product))
                {
                    resId = R.string.product_deleted;
                }
                else
                {
                    resId = R.string.product_not_deleted;
                }

                Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                RefreshItems();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    // Rename product action
    private void OnRenameProduct(String ProductName)
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_rename_product_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.rename_product_text));
        alertDialog.setCancelable(false);

        final EditText rename_entry = view.findViewById(R.id.rename_entry);
        final String product = ProductName;

        rename_entry.setText(product);
        rename_entry.setSelection(rename_entry.getText().length());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.ChangeProduct(product, rename_entry.getText().toString()))
                {
                    resId = R.string.product_renamed;
                }
                else
                {
                    resId = R.string.product_not_renamed;
                }

                Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                RefreshItems();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }
}
