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

// 일반 로그인
document.getElementById('login-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);

    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/login-page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },

            // 쿠키나 인증 헤더를 포함하여 서버로 보냄
            credentials: 'include',
            body: formData.toString()
        });

        const data = await response.json();
        console.log(data)
        if (response.success) {
            alert('로그인 성공!');
        } else {
            // alert(data.message);
            let messageElement = document.getElementById("messageElement");
            messageElement.textContent = data.message;
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
});





document.addEventListener('DOMContentLoaded', function () {
    const kakaoLoginBtn = document.getElementById('kakaoLoginBtn');

    // 카카오 로그인 버튼 클릭 처리
    kakaoLoginBtn.addEventListener('click', handleLogin);

    function handleLogin() {
        const authUrl = "/login";
        window.location.href = authUrl;
    }

    function handleOAuth2Redirect() {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');  // 서버에서 JWT 토큰을 전달했다고 가정
        const error = params.get('error');

        if (token) {
            localStorage.setItem('accessToken', token);  // 토큰 저장
            window.location.href = '/';  // 리디렉션
        } else {
            console.error(error);
            window.location.href = '/login';  // 로그인 페이지로 리디렉션
        }
    }

    if (window.location.pathname === '/oauth2/callback') {
        handleOAuth2Redirect();
    }

    // axios 인스턴스 생성
    const axiosInstance = axios.create({
        baseURL: '/login',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    // 요청 인터셉터 설정
    axiosInstance.interceptors.request.use(
        config => {
            const token = localStorage.getItem('accessToken');
            if (token) {
                config.headers['Authorization'] = 'Bearer ' + token;
            }
            return config;
        },
        error => {
            return Promise.reject(error);
        }
    );

    // API 요청 예시
    async function fetchUserData() {
        try {
            const response = await axiosInstance.get('/user/me');
            console.log(response.data);
        } catch (error) {
            console.error('API 요청 실패:', error);
        }
    }

    if (window.location.pathname === '/') {
        fetchUserData();
    }
});


