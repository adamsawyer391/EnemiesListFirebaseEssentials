package com.cosmic.firebaseauthentication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.models.Enemy;

import java.util.ArrayList;

public class EnemyAdapter extends RecyclerView.Adapter<EnemyAdapter.EnemyViewHolder> {

    private final Context context;
    private final ArrayList<Enemy> enemies;

    public EnemyAdapter(Context context, ArrayList<Enemy> enemies) {
        this.context = context;
        this.enemies = enemies;
    }

    @NonNull
    @Override
    public EnemyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.enemy_view_item, parent, false);
        return new EnemyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnemyViewHolder holder, int position) {
        holder.textView.setText(enemies.get(position).getName());
        Glide.with(context).load(enemies.get(position).getImage_url()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (enemies != null){
            return enemies.size();
        }else{
            return 0;
        }
    }


    static class EnemyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;

        public EnemyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.enemyImage);
            textView = itemView.findViewById(R.id.enemyName);
        }
    }
}
