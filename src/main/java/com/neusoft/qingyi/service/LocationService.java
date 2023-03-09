package com.neusoft.qingyi.service;

import com.neusoft.qingyi.util.ResponseResult;
import com.neusoft.qingyi.vo.MiniUserLocationVo;

import java.util.List;
import java.util.Map;

public interface LocationService {
    ResponseResult<?> addUserLocation(MiniUserLocationVo miniUserLocationVo);

    ResponseResult<?> getNearbyUsers(double longitude, double latitude, double radius);

    ResponseResult<?> removeUserLocation(String openid);
}
