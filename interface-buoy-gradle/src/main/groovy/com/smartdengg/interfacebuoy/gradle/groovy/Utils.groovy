package com.smartdengg.interfacebuoy.gradle.groovy

import com.android.SdkConstants

import java.nio.file.Path

class Utils {

  static Path toOutputPath(Path outputRoot, Path inputRoot, Path inputPath) {
    return outputRoot.resolve(inputRoot.relativize(inputPath))
  }

  static boolean isMatchCondition(String name) {
    return name.endsWith(SdkConstants.DOT_CLASS) && //
        !name.matches('.*/R\\$.*\\.class|.*/R\\.class') && //
        !name.matches('.*/BuildConfig\\.class')
  }
}