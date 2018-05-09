package com.showbuddy4.model;

import android.widget.Filter;

import com.showbuddy4.activity.NewSearchListActivity;
import java.util.ArrayList;

/**
 * Created by Ashish on 2/17/2018.
 */

public class CustomFilter extends Filter {

    NewSearchListActivity.MyAdapter adapter;
    ArrayList<SearchDataNew> filterList;


    public CustomFilter(ArrayList<SearchDataNew> filterList,NewSearchListActivity.MyAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

       //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<SearchDataNew> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.players= (ArrayList<SearchDataNew>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
