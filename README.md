This project started as a way for me to study random sampling algorithms
and has since evolved into a small (very simple) statistics package.

I plan to keep updating it over time, but have currently hit a snag
in that many of the distributions I have left to implement require
some sort of evaluation of the Gamma and Beta functions. Many java
libaries that implement these, such as the Apache Commons Math, also
implement classes Similar to what this project is aiming to implement
and it feels silly to include it as a dependecy. Plus I don't want
their designs to influence me. So I am currently looking either
for a java math package that just includes these fucntions or I
may try to find numerical algorithms to implement them myself,
Though that is unliekly as it would be a lot of work.

NOTE: I have only verified that these classes are working correctly
      in so far that I have coded the algorithms correctly. If you
      are looking for a statistical package that offers a high
      guaratee of numerical accuracy, then please look elsewhere.
