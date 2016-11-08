package com.sineom.thinkday.model;

import com.sineom.thinkday.bean.SocietyBean;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-08
 * Time: 23:55
 * DESIC
 */
public class SocietyModelImpl implements SocietyModel {
    @Override
    public SocietyBean saveSociety(String url, String title, String listzi) {
        SocietyBean bean = new SocietyBean();
        bean.Url = url;
        bean.title = title;
        bean.listzi = listzi;
        return bean;
    }
}
