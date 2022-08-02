package cn.gxkj.social.service;

import cn.gxkj.common.entity.PageResult;
import cn.gxkj.model.social_security.UserSocialSecurity;
import cn.gxkj.social.dao.UserSocialSecurityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.awt.image.RenderedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserSocialService {
	
    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;

    //分页查询用户的社保数据
	public PageResult findAll(Integer page, Integer pageSize, String companyId) {
		//需要借助feign调用远程微服务获取用户数据
		//查询社保表获取社保信息
		Page page1 = userSocialSecurityDao.findPage(companyId, new PageRequest(page - 1, pageSize));
		return new PageResult(page1.getTotalElements(),page1.getContent());
	}

	//根据id查询
	public UserSocialSecurity findById(String id) {
		Optional<UserSocialSecurity> optional = userSocialSecurityDao.findById(id);
		return optional.isPresent()? optional.get() : null;
	}

	public void save(UserSocialSecurity uss) {
		userSocialSecurityDao.save(uss);
	}


}
