// 웹 페이지 이동
var webTitle = document.getElementsByClassName('web-title');
webTitle[0].onclick = function () {
    window.location.href = '/main_page/index.html';
};

var loginButton = document.getElementsByClassName('login-button');
loginButton[0].onclick = function () {
    window.location.href = '/login/login-page.html';
};

var signupButton = document.getElementsByClassName('signup-button');
signupButton[0].onclick = function () {
    window.location.href = '/join_page/sign-up.html';
};

// 검색
document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const searchOption = document.getElementById('search-option');

    searchForm.addEventListener('submit', (event) => {
        event.preventDefault(); // 폼 제출 기본 동작을 막음

        // 검색어 옵션 가져오기
        const query = searchInput.value;
        const option = searchOption.value;

        window.location.href = `/search-results/search-results?option=${option}&query=${encodeURIComponent(query)}`;
    });
});
