package com.hhto.dmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * A list adapter for MenuFragment's list.
 */
public class MenuListAdapter extends ArrayAdapter<Restaurant.RestaurantMenu> {
    private static int ROW_LAYOUT = R.layout.menu_item;
    private Context context;
    private List<Restaurant.RestaurantMenu> menus;

    public MenuListAdapter(Context context, List<Restaurant.RestaurantMenu> menus) {
        super(context, ROW_LAYOUT, menus);
        this.context = context;
        this.menus = menus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(ROW_LAYOUT, parent, false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        TextView nameText = (TextView) rowView.findViewById(R.id.nameText);
        TextView menuText = (TextView) rowView.findViewById(R.id.menuText);

        // Get content data
        Restaurant.RestaurantMenu menu = menus.get(position);
        List<Restaurant.Course> courses = menu.getCourses();
        String language = pref.getString("pref_key_language", "en");
        Boolean showProperties = pref.getBoolean("pref_key_show_properties", true);

        // Set content
        SpannableStringBuilder collector = new SpannableStringBuilder();
        nameText.setText(menu.getRestaurant().getName());
        int titleColor = context.getResources().getColor(R.color.menu_blue);
        int propertiesColor = context.getResources().getColor(R.color.properties_red);
        int lineSpacing = (int) context.getResources().getDimension(R.dimen.course_spacing);
        for (Restaurant.Course course: courses) {
            SpannableString title = new SpannableString(course.getTitle(language));
            title.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            collector.append(title);
            if (showProperties) {
                SpannableString properties = new SpannableString(" " + course.getProperties());
                properties.setSpan(new ForegroundColorSpan(propertiesColor), 0, properties.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                collector.append(properties);
            }
            collector.append("\n");

            // Add a little spacing between courses
            SpannableString spacingLine = new SpannableString("\n");
            spacingLine.setSpan(new AbsoluteSizeSpan(lineSpacing, true), 0, spacingLine.length(), 0);
            collector.append(spacingLine);
        }

        menuText.setText(collector, TextView.BufferType.SPANNABLE);

        return rowView;
    }
}
