package com.lucene.util;

import java.util.List;

public class Page<T> {

	private int currentPage; // 当前页
	private int totalPage; // 总页数
	private int totalNum; // 总数据数
	private int offset = 2; // 每页显示数据数
	private List<T> list; // 需要现实的数据

	public Page() {
	}

	public Page(int currentPage, int totalNum, int offset, List<T> list) {
		this.currentPage = currentPage;
		this.totalNum = totalNum;
		this.offset = offset;
		this.list = list;
		int temp = totalNum / offset;
		this.totalPage = totalNum % offset == 0 ? temp:temp+1;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String toString() {
		return "Page [currentPage=" + currentPage + ", totalPage=" + totalPage + ", totalNum=" + totalNum + ", offset="
				+ offset + ", list=" + list + "]";
	}

}
