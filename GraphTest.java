import java.util.*;

public class GraphTest {

    public static void main(String[] args) {
        // Test with an undirected graph
        testGraph(false);
        System.out.println("--------------\n");
        // Test with a directed graph
        testGraph(true);
    }

    /**
     * This method tests the Graph class by creating a graph and running Dijkstra's algorithm
     * to find the shortest paths between randomly chosen cities.
     *
     * @param directed A boolean value to determine whether the graph is directed or not.
     */
    public static void testGraph(boolean directed) {
        int numCities = 100_000;  // Total number of cities in the graph (100,000)
        int numEdges = 1_000_000; // Total number of edges in the graph (1,000,000)
        int numQueries = 10;      // Number of shortest path queries to perform
        Random rand = new Random();  // Random object for generating random numbers

        // Create a new graph (directed or undirected)
        Graph graph = new Graph(directed);

        // Step 1: Generate city names
        // Create an array of city names "C00001", "C00002", ..., "C100000"
        String[] cities = new String[numCities];
        for (int i = 0; i < numCities; i++) {
            cities[i] = "C" + String.format("%05d", i + 1);
        }

        // Step 2: Add 1,000,000 random edges to the graph
        // Add random edges between cities with random weights between 1 and 1000
        for (int i = 0; i < numEdges; i++) {
            // Select two random cities
            String from = cities[rand.nextInt(numCities)];
            String to = cities[rand.nextInt(numCities)];
            if (from.equals(to)) continue; // Avoid self-loops (no edge from a city to itself)

            // Generate a random distance (weight) for the edge
            int distance = rand.nextInt(1000) + 1; // Random distance between 1 and 1000
            graph.addEdge(from, to, distance);  // Add the edge to the graph
        }

        // Step 3: Perform 10 shortest path queries using Dijkstra's algorithm
        int successfulPaths = 0;  // To track the number of successful queries (paths found)
        long totalTime = 0;       // To track the total time taken for all queries

        // Run 10 random queries to find the shortest path between random pairs of cities
        for (int i = 0; i < numQueries; i++) {
            // Randomly pick start and end cities
            String start = cities[rand.nextInt(numCities)];
            String end = cities[rand.nextInt(numCities)];

            // Record the start time for the query
            long startTime = System.nanoTime();
            // Perform Dijkstra's algorithm to find the shortest path
            List<String> path = graph.dijkstra(start, end);
            // Record the end time for the query
            long endTime = System.nanoTime();

            // Check if a valid path was found (i.e., path.size() > 1 means a path exists)
            if (path.size() > 1) {
                successfulPaths++;  // Increment successful paths counter
                // Print the details of the query and the time taken in milliseconds
                System.out.printf("Query %d: %s -> %s | Path Length: %d | Time: %d ms%n",
                        i + 1, start, end, path.size(), (endTime - startTime) / 1_000_000);
            } else {
                // Print if no path was found
                System.out.printf("Query %d: %s -> %s | No path found%n", i + 1, start, end);
            }

            // Add the time taken for this query to the total time
            totalTime += (endTime - startTime);
        }

        // Print the summary after all queries are complete
        System.out.println("\nSummary:");
        System.out.println("Total Queries: " + numQueries);  // Total number of queries
        System.out.println("Successful Paths: " + successfulPaths);  // Total number of successful queries
        // Print the average time per query in milliseconds
        System.out.println("Average Time per Query: " + (totalTime / numQueries / 1_000_000) + " ms");
    }
}
