![Java CI](https://github.com/aeronautical-informatics/SESTools/workflows/Java%20CI/badge.svg)
# System Entity Structures (SES) 
SES is a high-level ontology which was introduced for knowledge representation of decomposition, taxonomy and coupling of systems [^f1]. It has a set of elements and axioms. The elements of SES are Entity, Aspect, Specializationand Multiple-Aspect [^f2]. Real or artificial system components can be represented using Entity nodes [^f3]. An Entity is an object of interest, and can also have variables attached to it. An Aspect denotes the decomposition relationship of an Entity node. Specialization nodes represent the taxonomy of an entity. A Multi-Aspect is a special kind of aspect, which represents a multiplicity relationship that specifies the parent entity as a composition of multiple entities of the same type. Aspect, Specialization and Multi-Aspect are represented by one, two and three vertical lines respectively. Fig. 1 shows the elements of SES and Fig. 2 presents an example. There are six axioms of SES: (1) uniformity, (2) strict hierarchy, (3) alternating mode, (4) valid brothers, (5) attached variables, and (6) inheritance [^f4]. According to the uniformity axiom, any two nodes with the same labels have isomorphic subtrees. Strict hierarchy defines a restriction that prevents a label from appearing more than once down any path of the tree. Alternating mode recommends that, if a node is an Entity, then the successor is either Aspect or Specialization and vice versa. Valid brothers axiom disallow two brothers from having the same label. An attached variable specifies a constraint that variable types attached to the same item shall have distinct names. With inheritance, it is indicated that Specialization inherits all variables and Aspects.

Pruning is the operation in which a unique system structure is derived from an SES and the result is called Pruned Entity Structure (PES). SES represents a model of a given application domain in terms of decompositions, component taxonomies and coupling specifications. During modeling using SES, all the possible variations or configurations of a system are considered. As an SES describes a number of system configurations, the SES tree needs to be pruned to get a particular configuration. PES is therefore a selection-free tree. The pruning process normally reduces SES by removing choices of entity which has multiple aspects and specialization consists of multiple entities. An SES tree can be pruned by performing following tasks:
1. Assigning values to the variables.
2. Choosing particular subject from several Aspect nodes for several decomposition of the system on same hierarchical level.
3. Selecting one entity from various options of Specialization node.
4. Specifying cardinality in Multi-Aspect node.

# SESTools
SESEditor for building System Entity Structures and PESEditor for interactive pruning. SESEditor which is designed in such a way that a user can draw SES graphs on the screen almost as one
would on paper. An SES is represented by a directed tree structure. Here, objects are represented by nodes which are

# References
[^f1]: Kim, T.-G., Lee, C., Christensen, E. R., and Zeigler, B. P., “System entity structuring and model base management,” IEEE
Transactions on Systems Man and Cybernetics, Vol. 20, No. 5, 1990, pp. 1013–1024.
[^f2]: Zeigler, B. P., and Hammonds, P. E., Modeling and simulation-based data engineering: introducing pragmatics into ontologies
for net-centric information exchange, Elsevier, 2007.
[^f3]: Zeigler, B. P., Praehofer, H., and Kim, T. G., Theory of modeling and simulation: integrating discrete event and continuous
complex dynamic systems, Academic Press, 2000.
[^f4]: Zeigler, B. P., Multifacetted modelling and discrete event simulation, Academic Press, 1984.
