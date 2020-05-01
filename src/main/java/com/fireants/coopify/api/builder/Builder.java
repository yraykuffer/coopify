/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fireants.coopify.api.builder;

import java.util.List;

/**
 *
 * @author admin
 */
public interface Builder<T> {
    enum Type {
        CONTRI,
        PAYMENT
    }
    public List<T> build();
}
