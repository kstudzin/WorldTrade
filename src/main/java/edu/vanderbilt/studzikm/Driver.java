package edu.vanderbilt.studzikm;

import com.google.common.base.Supplier;
import edu.vanderbilt.studzikm.planning.Planner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		WorldTradeParser wtp = WorldTradeParser.parse(args);
		File countryFile = wtp.getCountryFile();
		File resourceFile = wtp.getResourcesFile();

		OutputStream outputStream = wtp.getOutputFile();
		int depth = wtp.getDepth();
		double gamma = wtp.getGamma();
		double failurePenalty = wtp.getFailurePenalty();
		double logisticGrowthRate = wtp.getLogisticGrowthRate();
		double sigmoidMidpoint = wtp.getSigmoidMidpoint();
		Supplier<Frontier> frontierSupplier = wtp.getFrontierSupplier();
		double initialProportion = wtp.getInitialProportion();
		double proportionStep = wtp.getProportionStep();
		int numSchedules = wtp.getNumberOfSchedules();
		Supplier<Planner> plannerSupplier = wtp.getPlannerSupplier();

		System.out.printf("Resource file name: %s\n", resourceFile);
		System.out.printf("Country file name:  %s\n", countryFile);

		try {
			Map<String, Resource> resources = ResourceFactory.importResources(resourceFile);
			System.out.printf("\nResource list: \n%s\n", resources);

			World world = CountryParser.createWorld(countryFile, resources, FunctionQualityCompuation::new, gamma);
			System.out.printf("\nInitial World State: %s\n", world);

			System.out.printf("\nInitial country quality: \n%s\n",
					world.stream()
					.map(country -> new StringBuilder("\t")
							.append(country.getName())
							.append('\t')
							.append(country.computeQuality()))
					.collect(Collectors.joining("\n")));

			SearchBuilder searchBuilder = new SearchBuilder()
					.setGamma(gamma)
					.setFailurePenalty(failurePenalty)
					.setLogisticGrowthRate(logisticGrowthRate)
					.setSigmoidMidpoint(sigmoidMidpoint)
					.setResources(resources)
					.setInitialQualities(world)
					.setFrontierSupplier(frontierSupplier)
					.setPlannerSupplier(plannerSupplier);

			double prop = initialProportion;
			double step = proportionStep;
			while (prop <= 1) {
				searchBuilder.setTransformProportion(prop);
				searchBuilder.setTransferProportion(prop);
				prop += step;
			}

			Search search = searchBuilder.build();
			List<Schedule> searchResult = search.search(world, world.getCountry("Self"), depth, numSchedules);

			List<ScheduleItem> items = searchResult.stream()
					.map(schedule -> schedule.stream()
							.max((x, y) -> Double.compare(x.getExpectedUtility(), y.getExpectedUtility())) // TODO Is this used?
							.orElse(null))
					.collect(Collectors.toList());

			items.forEach(item ->
					System.out.println("\nMax Expected Utility: " + item.getExpectedUtility() +
							" at search depth: " +item.getSchedulePosition() + "\n")
			);


			int i = 1;
			for (Schedule schedule : searchResult) {
				outputStream.write(String.format("======  Schedule %d  ======\n", i).getBytes());
				outputStream.write(schedule.toString().getBytes());
				outputStream.write('\n');
				i++;
			}

		} catch (IOException e) {
			System.out.printf("\nCould not parse resource file %s%n", resourceFile);
		}

		System.out.printf("\n\nExiting.\n");

	}
}
