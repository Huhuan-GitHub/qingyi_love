package com.neusoft.qingyi.service;

import com.neusoft.qingyi.vo.MiniUserLocationVo;

import java.util.List;
import java.util.Map;

public interface LocationService {
    void addUserLocation(String openid, double longitude, double latitude);

    List<MiniUserLocationVo> getNearbyUsers(double longitude, double latitude, double radius);

    void removeUserLocation(String openid);
}
