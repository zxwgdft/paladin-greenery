package com.paladin.organization.web;

import com.paladin.framework.common.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.others.RandomObject;
import com.paladin.organization.model.AppResourceModel;
import com.paladin.organization.model.Personnel;
import com.paladin.organization.service.AppResourceService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/1/8
 */
@RestController
@RequestMapping("/organization/test")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AppResourceService appResourceService;

    @GetMapping("/exception1")
    public String testException1() {
        throw new BusinessException("测试异常1", new RandomObject().createRandomObject(Personnel.class));
    }

    @GetMapping("/exception2")
    public String testException2() {
        throw new BusinessException(HttpStatus.UNAUTHORIZED, "未登录");
    }


    @GetMapping("/direct")
    public R<String> authenticateByAccount() {
        String messageId = UUIDUtil.createUUID();
        String messageData = "test message, hello!";
        String createTime = "2020-1-8";
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return R.success("OK");
    }

    @GetMapping("/mongo")
    public R<List<AppResourceModel>> mongo() {

//        AppResourceModel model = new AppResourceModel();
//
//        model.setAppId("0001");
//        model.setName("菜单权限");
//
//        List<DynamicProperty> properties = new ArrayList<>();
//
//
//        DynamicProperty p1 = new DynamicProperty("url", "URL");
//        DynamicProperty p2 = new DynamicProperty("code", "编码");
//        properties.add(p1);
//        properties.add(p2);
//
//        model.setProperties(properties);
//
//        appResourceService.createResourceModel(model);


        return R.success(appResourceService.findAppResourceModels("0001"));
    }


    @GetMapping("/do")
    public R doo() {


//        AppResourceSave resource = new AppResourceSave();
//        resource.setAppId("0001");
//        resource.setModelId("5e1839112a24ca182ba1a6e0");
//        resource.setName("周期考评");
//        resource.setParent("5e1e815ea3a72b3a322dc7f9");
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("url","/hf/grkpgl/zqkp");
//        properties.put("code","GRKPGL:zqkp");
//
//
//        resource.setProperties(properties);
//
//        appResourceService.createResource(resource);

        appResourceService.updateResourceParent("", "");

        return R.success();
    }

}
