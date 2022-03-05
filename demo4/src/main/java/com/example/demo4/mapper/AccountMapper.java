package com.example.demo4.mapper;


import com.example.demo4.beans.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
    public Account getAccount(Integer id);

    @Select("select * from account where id = #{id}")
    public Account getAccount1(Integer id);
}
