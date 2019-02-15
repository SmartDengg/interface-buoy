package com.smartdengg.interfacebuoy.compiler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间:  2019/02/14 15:28 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Retention(RetentionPolicy.CLASS) @Target(ElementType.METHOD) public @interface Buoy {
}
