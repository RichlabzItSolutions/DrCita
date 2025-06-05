package com.drcita.user.models.newProviderlist;

public class Pagination {

    private int currentPage;
    private int totalPages;
    private int pageSize;
    private int totalItems;

    // Getters and setters
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
}
