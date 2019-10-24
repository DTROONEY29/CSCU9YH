package uk.ac.stir.cs.android;


import android.app.Activity;
// import activity
import android.database.Cursor;
// import database cursor
import android.net.Uri;
// import URIs
import android.os.Bundle;
// import bundle
import android.view.View;
// import view
import android.view.View.OnClickListener;
// import click listener
import android.widget.Button;
// import button
import android.widget.EditText;
// import edit text
import android.widget.Toast;
// import toast

public class ModuleClientActivity extends Activity {
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
    /*
    -------------
    -----------------
    Variables
    -------------------------------
    */
    EditText codeText; /** Module code text field */
    EditText nameText; /** Module name text field */

    /**
     Create user interface and set up listeners for buttons.
     @param savedInstanceState
     previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
// create basic interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_provider);
// set up module fields, with only code being editable
        codeText = (EditText) findViewById(R.id.codeValue);
        nameText = (EditText) findViewById(R.id.nameValue);
        nameText.setFocusable(false);
// when clear is clicked, empty the module fields
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                codeText.setText("");
                                nameText.setText("");
                            }
                        });
// when translate is clicked, get module name corresponding to module code
        Button translateButton = (Button) findViewById(R.id.translateButton);
        translateButton.setOnClickListener(
                new OnClickListener() {
            public void onClick(View view) {
                try {
                    String moduleCode = codeText.getText().toString();
                    Uri uri = Uri.parse(CONTENT_URI + "/" + moduleCode);
                    Cursor cursor = managedQuery(uri, null, null, null, null);
                    if (cursor.getCount() == 1) {
                        cursor.moveToFirst();
                        String moduleName = cursor.getString(cursor.getColumnIndex("name"));
                        nameText.setText(moduleName);
                    }
                    else
                    { nameText.setText("");
                        throw
                                (new Exception("module code invalid"));
                    }
                }
                catch
                (Exception exception) {
// report problem in pop-up window
                    Toast.makeText(view.getContext(),
                            "Invalid data - " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show()
                    ;
                }
            }
        });
    }
}