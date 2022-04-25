package edu.vanderbilt.studzikm.planning.rdf;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
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

    private static final String SELECT_QUERY =
            "PREFIX ai: <%4$s>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "select ?ar1 ?ar2 ?ar3 {\n" +
                    "  ?g a ai:Goal .\n" +
                    "  ?g ai:obtain ?gr .\n" +
                    "  ?g ai:atTime \"%1$d\"^^xsd:int .\n" +
                    "\n" +
                    "  optional {\n" +
                    "    ?a1 a ai:Action .\n" +
                    "    ?a1 ai:atTime \"%1$d\"^^xsd:int .\n" +
                    "    ?a1 ai:hasOutput ?ar1 .\n" +
                    "    ?gr ai:requires ?ar1 .\n" +
                    "  }\n" +
                    "  optional {\n" +
                    "    ?a2 a ai:Action .\n" +
                    "    ?a2 ai:atTime \"%2$d\"^^xsd:int .\n" +
                    "    ?a2 ai:hasOutput ?ar2 .\n" +
                    "    ?g2 a ai:Goal .\n" +
                    "    ?g2 ai:obtain ?gr .\n" +
                    "    ?g2 ai:atTime \"%2$d\"^^xsd:int .\n" +
                    "    ?gr ai:requires ?ar2 .\n" +
                    "  }\n" +
                    "  optional {\n" +
                    "    ?a3 a ai:Action .\n" +
                    "    ?a3 ai:atTime \"%3$d\"^^xsd:int .\n" +
                    "    ?a3 ai:hasOutput ?ar3 .\n" +
                    "    ?g3 a ai:Goal .\n" +
                    "    ?g3 ai:obtain ?gr .\n" +
                    "    ?g3 ai:atTime \"%3$d\"^^xsd:int .\n" +
                    "    ?gr ai:requires ?ar3 .\n" +
                    "  }\n" +
                    "}";

    public Querior(InfModel model) {
        this.model = model;
        this.aiPrefix = this.model.getNsPrefixURI("ai");
    }

    public double historyAlignsWithGoal(int depth, int time) {
        ResultSet rs = executeSelect(String.format(SELECT_QUERY, time, time - 1, time - 2, aiPrefix));
        List<String> rsVars = rs.getResultVars();

        double score = .8;
        while (rs.hasNext()) {
            QuerySolution n = rs.next();
            for (String var : rsVars) {
                if (n.get(var) != null) {
                    score += .05;
                }
            }
        }
        return score == .8 ? .1 : score;
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
        log.trace("Query: " + query);
        return query;
    }

    private boolean executeQuery(String query) {
        return QueryExecutionFactory.create(
                QueryFactory.create(query),
                model).execAsk();
    }


    private ResultSet executeSelect(String query) {
//        System.out.println(query);
        return QueryExecutionFactory.create(
                QueryFactory.create(query),
                model).execSelect();
    }
}
