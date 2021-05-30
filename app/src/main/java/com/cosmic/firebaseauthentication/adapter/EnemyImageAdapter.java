package com.cosmic.firebaseauthentication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmic.firebaseauthentication.R;
import com.cosmic.firebaseauthentication.models.EnemyPhoto;

import java.util.ArrayList;

public class EnemyImageAdapter extends RecyclerView.Adapter<EnemyImageAdapter.EnemyImageViewHolder> {

    private static final String TAG = "EnemyImageAdapter";

    private final Context context;
    private final ArrayList<EnemyPhoto> enemyPhotos;

    public EnemyImageAdapter(Context context, ArrayList<EnemyPhoto> enemyPhotos) {
        this.context = context;
        this.enemyPhotos = enemyPhotos;
    }

    @NonNull
    @Override
    public EnemyImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.enemy_image_view_item, parent, false);
        return new EnemyImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnemyImageViewHolder holder, int position) {
        Glide.with(context).load(enemyPhotos.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (enemyPhotos != null){
            Log.d(TAG, "getItemCount: enemy photos was NOT null");
            return enemyPhotos.size();
        }else{
            Log.d(TAG, "getItemCount: enemy photos was null");
            return 0;
        }
    }

    static class EnemyImageViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;

        public EnemyImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.enemy_image);
        }
    }
}
