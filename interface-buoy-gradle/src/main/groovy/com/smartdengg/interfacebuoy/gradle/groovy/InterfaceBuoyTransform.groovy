package com.smartdengg.interfacebuoy.gradle.groovy

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

class InterfaceBuoyTransform extends Transform {

  @Override
  String getName() {
    return 'interfaceBuoy'
  }

  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS
  }

  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    return TransformManager.PROJECT_ONLY
  }

  @Override
  boolean isIncremental() {
    return true
  }

  @Override
  void transform(TransformInvocation invocation)
      throws TransformException, InterruptedException, IOException {

    TransformOutputProvider outputProvider = invocation.outputProvider
    if (!invocation.isIncremental()) outputProvider.deleteAll()

    def startTime = System.nanoTime()

    invocation.inputs.each { inputs ->

      inputs.directoryInputs.each { directoryInput ->

        Path inputRoot = directoryInput.file.toPath()
        Path outputRoot = outputProvider.getContentLocation(//
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY).toPath()

        if (invocation.isIncremental()) {
          directoryInput.changedFiles.each { File inputFile, Status status ->

            Path inputPath = inputFile.toPath()
            Path outputPath = Utils.toOutputPath(outputRoot, inputRoot, inputPath)

            switch (status) {
              case Status.NOTCHANGED:
                break
              case Status.ADDED:
              case Status.CHANGED:
                //direct run byte code
                Processor.directRun(inputPath, outputPath)
                break
              case Status.REMOVED:
                Files.deleteIfExists(outputPath)
                break
            }
          }
        } else {
          Processor.processFile(inputRoot, outputRoot)
        }
      }
    }
    println()
    println "======== >>>>  COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms  <<<< ========"
    println()
  }
}