package edu.vanderbilt.studzikm.investigation;

import edu.vanderbilt.studzikm.*;

import java.io.*;
import java.util.*;

public class GraphWidthInvestigation {

    private String[] countryFileNames = {
            "case1_countries",
            "case2_countries",
            "case3_countries"
    };
    private String[] resourceFileNames = {
            "resources",
            "resources2"
    };

    public static void main(String[] args) throws IOException {
        List<Double> proportions = new ArrayList<>();

        for (Integer width = 25; width > 0.01; width = width / 2) {
            proportions.add(width.doubleValue());
        }

        for (double width = 2.75; width > 1; width -= .5) {
            proportions.add(width);
        }

        for (double width = 1.20; width > 1; width -= .1) {
            proportions.add(width);
        }

        GraphWidthInvestigation invest = new GraphWidthInvestigation();
        invest.runInvestigation(proportions);
    }

    public void runInvestigation(List<Double> proportions) throws IOException {
        for (String resource : resourceFileNames) {

            Map<String, Resource> resources = ResourceFactory.importResources(new File("input/" + resource + ".csv"));
            for (String country : countryFileNames) {
                System.out.println(resource + " " + country);

                World world = CountryParser.createWorld(new File("input/" + country + ".csv"), resources, FunctionQualityCompuation::new, 0.9);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("graph-width-out/" + country + "_" + resource + ".csv"));) {


                    for (Double width : proportions) {
                        double proportionStep = width/100.0;
                        double proportionInitStep = proportionStep/3;
                        for (double proportionInit = proportionStep - 2 * proportionInitStep;
                             proportionInit <= proportionStep + 2 * proportionInitStep;
                            proportionInit += proportionInitStep) {

                            SearchBuilder searchBuilder = new SearchBuilder()
                                    .setGamma(0.9)
                                    .setFailurePenalty(0.05)
                                    .setLogisticGrowthRate(1.0)
                                    .setSigmoidMidpoint(0.0)
                                    .setResources(resources)
                                    .setInitialQualities(world)
                                    .setFrontierSupplier(() -> new BreadthFirstFrontier(100))
                                    // TODO add planner supplier
                                    .setPlannerSupplier(null);

                            int count = 0;
                            for (double proportion = proportionInit; proportion <= 1; proportion += proportionStep) {
                                searchBuilder.setTransformProportion(proportion);
                                count++;
                                searchBuilder.setTransferProportion(proportion);
                                count++;
                            }

                            long start = System.currentTimeMillis();
                            Schedule schedule = searchBuilder.build()
                                .search(world, world.getCountry("Self"), 150);
                            long end = System.currentTimeMillis();
                            ScheduleItem item = schedule.stream()
                                .max((x, y) -> Double.compare(x.getExpectedUtility(), y.getExpectedUtility()))
                                .orElse(null);

                            String line = width + "," +
                                    count + "," +
                                    schedule.getAverageNodesGenerated() + "," +
                                    proportionInit + "," +
                                    item.getSchedulePostion() + "," +
                                    item.getExpectedUtility() + "," +
                                    (end - start);
                            writer.write(line + "\n");
                            System.out.println(line);
                        }
                    }
                }
            }
        }

    }

}
