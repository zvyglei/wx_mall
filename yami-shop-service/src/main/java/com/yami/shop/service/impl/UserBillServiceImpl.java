package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.dao.UserBillMapper;
import com.yami.shop.service.UserBillService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zhao
 * @date: 2023/7/27 08:31
 */
@Service
public class UserBillServiceImpl extends ServiceImpl<UserBillMapper, UserBill> implements UserBillService {
}
