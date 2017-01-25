package com.appolica.sample.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.appolica.sample.BR;

import java.io.Serializable;


/**
 * Created by Alexander Iliev on 24.01.17.
 * Copyright © 2017 Appolica. All rights reserved.
 */
public class TestModel extends BaseObservable implements Serializable {
    private String name;
    private int number;

    public TestModel(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Bindable
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        notifyPropertyChanged(BR.number);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
