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

public class ListsHandleActivity extends ListActivity
{
    private enum ListsOperationType
    {
        OPERATION_LISTS_INVALID, OPERATION_LISTS_VIEW_EDIT, OPERATION_LIST_ADD,
        OPERATION_LIST_DELETE, OPERATION_LIST_RENAME
    }

    private ListsOperationType ListsOperation;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_handle);

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

        // Add all lists in the list to be displayed
        items.addAll(browser.GetAllLists());

        // Sort them
        Collections.sort(items);

        // Add "Add new list" choice if applicable
        if (ListsOperation == ListsOperationType.OPERATION_LIST_ADD)
        {
            items.add(0, getString(R.string.add_list_text));
        }

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                if ((ListsOperation == ListsOperationType.OPERATION_LISTS_VIEW_EDIT) ||
                   ((ListsOperation == ListsOperationType.OPERATION_LIST_ADD) && (position == 0)) ||
                    (ListsOperation == ListsOperationType.OPERATION_LIST_RENAME) ||
                    (ListsOperation == ListsOperationType.OPERATION_LIST_DELETE))
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

        switch (ListsOperation)
        {
            case OPERATION_LISTS_VIEW_EDIT:
                Log.d("onListItemClick()", "OPERATION_LISTS_VIEW_EDIT");
                OnEditList(itemSelected);
                break;
            case OPERATION_LIST_ADD:
                if (position == 0)
                {
                    Log.d("onListItemClick()", "OPERATION_LIST_ADD");
                    OnAddList();
                }
                break;
            case OPERATION_LIST_DELETE:
                Log.d("onListItemClick()", "OPERATION_LIST_DELETE");
                OnDeleteList(itemSelected);
                break;
            case OPERATION_LIST_RENAME:
                Log.d("onListItemClick()", "OPERATION_LIST_RENAME");
                OnRenameList(itemSelected);
                break;
            default:
                Log.d("onListItemClick()", "OPERATION_LISTS_INVALID");
                break;
        }
    }

    // Get operation
    private void GetOperation()
    {
        String operation;

        Intent intent = getIntent();
        operation = intent.getStringExtra("com.kmanolopoulos.KSupermarket.ListsOperation");

        if (operation.equals(getString(R.string.view_edit_lists_operation)))
        {
            ListsOperation = ListsOperationType.OPERATION_LISTS_VIEW_EDIT;
        }
        else if (operation.equals(getString(R.string.add_list_operation)))
        {
            ListsOperation = ListsOperationType.OPERATION_LIST_ADD;
        }
        else if (operation.equals(getString(R.string.delete_list_operation)))
        {
            ListsOperation = ListsOperationType.OPERATION_LIST_DELETE;
        }
        else if (operation.equals(getString(R.string.rename_list_operation)))
        {
            ListsOperation = ListsOperationType.OPERATION_LIST_RENAME;
        }
        else
        {
            ListsOperation = ListsOperationType.OPERATION_LISTS_INVALID;
        }
    }

    // Set activity title
    private void SetTitle()
    {
        String title;

        switch (ListsOperation)
        {
            case OPERATION_LISTS_VIEW_EDIT:
                title = getString(R.string.view_edit_lists_text);
                break;
            case OPERATION_LIST_ADD:
                title = getString(R.string.add_list_text);
                break;
            case OPERATION_LIST_DELETE:
                title = getString(R.string.delete_list_text);
                break;
            case OPERATION_LIST_RENAME:
                title = getString(R.string.rename_list_text);
                break;
            default:
                title = "";
                break;
        }

        setTitle(title);
    }

    // Edit list action
    private void OnEditList(String ListName)
    {
        Intent intent = new Intent(this, ListProductsHandleActivity.class);
        intent.putExtra("com.kmanolopoulos.KSupermarket.EditListName", ListName);
        startActivity(intent);
    }

    // Add list action
    private void OnAddList()
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_add_list_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.add_list_text));
        alertDialog.setCancelable(false);

        final EditText name_entry = view.findViewById(R.id.name_entry);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.AddList(name_entry.getText().toString()))
                {
                    resId = R.string.list_added;
                }
                else
                {
                    resId = R.string.list_not_added;
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

    // Delete list action
    private void OnDeleteList(String ListName)
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_delete_list_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete_list_text));
        alertDialog.setCancelable(false);

        final TextView message = view.findViewById(R.id.delete_message);
        final String list = ListName;

        message.setText("Delete " + list + "?");

        // Set up the buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.DeleteList(list))
                {
                    resId = R.string.list_deleted;
                }
                else
                {
                    resId = R.string.list_not_deleted;
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

    // Rename list action
    private void OnRenameList(String ListName)
    {
        final View view = getLayoutInflater().inflate(R.layout.alertdialog_rename_list_handle, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.rename_list_text));
        alertDialog.setCancelable(false);

        final EditText rename_entry = view.findViewById(R.id.rename_entry);
        final String list = ListName;

        rename_entry.setText(list);
        rename_entry.setSelection(rename_entry.getText().length());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int resId;
                DataFileBrowser db = new DataFileBrowser(getBaseContext());

                if (db.ChangeList(list, rename_entry.getText().toString()))
                {
                    resId = R.string.list_renamed;
                }
                else
                {
                    resId = R.string.list_not_renamed;
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
