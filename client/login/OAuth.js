document.addEventListener('DOMContentLoaded', function () {
    // Kakao 로그인 버튼 클릭 이벤트
    document.getElementById('kakaoLoginBtn').addEventListener('click', function () {
        window.location.href = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/oauth2/authorization/kakao?redirect_uri=http://localhost:5500/login/oauth2/code/kakao.html&response_type=code&mode=login';
    });

    // // 현재 페이지의 쿼리스트링에서 인가 코드 가져오기
    // const urlParams = new URLSearchParams(window.location.search);
    // const code = urlParams.get('code'); // 인가코드 추출
    // console.log(code);

});
