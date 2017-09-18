package org.smartregister.kip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.kip.R;
import org.smartregister.kip.activity.BaseActivity;
import org.smartregister.kip.view.SiblingPicture;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jason Rogena - jrogena@ona.io on 09/05/2017.
 */

public class SiblingPictureAdapter extends RecyclerView.Adapter<SiblingPicture> {

    private final BaseActivity baseActivity;
    private final ArrayList<String> siblingIds;
    private final HashMap<String, SiblingPicture> siblingPictures;

    public SiblingPictureAdapter(BaseActivity baseActivity, ArrayList<String> siblingIds) {
        this.baseActivity = baseActivity;
        this.siblingIds = siblingIds;
        siblingPictures = new HashMap<>();
    }

    /*@Override
    public int getCount() {
        return siblingIds.size();
    }

    @Override
    public Object getItem(int position) {
        return siblingPictures.get(siblingIds.get(position));
    }*/

    @Override
    public SiblingPicture onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(baseActivity)
                .inflate(R.layout.view_sibling_picture, parent, false);
        return new SiblingPicture(view);
    }

    @Override
    public void onBindViewHolder(SiblingPicture siblingPicture, int position) {
        if (siblingIds.size() > position) {
            siblingPicture.setChildBaseEntityId(baseActivity, siblingIds.get(position));
            siblingPictures.put(siblingIds.get(position), siblingPicture);
        }
    }

    @Override
    public long getItemId(int position) {
        return 4223 + position;
    }

    @Override
    public int getItemCount() {
        return siblingIds.size();
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!siblingPictures.containsKey(siblingIds.get(position))) {
            SiblingPicture siblingPicture = new SiblingPicture(baseActivity);
            siblingPicture.setId((int) getItemId(position));
            siblingPicture.setChildBaseEntityId(baseActivity, siblingIds.get(position));

            siblingPictures.put(siblingIds.get(position), siblingPicture);
        }

        return siblingPictures.get(siblingIds.get(position));
    }*/
}
