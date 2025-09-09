package com.jdbc.util;

public class MyUtil { //페이징 처리를 하는 클래스
	
	//전체페이지 갯수구하는 메소드
	public int getPageCount(int numPerPage, int dataCount) {
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		//페이지가 안떨어지고 나머지가 생기면 페이지카운트+1
		if(dataCount % numPerPage !=0) {
			pageCount++;
		}
		return pageCount;
		
	}
	
	//페이징처리하는 메소드
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		//현재 내가 6페이지를 보고싶으면
		//6페이지에 해당하는 데이터를 뿌려야하는데
		//그걸 어디에 뿌려야하는지 -> list.jsp에 뿌려야함.
		//pageIndexList()이거는 어디에 뿌려야하는지를 정하는 변수값임.
		
		int numPerBlock = 5; // 이전과 다음사이의 숫자의 개수 ◀이전 6 7 8 9 10 다음▶
		int currentPageSetup; //이전에 들어가있는값. ◀이전
		int page; //
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage==0 || totalPage==0) {
			return "";
		}
		
		//list.jsp
		//list.jsp?searchKey=name&searchValue=suzi
		
		if(listUrl.indexOf("?")!=-1) {//검색을 했으면(?가 있으면)
			listUrl = listUrl + "&";
		}else {
			listUrl = listUrl + "?";
		}
		
		//이전의 pageNum만들기 (◀이전=currentPageSetup) 수학공식느낌
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0) {//몫을 제외한 나머지가 없으면
			currentPageSetup = currentPageSetup - numPerBlock;
		}
		
		//◀이전 여기에 들어가는 링크만들기
		if(totalPage>numPerBlock && currentPageSetup>0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" +
						currentPageSetup + "\">◀이전</a>&nbsp;");
			//<a href="list.jsp?pageNum=9">◀이전</a>&nbsp; (위에거 만든거임)
		}
		
		
		//바로가기 페이지 1 2 3 4 5 <-이런거 만드는방법
		page = currentPageSetup +1;
		while(page<=totalPage && page<=(currentPageSetup + numPerBlock)) {
			
			if(page==currentPage) {
				sb.append("<font color =\"Fuchsia\">" + page +
						"</font>&nbsp;");
				//<font color = "Fuchsia">9</font>&nbsp;
			}else {
				sb.append("<a href=\"" + listUrl + "pageNum=" +
						page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=7">7</a>&nbsp;
			}
			
			page++;
		}
		
		
		//다음▶ 만들기
		if(totalPage - currentPageSetup > numPerBlock) {
			//누적해서 만들거라서 sb.append()
			sb.append("<a href=\"" + listUrl + "pageNum=" +
					page + "\">다음▶</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">다음▶</a>&nbsp; (위에거 만든거임)
		}
		return sb.toString();
	}
		
	
}
