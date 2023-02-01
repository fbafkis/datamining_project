# data mining project 

### First strategy: hybrid system, by Francesco Bertamini, to create the users' list, the query set and the utility matrix. 
### The Jaccard similarity has been developed by Kenan Henriksen.

In the Main class, at the beginning you can find some parameters to change to make the program work as you want.

* **mode**: it is a char, and it can assume value 'g' or 'r'. When g is selected it will generate the query set, the utility 
matrix and all the other variables from  and stops. When 'r' is selected the program will read the CSV files specified 
by the CSV filenames variables and will execute the algorithms producing the normalized and the dense utility matrix. 
* **the CSV files' filenames:** they refer to the CSV files' names that are required by
the program to run. The program generates them with the standard names assigned by default. 
* **k1**: it is the parameter that indicates the number of users to consider in the collaborative filter. 
* **k2** it is the parameter that indicates the number of queries to consider in the content-based filter.
* **UMRowsDimension** the desired height (number of users) for the utility matrix. 
* **UMColumnsDimension** the desired width (number of queries) for the utility matrix. 

First set up the parameters and run the program in 'g' mode. Then, to run the algorithms, run the program again in mode 'r'. 
Every time that the utility matrix dimensions are changed, you have to run again the program in 'g' mode. 