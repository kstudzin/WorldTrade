package edu.vanderbilt.studzikm;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class GraphWidthInvestigation {

    private String[] countryFileNames = {
            "input/case1_countries.csv",
            "input/case2_countries.csv",
            "input/case3_countries.csv"
    };
    private String[] resourceFileNames = {
            "input/case1_resources.csv",
            "input/case1_resources2.csv"
    };

    public static void main(String[] args) throws IOException {
        GraphWidthInvestigation invest = new GraphWidthInvestigation();
        invest.runInvestigation();
    }

    public void runInvestigation() throws IOException {
        for (String resource : resourceFileNames) {
            System.out.println(resource);

            Map<String, Resource> resources = ResourceFactory.importResources(new File(resource));
            for (String country : countryFileNames) {
                System.out.println(country);

                OutputStream out = new FileOutputStream("investigation/resource-country");
                World world = CountryParser.createWorld(new File(country), resources, FunctionQualityCompuation::new);

//                for (int width = 25; width > 0.01; width = width / 2) {
//                for (double width = 2.75; width > 1; width -= .25) {
                for (double width = 1.20; width > 1; width -= .05) {


//                    System.out.println("Width: " + width);
                    double proportionStep = width/100.0;
                    double proportionInitStep = proportionStep/3;
                    for (double proportionInit = proportionStep - 2 * proportionInitStep;
                         proportionInit <= proportionStep + 2 * proportionInitStep;
                         proportionInit += proportionInitStep) {
//                        System.out.println(width + " " + proportionInit);
                        SearchBuilder searchBuilder = new SearchBuilder()
                                .setGamma(0.9)
                                .setFailurePenalty(0.05)
                                .setLogisticGrowthRate(1.0)
                                .setSigmoidMidpoint(0.0)
                                .setResources(resources)
                                .setInitialQualities(world)
                                .setFrontierSupplier(() -> new BreadthFirstFrontier(100));

                        int count = 0;
                        for (double proportion = proportionInit; proportion <= 1; proportion += proportionStep) {
//                            System.out.println(proportion);
                            searchBuilder.setTransformProportion(proportion);
                            count++;
                            searchBuilder.setTransferProportion(proportion);
                            count++;
                        }

                        ScheduleItem item = searchBuilder.build()
                                .search(world, world.getCountry("Self"), 150)
                                .stream()
                                .max((x, y) -> Double.compare(x.getExpectedUtility(), y.getExpectedUtility()))
                                .orElse(null);

//                        out.write(String.format("%i,%d,%i,%d",width,proportionInit,item.getSchedulePostion(),item.getExpectedUtility()).getBytes());
//                            System.out.printf("%i,%d,%i,%d\n",width,proportionInit,item.getSchedulePostion(),item.getExpectedUtility());
                        System.out.println(count + " " + proportionInit + " " + item.getSchedulePostion() + " " + item.getExpectedUtility());
                    }
                }
            }
        }

    }

}
