package example.com.fielthyapps.Feature.Stress;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import example.com.fielthyapps.R;

public class QuestSectionsatuAdapter extends RecyclerView.Adapter<QuestSectionsatuAdapter.ViewHolder> {
//    private static QuestList[] listdata;

    private List<QuestList> questionList;

    //    public QuestSectionsatuAdapter(QuestList[] listdata) {
//        this.listdata = listdata;
//    }
    public QuestSectionsatuAdapter(List<QuestList> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestSectionsatuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_test_stress, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestSectionsatuAdapter.ViewHolder holder, int position) {
//        holder.quest.setText(listdata[position].getQuest().toString());
//        int selectedOption = listdata[position].getSelectedOption();
//        if (selectedOption != -1) {
//            holder.rG_answer.check(selectedOption);
//            Log.d("Coba Cek", "onBindViewHolder: " + holder.rG_answer);
//        } else {
//            holder.rG_answer.clearCheck();
//        }

        QuestList question = questionList.get(position);
        holder.bind(question);


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quest;
        RadioGroup rG_answer;

        public ViewHolder(View listItem) {
            super(listItem);
            quest = listItem.findViewById(R.id.tV_quest);
            rG_answer = listItem.findViewById(R.id.rG_answer);

        }

        void bind(final QuestList question) {
            quest.setText(question.getQuestionText());

            rG_answer.removeAllViews();
            for (int i = 0; i < question.getOptions().size(); i++) {
                RadioButton radioButton = new RadioButton(itemView.getContext());
                radioButton.setText(question.getOptions().get(i));
                radioButton.setId(View.generateViewId());

                // Set the text color of the RadioButton
                radioButton.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.black));
                ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#009688")); // Warna merah
                radioButton.setButtonTintList(colorStateList);
//                radioButton.setBackgroundResource(R.drawable.ic_stroke_input);

                rG_answer.addView(radioButton);


                if (i == question.getSelectedOption()) {
                    radioButton.setChecked(true);
                }
            }

            rG_answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // Setel nilai pilihan radio button ke model data
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listdata[position].setSelectedOption(checkedId);
//                    }
                    for (int i = 0; i < rG_answer.getChildCount(); i++) {
                        if (rG_answer.getChildAt(i).getId() == checkedId) {
                            question.setSelectedOption(i);
                            break;
                        }
                    }


                }
            });


        }
    }
}
