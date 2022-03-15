# World Trade

## Running

## Test Schedules

### Case 1

All countries have the same number of all raw resources.

#### Output

_**Schedule 1**_:  

- Non-waste resources weighted equally
- All defaults

```
input/case1_countries.csv input/case1_resources.csv --output output/case1_schedule2.out
```

_**Schedule 2**_:  

- Depth of 50
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
input/case1_countries.csv input/case1_resources.csv --output output/case1_schedule2.out --depth 50
```

_**Schedule 3**_:  

- Depth of 100  
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
input/case1_countries.csv input/case1_resources.csv --output output/case1_schedule3.out --depth 100
```

_**Schedule 4**_:  

- Depth of 100
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
input/case1_countries.csv input/case1_resources2.csv --output output/case1_schedule4.out --depth 100
```

### Case 2

#### Output

Each resource is available at 1.8 the required amount in a single country. This includes intermediate and final product resources to simulate other countries performing their own transforms, which does not happen in these scenarios.

_**Schedule 1**_:

- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters:
input/case2_countries.csv input/case1_resources.csv --output output/case2_schedule1.out

# Result
# Max Expected Utility: 0.6698595083403591 at search depth: 14
```

_**Schedule 2**_:  

- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/case1_resources2.csv --output output/case2_schedule2.out

# Result 
# Max Expected Utility: 0.8035134022390258 at search depth: 10  
```

_**Schedule 3**_:

- Breadth first search
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/case1_resources.csv --output output/case2_schedule3.out --frontier-type bfs

# Result
# Max Expected Utility: 0.6698595083403591 at search depth: 14
```

_**Schedule 4**_:

- Breadth first search
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case2_countries.csv input/case1_resources2.csv --output output/case2_schedule4.out --frontier-type bfs

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
input/case3_countries.csv input/case1_resources.csv --output output/case3_schedule1.out

# Results
# Max Expected Utility: 0.6697181905009687 at search depth: 6
```

_**Schedule 2**_:  

- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case3_countries.csv input/case1_resources2.csv --output output/case3_schedule2.out

# Results
# Max Expected Utility: 0.8034661186717644 at search depth: 8
```

_**Schedule 3**_:  

- Breadth first search
- Non-waste resources weighted equally
- Defaults for all the other parameters

```
# Parameters  
input/case3_countries.csv input/case1_resources2.csv --output output/case3_schedule3.out 

# Requests
# Max Expected Utility: 0.6697181905009687 at search depth: 6
```

_**Schedule 4**_:  

- Breadth first search
- Resources weighted such that final products higher than raw and intermediate products.
- Defaults for all the other parameters

```
# Parameters
input/case3_countries.csv input/case1_resources2.csv --output output/case3_schedule4.out --frontier-type bfs

# Requests
# Max Expected Utility: 0.8034661186717644 at search depth: 8
```