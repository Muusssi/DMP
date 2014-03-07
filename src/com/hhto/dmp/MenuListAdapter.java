package com.hhto.dmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by hmhagberg on 7.3.2014.
 */

public class MenuListAdapter extends ArrayAdapter<Restaurant.RestaurantMenu> {
    private final Context context;
    private final int resource;
    private final Restaurant.RestaurantMenu[] menus;

    private MenuListAdapter(Context context, int resource, Restaurant.RestaurantMenu[] menus) {
        super(context, resource, menus);
        this.context = context;
        this.resource = resource;
        this.menus = menus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.nameText);
        TextView menu = (TextView) rowView.findViewById(R.id.menuText);
        name.setText(menus[position].getRestaurant().getName());
        menu.setText(menus[position].getRestaurant().toString());

        return rowView;
    }
}
