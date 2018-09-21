package com.vpigadas.vivawallet.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.vpigadas.vivawallet.models.Items;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> successResult;
    private MutableLiveData<Throwable> errorResult;

    public MainViewModel(@NonNull Application application) {
        super(application);

        successResult = new MutableLiveData<>();
        errorResult = new MutableLiveData<>();
    }

    public MutableLiveData<List<Items>> getSuccessResult() {
        return successResult;
    }

    public MutableLiveData<Throwable> getErrorResult() {
        return errorResult;
    }
}
