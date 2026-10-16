package com.learn.apis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 集合框架对照：同一批数据，不同结构不同表现。
 *
 * 1. ArrayList vs LinkedList（这里只演示 List 语义；LinkedList 少做 get(i)）
 * 2. HashSet 去重 vs TreeSet 有序
 * 3. HashMap 计数 vs TreeMap 按 key 排序
 * 4. ArrayDeque 当栈、当队列
 * 5. PriorityQueue Top-3
 * 6. 邻接表表示简单无向图
 *
 * 概念卡片：notes/collections-framework.md
 *
 * 编译：javac -encoding UTF-8 -d out src\com\learn\apis\Ex09CollectionsCases.java
 * 运行：java -cp out com.learn.apis.Ex09CollectionsCases
 */
public class Ex09CollectionsCases {

    public static void main(String[] args) {
        demoListSetMap();
        System.out.println();
        demoTreeStructures();
        System.out.println();
        demoDequeStackQueue();
        System.out.println();
        demoTopK();
        System.out.println();
        demoGraphAdjacencyList();
    }

    /** 同一组词：List 保序可重复，Set 去重，Map 计数。 */
    private static void demoListSetMap() {
        System.out.println("== 1. List / Set / Map ==");
        String[] words = {"java", "go", "java", "rust", "go"};

        List<String> list = new ArrayList<String>();
        Set<String> hashUnique = new HashSet<String>();
        Map<String, Integer> counts = new HashMap<String, Integer>();

        for (int i = 0; i < words.length; i++) {
            list.add(words[i]);
            hashUnique.add(words[i]);
            Integer n = counts.get(words[i]);
            if (n == null) {
                n = 0;
            }
            counts.put(words[i], n + 1);
        }

        System.out.println("List (size=5, 有重复)     = " + list);
        System.out.println("HashSet (size=3)          = " + hashUnique);
        System.out.println("HashMap 计数              = " + counts);
    }

    /** TreeSet / TreeMap：自然序（字符串字典序）。 */
    private static void demoTreeStructures() {
        System.out.println("== 2. TreeSet / TreeMap ==");
        Set<String> sortedUnique = new TreeSet<String>();
        sortedUnique.add("pear");
        sortedUnique.add("apple");
        sortedUnique.add("banana");
        System.out.println("TreeSet 有序              = " + sortedUnique);

        Map<Integer, String> rank = new TreeMap<Integer, String>();
        rank.put(3, "铜牌");
        rank.put(1, "金牌");
        rank.put(2, "银牌");
        System.out.println("TreeMap 按 key 升序       = " + rank);
    }

    /** 栈 LIFO、队列 FIFO。优先 ArrayDeque，不用 java.util.Stack。 */
    private static void demoDequeStackQueue() {
        System.out.println("== 3. 栈与队列 (ArrayDeque) ==");

        ArrayDeque<String> stack = new ArrayDeque<String>();
        stack.push("first");
        stack.push("second");
        System.out.println("栈 pop                    = " + stack.pop() + ", " + stack.pop());

        ArrayDeque<String> queue = new ArrayDeque<String>();
        queue.addLast("A");
        queue.addLast("B");
        System.out.println("队列 poll                 = " + queue.pollFirst() + ", " + queue.pollFirst());
    }

    /** 最小堆：保留最大的 3 个分数（用小顶堆，堆顶是当前 Top 里最小的）。 */
    private static void demoTopK() {
        System.out.println("== 4. PriorityQueue Top-3 ==");
        int[] scores = {88, 95, 72, 99, 81, 93, 77};
        int k = 3;
        PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>(k);

        for (int i = 0; i < scores.length; i++) {
            minHeap.offer(scores[i]);
            if (minHeap.size() > k) {
                minHeap.poll(); // 去掉当前 Top 里最弱的
            }
        }

        List<Integer> top = new ArrayList<Integer>(minHeap);
        Collections.sort(top, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b.compareTo(a); // 降序打印
            }
        });
        System.out.println("分数 Top-3                = " + top);
    }

    /** 图：节点用 String，邻接表 Map<节点, 邻居列表>。 */
    private static void demoGraphAdjacencyList() {
        System.out.println("== 5. 图（邻接表）==");
        Map<String, List<String>> graph = new HashMap<String, List<String>>();

        addEdge(graph, "A", "B");
        addEdge(graph, "A", "C");
        addEdge(graph, "B", "C");

        System.out.println("A 的邻居                  = " + graph.get("A"));
        System.out.println("B 的邻居                  = " + graph.get("B"));
    }

    private static void addEdge(Map<String, List<String>> graph, String a, String b) {
        addNeighbor(graph, a, b);
        addNeighbor(graph, b, a); // 无向图：双向各记一条
    }

    private static void addNeighbor(Map<String, List<String>> graph, String from, String to) {
        List<String> neighbors = graph.get(from);
        if (neighbors == null) {
            neighbors = new ArrayList<String>();
            graph.put(from, neighbors);
        }
        neighbors.add(to);
    }
}
