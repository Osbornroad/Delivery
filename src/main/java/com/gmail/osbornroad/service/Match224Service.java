package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.Match224;

import java.util.Map;

public interface Match224Service {

    public Match224 findByConcat(String concat);
    public void saveMapMatch224(Map<String, String> map);
    public Match224 saveMatch224(Match224 match224);
}
