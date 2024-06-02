// // 1
// document.addEventListener('DOMContentLoaded', function () {
//     const kakaoLoginBtn = document.getElementById('kakaoLoginBtn');

//     // 카카오 로그인 버튼 클릭 처리
//     kakaoLoginBtn.addEventListener('click', handleLogin);
//     function handleLogin() {
//         const authUrl = "https://kauth.kakao.com/oauth/authorize?client_id=7df825c06a3162cabc499582d021d248&redirect_uri=http://localhost:5500/login/oauth2/code/kakao&response_type=code";
//         window.location.href = authUrl;
//         // 사용자를 카카오 인증 페이지로 리디렉션
//     }

//     // 인가 코드 추출
//     const params = new URLSearchParams(window.location.search);
//     const code = params.get('code');

//     if (code) {
//         handleKakaoLogin(code);
//         // 인가 코드가 있으면 handleKakaoLogin 함수 호출
//     }

//     async function handleKakaoLogin(code) {
//         try {
//             // 인가 코드를 서버로 전송하여 액세스 토큰 요청
//             const response = await axios.get(` https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/oauth/callback/kakao?code=${code}`);
//             console.log(response); // 토큰이 넘어올 것임

//             const ACCESS_TOKEN = response.data.accessToken;  // 액세스 토큰 추출
//             localStorage.setItem("token", ACCESS_TOKEN); // 액세스 토큰을 로컬 스토리지에 저장

//             window.location.href = 'http://localhost:5500/main_page/index.html'; // 메인 페이지로 리디렉션
//         } catch (error) {
//             console.error('소셜로그인 에러', error);
//             alert('로그인에 실패하였습니다.');
//             window.location.href = '/login/login-page'; // 로그인 실패하면 로그인 페이지로 리디렉션
//         }
//     }
// });


// 2
// document.addEventListener('DOMContentLoaded', function () {
//     const kakaoLoginButton = document.getElementById('kakaoLoginButton');

//     async function postCode(KAKAO_CODE) {
//         try {
//             // 인가 코드를 서버로 보내줌
//             const response = await axios.post('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/auth/kakao', null, {
//                 params: { authorizationCode: KAKAO_CODE }
//             });

//             // 서버에서 인가 코드를 가지고 카카오에서 개인정보를 받아온 뒤, 응답값으로 회원가입 유무를 판단할 email을 보내줌
//             const userEmail = response.data.success.kakao_account.email;

//             // 회원가입 유무 판단
//             const checkUser = await axios.post('/users/check/email', { email: userEmail });

//             if (checkUser.data.isEmailExisted) {
//                 // 이미 있는 계정이라면 서버에서 액세스 토큰 받고 홈으로 이동
//                 try {
//                     const tokenResponse = await axios.post('/auth', { email: userEmail });
//                     const { accessToken } = tokenResponse.data;
//                     localStorage.setItem('token', accessToken);
//                     window.location.href = '/';
//                     alert('로그인되었습니다!');
//                 } catch (e) {
//                     console.error(e.response);
//                 }
//             } else {
//                 console.error('error');
//                 alert('로그인에 실패하였습니다.');
//             }
//         } catch (e) {
//             console.error(e);
//         }
//     }

//     function handleKakaoLogin() {
//         // 현재 URL에서 인가 코드 추출
//         const KAKAO_CODE = new URL(window.location.href).searchParams.get('code');
//         if (KAKAO_CODE) {
//             postCode(KAKAO_CODE);
//         }
//     }

//     // 인가 코드가 있으면 처리 함수 호출
//     handleKakaoLogin();

//     // 로그인 버튼 클릭 이벤트 핸들러
//     kakaoLoginButton.addEventListener('click', function () {
//         const authUrl = 'https://kauth.kakao.com/oauth/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=YOUR_REDIRECT_URI&response_type=code';
//         window.location.href = authUrl;
//     });
// });



// 3
// 로그인 버튼 클릭
document.getElementById('kakaoLoginBtn').addEventListener('click', function () {
    Kakao.Auth.authorize({
        redirectUri: 'https://kauth.kakao.com/oauth/authorize?client_id=7df825c06a3162cabc499582d021d248&redirect_uri=http://localhost:5500/login/oauth2/code/kakao&response_type=code' // 카카오 개발자 사이트에 등록한 리다이렉트 URI
    });
});

// 현재 URL에서 인가 코드 파싱
function getAuthorizationCode() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('code');
}

// 인가 코드가 있으면 백엔드로 전달
const authorizationCode = getAuthorizationCode();  // 인가코드 추출 후 변수에 저장
if (authorizationCode) {
    fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer' + authorizationCode
        },
        body: JSON.stringify({
            code: authorizationCode
        })
    })
        .then(response => response.json())
        .then(data => {
            const accessToken = data.access_token;
            // 토큰을 저장하고 로그인 상태를 유지
            localStorage.setItem('kakao_access_token', accessToken);
            alert('로그인 성공!');
            // 필요한 경우, 로그인 후 리디렉션
            window.location.href = 'http://localhost:5500/main_page/index.html'; // 메인 페이지로 이동
        })
        .catch(error => {
            console.error('Error:', error);
            alert('로그인 실패');
        });
}