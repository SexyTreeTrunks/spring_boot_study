package org.zerock.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude="pageList")
public class MyPageMaker<T> {
	private static final int DEFAULT_COUNTPAGE = 10;
	private Page<T> result;
	
	private Pageable prevPage;
	private Pageable nextPage;
	private Pageable curPage;
	
	private int curPageNum;
	private int totalPageNum;
	
	private int countListNum; //한 페이지에 출력될 게시물 수
	private int countPageNum; // 한 화면에 출력될 페이지번호 수
	
	private List<Pageable> pageList;
	
	MyPageMaker(Page<T> result) {
		this.result = result;
		this.curPage = result.getPageable();
		this.curPageNum = curPage.getPageNumber() + 1;
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<>();
		this.countListNum = result.getSize();
		int tempcountPage = totalPageNum - curPageNum;
		this.countPageNum = (tempcountPage < DEFAULT_COUNTPAGE)? tempcountPage : DEFAULT_COUNTPAGE;  
		calcPages();
	}
	
	private void calcPages() {
		
		
		
	}

}
