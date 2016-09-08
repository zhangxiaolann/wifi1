package com.liuguilin.only.bean;

/**
 * EventBus3.0
 * Created by LGL on 2016/5/12.
 */
public class UserEvent {

    //类型
    private String type;
    //fragment接收的文本
    private String fragmentText;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFragmentText() {
        return fragmentText;
    }

    public void setFragmentText(String fragmentText) {
        this.fragmentText = fragmentText;
    }
}
