// 클릭 이벤트 핸들러 등록
var elements = document.getElementsByClassName('web-title');

// 클릭 시 페이지 이동
elements[0].onclick = function () {
    window.location.href = '/main.html';
};

var loginButton = document.getElementsByClassName('login-button');
loginButton[0].onclick = function () {
    window.location.href = '/login.html';
};

var signupButton = document.getElementsByClassName('signup-button');
signupButton[0].onclick = function () {
    window.location.href = '/.html';
};



 // 로그인 버튼 클릭 시 처리 함수
 document.querySelector(".login-button").addEventListener("click", function() {
    // 로그인 상태 변경
    isLoggedIn = true;
    // UI 업데이트
    document.querySelector(".login-button").style.display = "none";
    document.querySelector(".logout-button").style.display = "inline-block";
  });

  // 로그아웃 버튼 클릭 시 처리 함수
  document.querySelector(".logout-button").addEventListener("click", function() {
    // 로그인 상태 변경
    isLoggedIn = false;
    // UI 업데이트
    document.querySelector(".logout-button").style.display = "none";
    document.querySelector(".login-button").style.display = "inline-block";
    document.querySelector(".signup-button").style.display = "inline-block"; // 로그아웃시 회원가입 버튼 다시 표시
  });



//게시글 데이터
const reviewList = [{}, {}, ... {}];
const page = document.querySelector("div.page");


// 게시글의 총 개수
const totalReviewList = reviewList.length;

//slice()를 이용해 데이터의 전체 범위를 선택
reviewList.slice(0, totlaReviewList)
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



// fetch API를 사용하여 데이터를 불러오는 예시
fetch('/data')
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error fetching data:', error));