package com.hhto.dmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hmhagberg on 7.3.2014.
 */

public class MenuListAdapter extends ArrayAdapter<Restaurant.RestaurantMenu> {
    private static final int ROW_LAYOUT = R.layout.list_item;
    private final Context context;
    private final List<Restaurant.RestaurantMenu> menus;

    public MenuListAdapter(Context context, List<Restaurant.RestaurantMenu> menus) {
        super(context, ROW_LAYOUT, menus);
        this.context = context;
        this.menus = menus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(ROW_LAYOUT, parent, false);
        TextView nameText = (TextView) rowView.findViewById(R.id.nameText);
        TextView menuText = (TextView) rowView.findViewById(R.id.menuText);
        Restaurant.RestaurantMenu menu = menus.get(position);
        nameText.setText(menu.getRestaurant().getName());
        menuText.setText(menu.toString());

        return rowView;
    }
}
