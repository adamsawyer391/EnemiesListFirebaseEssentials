package com.cosmic.firebaseauthentication.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.repository.EnemyRepository;

import java.util.ArrayList;

public class EnemyViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Enemy>> enemies;

    public void getEnemyViewModel(Context context){
        enemies = EnemyRepository.getInstance(context).getEnemies();
    }

    public LiveData<ArrayList<Enemy>> getEnemies(){
        return enemies;
    }

}
