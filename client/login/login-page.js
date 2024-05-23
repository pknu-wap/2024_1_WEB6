// 메인 페이지로 이동
var mainPageButton = document.getElementsByClassName('web-title');
mainPageButton[0].onclick = function () {
    window.location.href = '/main_page/main.html';
};

// 회원가입 페이지로 이동
var signupButton = document.getElementsByClassName('signup-button');
signupButton[0].onclick = function () {
    window.location.href = '/join_page/join_page.html';
};

// 로그인 기능
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');

    // loginForm 폼 요소에 이벤트 리스너 추가: 폼 제출 시 발생
    loginForm.addEventListener('submit', (event) => {
        event.preventDefault();  // 페이지 새로고침x

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        const xhr = new XMLHttpRequest();  // XMLHttpRequest 객체 생성: 비동기
        xhr.open('POST', '/api/members/login-page', true);
        // HTTP 요청 초기화: (요청 유형, 요청을 보낼 url, true=비동기)
        xhr.setRequestHeader('Content-Type', 'application/json');
        // 헤더 설정: (헤더 이름, 헤더의 값: 전송할 데이터의 형식 지정)

        // onreadystatechange 이벤트 핸들러 정의
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {  // 요청 완료
                if (xhr.status === 200) {  // 요청 성공
                    // 로그인 성공
                    alert('Login successful!');
                    window.location.href = '/main_page/main.html'; // 로그인 성공 후 이동할 페이지
                } else {
                    // 로그인 실패
                    try {
                        const errorResponse = JSON.parse(xhr.responseText);
                        if (errorResponse && errorResponse.errorMessage) {
                            alert(errorResponse.errorMessage); // 서버에서 에러 메시지를 표시
                        } else {
                            alert('로그인 실패! 서버에서 에러 메시지를 받지 못했습니다.');
                        }
                    } catch (error) {
                        alert('로그인 실패! 서버 오류입니다.');
                    }
                }
            }
        };
        const data = JSON.stringify({  // json 형식으로 전송
            username: username,
            password: password
        });

        xhr.send(data);

    });

    function displayErrorMessage(message) {
        const errorMessageContainer = document.createElement('span');
        errorMessageContainer.style.color = 'red';
        errorMessageContainer.style.fontWeight = 'bold';
        errorMessageContainer.textContent = message;  // 함수 호출될 때마다 다른 오류메세지 표시

        const form = document.getElementById('login-form');  // form변수: 사용자의 로그인 정보
        const existingErrorMessage = form.querySelector('span[style="color: red; font-weight: bold;"]');
        if (existingErrorMessage) {
            existingErrorMessage.remove();
        }

        form.appendChild(errorMessageContainer);
    }
});


// 카카오 로그인?
// 카카오 SDK 초기화
Kakao.init('인증_KEY');

function kakaoLogin() {
    Kakao.Auth.login({
        scope: 'profile_nickname, account_email',
        success: function (authObj) {  // 로그인 성공
            Kakao.API.request({  // API 요청 보내는 함수
                url: '/v2/user/me',
                success: function(res) {  // 요청 성공 -> 응답 데이터 res
                    const kakaoAccount = res.kakao_account;
                    const kakaoUserData = {
                        email: kakaoAccount.email,
                        nickname: kakaoAccount.profile.nickname
                    };
                    kakaoLoginApi(kakaoUserData);
                }
            });
        }
    });
}

async function kakaoLoginApi(kakaoUserData) {
    const response = await fetch('/api/members/kakao-login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(kakaoUserData),
    });

    const response_json = await response.json();

    if (response.status == 200) {
        alert(response_json.msg);
        window.location.href = '/main_page/main.html';
    } else {
        alert('카카오 로그인 실패!');
    }
}


