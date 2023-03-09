package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.service.LocationService;
import com.neusoft.qingyi.util.ResponseResult;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Resource
    private LocationService locationService;

    @ApiOperation("小程序用户连接到地址服务")
    @PostMapping("/joinLocation")
    public ResponseResult<?> joinLocation(@RequestBody MiniUserLocationVo miniUserLocationVo) {
        return locationService.addUserLocation(miniUserLocationVo);
    }

    @ApiOperation("获取附近的人")
    @GetMapping("/getNearbyUsers")
    public ResponseResult<?> getNearbyUsers(@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude, @RequestParam(value = "radius", defaultValue = "10.0") double radius) {
        return locationService.getNearbyUsers(longitude, latitude, radius);
    }

    @ApiOperation("移除小程序用户的地址信息")
    @PostMapping("/removeUserLocation")
    public ResponseResult<?> removeUserLocation(@RequestParam("openid") String openid) {
        return locationService.removeUserLocation(openid);
    }
}
