package org.example.input;

import org.example.model.InputArticle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private static final String SRC_PATH = "src/main/resources/";

    public List<InputArticle> readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(SRC_PATH + fileName));
        List<InputArticle> listOfInputArticles = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null) {
                String[] tokens = line.split(" ");
                InputArticle inputArticle = new InputArticle(tokens[0], tokens[1]);
                listOfInputArticles.add(inputArticle);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            br.close();
    }
        return listOfInputArticles;
}}
