package org.linlinjava.ax.admin.dto;

import org.linlinjava.ax.db.domain.AxGoods;
import org.linlinjava.ax.db.domain.AxGoodsAttribute;
import org.linlinjava.ax.db.domain.AxGoodsProduct;
import org.linlinjava.ax.db.domain.AxGoodsSpecification;

public class GoodsAllinone {
    AxGoods goods;
    AxGoodsSpecification[] specifications;
    AxGoodsAttribute[] attributes;
    AxGoodsProduct[] products;

    public AxGoods getGoods() {
        return goods;
    }

    public void setGoods(AxGoods goods) {
        this.goods = goods;
    }

    public AxGoodsProduct[] getProducts() {
        return products;
    }

    public void setProducts(AxGoodsProduct[] products) {
        this.products = products;
    }

    public AxGoodsSpecification[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(AxGoodsSpecification[] specifications) {
        this.specifications = specifications;
    }

    public AxGoodsAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(AxGoodsAttribute[] attributes) {
        this.attributes = attributes;
    }

}
