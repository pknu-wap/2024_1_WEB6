document.addEventListener('DOMContentLoaded', function () {
    // Kakao 로그인 버튼 클릭 이벤트
    document.getElementById('kakaoLoginBtn').addEventListener('click', function () {
        window.location.href = 'https://kauth.kakao.com/oauth/authorize?client_id=7df825c06a3162cabc499582d021d248&redirect_uri=http://localhost:5500/login/oauth2/code/kakao&response_type=code';
    });

    // 현재 페이지의 쿼리스트링에서 인가 코드 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code'); // 인가코드 추출
    console.log(code);

    if (code) {
        // 인가 코드를 백엔드에 전달
        fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app?code=${code}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ code: code })
        })
            .then(response => response.json())
            .then(data => {
                // 백엔드로부터 받은 토큰을 저장하고 로그인 상태 유지
                if (data.token) {
                    localStorage.setItem('accessToken', data.token);
                    alert('로그인 성공!');
                    window.location.href = 'http://localhost:5500/main_page/index.html'; // 메인 페이지로 리디렉션
                } else {
                    alert('Failed to get token.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('요청 오류입니다.');
            });
    }
});
