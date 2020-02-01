package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asistencia.R;
import com.example.model.Beneficiary;
import com.example.model.Event;

import java.util.ArrayList;

public class AdapterEvent extends BaseAdapter {
    private ArrayList<Event> listItems;
    private Context context;

    public AdapterEvent(Context context, ArrayList<Event> listItems) {
        this.listItems = listItems;
        this.context = context;
    }



    @Override
    public int getCount() {
      return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event evento = (Event) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        TextView txtCurp = (TextView) convertView.findViewById(R.id.txtListCurp);
        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtListNombre);
        txtCurp.setText(evento.getNombre());
        txtNombre.setText(evento.getFechaInicio().toString());

        return convertView;
    }
}
