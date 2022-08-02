package cn.gxkj.social.service;


import cn.gxkj.model.social_security.CityPaymentItem;
import cn.gxkj.social.dao.CityPaymentItemDao;
import cn.gxkj.social.dao.PaymentItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentItemService {
	
    @Autowired
    private PaymentItemDao paymentItemDao;
	
    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;

    //根据城市id获取缴费项目
	public List<CityPaymentItem> findAllByCityId(String id) {
		return cityPaymentItemDao.findAllByCityId(id);
	}
}
