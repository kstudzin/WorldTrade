package edu.vanderbilt.studzikm;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.Reader;

public class RdfPlanner {

    private final Model model;

    public RdfPlanner(String rdfInputFilename) {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(rdfInputFilename, "TTL");
    }

    public  Double score(ActionResult<?> result) {
        return null;
    }
}
