package example.com.fielthyapps.Feature.Smoker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import example.com.fielthyapps.Feature.Stress.QuestList;
import example.com.fielthyapps.Feature.Stress.QuestSectionsatuAdapter;
import example.com.fielthyapps.R;

public class InformasiMenjauhiRokokAdapter extends RecyclerView.Adapter<InformasiMenjauhiRokokAdapter.ViewHolder> {
    private SmokerTipsList[] listdata;

    public InformasiMenjauhiRokokAdapter(SmokerTipsList[] listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public InformasiMenjauhiRokokAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_smoker, parent, false);
        InformasiMenjauhiRokokAdapter.ViewHolder viewHolder = new InformasiMenjauhiRokokAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InformasiMenjauhiRokokAdapter.ViewHolder holder, int position) {
        holder.desc.setText(listdata[position].getDesc().toString());
        holder.number.setImageResource(listdata[position].getImg_number());
        holder.icon.setImageResource(listdata[position].getImg_icon());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView desc;
        private ImageView number,icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.tV_desc);
            number = itemView.findViewById(R.id.iV_number);
            icon = itemView.findViewById(R.id.img_tips);
        }
    }
}
