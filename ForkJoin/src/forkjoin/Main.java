/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forkjoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class Main {
    private final String FILE_PATH = "cybersla.txt";
    private List<String> words = new ArrayList<>();
    private Map<String, Integer> cuenta;
    private long startTime, midTime, stopTime;

    public static void main(String[] args) {
        Main main = new Main();
        main.startTime = System.currentTimeMillis();
        main.init();
        main.midTime = System.currentTimeMillis();
        main.part();
        main.stopTime = System.currentTimeMillis();
        main.info();
    }
    
    private Main init() {
//        /* Alternativa 1 */
//        try {
//            words = Files.lines(Paths.get(FILE_PATH))
//                .map(e -> e.split("\\s+"))
//                .flatMap(Arrays::stream)
//                .map(e -> e.replaceAll("[^a-zA-Z]", ""))
//                .map(e -> e.toLowerCase())
//                .collect(Collectors.toList());
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }

        /* Alternativa 2 */
        try {
            File file = new File(FILE_PATH);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                for(String s : scan.nextLine().split(" ")) {
                    words.add(s.replaceAll("[^a-zA-Z]", ""));
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
            
        return this;
    }
    
    private Main part() {
        ForkJoinPool pool = new ForkJoinPool();
        cuenta = pool.invoke(new ForkJoin(words));
        return this;
    }
    
    private Main info() {
        System.out.println(String.format("Tiempo de ejecución: Total: %d; Init: %d; Task: %d",
                stopTime - startTime, midTime - startTime, stopTime - midTime));
        AtomicLong sumPalabras = new AtomicLong(0);
        cuenta.values().forEach((v) -> sumPalabras.addAndGet(v));
        System.out.println(String.format("#palabras: %d; Epalabras: %d", cuenta.keySet().size(), sumPalabras.longValue()));
        cuenta.forEach((k,v) -> System.out.println(k + " : " + v));
        return this;
    }
}
