package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mingle.Models.User;
import com.example.mingle.R;
import com.example.mingle.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<User> list;
    Context context;

    public UsersAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     User user = list.get(position);
    Picasso.get().load(user.getProfile()).placeholder(R.drawable.phone).into(holder.img);
    holder.name.setText(user.getUsername());

        FirebaseDatabase.getInstance().getReference().child("chats")//orders in descending bcoz firebase by default orders it in ascending order
                        .child((FirebaseAuth.getInstance().getUid())+user.getUserId())
                                .orderByChild("timestamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.hasChildren())
                                                        {
                                                            for(DataSnapshot ds: snapshot.getChildren())
                                                            {
                                                                holder.tap_to_chat.setText(ds.child("msg").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, chat.class);
            intent.putExtra("userid",user.getUserId());
            intent.putExtra("profilepic",user.getProfile());
            intent.putExtra("username",user.getUsername());
          context.startActivity(intent);

        }
    });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,tap_to_chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.username);
            tap_to_chat = itemView.findViewById(R.id.tap_to_chat);
        }
    }

}
