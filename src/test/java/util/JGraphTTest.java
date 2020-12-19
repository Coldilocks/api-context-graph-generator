//package util;
//
//import org.jgrapht.Graph;
//import org.jgrapht.generate.CompleteGraphGenerator;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.graph.SimpleGraph;
//import org.jgrapht.traverse.DepthFirstIterator;
//import org.jgrapht.util.SupplierUtil;
//
//import java.util.Iterator;
//import java.util.function.Supplier;
//
///**
// * @author coldilock
// * https://jgrapht.org/
// */
//public class JGraphTTest {
//    // number of vertices
//    private static final int SIZE = 10;
//
//    /**
//     * Main demo entry point.
//     *
//     * @param args command line arguments
//     */
//    public static void main(String[] args)
//    {
//        // Create the VertexFactory so the generator can create vertices
//        Supplier<String> vSupplier = new Supplier<String>()
//        {
//            private int id = 0;
//
//            @Override
//            public String get()
//            {
//                return "v" + id++;
//            }
//        };
//
//        // @example:generate:begin
//        // Create the graph object
//        Graph<String, DefaultEdge> completeGraph =
//                new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);
//
//        // Create the CompleteGraphGenerator object
//        CompleteGraphGenerator<String, DefaultEdge> completeGenerator =
//                new CompleteGraphGenerator<>(SIZE);
//
//        // Use the CompleteGraphGenerator object to make completeGraph a
//        // complete graph with [size] number of vertices
//        completeGenerator.generateGraph(completeGraph);
//        // @example:generate:end
//
//        // Print out the graph to be sure it's really complete
//        Iterator<String> iter = new DepthFirstIterator<>(completeGraph);
//        while (iter.hasNext()) {
//            String vertex = iter.next();
//            System.out
//                    .println(
//                            "Vertex " + vertex + " is connected to: "
//                                    + completeGraph.edgesOf(vertex).toString());
//        }
//    }
//}
