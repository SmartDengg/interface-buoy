package com.smartdengg.interfacebuoy.gradle.groovy

import com.smartdengg.interfacebuoy.gradle.java.CompactClassWriter
import com.smartdengg.interfacebuoy.gradle.java.Method
import com.smartdengg.interfacebuoy.gradle.java.MethodInstCollector
import com.smartdengg.interfacebuoy.gradle.java.Modifier
import groovy.transform.PackageScope
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class Processor {

  @PackageScope static void processFile(Path input, Path output) {

    Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
      @Override
      FileVisitResult visitFile(Path inputPath, BasicFileAttributes attrs) throws IOException {
        Path outputPath = Utils.toOutputPath(output, input, inputPath)
        directRun(inputPath, outputPath)
        return FileVisitResult.CONTINUE
      }

      @Override
      FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path outputPath = Utils.toOutputPath(output, input, dir)
        Files.createDirectories(outputPath)
        return FileVisitResult.CONTINUE
      }
    })
  }

  @PackageScope static void directRun(Path input, Path output) {
    if (Utils.isMatchCondition(input.toString())) {
      byte[] inputBytes = Files.readAllBytes(input)
      byte[] outputBytes = visitAndReturnBytecode(inputBytes)
      Files.write(output, outputBytes)
    } else {
      Files.copy(input, output)
    }
  }

  private static byte[] visitAndReturnBytecode(byte[] originBytes) {

    ClassReader classReader = new ClassReader(originBytes)
    ClassWriter classWriter = new CompactClassWriter(classReader,
        ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

    Map<String, List<Method>> classNonEmptyMethods = collectNonMethods(originBytes)
    Modifier classAdapter = new Modifier(classWriter, classNonEmptyMethods)
    try {
      classReader.accept(classAdapter, ClassReader.EXPAND_FRAMES)
      return classWriter.toByteArray()
    } catch (Exception e) {
      println "Exception occurred when visit code \n " + e.printStackTrace()
    }

    return originBytes
  }

  private static Map<String, List<Method>> collectNonMethods(byte[] bytes) {

    ClassReader classReader = new ClassReader(bytes)
    MethodInstCollector checker = new MethodInstCollector()
    try {
      classReader.accept(checker, ClassReader.SKIP_FRAMES)
    } catch (Exception e) {
      println "Exception occurred when visit code \n " + e.printStackTrace()
    }

    return checker.getMethods()
  }
}