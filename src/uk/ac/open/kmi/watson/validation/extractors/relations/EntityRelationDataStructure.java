package uk.ac.open.kmi.watson.validation.extractors.relations;


import uk.ac.open.kmi.watson.validation.utils.ModelUtils;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class EntityRelationDataStructure{
    
    public String sbjUri;
    public String objUri;
    public String pptUri;
    public int type;
    
    public String toString(){
        return UriUtil.splitNamespace(this.sbjUri)[1]+" "+
                UriUtil.splitNamespace(this.pptUri)[1]+" "+
                UriUtil.splitNamespace(this.objUri)[1];
    }
    
    public boolean equals(Object o){
        EntityRelationDataStructure er = (EntityRelationDataStructure)o;
        return sbjUri.equals(er.sbjUri) && objUri.equals(er.objUri) && pptUri.equals(er.pptUri);
    }
    
    
    public void printToString(boolean localName){
        if(localName){
            sbjUri = ModelUtils.getLocalName(sbjUri);
            objUri = ModelUtils.getLocalName(objUri);
            pptUri = ModelUtils.getLocalName(pptUri);
        }
        System.out.println(sbjUri);
        System.out.println(pptUri);
        System.out.println(objUri);
        System.out.println(V_CONSTANTS.toString(type));
        System.out.println("---");
    }
    
}