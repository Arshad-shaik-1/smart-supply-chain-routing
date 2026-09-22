package com.arshad.supply_chain_routing.graph;

import com.arshad.supply_chain_routing.dto.RouteResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DijkstraService {
    private final GraphBuilder graphBuilder;

    public DijkstraService(GraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    public RouteResponse findShortestPath(Long source , Long destination){
        Graph g = graphBuilder.buildGraph();

        Map<Long , Double> distances = new HashMap<>();
        Map<Long , Long> previous = new HashMap<>();

        for(Long location : g.getAdjacencyList().keySet()){
            distances.put(location , Double.POSITIVE_INFINITY);
        }
        distances.put(source , 0.0);

        PriorityQueue<Long> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        pq.add(source);

        while(!pq.isEmpty()){
            Long current = pq.poll();
            if(current.equals(destination)){
                break;
            }
            for(GraphEdge edge : g.getAdjacencyList().getOrDefault(current , List.of())){
                Long next = edge.getDestination();

                double newDistance = distances.get(current) + edge.getDistance();

                if(newDistance < distances.getOrDefault(next , Double.POSITIVE_INFINITY)){
                    distances.put(next , newDistance);
                    previous.put(next , current);
                    pq.add(next);
                }
            }
        }

        List<Long> path = buildPath(previous , source , destination);

//        Map<String , Object> result = new LinkedHashMap<>();
//        result.put("Source" , source);
//        result.put("Destination" , destination);
//        result.put("Distance" , distances.getOrDefault(
//                destination , Double.POSITIVE_INFINITY
//        ));
//        result.put("Path" , path);

        return new RouteResponse(
                source ,
                destination ,
                distances.getOrDefault(
                        destination , Double.POSITIVE_INFINITY
                ),
                path
        );


    }
    private List<Long> buildPath(Map<Long , Long> previous , Long src , Long dest){
        List<Long> path = new ArrayList<>();

        Long curr = dest;
        while(curr != null){
            path.add(curr);

            if(curr.equals(src)){
                break;
            }
            curr = previous.get(curr);
        }
        Collections.reverse(path);

        if(!path.isEmpty() && path.get(0).equals(src)){
            return path;
        }
        return List.of();
    }
}
