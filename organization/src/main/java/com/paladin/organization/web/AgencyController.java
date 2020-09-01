package com.paladin.organization.web;

import com.paladin.framework.service.PageResult;
import com.paladin.framework.spring.web.ControllerSupport;
import com.paladin.organization.service.AgencyService;
import com.paladin.organization.service.dto.AgencyQuery;
import com.paladin.organization.service.vo.OpenAgency;
import com.paladin.organization.service.vo.SimpleAgency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/8/31
 */
@Api("机构管理")
@RestController
@RequestMapping("/organization/agency")
public class AgencyController extends ControllerSupport {

    @Autowired
    private AgencyService agencyService;

    @ApiOperation(value = "获取人员信息列表-分页")
    @GetMapping("/find/page")
    public PageResult<OpenAgency> findAgencyPage(AgencyQuery param) {
        return agencyService.searchPage(param, OpenAgency.class);
    }

    @ApiOperation(value = "获取简单的人员信息列表")
    @GetMapping("/find/all/simple")
    public List<SimpleAgency> findSimpleAgency(AgencyQuery param) {
        return agencyService.searchAll(SimpleAgency.class, param);
    }


}
