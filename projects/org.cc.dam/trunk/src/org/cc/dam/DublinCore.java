//////////////////////////////////////////////////////////////////////
///                                                                ///
///  Dublin Core Metadata Set                                      ///
///                                                                ///
//////////////////////////////////////////////////////////////////////

package org.cc.dam;

public class DublinCore {
    public static final String NAMESPACE = "dc";
    public static final int TITLE = 0;
    public static final int CREATOR = 1;
    public static final int SUBJECT = 2;
    public static final int DESCRIPTION = 3;
    public static final int PUBLISHER = 4;
    public static final int CONTRIBUTOR = 5;
    public static final int DATE = 6;
    public static final int TYPE = 7;
    public static final int FORMAT = 8;
    public static final int IDENTIFIER = 9;
    public static final int SOURCE = 10;
    public static final int LANGUAGE = 11;
    public static final int RELATION = 12;
    public static final int COVERAGE = 13;
    public static final int RIGHTS = 14;
    public static String[] definition = {
        "A name given to the resource."                                                  ,
        "An entity primarily responsible for making the content of the resource."        ,
        "A topic of the content of the resource."                                        ,
        "An account of the content of the resource."                                     ,
        "An entity responsible for making the resource available."                       ,
        "An entity responsible for making contributions to the content of the resource." ,
        "A date of an event in the lifecycle of the resource."                           ,
        "The nature or genre of the content of the resource."                            ,
        "The physical or digital manifestation of the resource."                         ,
        "An unambiguous reference to the resource within a given context."               ,
        "A Reference to a resource from which the present resource is derived."          ,
        "A language of the intellectual content of the resource."                        ,
        "A reference to a related resource."                                             ,
        "The extent or scope of the content of the resource."                            ,
        "Information about rights held in and over the resource."
    };
    public static String[] uri = {
        "http://purl.org/dc/elements/1.1/title"       ,
        "http://purl.org/dc/elements/1.1/creator"     ,
        "http://purl.org/dc/elements/1.1/subject"     ,
        "http://purl.org/dc/elements/1.1/description" ,
        "http://purl.org/dc/elements/1.1/publisher"   ,
        "http://purl.org/dc/elements/1.1/contributor" ,
        "http://purl.org/dc/elements/1.1/date"        ,
        "http://purl.org/dc/elements/1.1/type"        ,
        "http://purl.org/dc/elements/1.1/format"      ,
        "http://purl.org/dc/elements/1.1/identifier"  ,
        "http://purl.org/dc/elements/1.1/source"      ,
        "http://purl.org/dc/elements/1.1/language"    ,
        "http://purl.org/dc/elements/1.1/relation"    ,
        "http://purl.org/dc/elements/1.1/coverage"    ,
        "http://purl.org/dc/elements/1.1/rights"
    };

    public static String definitionByType(int type) throws Exception {
        if (type < 0 || type > 14)
            throw new Exception("Invalid type");
        return definition[type];
    }

    public static int typeByString(String element) throws Exception {
        if      (element.equals("title"))       return 0;
        else if (element.equals("creator"))     return 1;
        else if (element.equals("subject"))     return 2;
        else if (element.equals("description")) return 3;
        else if (element.equals("publisher"))   return 4;
        else if (element.equals("contributor")) return 5;
        else if (element.equals("date"))        return 6;
        else if (element.equals("type"))        return 7;
        else if (element.equals("format"))      return 8;
        else if (element.equals("identifier"))  return 9;
        else if (element.equals("source"))      return 10;
        else if (element.equals("language"))    return 11;
        else if (element.equals("relation"))    return 12;
        else if (element.equals("coverage"))    return 13;
        else if (element.equals("rights"))      return 14;
        else                                    throw new Exception("Invalid element");
    }

    public static String uriByType(int type) throws Exception {
        if (type < 0 || type > 14)
            throw new Exception("Invalid type");
        return uri[type];
    }
}
