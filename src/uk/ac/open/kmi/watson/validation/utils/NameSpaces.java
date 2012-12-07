package uk.ac.open.kmi.watson.validation.utils;

import java.util.ArrayList;
/*
 * String.java
 *
 * Created on 27 September 2006, 18:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


/**
 *
 * @author cb7224
 */
public class NameSpaces {
    public final static String NS_FOAF = "http://xmlns.com/foaf/0.1/";
    public final static String NS_RSS = "http://purl.org/rss/1.0/";
    public final static ArrayList<String> DAML_OIL;
    public final static ArrayList<String> RDFS;
    public final static ArrayList<String> RDF;
    public final static ArrayList<String> OWL;
    
    static {
        DAML_OIL = new ArrayList();
        RDFS = new ArrayList();
        RDF = new ArrayList();
        OWL = new ArrayList();
        
        DAML_OIL.add("http://www.daml.org/2000/12/daml+oil#");
        DAML_OIL.add("http://www.daml.org/2001/03/daml+oil#");
        DAML_OIL.add("http://www.daml.org/2000/10/daml-ont#");
        
        RDFS.add("http://www.w3.org/TR/1999/PR-rdf-schema-19990303#");
        RDFS.add("http://www.w3.org/2000/01/rdf-schema#");
        
        RDF.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        
        OWL.add("http://www.w3.org/2002/07/owl#");
    } 
}
