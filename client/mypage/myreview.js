
// 프로필 수정 페이지로 이동
function editProfile() {
    var editProfileUrl = "../mypage/mypage.html";
    window.open(editProfileUrl, "_blank");
}

// 닉네임을 URL 인코딩하는 함수
function encodeNickname(nickname) {
    return encodeURIComponent(nickname);
}

// 사용자 닉네임에 기반한 리뷰 데이터를 가져오는 함수
async function fetchReviews(nickname) {
    var encodedNickname = encodeNickname(nickname);
    var apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/writer/${encodedNickname}/reviews`;

    try {
        const myRevieResponse = await fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/writer/${encodedNickname}/reviews`, {
             method: 'GET',
             credentials: 'include',
             headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        
        const data = await response.json();
        displayReviews(data);

    } catch (error) {
        console.error('Error fetching reviews:', error);
    }
}

// 리뷰 데이터를 화면에 표시하는 함수
function displayReviews(reviews) {
    var imageWrap = document.querySelector(".img-wrap");
    imageWrap.innerHTML = ''; // 기존 내용을 지움

    reviews.forEach(review => {
        var article = document.createElement("article");
        var img = document.createElement("img");
        img.src = review.imageUrl; // 리뷰 이미지 URL
        article.appendChild(img);

        var reviewTitle = document.createElement("div");
        reviewTitle.className = "review-title";
        var title = document.createElement("p");
        title.textContent = review.title; // 영화 제목
        var grade = document.createElement("p");
        grade.textContent = `★${review.grade}`; // 영화 평점
        reviewTitle.appendChild(title);
        reviewTitle.appendChild(rating);
        article.appendChild(reviewTitle);

        imageWrap.appendChild(article);

        // 클릭 이벤트 추가
        img.addEventListener("click", function () {
            // 클릭된 이미지의 URL을 가져옴
            var imageUrl = this.src;
            // 새로운 페이지로 이동 (리뷰 본문)
            window.location.href = '새로운페이지 URL';
        });
    });
}

// 로그인 상태 확인 및 리뷰 데이터 가져오기
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/mypage', {
            method: 'GET',
            credentials: 'include' // 쿠키를 포함하여 요청
        });

        const data = await response.json();
        console.log('Response Data:', data);

        if(response.ok) {
            if(data.success){
                let nickname = data.data.nickname;
                document.querySelector(".username").textContent = nickname;
                document.querySelector(".user-email").textContent = data.data.loginId;
                fetchReviews(nickname);
            }
            else {
                console.log('정보를 로드하는 데 실패했습니다.');
            }   
        } else {
            console.log('정보를 로드하는 데 실패했습니다.');
        }
    } catch (errors) {
        console.error('서버 에러:', errors);
    }
});