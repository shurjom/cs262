package com.example.cl43.menucode;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public FoodCount blockPlan = new FoodCount(0);
    public int mealCount;
    public int foodPD = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button configureButton = (Button)findViewById(R.id.foodButton);
        Button eatButton = (Button)findViewById(R.id.consumeButton);
	    Button undoButton = (Button)findViewById(R.id.undoButton);

        configureButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        EditText foodCount = (EditText) findViewById(R.id.countText);
                        int value = Integer.parseInt(foodCount.getText().toString());
                        blockPlan.changeCount(value);
                        Toast toast = Toast.makeText(getApplicationContext(),"Block count set to "+value,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );

        eatButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        mealCount = blockPlan.getCount();
                        foodPD++;
                        mealCount--;
                        if(mealCount < 0){
                            foodPD--;
                            mealCount = 0;
                            Toast toast = Toast.makeText(getApplicationContext(),"Cannot put in anymore meals. Please add more blocks.",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        blockPlan.changeCount(mealCount);
                        TextView foodPDCount = (TextView) findViewById(R.id.foodPDView);
                        EditText foodCount = (EditText) findViewById(R.id.countText);
                        foodPDCount.setText(Integer.toString(foodPD));
                        foodCount.setText(Integer.toString(mealCount));
                    }
                }
        );

        undoButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        mealCount = blockPlan.getCount();
                        foodPD--;
                        mealCount++;
                        if(foodPD < 0){
                            foodPD++;
                            mealCount--;
                            Toast toast = Toast.makeText(getApplicationContext(),"Nothing to undo.",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        blockPlan.changeCount(mealCount);
                        TextView foodPDCount = (TextView) findViewById(R.id.foodPDView);
                        EditText foodCount = (EditText) findViewById(R.id.countText);
                        foodPDCount.setText(Integer.toString(foodPD));
                        foodCount.setText(Integer.toString(mealCount));
                    }
                }
        );
    }
}
