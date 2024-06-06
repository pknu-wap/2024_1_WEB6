document.addEventListener("DOMContentLoaded", () => {
  // 웹 페이지 이동
  document.querySelector(".web-title").onclick = () => {
    window.location.href = "../main_page/index.html";
  };

  document.querySelector(".signup-button").onclick = () => {
    window.location.href = "../join_page/join_page2.html";
  };
});

// 일반 로그인
document
  .getElementById("login-form")
  .addEventListener("submit", async (event) => {
    event.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (username === "" || password === "") {
      alert("아이디와 비밀번호를 모두 입력해주세요.");
      return;
    }

    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);

    try {
      const response = await fetch(
        "https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/login-page",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          credentials: "include", // 쿠키나 인증 헤더를 포함하여 서버로 보냄
          body: formData.toString(),
        }
      );

      const data = await response.json();
      console.log(data);

      if (data.success) {
        // 로그인 성공
        function getCookie(name) {
          let cookieString = document.cookie;
          // let cookies = cookieString.split(';');
          console.log(cookieString);
          // var matches = document.cookie.match(new RegExp(
          //     "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
          // ));
          // return matches ? decodeURIComponent(matches[1]) : undefined;
        }

        // 예시: 세션 쿠키 확인
        var sessionId = getCookie("JSESSIONID");
        console.log("Session ID:", sessionId);
        window.location.href = "../main_page/index.html";
      } else {
        // 로그인 실패
        let messageElement = document.getElementById("messageElement");
        // messageElement.textContent = data.message;
        alert(data.message + "! 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  });

function getCookie(name) {
  var matches = document.cookie.match(
    new RegExp(
      "(?:^|; )" +
        name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, "\\$1") +
        "=([^;]*)"
    )
  );
  return matches ? decodeURIComponent(matches[1]) : undefined;
}
