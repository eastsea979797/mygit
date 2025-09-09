package com.jdbc.util;

public class MyUtil { //����¡ ó���� �ϴ� Ŭ����
	
	//��ü������ �������ϴ� �޼ҵ�
	public int getPageCount(int numPerPage, int dataCount) {
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		//�������� �ȶ������� �������� ����� ������ī��Ʈ+1
		if(dataCount % numPerPage !=0) {
			pageCount++;
		}
		return pageCount;
		
	}
	
	//����¡ó���ϴ� �޼ҵ�
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		//���� ���� 6�������� ���������
		//6�������� �ش��ϴ� �����͸� �ѷ����ϴµ�
		//�װ� ��� �ѷ����ϴ��� -> list.jsp�� �ѷ�����.
		//pageIndexList()�̰Ŵ� ��� �ѷ����ϴ����� ���ϴ� ��������.
		
		int numPerBlock = 5; // ������ ���������� ������ ���� ������ 6 7 8 9 10 ������
		int currentPageSetup; //������ ���ִ°�. ������
		int page; //
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage==0 || totalPage==0) {
			return "";
		}
		
		//list.jsp
		//list.jsp?searchKey=name&searchValue=suzi
		
		if(listUrl.indexOf("?")!=-1) {//�˻��� ������(?�� ������)
			listUrl = listUrl + "&";
		}else {
			listUrl = listUrl + "?";
		}
		
		//������ pageNum����� (������=currentPageSetup) ���а��Ĵ���
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0) {//���� ������ �������� ������
			currentPageSetup = currentPageSetup - numPerBlock;
		}
		
		//������ ���⿡ ���� ��ũ�����
		if(totalPage>numPerBlock && currentPageSetup>0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" +
						currentPageSetup + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=9">������</a>&nbsp; (������ �������)
		}
		
		
		//�ٷΰ��� ������ 1 2 3 4 5 <-�̷��� ����¹��
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
		
		
		//������ �����
		if(totalPage - currentPageSetup > numPerBlock) {
			//�����ؼ� ����Ŷ� sb.append()
			sb.append("<a href=\"" + listUrl + "pageNum=" +
					page + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">������</a>&nbsp; (������ �������)
		}
		return sb.toString();
	}
		
	
}
