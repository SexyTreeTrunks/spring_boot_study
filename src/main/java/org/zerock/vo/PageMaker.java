package org.zerock.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude="pageList")
public class PageMaker<T> {
	//PageMaker는 왜 필요한가
	//사용자가 클릭한 페이지마다 pagelist도 다르고 prevPage 도 다름
	//만약 controller에서 Page object 형태로 전달한다면 pageList를 갱신하는 작업을 view단에서 별도의 코드로 구현해야 하는데 그러는것보다 pagemaker에서 미리 처리하고 전달하는게 나음
	private Page<T> result;
	
	private Pageable prevPage;
	private Pageable nextPage;
	private Pageable curPage;
	
	private int curPageNum;
	private int totalPageNum;
	
	private List<Pageable> pageList;
	
	public PageMaker(Page<T> result) {
		this.result = result;
		this.curPage = result.getPageable();
		this.curPageNum = curPage.getPageNumber() + 1;
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<>();
		calcPages();
	}
	
	private void calcPages() { // 책버전
		int tempEndNum = (int)(Math.ceil(this.curPageNum/10.0)* 10);
		int startNum = tempEndNum - 9;
		
		Pageable startPage = this.curPage;
		for(int i = startNum; i <this.curPageNum; i++)
			startPage = startPage.previousOrFirst();
		this.prevPage = startPage.getPageNumber() <= 0? null: startPage.previousOrFirst();
		
		if(this.totalPageNum <tempEndNum) {
			tempEndNum = this.totalPageNum;
			this.nextPage = null;
		}
		
		for(int i=startNum; i<= tempEndNum; i++) {
			pageList.add(startPage);
			startPage = startPage.next();
		}
		this.nextPage = (startPage.getPageNumber() +1 < totalPageNum)? startPage:null;
	}
}
