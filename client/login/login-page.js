// 메인 페이지로 이동
document.querySelector('.web-title').onclick = () => {
    window.location.href = '/main_page/index.html';
};


// 회원가입 페이지로 이동
document.querySelector('.signup-button').onclick = () => {
    window.location.href = '/join_page/join_page2.html';
};

// 일반 로그인
document.getElementById('login-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (username === '' || password === '') {
        alert('아이디와 비밀번호를 모두 입력해주세요.');
        return;
    }

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

        if (response.ok && data.message === '로그인 성공') {
            window.location.href = '/main_page/index.html';
        } else {
            let messageElement = document.getElementById("messageElement");
            messageElement.textContent = data.message;
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
});
