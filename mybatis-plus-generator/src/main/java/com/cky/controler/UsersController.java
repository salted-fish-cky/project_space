package com.cky.controler;


import com.cky.entity.Users;
import com.cky.service.IUsersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
@RestController
@RequestMapping("/cky/users")
public class UsersController {

    @Autowired
    IUsersService usersService;
    @GetMapping("usersList")
    public List<Users> getUsers(){
        return usersService.getUsersList();
    }

}

