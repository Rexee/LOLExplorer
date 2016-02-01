package tsingularity.lolexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import tsingularity.lolexplorer.Model.MatchHistoryDisplay;

public class SummonerSummaryAdapter extends BaseAdapter {

    public LayoutInflater            mInflater;
    public List<MatchHistoryDisplay> mMatchHistory;
    public AQuery                    aq;

    public SummonerSummaryAdapter(Context context, List<MatchHistoryDisplay> matchHistory) {
        this.mMatchHistory = matchHistory;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(context);
    }

    @Override
    public int getCount() {
        return mMatchHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mMatchHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View v = convertView;

        if (v == null) {
            v = mInflater.inflate(R.layout.summoner_summary_list_item, parent, false);

            holder = new ViewHolder();
            holder.championIcon = (ImageView) v.findViewById(R.id.championIcon);
            holder.queueType = (TextView) v.findViewById(R.id.queueType);
            holder.kda = (TextView) v.findViewById(R.id.kda);
            holder.matchDate = (TextView) v.findViewById(R.id.matchDate);
            holder.matchDuration = (TextView) v.findViewById(R.id.matchDuration);
            holder.item0 = (ImageView) v.findViewById(R.id.mh_Item0);
            holder.item1 = (ImageView) v.findViewById(R.id.mh_Item1);
            holder.item2 = (ImageView) v.findViewById(R.id.mh_Item2);
            holder.item3 = (ImageView) v.findViewById(R.id.mh_Item3);
            holder.item4 = (ImageView) v.findViewById(R.id.mh_Item4);
            holder.item5 = (ImageView) v.findViewById(R.id.mh_Item5);
            holder.item6 = (ImageView) v.findViewById(R.id.mh_Item6);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        MatchHistoryDisplay currentMatch = mMatchHistory.get(position);

        aq.id(holder.championIcon).image(currentMatch.champion_url);
        aq.id(holder.queueType).text(currentMatch.queueType);
        aq.id(holder.kda).text("KDA: " + currentMatch.KDA_Num + " (" + currentMatch.KDA + ")");
        aq.id(holder.matchDate).text(currentMatch.matchCreation);
        aq.id(holder.matchDuration).text("Duration: " + currentMatch.matchDuration);

        aq.id(holder.item0).image(currentMatch.item0_url);
        aq.id(holder.item1).image(currentMatch.item1_url);
        aq.id(holder.item2).image(currentMatch.item2_url);
        aq.id(holder.item3).image(currentMatch.item3_url);
        aq.id(holder.item4).image(currentMatch.item4_url);
        aq.id(holder.item5).image(currentMatch.item5_url);
        aq.id(holder.item6).image(currentMatch.item6_url);


        if (currentMatch.winner) v.setBackgroundColor(0xFF000000 | Settings.COLOR_WINNER);
        else v.setBackgroundColor(0xFF000000 | Settings.COLOR_LOOSER);

        return v;
    }

    static class ViewHolder {
        public ImageView championIcon;
        public TextView  queueType;
        public TextView  kda;
        public TextView  matchDate;
        public TextView  matchDuration;
        public ImageView item0;
        public ImageView item1;
        public ImageView item2;
        public ImageView item3;
        public ImageView item4;
        public ImageView item5;
        public ImageView item6;

    }
}
