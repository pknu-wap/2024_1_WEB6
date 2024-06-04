// 로그인 상태 확인
document.addEventListener('DOMContentLoaded', async () => {
    try {
        // 요청
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/main', {
            method: 'GET',
            credentials: 'include' // 쿠키를 포함하여 요청
        });


        const data = await response.json();
        console.log('Session Data:', data);

        // UI 요소 참조
        const loginSection = document.getElementById('login-section');
        const userInfoSection = document.getElementById('user-info-section');
        const welcomeMessage = document.getElementById('welcome-message');

        // 응답 처리
        if (data.success) {  // 로그인된 상태
            // UI 업데이트
            welcomeMessage.innerHTML = `<a>${data.message}</a> 환영합니다!`;
            loginSection.style.display = 'none';

            document.querySelector('#welcome-message a').addEventListener('click', (event) => {
                event.preventDefault();
                // window.location.href = `/myreview/${encodeURIComponent(data.message)}`;  // 리뷰고나리ㄴ
                window.location.href = '../mypage/mypage.html'  // 회원정보수정
            });

            // 로그아웃 버튼 동작 설정
            document.getElementById('logout-button').addEventListener('click', async () => {
                try {
                    const logoutResponse = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/logout-page', {
                        method: 'GET',
                        credentials: 'include' // 쿠키를 포함하여 요청
                    });

                    console.log('로그아웃 응답:', logoutResponse);
                    const logoutData = await logoutResponse.json();
                    console.log('로그아웃 데이터:', logoutData);

                    if (logoutResponse.ok && !logoutData.success) {
                        window.location.href = '../main_page/index.html';
                    } else {
                        alert('로그아웃에 실패했습니다. 다시 시도해 주세요.');
                    }
                } catch (error) {
                    console.error('Error logging out:', error);
                    alert('서버에 연결하지 못했습니다.');
                }
            });

        } else {
            // 로그인되지 않은 상태
            console.log(data.message);  // 로그인을 해주세요.
            userInfoSection.style.display = 'none';
        }

    } catch (error) {
        console.error('서버 에러:', error);
    }
});

