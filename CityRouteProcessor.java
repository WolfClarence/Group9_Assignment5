import java.io.*;
import java.util.*;

public class CityRouteProcessor {
    public static void main(String[] args) {
        // Create an instance of the CityRouteGraph class to store and process the routes
        CityRouteGraph cityRouteGraph = new CityRouteGraph();

        // Read data from a CSV file containing the city routes
        try (BufferedReader br = new BufferedReader(new FileReader("routes.csv"))) {
            String line;

            // Iterate over each line in the CSV file
            while ((line = br.readLine()) != null) {
                // Split the line by commas to get the "from", "to", and "distance" values
                String[] parts = line.split(",");

                // Skip lines with an incorrect format (not exactly 3 parts)
                if (parts.length != 3) continue;

                // Trim any whitespace and parse the values
                String from = parts[0].trim();
                String to = parts[1].trim();
                int distance = Integer.parseInt(parts[2].trim());

                // Add edges to the graph for both directions (since it is undirected)
                cityRouteGraph.addEdge(from, to, distance);
                cityRouteGraph.addEdge(to, from, distance);
            }
        } catch (IOException e) {
            // If an error occurs while reading the file, print the stack trace
            e.printStackTrace();
        }

        // Specify the starting city and the destination city
        String start = "Q";
        String end = "I";

        // Find the shortest path between the start and end cities using Dijkstra's algorithm
        CityRouteGraph.PathResult result = cityRouteGraph.dijkstra(start, end);

        // Extract the path and total distance from the result object
        List<String> path = result.path;
        int totalDistance = result.totalDistance;

        // Output the shortest path and the total distance
        System.out.println("Shortest route：" + String.join(" -> ", path));
        System.out.println("Shortest distance：" + totalDistance);
    }
}
