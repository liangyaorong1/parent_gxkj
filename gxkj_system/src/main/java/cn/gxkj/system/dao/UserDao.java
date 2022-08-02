package cn.gxkj.system.dao;

import cn.gxkj.model.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--05 10:09:00
 */
public interface UserDao extends JpaRepository<User,String> , JpaSpecificationExecutor<User> {
    public User findByMobile(String mobile);
}
