package com.moviediscovery.common

import androidx.annotation.IntRange

interface Pagination {
    companion object {
        const val FIRST_PAGE = 1
        const val MAX_PAGE = 500
    }

    fun setCurrentPage(currentPage: Int)
    fun setTotalPageCount(totalPageCount: Int)
    fun getNextPage(): Int
    fun hasNextPage(): Boolean
}

class PaginationImpl : Pagination {

    /**
     * Using this API we can't request more than 500 pages
     */
    @IntRange(to = Pagination.MAX_PAGE.toLong())
    private var totalPageCount: Int = Pagination.MAX_PAGE
    private var currentPage: Int = Pagination.FIRST_PAGE

    override fun setCurrentPage(currentPage: Int) {
        this.currentPage = currentPage
    }

    override fun setTotalPageCount(totalPageCount: Int) {
        if (totalPageCount < Pagination.MAX_PAGE)
            this.totalPageCount = totalPageCount
    }

    override fun hasNextPage(): Boolean {
        return currentPage < totalPageCount
    }

    override fun getNextPage(): Int {
        return currentPage.plus(1)
    }
}