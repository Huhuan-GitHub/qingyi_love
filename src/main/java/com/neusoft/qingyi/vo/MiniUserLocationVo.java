package com.neusoft.qingyi.vo;

import com.neusoft.qingyi.pojo.MiniUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MiniUserLocationVo {
    private double[] points;
    private String openid;
    private MiniUser miniUser;

    private double distance;
}
