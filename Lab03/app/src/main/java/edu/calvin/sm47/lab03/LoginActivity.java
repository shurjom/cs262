package edu.calvin.sm47.lab03;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.calvin.sm47.lab03.R;

/**
 *
 * @author sm47
 */
public class LoginActivity extends Activity {

    private EditText passwordEditText;
    private TextView invalidTextView;
    private ImageView androidImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        invalidTextView = (TextView) findViewById(R.id.invalidTextView);
        invalidTextView.setVisibility(View.GONE);
        androidImageView = (ImageView) findViewById(R.id.androidImageView);
        androidImageView.setVisibility(View.GONE);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String password = passwordEditText.getText().toString();
                if (password.equals("haddock")) {
                    invalidTextView.setVisibility(View.GONE);
                    androidImageView.setVisibility(View.VISIBLE);
                } else {
                    androidImageView.setVisibility(View.GONE);
                    invalidTextView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

}
