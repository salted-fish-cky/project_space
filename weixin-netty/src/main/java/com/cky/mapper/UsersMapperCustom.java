package com.cky.mapper;

import com.cky.pojo.Users;
import com.cky.pojo.vo.FriendRequestVO;
import com.cky.pojo.vo.MyFriendsVO;
import com.cky.utils.MyMapper;

import java.util.List;

public interface UsersMapperCustom extends MyMapper<Users> {

    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    public List<MyFriendsVO> queryMyFriends(String userId);

    public void batchUpdateMsgSigned(List<String> msgIdList);

}