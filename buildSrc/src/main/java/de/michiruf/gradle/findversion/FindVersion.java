package de.michiruf.gradle.findversion;

import org.apache.maven.artifact.versioning.ComparableVersion;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FindVersion {

    public static String findClosestLowerOrEqualVersionInDirectory(File directory, String targetVersion) {
        return findClosestLowerOrEqualVersionInDirectory(directory, targetVersion, true);
    }

    public static String findClosestLowerOrEqualVersionInDirectory(File directory, String targetVersion, boolean onlySubdirectories) {
        if (!directory.isDirectory())
            throw new IllegalArgumentException("Given file is not a directory");

        var contents = onlySubdirectories ? directory.listFiles(File::isDirectory) : directory.listFiles();
        var contentsString = Arrays.stream(Objects.requireNonNull(contents))
                .map(File::getName)
                .toList();
        return findClosestLowerOrEqualVersion(contentsString, targetVersion);
    }

    public static String findClosestLowerOrEqualVersion(List<String> versions, String targetVersion) {
        var comparableTargetVersion = new ComparableVersion(targetVersion);
        var result = versions.stream()
                .map(ComparableVersion::new)
                .filter(comparableVersion -> comparableVersion.compareTo(comparableTargetVersion) <= 0)
                .max(ComparableVersion::compareTo);
        if (result.isEmpty()) {
            System.err.println("No version found. Did you pass versions to the function?");
            return null;
        }

        return result.get().toString();
    }
}
