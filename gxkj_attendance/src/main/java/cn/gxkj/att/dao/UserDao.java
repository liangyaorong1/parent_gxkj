package cn.gxkj.att.dao;

import cn.gxkj.model.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);

    List<User> findByCompanyId(String companyId);
}
