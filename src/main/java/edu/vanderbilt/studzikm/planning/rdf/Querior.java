package edu.vanderbilt.studzikm.planning.rdf;

import org.apache.jena.rdf.model.Model;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class Querior {

    private final Model model;
    private final String aiPrefix;
    private static final IntPredicate hasNext = i -> i > 0;
    private static final IntUnaryOperator next = i -> i - 1;
    private static final String ASK_QUERY_OPEN =
            "PREFIX ai: <%s> " +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                    "ASK { " ;

    private static final String ASK_BODY_ACTION_GOAL =
            "    ?a%1$d a ai:Action . " +
                    "    ?a%1$d ai:atTime \"%2$d\"^^xsd:int . " +
                    "    ?a%1$d ai:hasOutput ?ar%1$d . " +
                    "    ?g%1$d a ai:Goal . " +
                    "    ?g%1$d ai:obtain ?gr . " +
                    "    ?g%1$d ai:atTime \"%2$d\"^^xsd:int . " +
                    "    ?gr ai:requires ?ar%1$d . ";

    private static final String ASK_BODY_RESOURCE_TYPE =
            "    ?gr a ai:Resource . ";

    public Querior(Model model) {
        this.model = model;
        this.aiPrefix = this.model.getNsPrefixURI("ai");
    }

    public boolean historyAlignsWithGoal(int depth, int time) {
        StringBuilder sb = new StringBuilder()
                .append(String.format(ASK_QUERY_OPEN, aiPrefix));
        IntStream.rangeClosed(0, depth)
                .forEach(i -> sb.append(String.format(ASK_BODY_ACTION_GOAL, i, time - i)));
        sb.append(ASK_BODY_RESOURCE_TYPE);

        System.out.println(sb);

        return false;
    }
}
