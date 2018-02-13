/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import com.Service.impl.UserService;
import com.entity.User;
import com.Application;
import com.dao.impl.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

/**
 * 
 * @author Rob Winch
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ApplicationTests {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserDao ldapDao;

    @Autowired
    private UserService userService;

    @Test
    public void loginWithValidUserThenAuthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
            .user("ben")
            .password("benspassword");

        mockMvc.perform(login)
            .andExpect(authenticated().withUsername("ben"));
    }

    @Test
    public void loginWithInvalidUserThenUnauthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
            .user("invalid")
            .password("invalidpassword");

        mockMvc.perform(login)
            .andExpect(unauthenticated());
    }

    @Test
    public void testSearch(){
            User user = ldapDao.getUserByUid("jm");
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUid("yinjm");
        user.setCn("jm yin");
        user.setSn("yin");
        user.setPassword("123");
        try {
            ldapDao.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addGroup(){
        try {
            ldapDao.addGroup("ou=testers, ou=Groups");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createGroupRelations(){
        try {
            Map<String,String> map = new HashMap<>();
            map.put("cn","developers");
            map.put("ou","UserRelations");
            map.put("member","guanxin");
            map.put("type","Users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testName(){

    }
    @Test
    public void createUserRelations(){
        try {
          ldapDao.createUserRelation("admin","testers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addUserRelations(){
        try {
            List<String> list = new ArrayList<>();
            list.add("zhangsan");
            ldapDao.addUserRelation("developers",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void removeUserRelations(){
        try {
            List<String> list = new ArrayList<>();
            list.add("guanxin");
            ldapDao.removeUserRelation("developers",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addGroupRelations(){
        try {
            List<String> list= new ArrayList<>();
            list.add("developers");
            list.add("designers");
            ldapDao.addGroupRelation("programers",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDel(){
        ldapDao.deleteGroupRelation();
    }
    @Test
    public void testUpdate(){
        User user = new User();
        user.setUid("jm");
        user.setCn("jm yin");
        user.setSn("yin");
        user.setPassword("123");
        ldapDao.updateDesription(user);
    }

    @Test
    public void testRebindUpdate(){
        User user = new User();
        user.setUid("jm");
        user.setCn("jm xxx");
        user.setSn("yin");
        user.setPassword("123");
        ldapDao.updateUser(user);
    }

    @Test
    public void testAuthentication(){
        boolean bl = ldapDao.authentication("jm");
        if(bl){
            System.out.print("成功认证");
        }
    }

    @Test
    public void testGetAllUsers(){
        List<User> users = userService.getAllUsers();
    }
}
