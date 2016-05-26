/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forkjoin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author dordonez
 */
public class ForkJoin extends RecursiveTask<Map<String, Integer>>{
    private final Map<String, Integer> cuenta = new HashMap<>();
    private final List<String> palabras;
    private final int MAX_PALABRAS = 100;
    
    public ForkJoin(List<String> palabras) {
        this.palabras = palabras;
    }
    
    @Override
    protected Map<String, Integer> compute() {
        if(palabras.size() < MAX_PALABRAS) {
            for(String str : palabras) {
                if(str.isEmpty()) continue;
                cuenta.merge(str.toLowerCase(), 1, Integer::sum);
            }
        } else {
            ForkJoin left = new ForkJoin(palabras.subList(0, palabras.size()/2));
            left.fork();
            ForkJoin right = new ForkJoin(palabras.subList(palabras.size()/2, palabras.size()));
            cuenta.putAll(right.compute());
            left.join().forEach((k, v) -> cuenta.merge(k, v, Integer::sum));
        }
        return cuenta;
    }
    
}
