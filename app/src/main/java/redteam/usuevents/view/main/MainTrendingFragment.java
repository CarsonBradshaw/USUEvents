package redteam.usuevents.view.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redteam.usuevents.R;
import redteam.usuevents.adapter.TrendingEventsAdapter;
import redteam.usuevents.model.Event;

/**
 * Created by Admin on 6/14/2017.
 */

public class MainTrendingFragment extends Fragment {

    private View mView;

    private RecyclerView mRecyclerView;


    public static MainTrendingFragment getInstance() {
        return new MainTrendingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView==null){
            mView = inflater.inflate(R.layout.fragment_main_trending, container, false);
        }

        bindViews();

        //RecyclerView test code, remove once finalized
        StaggeredGridLayoutManager manager;
        if((getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getActivity().getResources().getConfiguration().screenWidthDp >= 600)
                || getActivity().getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }else{
            manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(manager);
        List<Event> eventList = new ArrayList<Event>();
        for(int i = 10; i<25; i++){
            Event event = new Event();
            event.setBeginDateTime("2017-12-"+i+"T18:00:00-07:00");
            event.setCategory("Aggie Football");
            event.setDescription("Come watch your Aggies crush New Mexico.");
            event.setEndDateTime("2017-12-"+i+"T21:00:00-07:00");
            event.setEventId(i + "");
            event.setImageUri("http://global.web.usu.edu/images/uploads/Carlos/Utah%20State%20University-%20early%20summer.jpg");
            event.setLatitude(41.750996996);
            event.setLocation("Maverik Stadium, Logan, UT");
            event.setLongitude(-111.806996772);
            event.setNumberInterested(i);
            event.setTopic("mFootball");
            event.setTitle("USU vs New Mexico");
            eventList.add(event);
        }
        TrendingEventsAdapter eventAdapter = new TrendingEventsAdapter(eventList);
        eventAdapter.setTrending(true);
        mRecyclerView.setAdapter(eventAdapter);
        //Remove all above between comments when done figuring out configuration

        return mView;
    }

    private void bindViews(){
        mRecyclerView = (RecyclerView)mView.findViewById(R.id.fragment_main_trending_recycler_view);
    }

}
