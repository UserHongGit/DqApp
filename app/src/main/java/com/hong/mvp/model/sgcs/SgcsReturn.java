package com.hong.mvp.model.sgcs;

import java.util.List;

public class SgcsReturn {
    private String tab;
    private List<GxEntity> entity_param_list;
    private GxEntity entity;

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public List<GxEntity> getEntity_param_list() {
        return entity_param_list;
    }

    public void setEntity_param_list(List<GxEntity> entity_param_list) {
        this.entity_param_list = entity_param_list;
    }

    public GxEntity getEntity() {
        return entity;
    }

    public void setEntity(GxEntity entity) {
        this.entity = entity;
    }
}
