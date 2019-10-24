package uk.ac.stir.cs.provider;

import android.app.Activity;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

public class ModuleProviderActivity extends Activity {

    private ContentResolver contentResolver;

    /**
     * Add module to database. If updating the database table does not work
     * (because the module does not exist), simply insert it into the
     * table.
     *
     * @param code module code
     * @param name module name
     */
    public void addModule(String code, String name) {
        Log.v("adding module", "code " + code + " name " + name);
        ContentValues values = new ContentValues();
        Uri uri = Uri.withAppendedPath(ModuleProvider.CONTENT_URI, code);
        values.put("code", code);
        values.put("name", name);
        int rows = contentResolver.update(uri, values, "", null);
        if (rows == 0) {
            uri = contentResolver.insert(uri, values);
        }
    }


    /**
     Set up modules in database and report completion.
     @param savedInstanceState saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContentResolver();// delete any existing module table
        contentResolver.delete(ModuleProvider.CONTENT_URI, null, null);
        // call addModule("Code", "Name") for each module to be added
        addModule("CSCU9V5", "Concurrent & Distributed Systems ");
        addModule("CSCU9A1", "Software Engineering 1");
        addModule("CSCU9Y7", "Computer Security & Forensics");
        setContentView(R.layout.activity_module_provider); // report completion
    }
}