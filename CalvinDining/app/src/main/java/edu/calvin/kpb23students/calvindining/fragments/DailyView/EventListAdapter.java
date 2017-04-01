package edu.calvin.kpb23students.calvindining.fragments.DailyView;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.calvin.kpb23students.calvindining.CalvinDiningService;
import edu.calvin.kpb23students.calvindining.R;

/**
 * <p>
 *  Handles displaying data given to display.
 * <p/>
 *
 * @author Kristofer Brink
 * @version Fall, 2016
 */
public class EventListAdapter extends EventListObserver {
    /**
     * Events changed to display item
     */
    static class DisplayItem {
        public final CalvinDiningService.Meal meal;
        public final String startTime;
        public final String endTime;
        public final String day;
        public DisplayItem(CalvinDiningService.Meal meal, String startTime, String endTime){
            this.meal = meal;
            this.startTime = startTime;
            this.endTime = endTime;
            this.day = null;
        }

        public DisplayItem() {
            this.meal = null;
            this.startTime = null;
            this.endTime = null;
            this.day = null;
        }
        public DisplayItem(String day) {
            this.meal = null;
            this.startTime = null;
            this.endTime = null;
            this.day = day;
        }
    }

    final Context context;
    final LayoutInflater layoutInflater;
    final ArrayList<DisplayItem> displayItems;
    final CalvinDiningService diningService;
    final Observer diningServiceObserver;
    final String diningVenueName;

    /**
     * Handles the displaying the events
     * @param context
     * @param layoutInflater
     */
    public EventListAdapter(Context context, LayoutInflater layoutInflater, final CalvinDiningService diningService, final String diningVenueName) {
        this.diningVenueName = diningVenueName;
        this.context = context;
        this.layoutInflater = layoutInflater;
        displayItems = new ArrayList<DisplayItem>();
        this.diningService = diningService;
        diningServiceObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                setEvents();
            }
        };
    }

    /**
     * handles first Observer
     */
    @Override
    protected void gainedFirstDataSetObserver() {
        diningService.addObserver(diningServiceObserver);
        // Do initial load of data after observer is added so that
        // we get updated with any changes that happened between
        // construction and when a subscriber was added.
        diningServiceObserver.update(null, null);
    }

    /**
     * handles when all observers have been lost
     */
    @Override
    protected void lostLastDataSetObserver() {
        diningService.deleteObserver(diningServiceObserver);
    }

    /**
     * use information from the server to set the events and convert things to Strings properly
     */
    private void setEvents() {
        final List<CalvinDiningService.Meal> meals = diningService.getEvents(diningVenueName);
        // http://stackoverflow.com/a/21862750/2948122
        new Handler(context.getMainLooper()).post(new Runnable() {

            private void makeDay(String day, Collection<CalvinDiningService.Meal> meals, DateFormat timeFormat){
                displayItems.add(new DisplayItem(day));
                for (CalvinDiningService.Meal meal: meals) {
                    // Make event display item
                    DisplayItem displayItem = new DisplayItem(
                            meal,
                            timeFormat.format(meal.getGregStartTime().getTime()),
                            timeFormat.format(meal.getGregEndTime().getTime())
                    );
                    displayItems.add(displayItem);
                    displayItems.add(new DisplayItem());
                }
            }


            @Override
            public void run() {
                displayItems.clear();

                // Set meals to mealArray
                CalvinDiningService.Meal[] mealArray = meals.toArray(new CalvinDiningService.Meal[meals.size()]);


                if (mealArray.length > 0) {
                    // Sort events by start time -------------------------------------------------------------------------------
                    Arrays.sort(mealArray, new Comparator<CalvinDiningService.Meal>() {
                        @Override
                        public int compare(CalvinDiningService.Meal o1, CalvinDiningService.Meal o2) {
                            return o1.getGregStartTime().compareTo(o2.getGregEndTime());
                        }
                    });
                    int startHour = mealArray[0].getGregStartTime().get(Calendar.HOUR_OF_DAY);

                    // Set displayItems
                    // Add events that are empty so it can fill in the gaps this assumes events don't overlap.
                    GregorianCalendar beginOverlap = (GregorianCalendar) mealArray[0].getGregStartTime().clone();
                    beginOverlap.set(Calendar.HOUR_OF_DAY, startHour);
                    beginOverlap.set(Calendar.MINUTE, 0);
                    GregorianCalendar endOverlap;


                    DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);

                    // Split into today and tomorrow then make them
                    // Get todays date
                    final Calendar today = Calendar.getInstance(); // get current time
                    today.setTimeZone(mealArray[0].getGregStartTime().getTimeZone()); // set to right time zone
                    ArrayList<CalvinDiningService.Meal> todayMeal = new ArrayList<CalvinDiningService.Meal>();
                    ArrayList<CalvinDiningService.Meal> tomorrowMeal = new ArrayList<CalvinDiningService.Meal>();
                    for (CalvinDiningService.Meal meal: mealArray) {
                        if (meal.getGregStartTime().get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                            todayMeal.add(meal);
                        } else {
                            tomorrowMeal.add(meal);
                        }
                    }

                    makeDay("Today", todayMeal, timeFormat);
                    makeDay("Tomorrow", tomorrowMeal, timeFormat);

                    //Arrays.stream(mealArray).filter(m->m.getGregStartTime().get(MealCount.DAY_OF_YEAR) == today.get(MealCount.DAY_OF_YEAR)).toArray();


                }
                // If no meals tell the user
                // TODO make this better and make between events not use the display item class or not?
                else {
                    DisplayItem betweenEvents = new DisplayItem("Could not find meals from Calvin's Server");
                    displayItems.add(betweenEvents);
                }
                notifyDataSetChanged(); // rerun getView to notice new changes
            }
        });
    }


    /**
     * gets the size of the display Items
     * @return count
     */
    @Override
    public int getCount() {
        return this.displayItems.size();
    }

    /**
     * gets an item from the displayItems
     * @param position
     * @return item
     */
    @Override
    public Object getItem(int position) {
        return displayItems.get(position);
    }

    /**
     * @param position position of item
     * @return id long identification of the item
     */
    @Override
    public long getItemId(int position) {
        return -1;
    }

    /**
     * Handles view
     * @param position position of displayItem
     * @param convertView handle view
     * @param parent
     * @return View of the labels
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("X", "Changing text " + String.valueOf(position));

        DisplayItem displayItem = this.displayItems.get(position);

        // TODO use something better than an if else
        if (displayItem.meal != null) {
            // If event
            TimeLabel timeLabel = (TimeLabel)layoutInflater.inflate(R.layout.time_label, parent, false);
            timeLabel.set(true, displayItem.meal.getName(), displayItem.startTime, displayItem.endTime, displayItem.meal.getDescription());
            return timeLabel;
        } else {
            if (displayItem.day == null) {
                // BetweenEvents
                TimeLabelBetween timeLabelBetween = (TimeLabelBetween) layoutInflater.inflate(R.layout.time_label_between, parent, false);
                timeLabelBetween.set(false);
                return timeLabelBetween;
            } else {
                TimeLabelBetween timeLabelBetween = (TimeLabelBetween) layoutInflater.inflate(R.layout.time_label_between, parent, false);
                timeLabelBetween.set(false, displayItem.day);
                return timeLabelBetween;
            }
        }
    }
}
