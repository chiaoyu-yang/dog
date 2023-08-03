package com.example.updatenickname;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class Adapter extends FirebaseRecyclerAdapter <Post, Adapter.Postviewholder> {

    private RetrieveData context;

    public Adapter(@NonNull FirebaseRecyclerOptions<Post> options, RetrieveData context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Postviewholder holder, int i, @NonNull Post post) {

        getRef(i).getKey();

        holder.Fname.setText(post.getFullName());
        holder.Email.setText(post.getEmail());
        holder.DOB.setText(post.getDateOfBirth());
        holder.Age.setText(post.getAge());
        holder.Gender.setText(post.getGender());

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50, 0, 50, 0)
                        .setContentHolder(new ViewHolder(R.layout.edit))
                        .setExpanded(false)
                        .create();

                View holderView = (LinearLayout) dialogPlus.getHolderView();

                EditText Fname = holderView.findViewById(R.id.fname);
                EditText Email = holderView.findViewById(R.id.email);
                EditText DOB = holderView.findViewById(R.id.dob);
                EditText Age = holderView.findViewById(R.id.age);
                EditText Gender = holderView.findViewById(R.id.gender);

                Fname.setText(post.getFullName());
                Email.setText(post.getEmail());
                DOB.setText(post.getDateOfBirth());
                Age.setText(post.getAge());
                Gender.setText(post.getGender());

                Button Update = holderView.findViewById(R.id.update);

                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();

                        map.put("FullName", Fname.getText().toString());
                        map.put("Email", Email.getText().toString());
                        map.put("DateOfBirth", DOB.getText().toString());
                        map.put("Age", Age.getText().toString());
                        map.put("Gender", Gender.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("UserData")
                                .child(getRef(i).getKey())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

                dialogPlus.show();


            }
        });

    }

    @NonNull
    @Override
    public Postviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity2,parent, false);
        return new Adapter.Postviewholder(view);
    }

    public class Postviewholder extends RecyclerView.ViewHolder {

        TextView Fname, Email, DOB, Age, Gender;
        ImageView Edit;

        public Postviewholder(@NonNull View itemView) {
            super(itemView);

            Fname = itemView.findViewById(R.id.fname);
            Email = itemView.findViewById(R.id.email);
            DOB = itemView.findViewById(R.id.dob);
            Age = itemView.findViewById(R.id.age);
            Gender = itemView.findViewById(R.id.gender);

            Edit = itemView.findViewById(R.id.edit);
        }
    }
}
