package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toner e Tintas on 27/01/2016.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
    private List<Categoria> listGroup;
    private HashMap<Categoria, List<Item>> listData;
    private LayoutInflater inflater;

    public ExpandableAdapter(Context context, List<Categoria> listGroup, HashMap<Categoria, List<Item>> listData) {
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {

        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        Item val = (Item) getChild(groupPosition, childPosition);

        return val.getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup holder;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.header_expandable_list_view, null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
        }
        else{
            holder = (ViewHolderGroup) convertView.getTag();
        }

        holder.tvGroup.setText(listGroup.get(groupPosition).getNome());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;
        Item val = (Item) getChild(groupPosition, childPosition);

        if (convertView == null){
            //convertView = inflater.inflate(R.layout.item_expandable_list_view, null);
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);

            holder.txtNome = (TextView) convertView.findViewById(R.id.txtNome);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.txtValor = (TextView) convertView.findViewById(R.id.txtValor);
        }
        else{
            holder = (ViewHolderItem) convertView.getTag();
        }


        holder.txtNome.setText(val.getNome());
        holder.txtDesc.setText(val.getDescricao());

        DecimalFormat df = new DecimalFormat("0.00");
        holder.txtValor.setText("R$ "+df.format(val.getValor()).toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderGroup{
        TextView tvGroup;
    }

    class ViewHolderItem{
        TextView txtNome;
        TextView txtDesc;
        TextView txtValor;
    }
}
