package org.example;

import org.example.scan.LocalRepositoryScanner;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the local folder path to scan: ");
        String folderPath = scanner.nextLine();


        System.out.print("Enter files to skip (comma-separated): ");
        Set<String> filesToSkip = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        System.out.print("Enter directories to skip (comma-separated): ");
        Set<String> directoriesToSkip = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        LocalRepositoryScanner repositoryScanner = new LocalRepositoryScanner(filesToSkip, directoriesToSkip);
        repositoryScanner.scanAndSave(folderPath);
    }
}