package com.cky.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * userDao 用来代替数据库
 */
public class UserDao {

    private static Map<String , User> users;
    private static List<Authority> authorities = null;
    static {
        authorities = new ArrayList<>();
        authorities.add(new Authority("Article-1","/article-1.jsp"));
        authorities.add(new Authority("Article-2","/article-2.jsp"));
        authorities.add(new Authority("Article-3","/article-3.jsp"));
        authorities.add(new Authority("Article-4","/article-4.jsp"));
        users = new HashMap<>();
        User user1 = new User("aaa",authorities.subList(0,2));
        users.put("aaa",user1);


        User user2 = new User("bbb",authorities.subList(2,4));
        users.put("bbb",user2);
    }
    public User get(String username){
        return users.get(username);
    }

    public void update(String username, List<Authority> authorities){
       users.get(username).setAuthorities(authorities);

    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public List<Authority> getAuthorities(String[] urls){
        List<Authority> authorityList = new ArrayList<>();

            for(Authority auth:authorities){
                if(urls != null){
                    for(String url:urls ){
                        if(url.equals(auth.getUrl())){
                            authorityList.add(auth);
                        }
                    }
                }
            }

        return authorityList;
    }
}
