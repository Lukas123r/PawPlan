package de.lshorizon.pawplan.domain.routines

/**
 * Use case for marking a routine as completed.
 */
fun interface MarkRoutineDone {
    suspend operator fun invoke(id: String)
}
