# World Trade

## Running

This application can be run several ways:

### Installed Application

1. Unpack the archive  
1a. unzip world-trade.zip  
1b. tar xf world-trade.tar  
2. `./bin/world-trade <countries csv> <resources csv> [OPTIONS]`

### Using Gradle Application Plugin

This code can be run using Gradle:

```
gradle run --args "input/case1_countries.csv input/resources.csv --output test.out"
```

### IDE

I have been running this code in IntelliJ. I open the Driver class, right-click, modify the run configuration with my desired parameters, and finally right-click and run.

## CLI Options

**--output FILENAME**  
**-o FILENAME**  
Specify the name of the file to write the schedule to. Default is STDOUT.  

**--depth INT**  
**-d INT**  
Specify the search depth. Default is 25.

**--gamma N**  
**-g N**  
Specify factor that decreases the reward for schedule items further in the future. Specifically, gamma^N where N is the position of the schedule item for which we are calculated the reward. Must be between 0 and 1. Default is 0.9.  

**--failure-penalty N**  
**-f N**  
Specify penalty for producing schedule that is not accepted. Must be a negative constant. Default is -0.5.

**--logistic-growth-rate N**  
**-l N**  
Specify the rate at which increase in reward increases probability of accepted schedule. Must be greater than zero. Default is 1.0.

**--sigmoid-midpoint N**  
**-s N**  
Specify the amount to shift the sigmoid along the _x_ axis. Positive values shift right, i.e. for the same reward there is a lower probability of acceptance. Negative values shift left, i.e. for the same reward there is a higher probability of acceptance. Default is zero.  

**--frontier-type  TYPE**  
**-t TYPE**  
Specify the type of frontier to use. Either `hdfs` for Heuristic Depth First Frontier or `bfs` for Breadth First Frontier. Default is Heuristic Depth First Frontier.

**--proportion N**  
**-p N**  
Specify the initial proportion for transfers and transforms (See description below). Must be between 0 and 1. Default is 0.025.

**--proportion-step N**  
**-ps N**  
Specify the interval between different proportions. Must be between 0 and 1. Default is 0.025.

_Note: The last two options work together. Suppose proportion = 0.1 and proportionStep = 0.25. Then the Transform and Transfer proportions would be 0.1, 0.35, 0.65, and 0.85. Details in Architecture section below._

## High Level Architecture

The core game scheduling code is in the `search()` method of the `Search` class. This method generates a graph of possible actions that a country can perform to change, and hopefully improve, its state. The method returns a schedule of proposed actions expected to improve the quality of the country's state and a utility score for the schedule. 

The `Search` class requires significant configuration described below. Note that this configuration can be significantly simplified by using the `SearchBuilder`.

### State Generator

A `Search` requires a `StateGenerator` that generates the next possible states for a country given its current state. These next possible states are dictated by a set of actions templates, configured at start up, that a country is allowed to take. 

Note: The descriptions below discuss the `DefaultStateGenerator` and may not apply to other implementations of `StateGenerator`.

#### Action Templates

The two `Actions` that a country can make are `Transfers`, where they give some amount of a resource to or take some amount of a resource from another country, or `Transforms`, where they transform some of their resources into new resources.

A `Transform` template specifies the ratios of input resources to output resources. For example, the housing template specifies that you need 5 people, 1 metallic element, 5 timber, and 3 metallic alloys to make 1 house, 1 housing waste, and 5 people. (People are not actually created in this transform. People in the output means that they are a resource that is not consumed by the process.) 

You can have multiple housing templates each specifying a different number of houses to create, or more specifically, a proportion of the total houses that can be made. That is, if you have the resources to make 10 houses and two housing templates with proportions .3 and .8, then the `StateGenerator` will generate a state where 3 houses have been made and 8 houses have been made. Specifying the proportion allows the user to control how many children states are generated and consequently the width of the graph.

A `Transfer` template similarly specifies the proportions of a country's resource that can be traded. For example, if the search generator is configured with a template to trade .5 timber and .7 timber, I have 10 timber, and there are 5 other countries, then the state generator will generate the following 20 states:
- 5 states, one for me to give 5 timber to each country
- 5 states, one for me to give 7 timber to each country
- 5 states, one for each country to give me 50% of their timber
- 5 states, one for each country to give me 70% fo their timber

As with Transforms, specifying Transfer proportions allows users to control the width of the search graph, and consequently balance graph width and search depth to maximize schedule utility while minimizing time and memory use.

#### Reward Computation

The reward computation for a country must also be given to the State Generator at construction time. This is used to calculate the increase in state quality for each new state generated. This project uses `DiscountedRewardComputation` but it can be extended by implementing other `RewardCompuations`.

### Frontier

The frontier determines the type of search that is performed. Currently two types of `Frontiers` are implemented: `HeuristicDepthFirstFrontier` and `BreadthFirstFrontier`

### Reached

The search can be configured with a set of reached nodes to avoid loops. Currently, only the no-op `NullReached` has been implemented. There is some coupling between `Frontier` and `Reached`; in order to use reached, a frontier that uses it correctly will likely need to be implemented.

_Note: Initial version of LRU Reached and Metrics Reached exist but are not fully tested. In summary, LRU reached doesn't allow recently explored nodes from being added to the frontier. However, the exact same node is not often added, so `MetricsReached` was added to compare nodes only on similar metrics, i.e. nodes generated by actions with the same amounts of the same resources._

### Schedule Factory

Schedule Factory converts a branch of nodes from the search graph to a Schedule. It's most important configuration is the computations required to create it.

#### Expected Utility Computation

Expected utility computation calculates the expected utility of a schedule item. It can be extended to change the type of utility computation. Currently, it requires a success probability computation.

#### Success Probability Computation

Success probability computation calculates that a schedule will be accepted by other countries. To configure this computation extend this class.

### Quality Computation

By implementing the `QualityComputation` interface, you can configure how the quality for a country is calculated.

#### Function Quality Computation

If the quality calculation you are looking to implement can be written as a `BiFunction` that takes two doubles and returns a double, you can use the `FunctionQualityComputation` class. 

## Test Schedules

### Case 1

All countries have the same number of all raw resources.

#### Output

_**Schedule 1**_:  

- Non-waste resources weighted equally
- All defaults

```
# Parameters
input/case1_countries.csv input/resources.csv --output output/case1_schedule1.out

# Result
# Max Expected Utility: 0.18725714268352023 at search depth: 25
```

_**Schedule 2**_:  

- Depth of 50
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters
input/case1_countries.csv input/resources2.csv --output output/case1_schedule2.out

# Results
# Max Expected Utility: 0.32874691129472733 at search depth: 25
```

_**Schedule 3**_:  

- Depth of 100  
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters
input/case1_countries.csv input/resources.csv --output output/case1_schedule3.out --frontier-type bfs

# Results
# Max Expected Utility: 0.18725714268352023 at search depth: 25
```

_**Schedule 4**_:  

- Depth of 100
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case1_countries.csv input/resources2.csv --output output/case1_schedule4.out --frontier-type bfs

# Results
# Max Expected Utility: 0.32874691129472733 at search depth: 25
```

### Case 2

#### Output

Each resource is available at 1.8 the required amount in a single country. This includes intermediate and final product resources to simulate other countries performing their own transforms, which does not happen in these scenarios.

_**Schedule 1**_:

- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters:
input/case2_countries.csv input/resources.csv --output output/case2_schedule1.out

# Result
# Max Expected Utility: 0.6698595083403591 at search depth: 14
```

_**Schedule 2**_:  

- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/resources2.csv --output output/case2_schedule2.out

# Result 
# Max Expected Utility: 0.8035134022390258 at search depth: 10  
```

_**Schedule 3**_:

- Breadth first search
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/resources.csv --output output/case2_schedule3.out --frontier-type bfs

# Result
# Max Expected Utility: 0.6698595083403591 at search depth: 14
```

_**Schedule 4**_:

- Breadth first search
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/resources2.csv --output output/case2_schedule4.out --frontier-type bfs

# Result
# Max Expected Utility: 0.8035134022390258 at search depth: 10
```

### Case 3

Each resource is available at 1.8 times the required amount in a single country and 1.5 times the required amount in another country. All other resources are available at exactly the expected amount. Population number has been randomized and varies widely for all countries.

_**Schedule 1**_:  

- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters
input/case3_countries.csv input/resources.csv --output output/case3_schedule1.out

# Results
# Max Expected Utility: 0.6697181905009687 at search depth: 6
```

_**Schedule 2**_:  

- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case3_countries.csv input/resources2.csv --output output/case3_schedule2.out

# Results
# Max Expected Utility: 0.8034661186717644 at search depth: 8
```

_**Schedule 3**_:  

- Breadth first search
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters  
input/case3_countries.csv input/resources.csv --output output/case3_schedule3.out --frontier-type bfs

# Requests
# Max Expected Utility: 0.6697181905009687 at search depth: 6
```

_**Schedule 4**_:  

- Breadth first search
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case3_countries.csv input/resources2.csv --output output/case3_schedule4.out --frontier-type bfs

# Requests
# Max Expected Utility: 0.8034661186717644 at search depth: 8
```