package com.stylefeng.guns.modular.system.model;

import java.util.List;

/**
 * @author lyd
 * @date 2019/1/20 22:03
 */

public class Data<E> {
    private List<E> value;

    private Integer code = 200;

    private String massage = "";

    private String redirect = "";

    public List<E> getValue() {
        return value;
    }

    public void setValue(List<E> value) {
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
