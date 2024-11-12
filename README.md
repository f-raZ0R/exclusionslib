How to use the current features:


-Tag Blacklists/Exclusions:
in a tag json, have your entry you want to exclude in brackets with the id field, like how you would add "required": false. add "excluded": true , just like you'd set the required field.
You should have your exclusions AFTER any entries that might contain the tag you wish to exclude.

-Feature placement Structure Overlap Predicate:
use "exclusions_lib:overlaps_structure" like any other block predicate filter.
optional fields are range, an integer from 1 to 32 to choose how far away it checks for from the feature's origin (you probably want this large if the feature is large), and structures, an array of structure ids for if you want to specify it to
only checking for specific structures.
You probably want to wrap this one in a minecraft:not 
