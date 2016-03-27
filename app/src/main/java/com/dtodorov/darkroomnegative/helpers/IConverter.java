package com.dtodorov.darkroomnegative.helpers;

/**
 * Created by diman on 3/27/2016.
 */
public interface IConverter<TFrom, TTo, TParam> {
     TTo convert(TFrom from, TParam param);
}
