package org.example;

import org.example.input.FileParser;
import org.example.model.InputArticle;

import java.io.IOException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String fileName = "list.txt";
        FileParser fileParser = new FileParser();
        List<InputArticle> list;
        try {
            list = fileParser.readFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(list.get(0));
    }
}