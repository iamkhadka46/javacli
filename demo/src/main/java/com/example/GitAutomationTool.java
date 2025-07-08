package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GitAutomationTool {

    private static final String GIT_STATUS_CMD = "git status --porcelain";
    private static final String GIT_ADD_CMD = "git add .";
    private static final String GIT_COMMIT_CMD = "git commit -m \"%s\"";
    private static final String GIT_PUSH_CMD = "git push origin HEAD";
    private static final String GIT_REV_PARSE_CMD = "git rev-parse --is-inside-work-tree";

    public static void main(String[] args) {
        // Check if the current directory is a Git repository
        if (!isGitRepository()) {
            System.out.println("Not a Git repository. Exiting...");
            return;
        }

        // Check for uncommitted changes
        if (!hasUncommittedChanges()) {
            System.out.println("No uncommitted changes found. Exiting...");
            return;
        }

        // Prompt user for a commit message
        String commitMessage = getCommitMessageFromUser();

        // Stage changes
        stageChanges();

        // Commit changes
        commitChanges(commitMessage);

        // Push changes to the current branch
        pushChanges();
    }

    /**
     * Checks if the current directory is a Git repository.
     *
     * @return true if the current directory is a Git repository, false otherwise
     */
    private static boolean isGitRepository() {
        try {
            Process process = Runtime.getRuntime().exec(GIT_REV_PARSE_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            return output != null && output.equals("true");
        } catch (IOException e) {
            System.err.println("Error checking Git repository: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks for uncommitted changes in the Git repository.
     *
     * @return true if uncommitted changes are found, false otherwise
     */
    private static boolean hasUncommittedChanges() {
        try {
            Process process = Runtime.getRuntime().exec(GIT_STATUS_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine() != null;
        } catch (IOException e) {
            System.err.println("Error checking for uncommitted changes: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a commit message from the user.
     *
     * @return the commit message
     */
    private static String getCommitMessageFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a commit message: ");
        return scanner.nextLine();
    }

    /**
     * Stages changes in the Git repository.
     */
    private static void stageChanges() {
        try {
            Process process = Runtime.getRuntime().exec(GIT_ADD_CMD);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error staging changes. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error staging changes: " + e.getMessage());
        }
    }

    /**
     * Commits changes in the Git repository.
     *
     * @param commitMessage the commit message
     */
    private static void commitChanges(String commitMessage) {
        try {
            String command = String.format(GIT_COMMIT_CMD, commitMessage);
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error committing changes. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error committing changes: " + e.getMessage());
        }
    }

    /**
     * Pushes changes to the current branch.
     */
    private static void pushChanges() {
        try {
            Process process = Runtime.getRuntime().exec(GIT_PUSH_CMD);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error pushing changes. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error pushing changes: " + e.getMessage());
        }
    }
}