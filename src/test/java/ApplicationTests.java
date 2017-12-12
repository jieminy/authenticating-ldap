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

import User.User;
import hello.Application;
import hello.LdapDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;

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
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapDao ldapDao;

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
    public void testLdap(){
            User user = ldapDao.getUserByUid("test");
            System.out.print(user.getCn()+"uid"+user.getUid());
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUid("jm");
        user.setCn("jm yin");
        user.setSn("yin");
        user.setUserPassword("123");
        ldapDao.addUser(user);
    }
    @Test
    public void testDel(){
        ldapDao.deleteUser("jm");
    }

}
