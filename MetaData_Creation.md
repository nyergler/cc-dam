# Introduction #

If you need to utilize personal or unimplemented MetaData content it can be easily created by generating a derived class.  This will allow you the freedom to utilize any form of metadata you see fit.

# Instructions #

  1. Download and Configure the Digital Asset Management Client.
  1. Create a new class in org.cc.dam.metadata which derives from org.cc.dam.metadata.MetaDataList.
    * Note: The general Naming Scheme is MetaDataList??? where the question marks are replaced by the name of the metadata.  i.e. MetaDataListDC for dublin core.
  1. Your constructor should add all tags to a storage mechanism a vector is provided by the super class.
  1. If you are not using the predefined vector for storage you must implement the methods 'String[.md](.md) getTags()' and 'int numTags()'.
  1. Regardless you must implement 'boolean isValid(String tag, String value)' and 'boolean containsTag(String tag)', which determine if a tag has valid data and if the tag is in the metadata list respectively.
  1. Once you have finished with the tag you currently need to update org.cc.dam.MetaDataLoader to add an instance of the MetaDataClass in the constructor.