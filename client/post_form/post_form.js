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

// 등록하기 버튼 선택
var submitButton = document.querySelector(".review-submit-button");

submitButton.addEventListener("click", function () {
  // 다른 페이지로 이동
  window.location.href = 'https://www.google.com';

  // 리뷰 업로드
  // ???
});
