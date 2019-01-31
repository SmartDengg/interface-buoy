package com.smartdengg.interfacebuoy.gradle.java;

import java.util.Objects;
import org.objectweb.asm.tree.InsnList;

/**
 * 创建时间:  2019/01/14 16:46 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Method {

  int access;
  String name;
  String desc;
  InsnList instructions;

  boolean contentsEq(int access, String name, String desc) {
    return this.access == access && //
        Objects.equals(this.name, name) && //
        Objects.equals(this.desc, desc);
  }
}
