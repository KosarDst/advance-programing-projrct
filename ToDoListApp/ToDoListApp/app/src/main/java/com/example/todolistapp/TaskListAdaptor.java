package com.example.todolistapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;

public class TaskListAdaptor extends RecyclerView.Adapter<TaskListAdaptor.ViewHolder> {
    private List<Task> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public TaskListAdaptor(Context context, List<Task> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_tasklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mData.get(position);
        holder.myTextView.setText(task.getTitle());
        DB.touchedtask = MainActivity.adapter.getItem(position);
        holder.is_done.setChecked(task.getIs_done());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        Switch is_done;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.Task_title);
            itemView.setOnClickListener(this);
            is_done = itemView.findViewById(R.id.switchdone);
            is_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DB.touchedtask = mData.get(getAdapterPosition());
                        DB.touchedtask.setIs_done(true);
                        myTextView.setBackground(context.getResources().getDrawable(R.drawable.donecolor));
                        is_done.setBackground(context.getResources().getDrawable(R.drawable.donecolor));
                    }else {
                        DB.touchedtask = mData.get(getAdapterPosition());
                        DB.touchedtask.setIs_done(false);
                        myTextView.setBackground(context.getResources().getDrawable(R.drawable.taskcolor));
                        is_done.setBackground(context.getResources().getDrawable(R.drawable.taskcolor));
                    }
                }
            });
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    Task getItem(int id) {
        return mData.get(id);
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}