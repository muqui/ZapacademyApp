package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asistencia.R;
import com.example.asistencia.ResultadoManual;
import com.example.model.Beneficiary;
import com.example.model.Persona;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Beneficiary> listItems;
    private Context context;

    public Adapter( Context context, ArrayList<Beneficiary> listItems) {
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
        Beneficiary persona = (Beneficiary)getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        TextView txtCurp = (TextView) convertView.findViewById(R.id.txtListCurp);
        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtListNombre);
        txtCurp.setText(persona.getCurp());
        txtNombre.setText(persona.getNombre());

        return convertView;
    }
}
