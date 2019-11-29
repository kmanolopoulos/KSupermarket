package com.kmanolopoulos.KSupermarket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DataFileBrowser extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "supermarketDB";
	private final String TABLE_NAME_PRODUCT = "Product";
    private final String TABLE_NAME_LIST = "List";
    private final String TABLE_NAME_LIST_PRODUCTS = "ListProducts";
	private final String PRODUCT_ID = "product_id";
	private final String PRODUCT_NAME = "product_name";
    private final String LIST_ID = "list_id";
    private final String LIST_NAME = "list_name";
    private final String LIST_PRODUCT_ID = "list_product_id";
    private final String LIST_DISPACHED = "list_dispatched";

    // Constructor
	public DataFileBrowser(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
    // Create database method
	public void onCreate(SQLiteDatabase dbase) 
	{
	    // Create Products table
		String CREATE_TABLE_PRODUCT =
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PRODUCT +
			"(" +
                 PRODUCT_ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                 PRODUCT_NAME	+ " TEXT UNIQUE  NOT NULL " +
	        ")";

		dbase.execSQL(CREATE_TABLE_PRODUCT);

		// Create Lists table
        String CREATE_TABLE_LIST =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LIST +
            "(" +
                LIST_ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LIST_NAME	+ " TEXT UNIQUE NOT NULL " +
            ")";

        dbase.execSQL(CREATE_TABLE_LIST);

        // Create ListProducts table
        String CREATE_TABLE_LIST_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LIST_PRODUCTS +
            "(" +
                LIST_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LIST_ID	+ " INTEGER NOT NULL, " +
                PRODUCT_ID	+ " INTEGER NOT NULL, " +
                LIST_DISPACHED + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + LIST_ID + ") REFERENCES " + TABLE_NAME_LIST + " ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(" + PRODUCT_ID + ") REFERENCES " + TABLE_NAME_PRODUCT + " ON UPDATE CASCADE ON DELETE CASCADE " +
            ")";

        dbase.execSQL(CREATE_TABLE_LIST_PRODUCTS);
	}

	@Override
    // Upgrade database method
	public void onUpgrade(SQLiteDatabase dbase, int oldVersion, int newVersion) 
	{
        // Actions here only if database schema changes
	}

	// Add a new Product to Products table
	public boolean AddProduct(String ProductName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME_PRODUCT + " (" + PRODUCT_NAME + ") VALUES(\'" + ProductName + "\')";

        // Ensure that Product name is not empty
        if (ProductName.length() <= 0)
            return false;

        // Add a new product
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Modify an existing Product name in Products table
    public boolean ChangeProduct(String OldProductName, String NewProductName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME_PRODUCT +
                     " SET " + PRODUCT_NAME + "=\'" + NewProductName + "\'" +
                     " WHERE " + PRODUCT_NAME + "=\'" + OldProductName + "\'";

        // Ensure that Product names are not empty
        if ((OldProductName.length() <= 0) || (NewProductName.length() <= 0))
            return false;

        // Modify product name
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Delete an existing Product from Products table
    public boolean DeleteProduct(String ProductName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME_PRODUCT +
                     " WHERE " + PRODUCT_NAME + "=\'" + ProductName + "\'";

        // Ensure that Product name is not empty
        if (ProductName.length() <= 0)
            return false;

        // Delete a product
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Get an Array of all Products inside Products table
    public ArrayList<String> GetAllProducts()
    {
        SQLiteDatabase dbase = this.getReadableDatabase();
        String sql = "SELECT " + PRODUCT_NAME +
                     " FROM " + TABLE_NAME_PRODUCT;
        ArrayList<String> all_products = new ArrayList<String>();
        Cursor result;

        // Get all products
        result = dbase.rawQuery(sql, null);

        if (result == null)
        {
            return all_products;
        }

        if (result.moveToFirst())
        {
            all_products.add(result.getString(result.getColumnIndex(PRODUCT_NAME)));

            while(result.moveToNext())
            {
                all_products.add(result.getString(result.getColumnIndex(PRODUCT_NAME)));
            }
        }
        result.close();

        return all_products;
    }

    // Add a new List to Lists table
    public boolean AddList(String ListName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME_LIST + " (" + LIST_NAME + ") VALUES(\'" + ListName + "\')";

        // Ensure that List name is not empty
        if (ListName.length() <= 0)
            return false;

        // Add a new list
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Modify an existing List name in Lists table
    public boolean ChangeList(String OldListName, String NewListName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME_LIST +
                     " SET " + LIST_NAME + "=\'" + NewListName +  "\'" +
                     " WHERE " + LIST_NAME + "=\'" + OldListName +  "\'";

        // Ensure that List names are not empty
        if ((OldListName.length() <= 0) && (NewListName.length() <= 0))
            return false;

        // Modify List name
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Delete an existing List from Lists table
    public boolean DeleteList(String ListName)
    {
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME_LIST +
                     " WHERE " + LIST_NAME + "=\'" + ListName + "\'";

        // Ensure that List name is not empty
        if (ListName.length() <= 0)
            return false;

        // Delete a list
        try
        {
            dbase.execSQL(sql);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Get an Array of all Lists inside Lists table
    public ArrayList<String> GetAllLists()
    {
        SQLiteDatabase dbase = this.getReadableDatabase();
        String sql = "SELECT " + LIST_NAME +
                     " FROM " + TABLE_NAME_LIST;
        ArrayList<String> all_lists = new ArrayList<String>();
        Cursor result;

        // Get all list names
        result = dbase.rawQuery(sql, null);

        if (result == null)
        {
            return all_lists;
        }

        if (result.moveToFirst())
        {
            all_lists.add(result.getString(result.getColumnIndex(LIST_NAME)));

            while(result.moveToNext())
            {
                all_lists.add(result.getString(result.getColumnIndex(LIST_NAME)));
            }
        }
        result.close();

        return all_lists;
    }

    // Add a Product to a List
    public boolean AddProductToList(String ProductName, String ListName)
    {
        int product_id, list_id;
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql1 = "SELECT " + PRODUCT_ID +
                     " FROM " + TABLE_NAME_PRODUCT +
                     " WHERE " + PRODUCT_NAME + "=\'" + ProductName + "\'";
        String sql2 = "SELECT " + LIST_ID +
                      " FROM " + TABLE_NAME_LIST +
                      " WHERE " + LIST_NAME + "=\'" + ListName + "\'";
        Cursor result1, result2;
        String sql3;

        // Ensure that Product and List name are not empty
        if ((ProductName.length() <= 0) || (ListName.length() <= 0))
            return false;

        // Check if Product name exists and find its Product ID
        result1 = dbase.rawQuery(sql1, null);

        if (result1 == null)
            return false;

        if (!result1.moveToFirst())
            return false;

        product_id = result1.getInt(result1.getColumnIndex(PRODUCT_ID));
        result1.close();

        // Check if List name exists and find its List ID
        result2 = dbase.rawQuery(sql2, null);

        if (result2 == null)
            return false;

        if (!result2.moveToFirst())
            return false;

        list_id = result2.getInt(result2.getColumnIndex(LIST_ID));
        result2.close();

        // Insert (List ID, Product ID) into Table holding a List data
        sql3 = "INSERT INTO " + TABLE_NAME_LIST_PRODUCTS +
               " (" + LIST_ID + "," + PRODUCT_ID + "," + LIST_DISPACHED + ")" +
               " VALUES(" + list_id + "," + product_id + "," + 0 + ");";
        try
        {
            dbase.execSQL(sql3);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Remove a Product from specific List
    public boolean DeleteProductFromList(String ProductName, String ListName)
    {
        int product_id, list_id;
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql1 = "SELECT " + PRODUCT_ID +
                " FROM " + TABLE_NAME_PRODUCT +
                " WHERE " + PRODUCT_NAME + "=\'" + ProductName + "\'";
        String sql2 = "SELECT " + LIST_ID +
                " FROM " + TABLE_NAME_LIST +
                " WHERE " + LIST_NAME + "=\'" + ListName + "\'";
        Cursor result1, result2;
        String sql3;

        // Ensure that Product and List name are not empty
        if ((ProductName.length() <= 0) || (ListName.length() <= 0))
            return false;

        // Check if Product name exists and find its Product ID
        result1 = dbase.rawQuery(sql1, null);

        if (result1 == null)
            return false;

        if (!result1.moveToFirst())
            return false;

        product_id = result1.getInt(result1.getColumnIndex(PRODUCT_ID));
        result1.close();

        // Check if List name exists and find its List ID
        result2 = dbase.rawQuery(sql2, null);

        if (result2 == null)
            return false;

        if (!result2.moveToFirst())
            return false;

        list_id = result2.getInt(result2.getColumnIndex(LIST_ID));
        result2.close();

        // Delete (List ID, Product ID) from Table holding a List data
        sql3 = "DELETE FROM " + TABLE_NAME_LIST_PRODUCTS +
                " WHERE " + LIST_ID + "=\'" + list_id + "\' AND " + PRODUCT_ID + "=\'" + product_id + "\'";

        try
        {
            dbase.execSQL(sql3);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Get Product dispatch status inside a specific list
    public boolean GetProductOfListDispatchStatus(String ProductName, String ListName)
    {
        int product_id, list_id;
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql1 = "SELECT " + PRODUCT_ID +
                " FROM " + TABLE_NAME_PRODUCT +
                " WHERE " + PRODUCT_NAME + "=\'" + ProductName + "\'";
        String sql2 = "SELECT " + LIST_ID +
                " FROM " + TABLE_NAME_LIST +
                " WHERE " + LIST_NAME + "=\'" + ListName + "\'";
        Cursor result1, result2, result3;
        String sql3;
        int DispatchStatus;

        // Ensure that Product and List name are not empty
        if ((ProductName.length() <= 0) || (ListName.length() <= 0))
            return false;

        // Check if Product name exists and find its Product ID
        result1 = dbase.rawQuery(sql1, null);

        if (result1 == null)
            return false;

        if (!result1.moveToFirst())
            return false;

        product_id = result1.getInt(result1.getColumnIndex(PRODUCT_ID));
        result1.close();

        // Check if List name exists and find its List ID
        result2 = dbase.rawQuery(sql2, null);

        if (result2 == null)
            return false;

        if (!result2.moveToFirst())
            return false;

        list_id = result2.getInt(result2.getColumnIndex(LIST_ID));
        result2.close();

        // Get Entry dispatch status
        sql3 = "SELECT " + LIST_DISPACHED +
                " FROM " + TABLE_NAME_LIST_PRODUCTS +
                " WHERE " + LIST_ID + "=" + list_id + " AND " + PRODUCT_ID + "=" + product_id;

        try
        {
            result3 = dbase.rawQuery(sql3, null);

            if (result3 == null)
                return false;

            if (!result3.moveToFirst())
                return false;

            DispatchStatus = result3.getInt(result3.getColumnIndex(LIST_DISPACHED));
            result3.close();
        }
        catch (Exception e)
        {
            return false;
        }

        if (DispatchStatus > 0)
            return true;

        return false;
    }

    // Set Product dispatch status inside a specific list
    public boolean SetProductOfListDispatchStatus(String ProductName, String ListName, boolean DispatchStatus)
    {
        int product_id, list_id;
        SQLiteDatabase dbase = this.getWritableDatabase();
        String sql1 = "SELECT " + PRODUCT_ID +
                " FROM " + TABLE_NAME_PRODUCT +
                " WHERE " + PRODUCT_NAME + "=\'" + ProductName + "\'";
        String sql2 = "SELECT " + LIST_ID +
                " FROM " + TABLE_NAME_LIST +
                " WHERE " + LIST_NAME + "=\'" + ListName + "\'";
        Cursor result1, result2;
        String sql3;
        int NewDispatchStatus;

        // Ensure that Product and List name are not empty
        if ((ProductName.length() <= 0) || (ListName.length() <= 0))
            return false;

        // Check if Product name exists and find its Product ID
        result1 = dbase.rawQuery(sql1, null);

        if (result1 == null)
            return false;

        if (!result1.moveToFirst())
            return false;

        product_id = result1.getInt(result1.getColumnIndex(PRODUCT_ID));
        result1.close();

        // Check if List name exists and find its List ID
        result2 = dbase.rawQuery(sql2, null);

        if (result2 == null)
            return false;

        if (!result2.moveToFirst())
            return false;

        list_id = result2.getInt(result2.getColumnIndex(LIST_ID));
        result2.close();

        // Update Entry dispatch status
        if (DispatchStatus)
            NewDispatchStatus = 1;
        else
            NewDispatchStatus = 0;

        sql3 = "UPDATE " + TABLE_NAME_LIST_PRODUCTS +
               " SET " + LIST_DISPACHED + "=" + NewDispatchStatus  +
               " WHERE " + LIST_ID + "=" + list_id + " AND " + PRODUCT_ID + "=" + product_id;

        try
        {
            dbase.execSQL(sql3);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // Get all Product names from a specific List
    public ArrayList<String> GetListProducts(String ListName)
    {
        int list_id;
        SQLiteDatabase dbase = this.getReadableDatabase();
        String sql1 = "SELECT " + LIST_ID +
                      " FROM " + TABLE_NAME_LIST +
                      " WHERE " + LIST_NAME + "=\'" + ListName + "\'";
        ArrayList<String> all_products = new ArrayList<String>();
        Cursor result1, result2;
        String sql2;

        // Find List ID from List name
        result1 = dbase.rawQuery(sql1, null);

        if (result1 == null)
            return all_products;

        if (!result1.moveToFirst())
            return all_products;

        list_id = result1.getInt(result1.getColumnIndex(LIST_ID));
        result1.close();

        // Find all Product names that belong to given List ID
        sql2 = "SELECT " + PRODUCT_NAME +
                " FROM " + TABLE_NAME_PRODUCT +
                " WHERE " + PRODUCT_ID + " IN (" +
                "SELECT " + PRODUCT_ID +
                " FROM " + TABLE_NAME_LIST_PRODUCTS +
                " WHERE " + LIST_ID + "=" + list_id + ")";

        result2 = dbase.rawQuery(sql2, null);

        if (result2 == null)
        {
            return all_products;
        }

        if (result2.moveToFirst())
        {
            all_products.add(result2.getString(result2.getColumnIndex(PRODUCT_NAME)));

            while(result2.moveToNext())
            {
                all_products.add(result2.getString(result2.getColumnIndex(PRODUCT_NAME)));
            }
        }
        result2.close();

        return all_products;
    }
}
