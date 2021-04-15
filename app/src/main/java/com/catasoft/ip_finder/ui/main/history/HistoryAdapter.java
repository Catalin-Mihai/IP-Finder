package com.catasoft.ip_finder.ui.main.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SearchHolder> {

    private List<SearchInfo> searchInfoList;
    private final AdapterListener listener;

    public HistoryAdapter(List<SearchInfo> searchInfoList, AdapterListener listener) {
        if (searchInfoList != null){
            this.searchInfoList = searchInfoList;
        }
        else{
            this.searchInfoList = new ArrayList<>();
        }
        this.listener = listener;
    }

    @Override
    @NonNull
    public HistoryAdapter.SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_item,
                parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        SearchInfo searchInfo = searchInfoList.get(position);
        holder.bind(searchInfo);
    }

    @Override
    public int getItemCount() {
        return searchInfoList.size();
    }

    public void setSearchInfoList(List<SearchInfo> searchInfoList){
        if (searchInfoList != null){
            this.searchInfoList = searchInfoList;
            notifyDataSetChanged();
        }
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        private final TextView tvIp;
        private final TextView tvSearchTime;
        private final ImageButton btnDelete;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            tvIp = itemView.findViewById(R.id.tvIp);
            tvSearchTime = itemView.findViewById(R.id.tvSearchTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchInfo value = searchInfoList.get(getAdapterPosition());
                    listener.onItemDelete(value);
                }
            });

            CardView cardView = itemView.findViewById(R.id.entryCardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchInfo value = searchInfoList.get(getAdapterPosition());
                    listener.startSearchItemActivity(value);
                }
            });
        }

        public void bind(SearchInfo searchInfo) {
            tvIp.setText(searchInfo.getQuery());
            tvSearchTime.setText(searchInfo.getCreatedAt());
        }
    }

    public interface AdapterListener {
        void onItemDelete(SearchInfo value);
        void startSearchItemActivity(SearchInfo searchInfo);
    }

}
