package com.smartdengg.interfacebuoy.gradle.groovy

import com.android.build.gradle.*
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class InterfaceBuoyPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    def androidPlugin = [AppPlugin, LibraryPlugin, FeaturePlugin]
        .collect { project.plugins.findPlugin(it) as BasePlugin }
        .find { it != null }

    if (!androidPlugin) {
      throw new GradleException(
          "'com.android.application' or 'com.android.library' or 'com.android.feature' plugin required.")
    }

    def extension = project.extensions.getByName("android") as BaseExtension
    extension.registerTransform(new InterfaceBuoyTransform())
  }
}