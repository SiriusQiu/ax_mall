package org.linlinjava.ax.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.ax.core.qcode.QCodeService;
import org.linlinjava.ax.db.domain.AxGoods;
import org.linlinjava.ax.db.service.AxGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateShareImageTest {
    @Autowired
    QCodeService qCodeService;
    @Autowired
    AxGoodsService axGoodsService;

    @Test
    public void test() {
        AxGoods good = axGoodsService.findById(1181010);
        qCodeService.createGoodShareImage(good.getId().toString(), good.getPicUrl(), good.getName());
    }
}
