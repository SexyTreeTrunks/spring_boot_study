# spring_boot_study
게시판 구현하기

[TOC]

## 1. FreeBoard 게시판
게시판 기본 지식없이 무작정 구현한 거

#### Controller 코드
- @RequestParam을 이용해서 페이지넘버에 해당되는 pNo값을 파라미터로 받는다 --ⓐ
- page 오브젝트를 view로 전달한다 --ⓑ
- freeBoard함수와 freeBoard2 함수둘다 같은 기능을 하지만 페이지넘버를 받는 uri 형식이 다르다

```
@GetMapping("/free-board")
public void freeBoard(@RequestParam(value="pNo", defaultValue="1") Integer pNo ,Model model) { // --ⓐ
    Pageable page = PageRequest.of(pNo - 1, 10, Sort.Direction.DESC, "bno");
    Page<FreeBoard> pages = fbrepo.findAll(page);
    model.addAttribute("contents", pages.getContent());
    model.addAttribute("pages", pages); // --ⓑ
}

@GetMapping("/free-board/{pNo}")
public String freeBoard2(@PathVariable Integer pNo ,Model model) {
    Pageable page = PageRequest.of(pNo - 1, 10, Sort.Direction.DESC, "bno");
    Page<FreeBoard> pages = fbrepo.findAll(page);
    model.addAttribute("contents", pages.getContent());
    model.addAttribute("pages", pages);
    return "free-board";
}
```

#### View 코드
- prev 버튼, next 버튼은 레알로 이전 다음페이지를 가리킨다
- pageList는 항상 1부터 10까지이며 현재페이지에 따라서 갱신또한 되지 않는다

```
<div class="jb-center">
        <ul class="pagination">
            <li th:if="${pages.hasPrevious()}==true"> 
                <a th:href="@{'~/free-board?pNo=' + ${pages.previousPageable().getPageNumber() +1}}">이전</a>
            </li>
            <th:block th:each="idx : ${#numbers.sequence(1,10)}">
            <li th:class="${idx}==(${pages.getNumber()} + 1)? active : ''">
                <a th:href="@{'~/free-board?pNo=' + ${idx}}" th:text="${idx}"></a>
            </li>
            </th:block>
            <li th:if="${pages.hasNext()}==true"> 
                <a th:href="@{'~/free-board?pNo=' + ${pages.nextPageable().getPageNumber() +1}}">다음</a>
            </li>
        </ul>
    </div>
```

#### 한계점..
- controller의 확장 한계
    - 단순히 전체 게시판에서 원하는 페이지의 결과물을 가져오는 것이 아니라 검색 조건과 키워드 등에 따라 결과물을 가져와야 함
    - 만약 -1번 페이지를 불러온다면? (파라미터의 최소/최댓값 지정및 처리)
    - **PageVO 필요!**
- page관련 각종 처리
    - 현재 페이지에 따라 pagelist와 prev, next 값이 달라져야함
    - Page 객체에 있는 함수만 갖고 하기엔 한계가있음
    - **PageMaker 필요!**

## 2. WebBoard 게시판
pageVO와 PageMaker를 구현하여 재사용가능하고 코드 가독성도 좋다.
책에 있는 코드를 그대로 구현함

#### PageMaker 코드
- calcPages()에서 pageList와 이전 페이지, 다음페이지를 계산하여 값을 저장함

```
public PageMaker(Page<T> result) {
    this.result = result;
    this.curPage = result.getPageable();
    this.curPageNum = curPage.getPageNumber() + 1;
    this.totalPageNum = result.getTotalPages();
    this.pageList = new ArrayList<Pageable>();
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
```

> 근데 view코드를 보고 pagemaker를 보니까 약간 이상함. <br>prevPage, nextPage는 view에서 이전,다음페이지가 있는지 없는지 확인하는 용도로만 쓰임. <br>pageList도 마찬가지임. 어짜피 여기서 활용하는건 getNumber로 몇번째 페이지인지 알면됨. <br>**즉 굳이 이렇게 pageable로, pageable 리스트로 구현할 필요가 없다는 것임.**

#### Controller 코드
- PageVO 객체의 값을 파라미터로 지정 
- @ModelAttribute를 통해 pageVO값을 model로 전달 (@ModelAttribute("name") Object obj == model.addAttribute("name",obj))

```
@GetMapping("/list")
public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
    Pageable page = vo.makePageable(0, "bno");
    Page<WebBoard> result= repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
        
    model.addAttribute("result", new PageMaker(result));
}
```

#### View 코드
- pageList에 있는 pageable의 number값을 하나씩 불러와서 href값으로 지정
- 해당 href값과 form에서 보내는 hidden input값들을 이용해서 javascript로 링크처리함

```
<nav>
    <div>
        <ul class="pagination">
            <li class="page-item" th:if="${result.prevPage}">
                <a th:href="${result.prevPage.pageNumber} + 1">PREV [[${result.prevPage.pageNumber} + 1]]</a>
            </li>
            <li class="page-item" th:classappend="${p.pageNumber == result.curPageNum -1}? active:''" th:each="p:${result.pageList}">
                <a th:href="${p.pageNumber + 1}">[[${p.pageNumber + 1}]]</a>
            </li>
            <li class="page-item" th:if="${result.nextPage}">
                <a th:href="${result.nextPage.pageNumber} + 1">NEXT [[${result.nextPage.pageNumber} + 1]]</a>
            </li>
        </ul>
    </div>
</nav>
<form id='f1' th:action="@{list}" method="get">
<!-- action은 폼을전송할 서버쪽 스크립트파일,  -->
    <input type='hidden' name="page" th:value=${result.curPageNum}>
    <input type='hidden' name="size" th:value=${result.curPage.pageSize}>
    <input type='hidden' name="type" th:value=${pageVO.type}>
    <input type='hidden' name="keyword" th:value=${pageVO.keyword}>
</form>
```

- 자바스크립트에서는 f1이란 id가진 form 태그를 가져오고
- a 태그에 click이벤트가 일어나면 
- form 태그 안에서 name="page" 라는 요소가진거 찾아낸 뒤 (name="page"인 input 태그 찾아냄)
- 얘의 value값을 자신의(a태그) href attribute 값으로 변경 (찾아낸 input태그의 value값을 클릭이벤트가 일어난 a태그의 href값으로 설정)

```
$(document).ready(function() {
    var formObj = $("#f1");
    $(".pagination a").click(function(e) { 
        e.preventDefault(); //a태그의 기본동작을막음
        formObj.find('[name="page"]').val($(this).attr('href')); 
        formObj.submit();
    });
});
```

#### 또 한계점..
- 쓸데없이 큰 pageMaker
    - prevPage,nextPage,pageList안에 들어있는 pageable정보 중 오로지 페이지번호에 대한 정보만 view에서 쓰임
    - pageMaker가 페이지번호만 넘겨주도록 수정 필요 (그럼 model에 보내는 데이터의 양도 줄어들겠징?)

