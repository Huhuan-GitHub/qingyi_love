package com.neusoft.qingyi.service;

import com.neusoft.qingyi.util.ResponseResult;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;

public interface LocationService {
    ResponseResult<?> addUserLocation(MiniUserLocationVo miniUserLocationVo);

    ResponseResult<?> getNearbyUsers(double longitude, double latitude, double radius);

    ResponseResult<?> removeUserLocation(String openid);

    GeoResults<RedisGeoCommands.GeoLocation<String>> getNearUserLocation(double longitude, double latitude, double radius);

}