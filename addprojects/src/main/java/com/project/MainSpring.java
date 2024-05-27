package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainSpring {
    // this is test for git 123456
    public static void main(String[] args) {
        // $ run OpenURL python script

        SpringApplication.run(MainSpring.class, args);

        System.out.println("Main Spring!!!!");
        String WindowsOpenURL_CD = "D:/vs_java_codes/AddProducts_Update/addprojects/OpenUrl.py";

        runPythonScript(WindowsOpenURL_CD);

    }

    public static void runPythonScript(String scriptPath) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python script executed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
