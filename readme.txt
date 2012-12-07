This repository contains the source of the library used to create the set of indexes for the Watson Semantic Web Search Engine (see http://watson.kmi.open.ac.uk).

The indexes of Watson are made of for main directory, which are based on the Apache Lucene library:
   - 1 index for documents (ontologies)
   - 1 index for entities (classes, properties, individuals)
   - 1 index for object relationships
   - 1 index for literal relationships

This library can be used to create these indexes, using a set of files as input, or a more complex structure from a database or a queue of documents. It is based on a system of extractors (that extract metadata, entities, etc. from the documents) and popuators (that poluate the indexes with the extracted values).

The build.xml file contains the basic procedure for building and running a test of the library. To compile it, use 

$ ant clean
$ ant build

A test is provided that indexes a set of 3 basic, toy ontologies (in the examples directory). To run it, use 

$ ant test

it should create 4 directories "documents-test", "entities-test", "relations-test" and "literals-test" that correspond to the four indexes for Watson. The Watson services API (https://github.com/mdaquin/Watson-Service-API) provide a complete library to use these indexes to reproduce the functionality of Watson on the created indexes.
