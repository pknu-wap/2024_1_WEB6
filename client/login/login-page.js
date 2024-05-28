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


document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        const formData = new URLSearchParams();
        formData.append('username', document.getElementById('username').value);
        formData.append('password', document.getElementById('password').value);

        try {
            const response = await fetch('http://localhost:8080/api/members/login-page', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            });

            // 최종 리다이렉트된 URL에서 JSON 응답 처리
            const data = await response.json();
            if (response.ok) {
                // 성공적인 로그인 처리
                console.log('Login successful:', data.message);
                // 로그인 성공 시 서버에서 리다이렉트를 처리함
            } else {
                // 로그인 실패 처리
                console.error('Login failed:', data);
                messageElement.innerText = data.message;
            }
        } catch (error) {
            console.error('Error during login:', error);
        }
    });
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
                success: function (res) {  // 요청 성공 -> 응답 데이터 res
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


