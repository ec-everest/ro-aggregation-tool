# ro-aggregation-tool

For a complete description of this tool and other research objects components visit http://everest.expertsystemlab.com

The aggregation tool allows researchers to create a new research object with the content of other existing research objects or to stablish relations with these research objects without copying their content. For instance, you can create research object grouping all the bibliographic research objects produced in 2015 referring to the Earthquakes in Mount Etna. 

The aggregation tool is integrated in the semantic search engine so that users can first filter the research object collection, by using the advanced search functionalities, and then use the aggregation tool to provide the title and description of the new research object and manually refine the subset of research objects to aggregate. Therefoe this tool has to be deployed along with a solr index following the configuration provided in https://github.com/ec-everest/semantic-enrichment-service/tree/master/everest-github-enricher-solr

## Installation

The aggregation tool is implemented in Java 8 and uses maven 3 to manage the libraries dependencies and generate the war files that are going to be deployed. The aggregation tool is implemented as a rest API using the framework Jersey 2 that can be deployed on any servlet container supporting Servlet 2.5 and higher, such as Tomcat 8.  

The aggregation tool strongly depends on the solr index populated with the semantic enrichment service. Therefore it is necessary to make sure that the solr index is correctly installed.

To install the aggregation tool first clone this following repository using `git clone`

Then use `maven install` to compile the aggregation tool in the root folder. This command generates the rocreation.war file that contains the aggregation tool web service. This file must be deployed in the servlet container in the path `/rocreation`. 

## Usage

Currently the aggregation tool is integrated in the semantic search engine. The user must query the research object collection to define the candidate set of research objects to be aggregated. Note that he have the possibility to refine this subset of research objects in the aggregation tool. The aggregation tool can be launched by clicking the `CREATE AGGREGATED RO` button.

![aggregation tool image](http://everest.expertsystemlab.com/home/aggscreen.png)

Once in the aggregation tool the user is prompted to provide the title and description of the new research object. The tool displays the subset of research objects to be included in the aggregated research object and offers the possibility to discard some of them by using a tick box along each research object. Finally, the user may choose to copy the content of these research objects or to stablish relations with them. The “Generate” button triggers the creation of the new research object. The research object author is the “Generation Service” and depending on the number of research objects, the number of resources in each research object and their size the process may take some time to finish. 
