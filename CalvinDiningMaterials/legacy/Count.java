package edu.calvin.kpb23students.calvindining.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.calvin.kpb23students.calvindining.R;



/**
 * <p>
 *     This fragment shows the calendar
 * </p>
 */
public class Count extends Fragment {
    public Count() {
        // Required empty public constructor
    }

    public class FoodCount {

        public int myCount;

        public FoodCount(int value){this.myCount = value;}

        void changeCount(int newCount){this.myCount = newCount;}

        int getCount(){
            return this.myCount;
        }
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static Count newInstance(String param1, String param2) {
        Count fragment = new Count();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FoodCount blockPlan = new FoodCount(0);
    public int mealCount;
    public int foodPD = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button configureButton = (Button)findViewById(R.id.foodButton);
        Button eatButton = (Button)findViewById(R.id.consumeButton);
        Button undoButton = (Button)findViewById(R.id.undoButton);
        Button keyboardButton = (Button)findViewById(R.id.keyboardButton);


        keyboardButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }
                }
        );

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_count, container, false);
    }


}
