# Surface_Classification

Abstract: Provides background information on topology of 3-d manifolds. As well as motivation for the program.

GUI: Minimal functionality that allows users to create a word and print a solution to the eclipse console

Letter Class:
  Serves as being the base of planar models. 
  no reduction methods or anything noteworthy exist in this class

Word Class:
  contains an array of letters which is a word and contains a boolean value is_surface
  this class contains basic rule manipulations

Reduction_Object:
  outer shell for word class
  Contains methods by which the word is reduced into its most basic components
  this was done because the word class was already topping 300 lines and I ~~apparently write excessively bloated code~~ was toeing 
  the line of concise and readable
