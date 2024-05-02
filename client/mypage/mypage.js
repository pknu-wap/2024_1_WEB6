// 클릭 이벤트 핸들러 등록
var elements = document.getElementsByClassName('web-title');

// 타이틀 클릭 시 메인 페이지로 이동
elements[0].onclick = function () {
    window.location.href = 'https://www.google.com';
};

// 프로필 수정 페이지로 이동
function editProfile() {
    var editProfileUrl = "https://www.naver.com/";
    window.open(editProfileUrl, "_blank");
}



//게시글 데이터
const reviewList = [{}, {}, ... {}]; 
const page = document.querySelector("div.page");

//게시글의 총 개수   
const totalPageList = pageList.length; 

//slice()를 이용해 데이터의 전체 범위를 선택
pageList.slice(0, totlaPageList)

//map을 이용해 선택한 범위의 데이터를 조회
	.map((e, i) => {
  		const lists = document.createElement("div");
        const name = document.createElement("span");
        const content = document.createElement("p");
        const date = document.createElement("span");
        page.append(lists);
        lists.append(name);
        lists.append(date);
        lists.append(content);
        name.innerText = e.name;
        date.inneerText = e.date;
        content.innerText = e.content;
	});


