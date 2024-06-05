const mainPageButton = document.querySelector('.web-title');
if (mainPageButton) {
    mainPageButton.addEventListener('click', () => {
        window.location.href = '/main_page/index.html';
    });
}

// 로그아웃
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


// 회원 정보 조회
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/mypage', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                document.getElementById('user-id').value = data.data.loginId;
                document.getElementById('nickname').value = data.data.nickname;
                console.log(data.message);
            } else {
                console.log('정보를 로드하는 데 실패했습니다.');
            }
        } else {
            console.log('정보를 로드하는 데 실패했습니다.');
        }
    } catch (errors) {
        console.error('서버 오류입니다.', errors);
    }
});



// 닉네임 중복 검사
document.getElementById('check-nickname').addEventListener('click', async () => {
    const loginId = document.getElementById('user-id').value;
    const nickname = document.getElementById('nickname').value;
    const currentPassword = "";
    const newPassword = "";
    const confirmPassword = "";

    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/mypage/duplicate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nickname,
                loginId,
                currentPassword,
                newPassword,
                confirmPassword
            })
        });

        if (response.success) {
            const data = await response.json();

            if (data.success) {  // 중복 확인 성공
                document.getElementById('nicknameChecked').value = 'true';
                document.getElementById('nickname-error').textContent = data.message;
            } else {  // 중복 확인 실패
                document.getElementById('nicknameChecked').value = 'false';
                console.log(data.message);

                if (data.errors) {
                    if (data.errors.valid_nickname) {     // 닉네임 유효성 검사 탈락
                        document.getElementById('nickname-error').textContent = data.errors.valid_nickname;
                    } else if (data.errors.duplicate) {    // 닉네임이 중복될 때
                        document.getElementById('nickname-error').textContent = data.errors.duplicate;
                    } else if (data.errors.change) {       // 닉네임에 변경사항이 없을 때
                        document.getElementById('nickname-error').textContent = data.errors.change;
                    } else {
                        alert('중복 확인 실패. 다시 시도해주세요.');
                    }
                } else {
                    alert('중복 확인 실패. 다시 시도해주세요.');
                }
            }
        } else {
            console.error('Response not ok:', response.statusText);
            alert('중복 확인 실패. 다시 시도해주세요.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error:', error);
    }
});

// 회원 정보 수정 저장
document.getElementById('update-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const nickname = document.getElementById('nickname').value;
    const loginId = document.getElementById('user-id').value;
    const currentPassword = document.getElementById('current-password').value || '';
    const newPassword = document.getElementById('new-password').value || '';
    const confirmPassword = document.getElementById('confirm-new-password').value || '';
    const isNicknameChecked = document.getElementById('nicknameChecked').value === 'true';

    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/mypage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nickname,
                loginId,
                currentPassword,
                newPassword,
                confirmPassword,
                isNicknameChecked
            }),
        });

        const data = await response.json();

        if (response.success) {   // 수정 성공
            if (data.success) {
                alert(data.message);
                window.location.href = '/main_page/index.html';
            } else {   // 수정 실패 
                if (!data.nicknameChecked && data.errors && data.errors.nickname) {  // 닉네임 수정 실패
                    alert(data.message);
                } else if (data.errors && data.errors.errorMessage) {  // 현재 비밀번호가 불일치
                    console.log(data.message);
                    document.getElementById('current-password-error').textContent = data.errors.errorMessage;
                } else if (data.errors && data.errors.valid_password) {  // 새 비밀번호의 유효성 검사 탈락
                    console.log(data.message);
                    document.getElementById('new-password-error').textContent = data.errors.valid_password;
                } else if (data.errors && data.errors.confirm_password) {  // 비밀번호 확인 불일치
                    console.log(data.message);
                    document.getElementById('confirm-new-password-error').textContent = data.errors.confirm_password;
                }
            }
        } else {
            console.error('Response not ok:', data);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
});

// 회원탈퇴
document.getElementById('remove-account').addEventListener('click', () => {
    document.getElementById('passwordModal').style.display = 'block';
});

function submitPassword() {
    const password = document.getElementById('password').value;

    fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/withdraw', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                console.log(data.success)
                alert('정삭적으로 탈퇴 처리 되었습니다');
                window.location.href = '/main_page/index.html'; // 메인 페이지로 이동
            } else {
                alert('비밀번호가 틀렸습니다');
                document.getElementById('passwordModal').style.display = 'block'; // 비밀번호 입력 받는 창으로
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('요청 오류입니다.');
        });
}



