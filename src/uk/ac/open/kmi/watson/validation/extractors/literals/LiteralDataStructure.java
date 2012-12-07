package uk.ac.open.kmi.watson.validation.extractors.literals;

import uk.ac.open.kmi.watson.validation.utils.UriUtil;

public class LiteralDataStructure{
    
    public String sbjUri;
    public String pptUri;
    public String value;
    public String language;
    
    public String toString(){
        return UriUtil.splitNamespace(this.sbjUri)[1]+" "+
                UriUtil.splitNamespace(this.pptUri)[1]+" "+
                this.value+" langu= "+this.language;
    }

    
    public boolean equals(Object o){
        LiteralDataStructure l = (LiteralDataStructure)o;
        boolean res=  (sbjUri == l.sbjUri &&
                pptUri == l.pptUri &&
                value.equals(l.value));
        if (language!= null)
            res = res && language.equals(l.language);
        else res = res && l.language==null;
        return res;
    }
}