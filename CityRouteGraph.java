import java.util.*;

public class CityRouteGraph {
    // A class to represent a city and the distance to another city
    private static class CityRoute {
        String city;  // The name of the city
        int distance;  // The distance from the current city to this city

        // Constructor to initialize a CityRoute object with city name and distance
        public CityRoute(String city, int distance) {
            this.city = city;
            this.distance = distance;
        }
    }

    // A class to store the result of a shortest path query
    public static class PathResult {
        List<String> path;  // The list of cities in the shortest path
        int totalDistance;  // The total distance of the shortest path

        // Constructor to initialize the PathResult with a path and its total distance
        public PathResult(List<String> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }

    // Adjacency list to represent the graph (cities and routes)
    private Map<String, List<CityRoute>> adjList = new HashMap<>();

    // Method to add an edge between two cities with a given distance
    public void addEdge(String from, String to, int distance) {
        // Add an empty list if the city doesn't already exist in the adjacency list
        adjList.putIfAbsent(from, new ArrayList<>());

        // Add the neighboring city and the distance to the adjacency list
        adjList.get(from).add(new CityRoute(to, distance));
    }

    // Method to get the neighboring cities (routes) from a given city
    public List<CityRoute> getNeighbors(String city) {
        // Return the neighbors if the city exists, otherwise return an empty list
        return adjList.getOrDefault(city, new ArrayList<>());
    }

    // Method to compute the shortest path using Dijkstra's algorithm
    public PathResult dijkstra(String start, String end) {
        // Maps to store the shortest known distance and the previous city in the path for each city
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        // Priority queue (min-heap) to always expand the city with the current shortest distance
        PriorityQueue<CityRoute> queue = new PriorityQueue<>(Comparator.comparingInt(r -> r.distance));

        // Initialize all distances to infinity except for the start city
        for (String city : adjList.keySet()) {
            distances.put(city, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        // Add the start city to the priority queue with distance 0
        queue.add(new CityRoute(start, 0));

        // Perform Dijkstra's algorithm
        while (!queue.isEmpty()) {
            // Get the city with the smallest tentative distance
            CityRoute current = queue.poll();

            // If the end city is reached, break out of the loop
            if (current.city.equals(end)) break;

            // Explore each neighboring city (connected by an edge)
            for (CityRoute neighbor : getNeighbors(current.city)) {
                // Calculate the new distance to the neighbor
                int newDist = distances.get(current.city) + neighbor.distance;

                // If a shorter path to the neighbor is found, update its distance and previous city
                if (newDist < distances.getOrDefault(neighbor.city, Integer.MAX_VALUE)) {
                    distances.put(neighbor.city, newDist);
                    prev.put(neighbor.city, current.city);
                    queue.add(new CityRoute(neighbor.city, newDist));  // Add the neighbor to the priority queue
                }
            }
        }

        // Reconstruct the shortest path by tracing back from the end city to the start city
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.add(at);  // Add each city to the path
        }
        Collections.reverse(path);  // Reverse the path to get it from start to end

        // Get the total distance of the shortest path
        int totalDistance = distances.getOrDefault(end, Integer.MAX_VALUE);

        // Return the path and the total distance as the result
        return new PathResult(path, totalDistance);
    }
}
