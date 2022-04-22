package edu.vanderbilt.studzikm.planning.rdf;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.InfModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;

public class Querior {

    private Logger log = LogManager.getLogger(Querior.class);

    private final InfModel model;
    private final String aiPrefix;
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

    private static final String ASK_QUERY_CLOSE = "}";

    public Querior(InfModel model) {
        this.model = model;
        this.aiPrefix = this.model.getNsPrefixURI("ai");
    }

    public boolean historyAlignsWithGoal(int depth, int time) {
        return executeQuery(buildAskQuery(depth, time));
    }

    private String buildAskQuery(int depth, int time) {
        StringBuilder sb = new StringBuilder()
                .append(String.format(ASK_QUERY_OPEN, aiPrefix));
        IntStream.rangeClosed(0, depth)

                // Filter times less 0
                .takeWhile(i -> time - i >= 0)
                .forEach(i -> sb.append(String.format(ASK_BODY_ACTION_GOAL, i, time - i)));
        sb.append(ASK_BODY_RESOURCE_TYPE);
        sb.append(ASK_QUERY_CLOSE);

        String query = sb.toString();
        log.info("Query: " + query);
        return query;
    }

    private boolean executeQuery(String query) {
        return QueryExecutionFactory.create(
                QueryFactory.create(query),
                model).execAsk();
    }
}
