// 글자수 표시
const textarea = document.querySelector('.in_content');
const counter = document.getElementById('counter');

textarea.addEventListener('input', function () {
  const remaining = textarea.maxLength - textarea.value.length;
  const used = textarea.maxLength - remaining;
  counter.textContent = `${used}/${textarea.maxLength}자`; // 사용된 글자 수/총 글자 수
});

// 클릭 이벤트 핸들러 등록
var elements = document.getElementsByClassName('web-title');
elements[0].onclick = function () {
  // 메인 페이지의 URL로 변경
  window.location.href = 'https://www.google.com';
};





// 이미지 URL 목록
var imageUrls = [
  "https://joyposter.cafe24.com//NEW-posters/FMV/FMV-308.jpg",
  "https://joyposter.cafe24.com//NEW-posters/FMV/FMV-309.jpg",
  "https://joyposter.cafe24.com//NEW-posters/FMV/FMV-310.jpg",
  // 다른 이미지 URL들...
];

// 이미지를 동적으로 추가하는 함수
function addImages() {
  var imageWraps = document.querySelectorAll(".img-wrap");

  // 각 이미지 컨테이너에 이미지를 동적으로 추가
  imageWraps.forEach(function (wrap) {
    imageUrls.forEach(function (url) {
      var article = document.createElement("article");
      var img = document.createElement("img");
      img.src = url;
      article.appendChild(img);
      wrap.appendChild(article);

      // 클릭 이벤트
      img.addEventListener("click", function () {
        // 클릭된 이미지의 URL을 가져옴
        var imageUrl = this.src;
        // 새로운 페이지로 이동 (리뷰 본문)
        window.location.href = '새로운페이지 URL';
      });
    });
  });
}

// 초기 리뷰 수를 0으로 설정
var reviewCount = 0;

// 리뷰 작성할 때마다 리뷰 수를 증가시키고 화면에 업데이트하는 함수
function addReview() {
  reviewCount++;
  // 화면에 리뷰 수 업데이트
  var postCount = document.querySelector(".count_review");
  postCount.textContent = reviewCount;

  // 이미지 추가 함수 호출
  addImages();
}







// 등록하기 버튼 선택
var submitButton = document.querySelector(".review-submit-button");

submitButton.addEventListener("click", function () {
  // 다른 페이지로 이동
  window.location.href = 'https://www.google.com';

  // 리뷰 업로드
  // ???
});
