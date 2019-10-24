package uk.ac.stir.cs.provider;

import android.content.ContentUris;
// import content URI
import android.content.Context;
// import context
import android.content.ContentResolver;
// import content resolver
import android.content.ContentProvider;
// import content provider
import android.content.ContentValues;
// import content values
import android.content.UriMatcher;
// import URI matcher
import android.database.Cursor;
// import database cursor
import android.database.SQLException;
// import SQL exception
import android.database.sqlite.SQLiteDatabase;
// import SQLite database
import android.database.sqlite.SQLiteOpenHelper;// import SQLite helper
import android.database.sqlite.SQLiteQueryBuilder; // import SQLite query
import android.net.Uri;
// import URI
import android.text.TextUtils;
// import text utilities
public class ModuleProvider extends ContentProvider {
/*
-------------------------------
Constants
------------------------------
*/
    /** Database modules table */
    public final static String DATABASE_TABLE = "modules";
    /** Content provider name */
    public final static String PROVIDER_NAME = "uk.ac.stir.cs.provider.Module";
    /** Content provider URI */
    public final static Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" + DATABASE_TABLE);
    /** URI code for all modules */
    private final static int ALL_MODULES = 1;
    /** URI code for one module */
    private final static int ONE_MODULE = 2;
    /*
    -----------
    -----------------
    --
    Variables
    -------------------------------
    */
    private ContentResolver contentResolver;
    private SQLiteDatabase modulesDatabase;
    private UriMatcher uriMatcher;
/** Content provider name */
/*
---------------------------
----
Methods
----
----------------------------
*/
    /**
     Delete a selection from the module table.
     @param uri URI for deletion
     @param selection optional filter on rows to be deleted
     @param arguments selection arguments (argument replaces '?' in selection)
     @return number of rows deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] arguments) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ALL_MODULES:
                count = modulesDatabase.delete(DATABASE_TABLE, selection, arguments);
                break;
            case ONE_MODULE:
                String code = uri.getPathSegments().get(1);
                code = "code = '" + code + "'";
                if (!TextUtils.isEmpty(selection))
                    code += " AND (" + selection + ")";
                count = modulesDatabase.delete(DATABASE_TABLE, code, arguments);
                break;
            default:
                throw (new IllegalArgumentException("Unknown URI '" + uri + "'"));
        }
        contentResolver.notifyChange(uri, null);
        return (count);
    }
    /**
     Return MIME type for a module query.
     @param uri URI for querying
     @return MIME return type
     */
    @Override
    public
    String getType(Uri uri) {
        String mimeType;
        switch (uriMatcher.match(uri)) {
            case ALL_MODULES:
                mimeType = "vnd.uk.ac.stir.cs.cursor.dir/modules";
                break;
            case ONE_MODULE:
                mimeType = "vnd.uk.ac.stir.cs.cursor.item/modules";
                break;
            default:
                throw (new IllegalArgumentException("Invalid URI '" + uri + "'"));
        }
        return (mimeType);
    }
    /**
     Insert values into the module table.
     @param uri URI for insertion
     @param values column name/value pairs to be inserted
     @return URI of the newly inserted items
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri;
        long rowIdentifier = modulesDatabase.insert(DATABASE_TABLE, "", values);
        if (rowIdentifier > 0) {
            newUri = ContentUris.withAppendedId(CONTENT_URI, rowIdentifier);
            contentResolver.notifyChange(newUri, null);
        }
        else
            throw (new SQLException("Failed to insert row into '" + uri + "'"));
        return (newUri);
    }
/**
 Create modules database

 @return
 true if database was created successfully
 */
@Override
public boolean onCreate() { uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "modules", ALL_MODULES);
        uriMatcher.addURI(PROVIDER_NAME, "modules/*", ONE_MODULE);
        Context context = getContext();
        contentResolver = context.getContentResolver();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        modulesDatabase = databaseHelper.getWritableDatabase();
        boolean result = modulesDatabase != null;
        return (result);
    }
    /**
     Query database.
     @param uri URI for querying
     @param projection list of columns for database cursor (null = all columns)
     @param selection optional filter on rows to be queried (null = all rows)
     @param arguments selection arguments (argument replaces '?' in selection)
     @param sortOrder sorting order (null or empty = sort by code)
     @return database cursor position
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] arguments, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DATABASE_TABLE);
        if (uriMatcher.match(uri) == ONE_MODULE) {
            String code = uri.getPathSegments().get(1);
            sqlBuilder.appendWhere("code = '" + code + "'");
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = "code";
        }
        Cursor cursor = sqlBuilder.query(modulesDatabase, projection, selection, arguments, null, null, sortOrder);
        cursor.setNotificationUri(contentResolver, uri);
        return (cursor);
    }
    /**
     Update database.
     @param uri URI for updating
     @param values mapping from column names to values
     @param selection optional filter on rows to be updated
     @param arguments selection arguments (argument replaces '?in selection)
     @return number of updated rows
     */
    @Override
    public int
    update(Uri uri, ContentValues values, String selection, String[] arguments) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case
                    ALL_MODULES:
                count = modulesDatabase.update(DATABASE_TABLE, values, selection, arguments);


                break;
            case
                    ONE_MODULE:
                String code = uri.getPathSegments().get(1);
                code = "code = '" + code + "'";
                if (!TextUtils.isEmpty(selection))
                code += " AND (" + selection + ")";
                count = modulesDatabase.update(DATABASE_TABLE, values, code, arguments);
                break;
            default:
                throw (new IllegalArgumentException("Unknown URI " + uri));
        }
        contentResolver.notifyChange(uri, null);
        return (count);
    }
}
class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "courses";
    private final static int DATABASE_VERSION = 1;

    /**
     * Database helper class.
     *
     * @param context context
     */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Handle database creation.
     *
     * @param database database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + ModuleProvider.DATABASE_TABLE + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "code TEXT NOT NULL," + "name TEXT NOT NULL" + ");");
    }

    /**
     * Handle
     * database upgrade
     * .
     *
     * @param database   database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + ModuleProvider.DATABASE_TABLE);
        onCreate(database);
    }
}

