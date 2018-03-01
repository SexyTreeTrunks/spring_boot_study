package org.zerock.vo;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude="content")
public class MyPageMaker<T> {
	private static final int DEFAULT_PAGELIST_SIZE = 10;
	private List<T> content;
	
	private int prevPageNum;
	private int nextPageNum;
	private int curPageNum;
	
	private int curPageSize;
	private int totalPageNum;
	
	private int startPageNum;
	private int endPageNum;
	
	public MyPageMaker(Page<T> result) {
		this.content = result.getContent();
		this.curPageSize = result.getPageable().getPageSize();
		this.curPageNum = result.getPageable().getPageNumber() + 1;
		this.totalPageNum = result.getTotalPages();
		calcPages();
	}
	
	private void calcPages() {
		prevPageNum = ((curPageNum - 1) / DEFAULT_PAGELIST_SIZE) * DEFAULT_PAGELIST_SIZE;
		startPageNum = prevPageNum + 1;
		if((startPageNum + DEFAULT_PAGELIST_SIZE) > totalPageNum)
			endPageNum = totalPageNum;
		else
			endPageNum = prevPageNum + 10;
		nextPageNum = endPageNum + 1;
	}
	
	public boolean hasPrevPage() {
		return (prevPageNum == 0)? false : true;
	}
	
	public boolean hasNextPage() {
		return (nextPageNum > totalPageNum)? false : true;
	}
}
