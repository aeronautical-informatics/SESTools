# SESTools

## System Entity Structures (SES)

SES is a high-level ontology which was introduced for knowledge representation
of decomposition, taxonomy and coupling of systems [1]. It has a set of
elements and axioms. The elements of SES are Entity, Aspect, Specializationand
Multiple-Aspect [2]. Real or artificial system components can be represented
using Entity nodes [3]. An Entity is an object of interest, and can also have
variables attached to it. An Aspect denotes the decomposition relationship of
an Entity node. Specialization nodes represent the taxonomy of an entity. A
Multi-Aspect is a special kind of aspect, which represents a multiplicity
relationship that specifies the parent entity as a composition of multiple
entities of the same type. Aspect, Specialization and Multi-Aspect are
represented by one, two and three vertical lines respectively. Fig. 1 shows the
elements of SES and Fig. 2 presents an example. There are six axioms of SES:
(1) uniformity, (2) strict hierarchy, (3) alternating mode, (4) valid brothers,
(5) attached variables, and (6) inheritance [4]. According to the uniformity
axiom, any two nodes with the same labels have isomorphic subtrees. Strict
hierarchy defines a restriction that prevents a label from appearing more than
once down any path of the tree. Alternating mode recommends that, if a node is
an Entity, then the successor is either Aspect or Specialization and vice
versa. Valid brothers axiom disallow two brothers from having the same label.
An attached variable specifies a constraint that variable types attached to the
same item shall have distinct names. With inheritance, it is indicated that
Specialization inherits all variables and Aspects.

Pruning is the operation in which a unique system structure is derived from an
SES and the result is called Pruned Entity Structure (PES). SES represents a
model of a given application domain in terms of decompositions, component
taxonomies and coupling specifications. During modeling using SES, all the
possible variations or configurations of a system are considered. As an SES
describes a number of system configurations, the SES tree needs to be pruned to
get a particular configuration. PES is therefore a selection-free tree. The
pruning process normally reduces SES by removing choices of entity which has
multiple aspects and specialization consists of multiple entities. An SES tree
can be pruned by performing following tasks:

1. Assigning values to the variables.
2. Choosing particular subject from several Aspect nodes for several
   decomposition of the system on same hierarchical level.
3. Selecting one entity from various options of Specialization node.
4. Specifying cardinality in Multi-Aspect node.

## SESEditor & PESEditor

SESEditor for building System Entity Structures and PESEditor for interactive
pruning. SESEditor which is designed in such a way that a user can draw SES
graphs on the screen almost as one would on paper. An SES is represented by a
directed tree structure. Here, objects are represented by nodes which are
connected using edges. Elements icons are added in the toolbox for easy access.
The vertices or elements and edges can be drawn by clicking and dragging the
mouse. Also nodes and edges can be easily moved to any position. The drawing
panel is synchronized with the bottom-left tree. If there is an element
addition in one place, either in the white drawing panel or the left tree, it
will be added in both the sections automatically. Variables can be attached to
the nodes. Eventually, the attached variables are listed in the variable table
on the right top corner during any node selection from either of the
trees.Furthermore, selection constraints can be added to the aspect node to
restrict the choices of entities. These constraints are specified using XPath.
Like variables, constraints are also listed on the constraint table on the
right side of the editor during aspect node selection.

PES Editor has been developed as an interactive pruning tool. The user
basically selects each and every decision point, such as a specialization node,
and resolves the decision. It supports all the design patterns proposed in
Deatcu et al. [5] for interactive pruning of SES. Its GUI looks very similar to
the SES Editor. It also has variable table for displaying variables or editing
values of variables. Constraint table and console window work exactly the same
way. Here, the left side tree is also synchronized with the white drawing panel
and nodes are movable. Unlike SES Editor, here we can not create new projects,
but only open SES models created in SES Editor. New elements cannot be added or
deleted; the existing SES cannot be edited. The main functionality of PES
Editor is interactive pruning of an SES model.

## SESTools Publications and Presentations

1. Karmokar, B.C., Durak, U., Jafer, S., Chhaya, B.N. and Hartmann, S., 2019.
   Tools for Scenario Development Using System Entity Structures. In AIAA
   Scitech 2019 Forum (p. 1712).

2. Karmokar, B.C., Durak, U., Hartmann, S. and Ziegler, B.P., 2019, July.
   Towards a standard computational representation for system entity
   structures. In Proceedings of the 2019 Summer Simulation Conference (pp.
   1-11).

2. Karmokar, B.C., Durak, U., Hartmann, S. and Ziegler, B.P., 2019, July.
   Towards a standard computational representation for system entity
   structures. In Proceedings of the 2019 Summer Simulation Conference (pp.
   1-11). 

3. Ellis, O. and Durak, U., 2020. Simulation Based Development and Verification
   of Drogue Detection Algorithms for Autonomous Air to Air Refuelling. In AIAA
   Scitech 2020 Forum (p. 0670).

## References

[1] Kim, T.-G., Lee, C., Christensen, E. R., and Zeigler, B. P., “System entity
structuring and model base management,” IEEETransactions on Systems Man and
Cybernetics, Vol. 20, No. 5, 1990, pp. 1013–1024.

[2] Zeigler, B. P., and Hammonds, P. E., Modeling and simulation-based data
engineering: introducing pragmatics into ontologies for net-centric information
exchange, Elsevier, 2007.

[3] Zeigler, B. P., Praehofer, H., and Kim, T. G., Theory of modeling and
simulation: integrating discrete event and continuous complex dynamic systems,
Academic Press, 2000.

[4] Zeigler, B. P., Multifacetted modelling and discrete event simulation,
Academic Press, 1984.

[5] Deatcu, C., Folkerts, H., Pawletta, T., and Durak, U., “Design patterns for
variability modeling using SES ontology,” Proceedings of the Model-driven
Approaches for Simulation Engineering Symposium, Society for Computer
Simulation International, 2018.
