package redteam.usuevents.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import redteam.usuevents.R;
import redteam.usuevents.model.Event;
import redteam.usuevents.view.event.EventActivity;
import redteam.usuevents.view.main.MainActivity;
import redteam.usuevents.view.main.ManageSubscriptionsCallback;

/**
 * Created by Admin on 7/8/2017.
 */

public class HomeEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ManageSubscriptionsCallback manageSubscriptionsCallback;

    private List<Event> mEventList;
    private boolean mTrending = false;
    private boolean mSubscriptions = false;

    public HomeEventsAdapter(){
        this.mEventList = Collections.emptyList();
    }

    public void setManageSubscriptionsCallback(ManageSubscriptionsCallback callBack){
        this.manageSubscriptionsCallback = callBack;
    }

    public HomeEventsAdapter(List<Event> eventList){
        this.mEventList = eventList;
    }

    public void setEventList(List<Event> eventList){
        this.mEventList = eventList;
    }

    public void setTrending(boolean bool){
        this.mTrending = bool;
    }

    public void setSubscriptions(boolean bool){
        this.mSubscriptions = bool;
    }

    @Override
    public int getItemViewType(int position) {
        if(mSubscriptions && position==0){
            return R.layout.subscriptions_settings_header;
        }
        return R.layout.list_item_event_home;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.subscriptions_settings_header){
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            view.findViewById(R.id.subscriptions_header_manage_clickable_area).setOnClickListener(this);
            return new RecyclerView.ViewHolder(view) {

            };
        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new HomeEventsAdapter.EventHolder(layoutInflater, parent, viewType, mTrending);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mSubscriptions && position == 0) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            holder.itemView.setLayoutParams(layoutParams);
            return;
        }
        Event event = mEventList.get(position - (mSubscriptions ? 1 : 0));
        ((EventHolder)holder).bind(event);
    }

    @Override
    public int getItemCount() {
        return mEventList.size() + (mSubscriptions ? 1 : 0);
    }

    @Override
    public void onClick(View view) {
        manageSubscriptionsCallback.updateManageViews();
    }


    public static class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Event mEvent;
        private boolean mTrending;

        private TextView mTitle;
        private TextView mLocation;
        private TextView mTime;
        private ImageView mImage;
        private TextView mTitleUnderline;
        private TextView mNumberInterested;
        private CardView mCardView;

        public void bind(Event event) {
            mEvent = event;

            mTitle.setText(mEvent.getTitle());
            mLocation.setText(mEvent.getLocation());
            mTime.setText(mEvent.getHumanReadableTime());
            if(mTrending){
                mNumberInterested.setText(mEvent.getNumberInterested() + " Interested");
            }
            //Glide has issues with loading on rotation from an abstracted adapter. Need to either migrate back into fragment or disallow rotation.
//            Glide.with(itemView).load(mEvent.getImageUri()).apply(RequestOptions.fitCenterTransform()).into(mImage);
        }

        public void applyTrendingStyles(){
            mCardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
            mTitle.setTextColor(Color.WHITE);
            mLocation.setTextColor(Color.WHITE);
            mTime.setTextColor(Color.WHITE);
            mTitleUnderline.setVisibility(View.VISIBLE);
            mNumberInterested.setVisibility(View.VISIBLE);
        }

        public EventHolder(LayoutInflater inflater, ViewGroup parent, int layoutResourceId, boolean isTrending) {
            super(inflater.inflate(layoutResourceId, parent, false));
            mTrending = isTrending;

            mTitle = (TextView) itemView.findViewById(R.id.list_item_event_title);
            mLocation = (TextView) itemView.findViewById(R.id.list_item_event_location);
            mTime = (TextView) itemView.findViewById(R.id.list_item_event_time);
            mImage = (ImageView) itemView.findViewById(R.id.list_item_event_image);
            if(mTrending) {
                mCardView = (CardView) itemView.findViewById(R.id.list_item_event_cardview);
                mTitleUnderline = (TextView) itemView.findViewById(R.id.list_item_event_trending_underline);
                mNumberInterested = (TextView) itemView.findViewById(R.id.list_item_event_trending_number_interested);
                applyTrendingStyles();
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), EventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.EVENT_KEY, mEvent);
            intent.putExtras(bundle);
            itemView.getContext().startActivity(intent);
        }
    }



}
