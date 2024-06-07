function validatePassword(password) {
    const regex = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
    return regex.test(password);
}

function validateNickname(nickname) {
  const regex = /^[가-힣a-zA-Z0-9]{2,10}$/;
  return regex.test(nickname);
}

function validateForm(event) {
  event.preventDefault(); // 폼 제출 방지

    const email =
        document.querySelector(".email").value +
        "@" +
        document.querySelector("select").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const nickname = document.getElementById("nickname").value;
    const passwordMessageElement = document.getElementById("passwordMessage");
    const nicknameMessageElement = document.getElementById("nicknameMessage");


  let isValid = true;

    // 비밀번호 유효성 검사
    if (!validatePassword(password)) {
        passwordMessageElement.textContent =
            "유효하지 않은 비밀번호입니다. 비밀번호는 8자 이상 16자 이하의 길이여야 하며, 영문, 숫자, 특수문자를 모두 포함해야 합니다.";
        passwordMessageElement.className = "error";
        isValid = false;
    } else if (password !== confirmPassword) {
        passwordMessageElement.textContent =
            "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
        passwordMessageElement.className = "error";
        isValid = false;
    } else {
        passwordMessageElement.textContent = "유효한 비밀번호입니다.";
        passwordMessageElement.className = "success";
    }

    // 닉네임 유효성 검사
    if (!validateNickname(nickname)) {
        nicknameMessageElement.textContent =
            "유효하지 않은 닉네임입니다. 닉네임은 2자 이상 10자 이하의 한글, 영문 대소문자, 숫자로 구성되어야 합니다.";
        nicknameMessageElement.className = "error";
        isValid = false;
    } else {
        nicknameMessageElement.textContent = "유효한 닉네임입니다.";
        nicknameMessageElement.className = "success";
    }

    // 유효성 검사가 모두 통과되었을 때 폼 제출 및 리다이렉트
    if (isValid) {
        const formData = {
            loginId: email,
            nickname: nickname,
            password: password,
            confirmPassword: confirmPassword,
        };

        fetch(
            "https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/sign-up",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
                redirect: "manual", // 리다이렉트 방지
            }
        )
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }
                console.log("ok");
                return response.json(); // JSON 응답을 JavaScript 객체로 변환
            })
            .then((data) => {
                console.log(data);
                if (data.success) {
                    alert("가입 성공!");
                    window.location.href = "../login/login-page.html"; // 업로드된 파일로 리다이렉트
                } else {
                    alert("가입에 실패했습니다: " + data.errors.duplicate);
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("가입 중 오류가 발생했습니다. 다시 시도해 주세요.");
            });
    }
}