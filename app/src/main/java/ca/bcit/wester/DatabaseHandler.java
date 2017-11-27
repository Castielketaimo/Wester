package ca.bcit.wester;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Wrapper/Handler for the application.
 *
 * @author Team <Void>
 */
public class DatabaseHandler extends SQLiteOpenHelper
{

    /** Wester database name. */
    private static final String DB_NAME = "WesterDB";

    /** Database version number. */
    private static final int DB_VERSION = 2;

    /**
     * Provides helper with applications context.
     *
     * @param context - Apps Current Content
     */
    public DatabaseHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db - SQLite db connection
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //dropTables(db); // For development only.
        initDatabase(db);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db - SQLite db connection
     * @param oldVersion - Old db version number
     * @param newVersion - New db version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        dropTables(db);
        initDatabase(db);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db - SQLite db connection
     */
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        initDatabase(db);
    }

    /**
     * Initializes the structure of the database.
     *
     * @param db - SQLite db connection
     */
    private static void initDatabase(SQLiteDatabase db)
    {
        // Service table.
        String service_table_sql =
            "CREATE TABLE IF NOT EXISTS Service (" +
            "_serviceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Name TEXT, " +
            "Longitude REAL, " +
            "Latitude REAL, " +
            "Tags TEXT, " +
            "Category TEXT, " +
            "Description TEXT, " +
            "Hours TEXT, " +
            "Address TEXT, " +
            "Postal TEXT, " +
            "Phone TEXT, " +
            "Email TEXT, " +
            "Website TEXT);";

        // Create the tables/execute the SQL.
        db.execSQL(service_table_sql);
    }

    /**
     * Checks if the service table is empty
     * @return true if it is
     */
    public boolean checkTableIsEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM Service";
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int icount = c.getInt(0);
        c.close();
        if(icount>0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Clears the database of all tables.
     *
     * @param db - SQLite db connection
     */
    private static void dropTables(SQLiteDatabase db)
    {
        // Query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM WesterDB WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        // Iterate over the result set, adding every table name to a list
        while (c.moveToNext())
        {
            tables.add(c.getString(0));
        }

        // Call DROP TABLE on every table name
        for (String table : tables)
        {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }

        // Close cursor.
        c.close();
    }
}
