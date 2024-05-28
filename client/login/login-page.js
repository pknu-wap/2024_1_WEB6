// 메인 페이지로 이동
var mainPageButton = document.getElementsByClassName('web-title');
mainPageButton[0].onclick = function () {
    window.location.href = '/main_page/index.html';
};

// 회원가입 페이지로 이동
var signupButton = document.getElementsByClassName('signup-button');
signupButton[0].onclick = function () {
    window.location.href = '/join_page/join_page.html';
};

// 로그인
document.getElementById('login-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const requestBody = JSON.stringify({
        username: username,
        password: password
    });

    try {
        const response = await fetch('http://localhost:8080/api/members/login-page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: requestBody
        });

        const data = await response.json();
        if (response.ok) {
            alert('로그인 성공!');
        } else {
            alert('로그인 실패: ' + data.message);
            messageElement.innerText = data.message;
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
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
        window.location.href = '/main_page/index.html';
    } else {
        alert('카카오 로그인 실패!');
    }
}


