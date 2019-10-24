package com.example.currency;

import android.app.Activity;   // import activity
import android.os.Bundle;    // import bundle
import android.view.View;    // import view
import android.widget.Button;   // import button
import android.widget.EditText;   // import edit text
import android.widget.Toast;   // import toast

public class MainActivity extends Activity {

    /* ------------------------------- Constants ------------------------------ */
// define constants for one pound in other currencies (e.g. GBPtoXYZ)
    final static double GBP = 1.0;
    final static double GBP_CAD = 1.65;
    final static double GBP_zloty = 4.87;



//      /* ------------------------------ Variables ------------------------------- */
//      /** Pound text field */
 EditText poundText;
 EditText cadText;
 EditText zlotyText;

// add similar fields for other currencies

    /* ------------------------------- Methods -------------------------------- */

    /**
     * Create user interface and set up listeners for buttons.
     *
     * @param savedInstanceState previously saved state
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // create basic interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// set up currency fields, with only pounds being focusable (i.e. editable)
        poundText = (EditText) findViewById(R.id.poundValue);
        poundText.setFocusable(true);
        cadText = (EditText) findViewById(R.id.cadValue);
        cadText.setFocusable(false);
        zlotyText = (EditText) findViewById(R.id.zlotyValue);
        zlotyText.setFocusable(false);
// similarly set up other currencies, calling "setFocusable(false)" for them
// when convert is clicked, convert pounds to other currencies
        Button convertButton = (Button) findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    String poundString = poundText.getText().toString();
                    float pounds = Float.parseFloat(poundString);

                    float CAD = Math.round(pounds * GBP_CAD * 100) / 100f;
                    cadText.setText(String.valueOf(CAD));
                    float zloty = Math.round(pounds * GBP_zloty * 100) / 100f;
                    zlotyText.setText(String.valueOf(zloty));

                    }
 catch(Exception exception){
                        // report problem in pop-up window
                        Toast.makeText(view.getContext(), "Invalid data - try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                poundText.setText("");
                cadText.setText("1.65");
                zlotyText.setText("4.87");

            }
            });

        }
    }
