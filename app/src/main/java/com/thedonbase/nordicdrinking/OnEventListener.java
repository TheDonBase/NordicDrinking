package com.thedonbase.nordicdrinking;//
// Created by TheDonBase on 2020-11-11.
// Copyright (c) 2020 CroazStudio. All rights reserved.
//

import org.json.JSONException;

public interface OnEventListener<T> {
    public void onSuccess(T object) throws JSONException;
    public void onFailure(Exception e);
}
