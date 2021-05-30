package com.cosmic.firebaseauthentication.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cosmic.firebaseauthentication.models.EnemyPhoto;
import com.cosmic.firebaseauthentication.repository.EnemyImageRepository;

import java.util.ArrayList;

public class EnemyImageViewModels extends ViewModel {

    private MutableLiveData<ArrayList<EnemyPhoto>> photos;

    public void enemyImageViewModel(Context context, String id){
        photos = EnemyImageRepository.getInstance(context, id).getImages();
    }

    public MutableLiveData<ArrayList<EnemyPhoto>> getPhotos(){
        return photos;
    }
}
