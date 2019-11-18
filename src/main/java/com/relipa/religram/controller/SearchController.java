package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("v1/search")
@Api(tags = {"search"})
public class SearchController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    @ApiOperation(value = "${search-user.get.value}", notes = "${search-user.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserInfoBean.class, responseContainer = "List")})
    public ResponseEntity<List<UserInfoBean>> searchByName(@ApiParam(value = "${search-user.get.param.name}", required = true) @RequestParam String name,
                                                           @ApiParam(value = "${search-user.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page) {
        return ok(userService.searchUsersByName(name, page));
    }
}
