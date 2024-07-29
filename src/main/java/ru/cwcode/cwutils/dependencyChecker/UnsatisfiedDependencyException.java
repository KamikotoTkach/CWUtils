package ru.cwcode.cwutils.dependencyChecker;

public class UnsatisfiedDependencyException extends RuntimeException {
  PluginRequirement requirement;
  
  public UnsatisfiedDependencyException(PluginRequirement requirement) {
    this.requirement = requirement;
  }
  
  @Override
  public String getMessage() {
    return "Requirement " + requirement + " not satisfied, current version: " + requirement.getProvidedVersion();
  }
}
