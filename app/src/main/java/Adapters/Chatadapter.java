package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mingle.Models.Messages;
import com.example.mingle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chatadapter extends RecyclerView.Adapter{
ArrayList<Messages> msglist;
Context context;
String receiver_id;
int sender_view_type = 1;

    public Chatadapter(ArrayList<Messages> msglist, Context context, String receiver_id) {
        this.msglist = msglist;
        this.context = context;
        this.receiver_id = receiver_id;
    }

    int receiver_view_type = 2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == sender_view_type)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.send_chat,parent,false);
            return new Senderviewholder(view);
        }

        else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_chat,parent,false);
            return new Receiverviewholder(view);
        }
    }

    public Chatadapter(ArrayList<Messages> msglist, Context context) {
        this.msglist = msglist;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(msglist.get(position).getMid().equals(FirebaseAuth.getInstance().getUid()))
        {
               return sender_view_type;
        }
        else{
            return receiver_view_type;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = msglist.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete the message ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase db =  FirebaseDatabase.getInstance();
                            String senderoom= FirebaseAuth.getInstance().getUid()+receiver_id;
                            db.getReference().child("chats").child(senderoom).child(messages.getMsgid())
                                    .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });
        if(holder.getClass() == Senderviewholder.class)
        {
            ((Senderviewholder)holder).senderMsg.setText(messages.getMsg());
        }
        else {
            ((Receiverviewholder)holder).receiverMsg.setText(messages.getMsg());
        }


    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    public class Receiverviewholder extends RecyclerView.ViewHolder{

        TextView  receiverMsg,receiverTime;
        public Receiverviewholder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiver_text);
            receiverTime = itemView.findViewById(R.id.receiver_time);

        }
    }

    public class Senderviewholder extends RecyclerView.ViewHolder{

        TextView  senderMsg,senderTime;
        public Senderviewholder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sender_text);
            senderTime = itemView.findViewById(R.id.sender_time);

        }
    }
}
