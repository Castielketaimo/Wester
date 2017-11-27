package ca.bcit.wester.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.wester.DatabaseHandler;
import ca.bcit.wester.models.Service;

/**
 * Controller for the Service table.
 *
 * @author Team <Void>
 */
public class ServiceController extends DatabaseHandler
{
    /** Service table name. */
    private static final String TABLE_NAME = "Service";

    /** ID column name. */
    private static final String ID_COLUMN_NAME = "_serviceID";


    /** List of services.**/
    private static ArrayList<Service> serviceList = new ArrayList<Service>();

    /**
     * Inherit/use DatabaseHandler constructor.
     *
     * @param context - Apps Current Content
     */
    public ServiceController(Context context)
    {
        super(context);
    }

    /**
     * Creates a new service and insert into the database.
     *
     * @param service - service being made.
     * @return success - true service was made.
     */
    public boolean create(Service service)
    {
        // Prepare insertion.
        ContentValues values = new ContentValues();
        values.put("Name", service.getName());
        values.put("Longitude", service.getLongitude());
        values.put("Latitude", service.getLatitude());
        values.put("Tags", convertArrayListToString(service.getTags()));
        values.put("Hours", service.getHours());
        values.put("Address", service.getAddress());
        values.put("Postal", service.getPostalCode());
        values.put("Phone", service.getPhone());
        values.put("Email", service.getEmail());
        values.put("Website", service.getWebsite());
        values.put("Category", service.getCategory());
        values.put("Description", service.getDescription());

        // Connect to database, insert, and close database.
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.insert(TABLE_NAME, null, values) > 0;
        db.close();

        // Return whether success or not.
        return success;
    }

    /**
     * Returns all service records.
     *
     * @return recordList - All services in Service Table
     */
    public List<Service> read()
    {
        // New list of service.
        List<Service> recordsList = new ArrayList<Service>();

        // SQL statement to grab all services ordered by descending.
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC";

        // Create new db instance, and cursor instance.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Go through and add all services to the service list.
        if (cursor.moveToFirst())
        {
            do
            {
                // Get Service details
                int _serviceID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
                String Name = cursor.getString(cursor.getColumnIndex("Name"));
                double Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude")));
                double Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude")));
                ArrayList<String> Tags = convertStringToArrayList(cursor.getString((cursor.getColumnIndex("Tags"))));
                String Category = cursor.getString(cursor.getColumnIndex("Category"));
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String Hours = cursor.getString(cursor.getColumnIndex("Hours"));
                String Address = cursor.getString(cursor.getColumnIndex("Address"));
                String Postal = cursor.getString(cursor.getColumnIndex("Postal"));
                String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
                String Email = cursor.getString(cursor.getColumnIndex("Email"));
                String Website = cursor.getString(cursor.getColumnIndex("Website"));

                // Create new service instance, with serviceID.
                Service service = new Service(_serviceID, Name, Longitude, Latitude, Tags, Category, Description, Hours, Address, Postal, Phone, Email, Website);

                // Add record to list.
                recordsList.add(service);
            }
            while (cursor.moveToNext());
        }

        // Close cursor and database.
        cursor.close();
        db.close();

        // Return list of services.
        return recordsList;
    }

    /**
     * Read a single service.
     *
     * @param serviceID - Service Id in the database.
     * @return service - Service object found by ID.
     */
    public Service readSingleRecordById(int serviceID)
    {
        // Create service object.
        Service service = null;

        // Grab service from DB with specific service id.
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE _serviceID = " + serviceID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // If service exists.
        if (cursor.moveToFirst())
        {
            // Get service details
            int _serviceID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            double Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude")));
            double Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude")));
            ArrayList<String> Tags = convertStringToArrayList(cursor.getString((cursor.getColumnIndex("Tags"))));
            String Category = cursor.getString(cursor.getColumnIndex("Category"));
            String Description = cursor.getString(cursor.getColumnIndex("Description"));
            String Hours = cursor.getString(cursor.getColumnIndex("Hours"));
            String Address = cursor.getString(cursor.getColumnIndex("Address"));
            String Postal = cursor.getString(cursor.getColumnIndex("Postal"));
            String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
            String Email = cursor.getString(cursor.getColumnIndex("Email"));
            String Website = cursor.getString(cursor.getColumnIndex("Website"));

            // Assign the service object.
            service = new Service(_serviceID, Name, Longitude, Latitude, Tags, Category, Description, Hours, Address, Postal, Phone, Email, Website);
        }

        // Close cursor and db.
        cursor.close();
        db.close();

        // Return the service.
        return service;
    }

    public List<Service> readRecordsByCategory(String category)
    {
        serviceList.clear();
//        // New list of service.
//        List<Service> recordsList = new ArrayList<Service>();

        // SQL statement to grab all services ordered by descending.
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE Category LIKE '" + category + "'";

        // Create new db instance, and cursor instance.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Go through and add all services to the service list.
        if (cursor.moveToFirst())
        {
            do
            {
                // Get Service details
                int _serviceID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
                String Name = cursor.getString(cursor.getColumnIndex("Name"));
                double Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude")));
                double Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude")));
                ArrayList<String> Tags = convertStringToArrayList(cursor.getString((cursor.getColumnIndex("Tags"))));
                String Category = cursor.getString(cursor.getColumnIndex("Category"));
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String Hours = cursor.getString(cursor.getColumnIndex("Hours"));
                String Address = cursor.getString(cursor.getColumnIndex("Address"));
                String Postal = cursor.getString(cursor.getColumnIndex("Postal"));
                String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
                String Email = cursor.getString(cursor.getColumnIndex("Email"));
                String Website = cursor.getString(cursor.getColumnIndex("Website"));

                // Create new service instance, with serviceID.
                Service service = new Service(_serviceID, Name, Longitude, Latitude, Tags, Category, Description, Hours, Address, Postal, Phone, Email, Website);

                // Add record to list.
                serviceList.add(service);
            }
            while (cursor.moveToNext());
        }

        // Close cursor and database.
        cursor.close();
        db.close();

        // Return list of services.
        return serviceList;
    }


    public List<Service> readRecordsByDescription(String Descripton)
    {
        serviceList.clear();
//        // New list of service.
//        List<Service> recordsList = new ArrayList<Service>();

        // SQL statement to grab all services ordered by descending.
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE Description LIKE '%" + Descripton + "%' " +
                "OR Category LIKE '%" + Descripton + "%'" +
                "OR Name LIKE '%" + Descripton + "%'";

        // Create new db instance, and cursor instance.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Go through and add all services to the service list.
        if (cursor.moveToFirst())
        {
            do
            {
                // Get Service details
                int _serviceID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
                String Name = cursor.getString(cursor.getColumnIndex("Name"));
                double Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude")));
                double Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude")));
                ArrayList<String> Tags = convertStringToArrayList(cursor.getString((cursor.getColumnIndex("Tags"))));
                String Category = cursor.getString(cursor.getColumnIndex("Category"));
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String Hours = cursor.getString(cursor.getColumnIndex("Hours"));
                String Address = cursor.getString(cursor.getColumnIndex("Address"));
                String Postal = cursor.getString(cursor.getColumnIndex("Postal"));
                String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
                String Email = cursor.getString(cursor.getColumnIndex("Email"));
                String Website = cursor.getString(cursor.getColumnIndex("Website"));

                // Create new service instance, with serviceID.
                Service service = new Service(_serviceID, Name, Longitude, Latitude, Tags, Category, Description, Hours, Address, Postal, Phone, Email, Website);

                // Add record to list.
                serviceList.add(service);
            }
            while (cursor.moveToNext());
        }

        // Close cursor and database.
        cursor.close();
        db.close();

        // Return list of services.
        return serviceList;
    }


    /**
     * Update Service.
     *
     * @param service - Service being updated.
     * @return success - True if update was successful.
     */
    public boolean update(Service service)
    {
        // Prepare update.
        ContentValues values = new ContentValues();
        values.put("Name", service.getName());
        values.put("Longitude", service.getLongitude());
        values.put("Latitude", service.getLatitude());
        values.put("Tags", convertArrayListToString(service.getTags()));
        values.put("Hours", service.getHours());
        values.put("Address", service.getAddress());
        values.put("Postal", service.getPostalCode());
        values.put("Phone", service.getPostalCode());
        values.put("Email", service.getPostalCode());
        values.put("Website", service.getPostalCode());
        values.put("Category", service.getCategory());
        values.put("Description", service.getDescription());

        // Prepare update on specific service id.
        String where = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { Integer.toString(service.getID()) };

        // Connect, update, and close connection.
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.update(TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        // Return whether successful or not.
        return success;
    }

    /**
     * Delete an service record.
     *
     * @param serviceID - Id for the record to delete.
     * @return success - True if deletion worked.
     */
    public boolean delete(int serviceID)
    {
        // Connect, delete, and close connection.
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(TABLE_NAME, ID_COLUMN_NAME + " ='" + serviceID + "'", null) > 0;
        db.close();

        // Return whether successful.
        return success;
    }

    /**
     * Converts the ArrayList to a String for insertion
     * @param tags - ArrayList of tags
     * @return - the ArrayList as a string separated by spaces
     */
    private String convertArrayListToString(ArrayList<String> tags)
    {
        String stringTag = "";
        for(int i = 0; i < tags.size(); i++)
        {
            stringTag += tags.get(i)+ " ";
        }
        return stringTag;
    }

    /**
     * Converts the String to a ArrayList for reading
     * @param tags - String of tags separated by spaces
     * @return - ArrayList of tags
     */
    private ArrayList<String> convertStringToArrayList(String tags)
    {
        ArrayList<String> arrayListTag = new ArrayList<>();
        String[] arrayTag = tags.split(" ");
        for(int i = 0; i < arrayTag.length; i++)
        {
            arrayListTag.add(arrayTag[i]);
        }
        return arrayListTag;
    }

    /**
     * Gets the services current in view.
     * @return ArrayList of services
     */
    public static ArrayList<Service> getServiceList() {
        return serviceList;
    }

    public List<Service> readAllIntoView()
    {
        serviceList.clear();
        // New list of service.
//        List<Service> recordsList = new ArrayList<Service>();

        // SQL statement to grab all services ordered by descending.
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC";

        // Create new db instance, and cursor instance.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // Go through and add all services to the service list.
        if (cursor.moveToFirst())
        {
            do
            {
                // Get Service details
                int _serviceID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
                String Name = cursor.getString(cursor.getColumnIndex("Name"));
                double Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Longitude")));
                double Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Latitude")));
                ArrayList<String> Tags = convertStringToArrayList(cursor.getString((cursor.getColumnIndex("Tags"))));
                String Category = cursor.getString(cursor.getColumnIndex("Category"));
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String Hours = cursor.getString(cursor.getColumnIndex("Hours"));
                String Address = cursor.getString(cursor.getColumnIndex("Address"));
                String Postal = cursor.getString(cursor.getColumnIndex("Postal"));
                String Phone = cursor.getString(cursor.getColumnIndex("Phone"));
                String Email = cursor.getString(cursor.getColumnIndex("Email"));
                String Website = cursor.getString(cursor.getColumnIndex("Website"));

                // Create new service instance, with serviceID.
                Service service = new Service(_serviceID, Name, Longitude, Latitude, Tags, Category, Description, Hours, Address, Postal, Phone, Email, Website);

                // Add record to list.
                serviceList.add(service);
            }
            while (cursor.moveToNext());
        }

        // Close cursor and database.
        cursor.close();
        db.close();

        // Return list of services.
        return serviceList;
    }
}
