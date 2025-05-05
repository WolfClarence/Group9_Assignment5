import java.util.*;

public class Graph {
    // Edge class representing a connection from one node to another with a weight
    public static class Edge {
        String to;   // The destination city
        int weight;  // The weight (distance) of the edge from the current node to the destination

        // Constructor to initialize an edge with a destination and a weight
        public Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Whether the graph is directed or undirected
    private final boolean directed;
    // Adjacency list to store the graph where each node maps to a list of edges
    private final Map<String, List<Edge>> adjList;

    // Constructor to initialize the graph, specifying whether it is directed
    public Graph(boolean directed) {
        this.directed = directed;
        this.adjList = new HashMap<>();
    }

    // Adds a new node to the graph (if it doesn't already exist)
    public void addNode(String city) {
        adjList.putIfAbsent(city, new ArrayList<>());
    }

    // Adds an edge with default weight 1 between two nodes (cities)
    public void addEdge(String from, String to) {
        addEdge(from, to, 1);
    }

    // Adds an edge between two nodes (cities) with a specified weight
    public void addEdge(String from, String to, int weight) {
        // Ensure both nodes are present in the adjacency list
        addNode(from);
        addNode(to);

        // Add the edge from 'from' to 'to' with the specified weight
        adjList.get(from).add(new Edge(to, weight));

        // If the graph is undirected, also add the reverse edge from 'to' to 'from'
        if (!directed) {
            adjList.get(to).add(new Edge(from, weight));
        }
    }

    // Parses an input line of the form "from,to,distance" and adds the corresponding edge
    public void addEdgeFromInput(String inputLine) {
        // Assumes input like: "ES,GB,650"
        String[] parts = inputLine.trim().split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid input format. Expected format: from,to,distance");
        }

        String from = parts[0].trim();
        String to = parts[1].trim();
        int distance;
        try {
            // Parse the distance (third part of the input line)
            distance = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Distance must be an integer.");
        }

        // Add the edge to the graph using the parsed data
        addEdge(from, to, distance);
    }

    // Returns the list of neighboring nodes (cities) for a given city
    public List<Edge> getNeighbors(String city) {
        return adjList.getOrDefault(city, new ArrayList<>());
    }

    // Computes the shortest path from the start city to the end city using Dijkstra's algorithm
    public List<String> dijkstra(String start, String end) {
        // Map to store the shortest distance from the start city to each city
        Map<String, Integer> dist = new HashMap<>();
        // Map to store the previous city on the shortest path to each city
        Map<String, String> prev = new HashMap<>();
        // Priority queue to select the node with the smallest tentative distance (min-heap)
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        // Initialize all cities' distances to infinity
        for (String node : adjList.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        // Distance to the start city is 0
        dist.put(start, 0);
        // Add the start city to the priority queue
        pq.add(new Edge(start, 0));

        // Perform Dijkstra's algorithm to find the shortest path
        while (!pq.isEmpty()) {
            // Extract the city with the smallest tentative distance
            Edge current = pq.poll();
            // If we reach the destination city, break out of the loop
            if (current.to.equals(end)) break;

            // Explore all neighboring cities
            for (Edge neighbor : getNeighbors(current.to)) {
                // Calculate the new distance to the neighbor
                int newDist = dist.get(current.to) + neighbor.weight;
                // If this new distance is shorter, update the distance and previous city
                if (newDist < dist.getOrDefault(neighbor.to, Integer.MAX_VALUE)) {
                    dist.put(neighbor.to, newDist);
                    prev.put(neighbor.to, current.to);
                    // Add the neighbor to the priority queue with the new distance
                    pq.add(new Edge(neighbor.to, newDist));
                }
            }
        }

        // Reconstruct the shortest path by tracing the previous cities from the end city
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.add(at);  // Add each city to the path
        }
        Collections.reverse(path);  // Reverse the path to get it from start to end

        // Print the shortest distance to the end city
        System.out.println("Shortest distance: " + dist.getOrDefault(end, -1));
        // Return the shortest path as a list of cities
        return path;
    }
}
