package com.cosmic.firebaseauthentication.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cosmic.firebaseauthentication.models.Enemy;
import com.cosmic.firebaseauthentication.repository.SingleEnemyRepository;

import java.util.ArrayList;

public class SingleEnemyViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Enemy>> enemy;

    public void connectToSingleEnemyViewModel(Context context, String id){
        enemy = SingleEnemyRepository.getInstance(context, id).getEnemy();
    }

    public MutableLiveData<ArrayList<Enemy>> getEnemy(){
        return enemy;
    }

}
