/**
 * Structure of the syntactic tree.
 *
 * This syntactic tree is at the core the applications.
 * The "natural" language for the argumentation semantics is an instanciation of such a tree (see package {@link fr.irit.sesame.lang}).
 *
 * This package does not perform any change of the tree itself, for instance when a choice is made at a {@link ChooserNode}.
 * It only provides all the informations needed to perform those changes, such that the UI can perfomr them.
 *
 * @author Joseph Boudou
 */
package fr.irit.sesame.tree;
