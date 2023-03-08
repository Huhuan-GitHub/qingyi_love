package com.neusoft.qingyi.service.impl;

import cn.hutool.json.JSONUtil;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.service.LocationService;
import com.neusoft.qingyi.util.RedisUtils;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void addUserLocation(String openid, double longitude, double latitude) {
        // 存储到Redis中，使用geo结构存储
        stringRedisTemplate.opsForGeo().add(RedisUtils.USER_LOCATION, new Point(longitude, latitude), openid);
    }

    @Override
    public List<MiniUserLocationVo> getNearbyUsers(double longitude, double latitude, double radius) {
        // 创建一个圆形范围，以 (longitude, latitude) 为圆心，半径为 radius 公里
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS));

        // 创建一个参数对象，用于指定查询结果包含距离信息
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance();

        // 执行地理空间查询，返回一个包含查询结果的 GeoResults 对象
        // 查询 user:location 的地理位置，并返回在 within 范围内的所有成员及与圆心的距离
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().radius(RedisUtils.USER_LOCATION, within, args);
        List<MiniUserLocationVo> res = new ArrayList<>();
        if (results != null) {
            // 遍历查询结果，并输出每个成员的名称和与圆心的距离
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                // 获取成员的名称，这里获取到的是用户的openid
                String openid = result.getContent().getName();

                // 根据成员名称查询Redis信息
                double x = result.getContent().getPoint().getX();
                double y = result.getContent().getPoint().getY();
                // 获取成员与圆心的距离
                double distance = result.getDistance().getValue();

                /*
                这里查询用户信息，会多次查询数据库，所以我们不直接查询数据库，不然会对数据库造成较大的压力，所以我们采用的方案是:
                    在用户连接到该服务的WebSocket的时候，那么我们就将对应的用户信息查询出来，存放到redis中，
                    后续拿到用户的openid的时候，直接通过redis获取即可，这样就在一定程度上减轻了数据库的读写压力
                 */
                String miniUserJson = stringRedisTemplate.opsForValue().get(RedisUtils.LOCATION_MINIUSER + openid);
                MiniUser miniUser = JSONUtil.toBean(miniUserJson, MiniUser.class);
                MiniUserLocationVo miniUserLocationVo = new MiniUserLocationVo(new double[]{x, y}, openid, miniUser, distance);
                res.add(miniUserLocationVo);
            }
        }
        return res;
    }

    @Override
    public void removeUserLocation(String openid) {
        stringRedisTemplate.opsForGeo().remove(RedisUtils.USER_LOCATION, openid);
    }
}
