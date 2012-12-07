package uk.ac.open.kmi.watson.validation.utils;

import java.util.Iterator;
import java.util.Vector;
import uk.ac.open.kmi.watson.validation.extractors.relations.EntityRelationDataStructure;


public class V_CONSTANTS {
    
    // entity types...100
    public static final int CLASS_ENTITY = 101;
    public static final int PROPERTY_ENTITY = 102;
    public static final int INDIVIDUAL_ENTITY = 103;
    
    //entity relations...200
    public static final int SUB_CLASS_OF = 201;
    public static final int DISJOINT_WITH = 202;
    public static final int EQUIVALENT = 203;
    public static final int LITERAL = 204;
    public static final int SAME_AS = 205;
    public static final int DIFFERENT_FROM = 206;
    public static final int DOMAIN = 207;
    public static final int RANGE = 208;
    public static final int SUBPROPERTY_OF = 209;
    public static final int COMMENT = 210;
    public static final int LABEL = 211;
    public static final int TYPE_OF = 212;
    
    //OWL species...300
    public static final int NOT_OWL_LANGUAGE = 300;
    public static final int OWL_FULL = 301;
    public static final int OWL_DL = 302;
    public static final int OWL_LITE = 303;
    
    //SC relations...400
    public static final int VOCABULARY_OVERLAP = 400;
    
    //ontological languages...400
    public static final int OWL = 401;
    public static final int DAML_OIL = 402;
    public static final int RDFS = 403;
    
    //extractor codes pow2
    public static final long CLASS_POPULATOR = (long) Math.pow(2, 1);
    public static final long CLASS_RELATION_POPULATOR = (long) Math.pow(2, 2);
    public static final long DL_EXPRESSIVITY_POPULATOR = (long) Math.pow(2, 3);
    public static final long INCONSISTENCY_POPULATOR = (long) Math.pow(2, 4);
    public static final long INDIVIDUAL_POPULATOR = (long) Math.pow(2, 5);
    public static final long INDIVIDUAL_RELATION_POPULATOR= (long) Math.pow(2, 6);
    public static final long LANGUAGE_POPULATOR = (long) Math.pow(2, 7);
    public static final long OWL_SPECIE_POPULAOTR = (long) Math.pow(2, 8);
    public static final long LITERAL_POPULATOR = (long) Math.pow(2, 9);
    public static final long PROPERTIES_POPULAOTR = (long) Math.pow(2, 10);
    public static final long PROPERTIES_RELATION_POPULATOR = (long) Math.pow(2, 11);
    public static final long SC_COMMENT_POPULAOTR = (long) Math.pow(2, 12);
    public static final long SC_LABEL_POPULATOR = (long) Math.pow(2, 13);
    public static final long URI_POPULATOR = (long) Math.pow(2, 14);
    public static final long UNSATISFIABILITY_POPUPALTOR = (long) Math.pow(2, 15);
    public static final long BREADTH_POPULATOR = (long) Math.pow(2, 16);
    public static final long DENSITY_POPULATOR = (long) Math.pow(2, 17);
    public static final long DEPTH_POPULATOR = (long) Math.pow(2, 18);
    public static final long VO_POPULATOR = (long) Math.pow(2, 19);
    public static final long IMPORTS_POPULATOR = (long) Math.pow(2, 20);
    public static final long DOMAIN_DENSITY_POPULATOR = (long) Math.pow(2, 21);
    public static final long RANGE_DENSITY_POPULATOR =  (long) Math.pow(2, 22);
    public static final long SUB_CLASS_DENSITY_POPULATOR = (long) Math.pow(2, 23);
    public static final long NAME_SPACE_POPULATOR = (long) Math.pow(2, 24);
    public static final long NUM_OF_STATEMENTS_POPULATOR = (long) Math.pow(2, 25);
    public static final long POPULARITY_POPULATOR = (long) Math.pow(2, 26);
    public static final long AXIOM_EXTENTION_POPULATOR = (long) Math.pow(2, 27);
    public static final long RESTRICTED_AXIOM_EXTENTION_POPULATOR = (long) Math.pow(2, 28);
    public static final long SEMANTIC_DUPLICATION_POPULATOR = (long) Math.pow(2, 29);
    public static final long SEMANTIC_EXTENTION_POPULATOR = (long) Math.pow(2, 30);
    public static final long ENTITY_RELATION_POPULATOR = (long) Math.pow(2, 31);
    public static final long PROVENANCE_POPULATOR = (long) Math.pow(2, 32);   
    public static final long SIZE_POPULATOR = (long) Math.pow(2, 33);   
    
    
    public static String toString(int type) {
        switch(type){
            case V_CONSTANTS.CLASS_ENTITY : return "Entity type : CLASS";
            case V_CONSTANTS.PROPERTY_ENTITY : return "Entity type : PROPERTY";
            case V_CONSTANTS.INDIVIDUAL_ENTITY : return "Entity type : INDIVIDUAL";
            
            case V_CONSTANTS.OWL_FULL : return "Entity type : OWL_FULL";
            case V_CONSTANTS.OWL_DL : return "Entity type : OWL_DL";
            case V_CONSTANTS.OWL_LITE : return "Entity type : OWL_LITE";
            
            case V_CONSTANTS.OWL : return "Entity type : OWL";
            case V_CONSTANTS.DAML_OIL : return "Entity type : DAML+OIL";
            case V_CONSTANTS.RDFS : return "Entity type : RDFS";
            
            case V_CONSTANTS.SUB_CLASS_OF : return "Relation type : SUB_CLASS";
            case V_CONSTANTS.DISJOINT_WITH : return "Relation type : DISJOINT_WITH";
            case V_CONSTANTS.EQUIVALENT : return "Relation type : EQUIVALENT";
            case V_CONSTANTS.LITERAL : return "Relation type : LITERAL";
            case V_CONSTANTS.SAME_AS : return "Relation type : SAME_AS";
            case V_CONSTANTS.DIFFERENT_FROM : return "Relation type : DIFFERENT_FROM";
            case V_CONSTANTS.DOMAIN : return "Relation type : DOMAIN";
            case V_CONSTANTS.RANGE : return "Relation type : RANGE";
            case V_CONSTANTS.SUBPROPERTY_OF : return "Relation type : SUBPROPERTY_OF";
            case V_CONSTANTS.COMMENT : return "Relation type : COMMENT";
            case V_CONSTANTS.LABEL : return "Relation type : LABEL";
            case V_CONSTANTS.TYPE_OF : return "Relation type : TYPE_OF";
        }
        return "UNKNOWN_TYPE";
    }
    
    public static void printRelationVectorSize(Vector<EntityRelationDataStructure> v){
        int sbc = 0;  int eqv = 0;
        int dsj = 0;  int sam = 0;
        int rng = 0;  int lit = 0;
        int dmn = 0;  int sbp = 0;
        int dff = 0;  int cmm = 0;
        int lbl = 0;  int tof = 0;
        
        for (EntityRelationDataStructure r : v){
            switch(r.type){
                case V_CONSTANTS.SUB_CLASS_OF : sbc++; break;
                case V_CONSTANTS.DISJOINT_WITH : dsj++; break;
                case V_CONSTANTS.EQUIVALENT : eqv++; break;
                case V_CONSTANTS.LITERAL : lit++; break;
                case V_CONSTANTS.SAME_AS : sam++; break;
                case V_CONSTANTS.DIFFERENT_FROM : dff++; break;
                case V_CONSTANTS.DOMAIN : dmn++; break;
                case V_CONSTANTS.RANGE : rng++; break;
                case V_CONSTANTS.SUBPROPERTY_OF : sbp++; break;
                case V_CONSTANTS.COMMENT : cmm++; break;
                case V_CONSTANTS.LABEL : lbl++; break;
                case V_CONSTANTS.TYPE_OF : tof++; break;
            }
        }
        if(sbc !=0) System.out.println(sbc + " SUB_CLASS_OF");
        if(tof !=0) System.out.println(tof + " TYPE_OF");
        if(dsj !=0) System.out.println(dsj + " DISJOINT_WITH");
        if(dff !=0) System.out.println(dff + " DIFFERENT_FROM");
        if(sam !=0) System.out.println(sam + " SAME_AS");
        if(eqv !=0) System.out.println(eqv + " EQUIVALENT");
        if(sbp !=0) System.out.println(sbp + " SUB_PROPERTY_OF");
        if(dmn !=0) System.out.println(dmn + " DOMAIN");
        if(rng !=0) System.out.println(rng + " RANGE");
        if(cmm !=0) System.out.println(cmm + " COMMENT");
        if(lbl !=0) System.out.println(lbl + " LABEL");
        if(lit !=0) System.out.println(lit + " LITERAL");
        System.out.println("---");
    }
    
}
