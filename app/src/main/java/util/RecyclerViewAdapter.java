package util;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoughtswriter.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import modal.Thoughtclass;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<Thoughtclass> thoughtclassList;
    Context context;


    public RecyclerViewAdapter() {
    }

    public RecyclerViewAdapter(List<Thoughtclass> thoughtclassList, Context context) {
        this.thoughtclassList = thoughtclassList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view=LayoutInflater.from(context).inflate(R.layout.thought_row_list,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Thoughtclass thoughtclass=thoughtclassList.get(position);
        String imageurl;
        imageurl=thoughtclass.getImageurl();

        Picasso.get().load(imageurl).placeholder(R.drawable.after_cookie).fit().into(holder.imageView);

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(thoughtclass.getTimeadded().getSeconds()*1000);
        holder.timeadded.setText(timeAgo);


        holder.tittle.setText(thoughtclass.getTittle());
        holder.thought.setText(thoughtclass.getThought());
        holder.name.setText(thoughtclass.getUsername());



    }

    @Override
    public int getItemCount() {
        return thoughtclassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tittle,thought,timeadded,name;
        public ImageView imageView;
        String username;
        String userid;
        ImageButton sharebutton;



        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            tittle=itemView.findViewById(R.id.tittleTextview);
            thought=itemView.findViewById(R.id.thought_Textview);
            timeadded=itemView.findViewById(R.id.date_Textview);
            imageView=itemView.findViewById(R.id.imageViewthoughtrow);
            name=itemView.findViewById(R.id.rowusername);
            sharebutton=itemView.findViewById(R.id.rowsharebuton);
            sharebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "sharing button", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
