package com.pdm.pokerdice.repo.tm

interface TransactionManager {
    fun <R> run(block: Transaction.() -> R): R
}