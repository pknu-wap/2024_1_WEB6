const mainPageButton = document.querySelector('.web-title');
if (mainPageButton) {
    mainPageButton.addEventListener('click', () => {
        window.location.href = '/main_page/index.html';
    });
}

const logoutButton = document.querySelector('.logout-button');
if (logoutButton) {
    logoutButton.addEventListener('click', () => {
        // 로그아웃
    });
}


// 마이페이지 정보 로드
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('http://localhost:8080/api/mypage', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            document.getElementById('user-id').value = data.loginId;
            document.getElementById('nickname').value = data.nickname;
            // 다른 필요한 필드들도 여기에 추가
        } else {
            console.error('Failed to load user information');
        }
    } catch (error) {
        console.error('Error:', error);
    }
});

// 닉네임 중복 확인
document.getElementById('check-nickname').addEventListener('click', async () => {
    const nickname = document.getElementById('nickname').value;
    const loginId = document.getElementById('user-id').value;

    const requestBody = JSON.stringify({
        nickname: nickname,
        loginId: loginId,
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
        nicknameChecked: false
    });

    try {
        const response = await fetch('http://localhost:8080/api/mypage/duplicate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: requestBody
        });

        const data = await response.json();

        if (response.ok) {
            document.getElementById('nicknameChecked').value = 'true';
            alert(result.message);
        } else {
            document.getElementById('nicknameChecked').value = 'false';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('nickname-error').textContent = '오류가 발생했습니다. 다시 시도해주세요.';
    }
});

// 회원 정보 수정 저장
document.getElementById('update-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const nickname = document.getElementById('nickname').value;
    const loginId = document.getElementById('user-id').value;
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-new-password').value;
    const nicknameChecked = false; // 예시로 고정값 사용. 실제로는 중복 확인 후 값 설정

    const requestBody = {
        nickname: nickname,
        loginId: loginId,
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword,
        nicknameChecked: nicknameChecked
    };

    try {
        const response = await fetch('http://localhost:8080/api/mypage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (response.ok) {
            alert('회원 정보가 성공적으로 수정되었습니다.');
        } else {
            // 서버에서 반환된 오류 메시지를 해당 입력 필드 아래에 표시
            if (data.errors) {
                if (data.errors.nickname) {
                    document.getElementById('nickname-error').textContent = data.errors.nickname;
                }
                if (data.errors.currentPassword) {
                    document.getElementById('current-password-error').textContent = data.errors.currentPassword;
                }
                if (data.errors.newPassword) {
                    document.getElementById('new-password-error').textContent = data.errors.newPassword;
                }
                if (data.errors.confirmPassword) {
                    document.getElementById('confirm-new-password-error').textContent = data.errors.confirmPassword;
                }
            } else {
                alert('오류가 발생했습니다: ' + data.message);
            }
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
});
